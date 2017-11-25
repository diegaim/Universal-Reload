package com.diegaim.universalreloadapp;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Menu_utama extends Activity {
	Button btn_transaksi_baru,btn_catatan_transaksi, btn_pengaturan, btn_bantuan, btn_tentang, btn_keluar;
	ImageButton btn_tambah_saldo;
	TextView txt_saldo;
	String saldo, pin;
	
	//membuat objek untuk memanggil class sqlite helper
	SQLiteHelper sqLiteHelper = new SQLiteHelper(Menu_utama.this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_utama);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#fe0000"));
	    getActionBar().setBackgroundDrawable(colorDrawable);
			    
		btn_transaksi_baru = (Button) findViewById(R.id.button1);
		btn_catatan_transaksi = (Button) findViewById(R.id.button2);
		btn_pengaturan = (Button) findViewById(R.id.button3);
		btn_bantuan = (Button) findViewById(R.id.button4);
		btn_tentang = (Button) findViewById(R.id.button5);
		btn_keluar = (Button) findViewById(R.id.button6);
		txt_saldo = (TextView) findViewById(R.id.textView2);
		btn_tambah_saldo = (ImageButton) findViewById(R.id.imageButton1);
		
	    ambil_akun();
	    if (pin == null || pin.equals("") || saldo == null || saldo.equals("")){
	    	alert_atur_app();
	    }else{
	    	HashMap<String, String> hashMapnoserver = sqLiteHelper.info_no_server();
	        String no_server_aktif = hashMapnoserver.get("no_server");
	        if (no_server_aktif == null){
	        	alert_atur_app();
			}
	    }
		
	    //aksi untuk btn_transaksi_baru
	    btn_transaksi_baru.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View v) {
        		Intent i = new Intent(getApplicationContext(), Transaksi_baru.class);
     			startActivity(i);
     			finish();
 			}
 		});
	    
	    //aksi untuk btn_catatan_transaksi
	    btn_catatan_transaksi.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View v) {
 				Intent i = new Intent(getApplicationContext(), Catatan_transaksi.class);
 				startActivity(i);
 				finish();
 			}
 		});
	    
	    //aksi untuk btn_pengaturan
 		btn_pengaturan.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View v) {
 				Intent i = new Intent(getApplicationContext(), Pengaturan.class); 				
 				startActivity(i);
 				finish();
 			}
 		});
 		
 		//aksi untuk btn_tambah_saldo
 		btn_tambah_saldo.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View v) {
 				tambah_saldo();
 			}
 		});
	    
	    //aksi untuk btn_bantuan
		btn_bantuan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), Bantuan.class);
				startActivity(i);
				finish();
			}
		});
		
		//aksi untuk btn_tentang
		btn_tentang.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), Tentang.class);
				startActivity(i);
				finish();
			}
		});
		
		//aksi untuk btn_keluar
		btn_keluar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				keluar_alert();
			}
		});
	}
	
	@Override
	public void onBackPressed(){
		//untuk matiin fungsi back button
		keluar_alert();
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
	
	public void alert_atur_app(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		
		alertDialogBuilder.setTitle("Peringatan");
		alertDialogBuilder
	    .setMessage("Aplikasi membutuhkan informasi Sisa Saldo dan PIN Universal Reload anda. Mohon lakukan pengaturan aplikasi agar aplikasi dapat berjalan.")
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
	
	public void ambil_akun(){
        try{
        	//ambil data pin dan saldo
        	HashMap<String, String> hashMapHasil = sqLiteHelper.info_akun();
        	for (int i = 0; i < hashMapHasil.size(); i++) {
                pin = hashMapHasil.get("pin");
                saldo = hashMapHasil.get("saldo");
                txt_saldo.setText("Rp."+saldo);
            }
        }catch(Exception e){
        	Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        } 
	}
	
	public void tambah_saldo() {
        LinearLayout layoutInput = new LinearLayout(this);
        layoutInput.setOrientation(LinearLayout.VERTICAL);

        final EditText txt_tambah_saldo = new EditText(this);
        txt_tambah_saldo.setHint("Saldo");
        txt_tambah_saldo.setInputType(InputType.TYPE_CLASS_NUMBER);
        txt_tambah_saldo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        layoutInput.addView(txt_tambah_saldo);

        AlertDialog.Builder builderInsertBiodata = new AlertDialog.Builder(this);
        builderInsertBiodata.setTitle("Tambah Saldo");
        builderInsertBiodata.setView(layoutInput);
        builderInsertBiodata.setPositiveButton("Tambah",new DialogInterface.OnClickListener() {
        	@Override
            public void onClick(DialogInterface dialog, int which) {
            	//cek apakah ada inputan yg dimasukkan
        		if (txt_tambah_saldo.getText().toString().equals("")){
            		Toast.makeText(getApplicationContext(), "Tidak ada nomor nominal saldo yang dimasukkan", Toast.LENGTH_SHORT).show();
            	}else{
            		int saldo_tambah = Integer.parseInt(saldo) + Integer.parseInt(txt_tambah_saldo.getText().toString());
            		sqLiteHelper.tambah_saldo(saldo_tambah);
            		saldo = String.valueOf(saldo_tambah);
            		txt_saldo.setText("Rp."+saldo);
            		Toast.makeText(getApplicationContext(), "Saldo telah berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            	}
            }
        });

        builderInsertBiodata.setNegativeButton("Batal",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builderInsertBiodata.show();
    }
}