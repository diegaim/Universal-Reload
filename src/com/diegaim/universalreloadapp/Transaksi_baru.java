package com.diegaim.universalreloadapp;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Transaksi_baru extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
	Spinner spin_jenis_pulsa, spin_nominal_pulsa;
	EditText txt_nomor_tujuan;
	Button btn_kirim, btn_bersihkan;
	
	String saldo;
	String[] jenis_pulsa = { "Operator", "Telkomsel", "As", "XL", "Axis","3", "Smartfren","INDOSAT"};
	HashMap<String, String []> hash_jenis_pulsa = new HashMap<String, String []>();
	
	SQLiteHelper sqLiteHelper = new SQLiteHelper(Transaksi_baru.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		generateData();
		setContentView(R.layout.activity_transaksi_baru);
		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#fe0000"));
	    getActionBar().setBackgroundDrawable(colorDrawable);
	    getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
	    
	    spin_jenis_pulsa = (Spinner) findViewById(R.id.spinner1);
	    spin_nominal_pulsa = (Spinner) findViewById(R.id.spinner2);
	    txt_nomor_tujuan = (EditText) findViewById(R.id.editText1);
	    txt_nomor_tujuan.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
	    btn_kirim = (Button) findViewById(R.id.button1);
	    btn_bersihkan = (Button) findViewById(R.id.button2);
	    btn_bersihkan.setVisibility(View.GONE);
	    
	    //cek nomor server
        HashMap<String, String> hashMapnoserver = sqLiteHelper.info_no_server();
        String no_server_aktif = hashMapnoserver.get("no_server");
        if (no_server_aktif == null){
        	alert_nomor_server();
		}
	    
	    //Untuk mengisi nilai pada spinner
	    spin_jenis_pulsa.setOnItemSelectedListener(this);
	    ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, jenis_pulsa);
	    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spin_jenis_pulsa.setAdapter(aa);
	    
	    //ambil info saldo
        HashMap<String, String> hashMapHasil = sqLiteHelper.info_akun();
    	for (int i = 0; i < hashMapHasil.size(); i++) {
            saldo = hashMapHasil.get("saldo");
        }
    	
    	//listener saat form
    	txt_nomor_tujuan.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if (spin_jenis_pulsa.getSelectedItem().toString().equals("Operator") && txt_nomor_tujuan.getText().toString().equals("")){
					btn_bersihkan.setVisibility(View.GONE);
				}else{
					btn_bersihkan.setVisibility(View.VISIBLE);
				}
			}
    		
    	});
    	
    	
	    
	    //aksi untuk btn_bersihkan
	    btn_bersihkan.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View v) {
 				spin_jenis_pulsa.setSelection(0);
 				txt_nomor_tujuan.setText("");
 				btn_bersihkan.setVisibility(View.GONE);
 			}
 		});
	    
	    //aksi untuk btn_kirim
	    btn_kirim.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View v) {
 				if (spin_jenis_pulsa.getSelectedItem().toString().equals("Operator") ||spin_nominal_pulsa.getSelectedItem().toString().equals("Pilih Nominal") || txt_nomor_tujuan.getText().toString().equals("")){
 					//Kondisi kalau operator dan nominal belum dipilih
 					alert_data_belum_diisi();
 				}else{
 					//Kondisi kalau operator dan nominal telah dipilih
 					//cek lagi saldonya cukup atau tidak
 					if (Integer.parseInt(spin_nominal_pulsa.getSelectedItem().toString()) > Integer.parseInt(saldo)){
 						alert_saldo_kurang();
 					}else{
 						kirim_tanya();
 					}
 				}
 			}
 		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.transaksi_baru, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.men_menu_utama) {
			Intent i = new Intent(getApplicationContext(), Menu_utama.class);
			startActivity(i);
			finish();
		}else if (id == R.id.men_catatan_transaksi) {
			Intent i = new Intent(getApplicationContext(), Catatan_transaksi.class);
			startActivity(i);
			finish();
		}else if (id == R.id.men_pengaturan) {
			Intent i = new Intent(getApplicationContext(), Pengaturan.class);
			startActivity(i);
			finish();
		}else if (id == R.id.men_bantuan) {
			Intent i = new Intent(getApplicationContext(), Bantuan.class);
			startActivity(i);
			finish();
		}else if (id == R.id.men_tentang) {
			Intent i = new Intent(getApplicationContext(), Tentang.class);
			startActivity(i);
			finish();
		}else if (id == R.id.men_keluar) {
			keluar_alert();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		fillComboNominal(jenis_pulsa[position]);
		if (spin_jenis_pulsa.getSelectedItem().toString().equals("Operator") && txt_nomor_tujuan.getText().toString().equals("")){
			btn_bersihkan.setVisibility(View.GONE);
		}else{
			btn_bersihkan.setVisibility(View.VISIBLE);
		}
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		Toast.makeText(this, "Pilih jenis pulsa terlebih dahulu!", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onBackPressed(){
		Intent i = new Intent(getApplicationContext(), Menu_utama.class);
		startActivity(i);
		finish();
	}
	
	//meng generate sub data
	private void generateData(){
		hash_jenis_pulsa.put("Operator", new String[] {"Nominal Pulsa"});
		hash_jenis_pulsa.put("Telkomsel", new String[] {"Nominal Pulsa","5000","10000","20000","25000","50000","100000"});
		hash_jenis_pulsa.put("As", new String[] {"Nominal Pulsa","5000","10000","20000","25000","50000","100000"});
		hash_jenis_pulsa.put("XL", new String[] {"Nominal Pulsa","5000","10000","25000","50000","100000"});
		hash_jenis_pulsa.put("Axis", new String[] {"Nominal Pulsa","5000","10000","25000","50000","100000"});
		hash_jenis_pulsa.put("3", new String[] {"Nominal Pulsa","1000","3000",	"5000","10000","20000","30000","50000","100000"});
		hash_jenis_pulsa.put("Smartfren", new String[] {"Nominal Pulsa","5000","10000","20000","25000","50000","100000"});
		hash_jenis_pulsa.put("INDOSAT", new String[] {"Nominal Pulsa","5000","10000","25000","50000","100000"});
		
	}

	//kontrol agar saat operator diklik data pada spinner nominal berubah sesuai dengan generate sub data
	private void fillComboNominal(String snegara){
		String[] kota = null;
		ArrayAdapter<String> aa = null;
		try {
			kota = hash_jenis_pulsa.get(snegara);
			aa = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, kota);
		} catch (NullPointerException e) {
			aa = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, new String[] {});
		}
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin_nominal_pulsa.setAdapter(aa);
	}
	
	public void keluar_alert(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Konfirmasi");
		alertDialogBuilder
	    .setMessage("Yakin ingin keluar?")
	    .setCancelable(false)
	    .setPositiveButton("Iya",new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog,int id) {
		    	 finish();
		     }
	     })
	    .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog,int id) {
		    	 dialog.cancel();
		     }
	    });
	    AlertDialog alertDialog = alertDialogBuilder.create();
	    alertDialog.show();
	}
	
	public void kirim_tanya(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	   	alertDialogBuilder.setTitle("Data sudah benar?");
	   	alertDialogBuilder
	    .setMessage("Jenis Operator  : "+spin_jenis_pulsa.getSelectedItem().toString()+"\nNominal Pulsa  : "+spin_nominal_pulsa.getSelectedItem().toString()+"\nNomor Tujuan   : "+txt_nomor_tujuan.getText().toString())
	    .setCancelable(false)
	    .setPositiveButton("Iya",new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog,int id) {
		    	Intent i = new Intent(getApplicationContext(), Proses_transaksi.class);
				i.putExtra("operator", spin_jenis_pulsa.getSelectedItem().toString());
				i.putExtra("nominal_pulsa", spin_nominal_pulsa.getSelectedItem().toString());
				i.putExtra("nomor", txt_nomor_tujuan.getText().toString());
		 		startActivity(i);
		 		finish();
		     }
	     })
	    .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog,int id) {
		    	 dialog.cancel();
		     }
	    });
	    AlertDialog alertDialog = alertDialogBuilder.create();
	    alertDialog.show();
	}
	
	public void alert_saldo_kurang(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	   	alertDialogBuilder.setTitle("Peringatan!");
	   	alertDialogBuilder
	    .setMessage("Sisa saldo yang anda miliki kurang untuk melakukan transaksi, \nTetap lanjutkan?")
	    .setCancelable(false)
	    .setNegativeButton("Lanjutkan",new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog,int id) {
		    	 kirim_tanya();
		     }
	    })
	    .setPositiveButton("Tidak",new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog,int id) {
		    	Toast.makeText(getApplicationContext(), "Transaksi telah dibatalkan", Toast.LENGTH_SHORT).show();
		    	Intent i = new Intent(getApplicationContext(), Menu_utama.class);
		 		startActivity(i);
		 		finish();
		     }
	     });
	    AlertDialog alertDialog = alertDialogBuilder.create();
	    alertDialog.show();
	}
	
	public void alert_data_belum_diisi(){
		new AlertDialog.Builder(this)
	    .setTitle("Kesalahan!")
	    .setMessage("Data yang dimasukkan belum lengkap")
	    .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
			 @Override
			 public void onClick(DialogInterface dlg, int sumthin) {
				 // TODO Auto-generated method stub 
			 }
	    }).show();
	}
	
	public void alert_nomor_server(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		
		alertDialogBuilder.setTitle("Peringatan");
		alertDialogBuilder
	    .setMessage("Nomor server belum dipilih, mohon pilih nomor server terlebih dahulu agar dapat melakukan transaksi")
	    .setCancelable(false)
	    .setPositiveButton("Buka pengaturan",new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog,int id) {
		    	Intent i = new Intent(getApplicationContext(), Pengaturan.class);
				i.putExtra("diatur", "n");
				startActivity(i);
				finish();
		     }
	     });
	    AlertDialog alertDialog = alertDialogBuilder.create();
	    alertDialog.show();
	}
}