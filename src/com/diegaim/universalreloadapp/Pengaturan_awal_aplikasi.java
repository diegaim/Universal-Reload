package com.diegaim.universalreloadapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class Pengaturan_awal_aplikasi extends Activity {
	Button btn_selanjutnya;
	WebView wb_info;
	EditText txt_pin;
	
	String pesan;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pengaturan_awal_aplikasi);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#fe0000"));
		getActionBar().setBackgroundDrawable(colorDrawable);
		
		
		wb_info = (WebView) findViewById(R.id.webView1);
		btn_selanjutnya = (Button) findViewById(R.id.button1);
		txt_pin = (EditText) findViewById(R.id.editText1);
		txt_pin.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
		
		//inisialisasi pesan
		String teks="<h4>Petunjuk</h4>"
				+ "<p align=justify>PIN digunakan untuk melakukan validasi saat melakukan transaksi penjualan. PIN didapatkan saat anda mendaftar pada Agen pulsa Universal Reload yang berupa 4 digit angka. Apabila anda belum terdaftar pada Agen pulsa Universal Reload, anda perlu untuk datang ke gerai yang bekerja sama dengan Universal Reload untuk melakukan pendaftaran sebagai member.</p>";
		wb_info.loadData(teks, "text/html", "UTF-8");
		
		//inisialisasi selamat datang
		Intent intent = getIntent();
		String pesan = intent.getStringExtra("pesan");
		
		if (pesan.equals("belum dibaca")){
			selamat_datang();
		}
		
		
		//aksi untuk btn_selanjutnya
		btn_selanjutnya.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View v) {
				if (txt_pin.getText().toString().equals("") || txt_pin.getText().toString().length() < 4 ){
					alert_salah();
				}else{
					Intent i = new Intent(getApplicationContext(), Pengaturan_awal_aplikasi_2.class);
	 				i.putExtra("pin", txt_pin.getText().toString());
	 				startActivity(i);
	 				finish();
				}
 			}
 		});
	}
	
	@Override
	public void onBackPressed(){
		alert_isi();
	}
	
	public void selamat_datang(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Selamat datang");
		alertDialogBuilder
	    .setMessage("Aplikasi memerlukan beberapa data sebelum dapat digunakan, mohon isi form yang tersedia.")
	    .setCancelable(false)
	    .setNegativeButton("Oke",new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog,int id) {
		    	 dialog.cancel();
		     }
	    });
	    AlertDialog alertDialog = alertDialogBuilder.create();
	    alertDialog.show();
	}
	
	public void alert_isi(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Peringatan");
		alertDialogBuilder
	    .setMessage("Mohon selesaikan proses pengaturan awal aplikasi agar aplikasi dapat digunakan")
	    .setCancelable(false)
	    .setNegativeButton("Oke",new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog,int id) {
		    	 dialog.cancel();
		     }
	    });
	    AlertDialog alertDialog = alertDialogBuilder.create();
	    alertDialog.show();
	}
	
	public void alert_salah(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Peringatan");
		alertDialogBuilder
	    .setMessage("PIN salah, mohon pastikan mengisi pin dengan benar")
	    .setCancelable(false)
	    .setNegativeButton("Oke",new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog,int id) {
		    	 dialog.cancel();
		     }
	    });
	    AlertDialog alertDialog = alertDialogBuilder.create();
	    alertDialog.show();
	}
}
