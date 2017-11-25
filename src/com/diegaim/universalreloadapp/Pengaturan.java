package com.diegaim.universalreloadapp;

import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.InputType;

import java.util.ArrayList;
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
import android.widget.*;

@SuppressLint("NewApi")
public class Pengaturan extends ActionBarActivity implements AdapterView.OnItemSelectedListener{
	Button btn_tambah_server, btn_simpan, btn_hapus_server;
	EditText txt_saldo, txt_pin;
	Spinner spin_no_server;
	String saldo, pin, no_server_aktif, diatur;
	ArrayList<String> semua_no_server = new ArrayList<String>();
	ArrayAdapter<String> arrayAdapter;
	
	SQLiteHelper sqLiteHelper = new SQLiteHelper(Pengaturan.this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pengaturan);
		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#fe0000"));
	    getActionBar().setBackgroundDrawable(colorDrawable);
	    getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		
	    btn_simpan = (Button) findViewById(R.id.button1);
	    btn_tambah_server = (Button) findViewById(R.id.button2);
	    btn_hapus_server = (Button) findViewById(R.id.button3);
		txt_saldo = (EditText) findViewById(R.id.editText1);
		txt_saldo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
		txt_pin = (EditText) findViewById(R.id.editText2);
		txt_pin.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
		spin_no_server = (Spinner) findViewById(R.id.spinner1);
		
		//nilai default diatur
		diatur = "y";
		Intent intent = getIntent();
		if (intent.getStringExtra("diatur") != null){
			diatur = intent.getStringExtra("diatur");
		}
		//untuk mengambil data saldo dan pin
		if (diatur.equals("n")){ //cek apakah sudah diatur sebelumnya
			//nothing
		}else{
			ambil_data_sqlite();
			txt_saldo.setText(saldo.toString());
			txt_pin.setText(pin.toString());
		}
		//cek apakah ada nomor server yg telah dipilih
		HashMap<String, String> hashMapnoserver = sqLiteHelper.info_no_server();
    	no_server_aktif = hashMapnoserver.get("no_server");
		if (no_server_aktif == null){
			semua_no_server.add("Pilih nomor server!");
			diatur = "n";
		}else{
			semua_no_server.add(no_server_aktif);
		}
		//mengambil data no server
		tampil_semua_no_server();
		//untuk set item pada spinner
		spin_no_server.setOnItemSelectedListener(this);
		arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, semua_no_server);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin_no_server.setAdapter(arrayAdapter);
		spin_no_server.setSelection(0);
	    
		//aksi untuk btn_simpan
		btn_simpan.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View v) {
				//cek kelengkapan data sebelum menyimpan
				if (txt_saldo.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "Isikan Saldo terlebih dahulu!", Toast.LENGTH_SHORT).show();
				}else if (txt_pin.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "Isikan PIN terlebih dahulu!", Toast.LENGTH_SHORT).show();
				}else if (spin_no_server.getSelectedItem().toString().equals("Pilih nomor server!")){
					Toast.makeText(getApplicationContext(), "Pilih nomor server terlebih dahulu!", Toast.LENGTH_SHORT).show();
				}else{
					//melakukan penyimpanan pengaturan
					if (diatur.equals("n")){
						String a= txt_pin.getText().toString();
						String b = txt_saldo.getText().toString();
						String c = spin_no_server.getSelectedItem().toString();
						sqLiteHelper.tambah_akun(a, b);
						sqLiteHelper.simpan_pengaturan_baru(c);
		        		Toast.makeText(getApplicationContext(), "Pengaturan telah tersimpan", Toast.LENGTH_SHORT).show();
		        		diatur = "y";
					}else{
						String a= txt_pin.getText().toString();
						int b = Integer.parseInt(txt_saldo.getText().toString());
						String c = spin_no_server.getSelectedItem().toString();
						sqLiteHelper.simpan_pengaturan(a, b, no_server_aktif, c);
		        		Toast.makeText(getApplicationContext(), "Pengaturan telah tersimpan", Toast.LENGTH_SHORT).show();
		        		diatur = "y";
					}
				}
 			}
 		});
		
		//aksi untuk btn_tambah_server
		btn_tambah_server.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View v) {
				tambah_server();
 			}
 		});

		//aksi untuk btn_hapus_server
		btn_hapus_server.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View v) {
				if (spin_no_server.getSelectedItem().toString().equals("Pilih nomor server!")){
	        		Toast.makeText(getApplicationContext(), "Pilih nomor server terlebih dahulu!", Toast.LENGTH_SHORT).show();
				}else{
					hapus_server();
				}
 			}
 		});
	}
	
	public void ambil_data_sqlite(){
        try{
        	//ambil data pin dan saldo
        	HashMap<String, String> hashMapBiodata = sqLiteHelper.info_akun();
        	for (int i = 0; i < hashMapBiodata.size(); i++) {
                pin = hashMapBiodata.get("pin");
                saldo = hashMapBiodata.get("saldo");
            }
        	//ambil data no_server yg aktif
        	HashMap<String, String> hashMapnoserver = sqLiteHelper.info_no_server();
        	no_server_aktif = hashMapnoserver.get("no_server");
        }catch(Exception e){
        	Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        } 
	}
	
	@Override
	public void onBackPressed(){
		if (diatur.equals("n")){
			atur_dulu();
		}else{
			Intent i = new Intent(getApplicationContext(), Menu_utama.class);
			startActivity(i);
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pengaturan, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.men_menu_utama) {
			if (diatur.equals("n")){
				atur_dulu();
			}else{
				Intent i = new Intent(getApplicationContext(), Menu_utama.class);
				startActivity(i);
				finish();
			}
		}else if (id == R.id.men_transaksi_baru) {
			if (diatur.equals("n")){
				atur_dulu();
			}else{
				Intent i = new Intent(getApplicationContext(), Transaksi_baru.class);
				startActivity(i);
				finish();
			}
		}else if (id == R.id.men_catatan_transaksi) {
			if (diatur.equals("n")){
				atur_dulu();
			}else{
				Intent i = new Intent(getApplicationContext(), Catatan_transaksi.class);
				startActivity(i);
				finish();
			}
		}else if (id == R.id.men_bantuan) {
			if (diatur.equals("n")){
				atur_dulu();
			}else{
				Intent i = new Intent(getApplicationContext(), Bantuan.class);
				startActivity(i);
				finish();
			}
		}else if (id == R.id.men_tentang) {
			if (diatur.equals("n")){
				atur_dulu();
			}else{
				Intent i = new Intent(getApplicationContext(), Tentang.class);
				startActivity(i);
				finish();
			}
		}else if (id == R.id.men_keluar) {
			keluar_alert();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void atur_dulu(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	   	alertDialogBuilder.setTitle("Peringatan");
	   	alertDialogBuilder
	    .setMessage("Aplikasi membutuhkan informasi Sisa Saldo dan PIN Universal Reload anda. Mohon lakukan pengaturan aplikasi agar aplikasi dapat berjalan.")
	    .setCancelable(false)
	    .setPositiveButton("Kembali",new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog,int id) {
		    	dialog.cancel();
		     }
	     });
	    AlertDialog alertDialog = alertDialogBuilder.create();
	    alertDialog.show();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		Toast.makeText(this, "Silahkan Pilih No Server", Toast.LENGTH_SHORT).show();

	}
	
	public void tampil_semua_no_server(){
		ArrayList<HashMap<String, String>> data = sqLiteHelper.tampil_semua_no_server();
		for (int i = 0; i < data.size(); i++) {
			semua_no_server.add(data.get(i).get("no_server"));
		}
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
	
	public void tambah_server() {
        /* layout akan ditampilkan pada AlertDialog */
        LinearLayout layoutInput = new LinearLayout(this);
        layoutInput.setOrientation(LinearLayout.VERTICAL);

        final EditText txt_tambah_server = new EditText(this);
        txt_tambah_server.setHint("No server");
        txt_tambah_server.setInputType(InputType.TYPE_CLASS_NUMBER);
        txt_tambah_server.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        layoutInput.addView(txt_tambah_server);

        AlertDialog.Builder builderTambahSaldo = new AlertDialog.Builder(this);
        builderTambahSaldo.setTitle("Tambah Nomor Server");
        builderTambahSaldo.setView(layoutInput);
        builderTambahSaldo.setPositiveButton("Tambah",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	//cek apakah ada inputan yg dimasukkan
            	if (txt_tambah_server.getText().toString().equals("")){
            		Toast.makeText(getApplicationContext(), "Tidak ada nomor server yang dimasukkan", Toast.LENGTH_SHORT).show();
            	}else{
            		sqLiteHelper.tambah_no_server(txt_tambah_server.getText().toString(), "tdk_dipilih");
                    semua_no_server.clear();
                    //ambil data no_server yg aktif
                    HashMap<String, String> hashMapnoserver = sqLiteHelper.info_no_server();
                    no_server_aktif = hashMapnoserver.get("no_server");
             		if (no_server_aktif == null){
             			semua_no_server.add("Pilih nomor server!");
             		}else{
	             		semua_no_server.add(no_server_aktif);
             		}
             		//ambil data no server yg tidak aktif
            		tampil_semua_no_server();
            		Toast.makeText(getApplicationContext(), "Berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            	}
            }
        });
        builderTambahSaldo.setNegativeButton("Batal",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builderTambahSaldo.show();
    }
	
	public void hapus_server(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Konfirmasi");
	   	alertDialogBuilder
	    .setMessage("Hapus "+spin_no_server.getSelectedItem().toString()+" dari nomor server ?")
	    .setCancelable(false)
	    .setPositiveButton("Iya",new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog,int id) {
		    	 sqLiteHelper.hapus_no_server(spin_no_server.getSelectedItem().toString());
                    Toast.makeText(getApplicationContext(), spin_no_server.getSelectedItem().toString()+" berhasil dihapus", Toast.LENGTH_SHORT).show();
                    //ambil data no_server yg aktif
                    HashMap<String, String> hashMapnoserver = sqLiteHelper.info_no_server();
                    no_server_aktif = hashMapnoserver.get("no_server");
                    //mengecek apakah masih ada cs yang terpilih setelah penghapusan data
                    semua_no_server.clear();
                    if (no_server_aktif == null){
                    	semua_no_server.add("Pilih nomor server!");
                    	diatur = "n";
            		}else{
            			semua_no_server.add(no_server_aktif);
            		}
                    //ambil semua no_sever yg tdk aktif
	                tampil_semua_no_server();
	                arrayAdapter.notifyDataSetChanged();
	                spin_no_server.setSelection(0);
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
}
