package com.diegaim.universalreloadapp;

import java.util.ArrayList;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

@SuppressLint("NewApi")
public class Pengaturan_awal_aplikasi_3 extends Activity implements AdapterView.OnItemSelectedListener {
	Button btn_simpan;
	WebView wb_info;
	Spinner sp_server;
	ArrayList<String> semua_no_server = new ArrayList<String>();
	ArrayAdapter<String> arrayAdapter;
	
	SQLiteHelper sqLiteHelper = new SQLiteHelper(Pengaturan_awal_aplikasi_3.this);
	String pin, saldo, no_server_aktif;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pengaturan_awal_aplikasi_3);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#fe0000"));
		getActionBar().setBackgroundDrawable(colorDrawable);
		
		wb_info = (WebView) findViewById(R.id.webView1);
		btn_simpan = (Button) findViewById(R.id.button1);
		sp_server = (Spinner) findViewById(R.id.spinner1);
		
		//inisialisasi pesan
		String teks="<h4>Petunjuk</h4>"
				+ "<p align=justify>Nomor server digunakan untuk mengirimkan SMS transaksi ke Agen pulsa Universal Reload agar transaksi dapat diproses.</p>";
		wb_info.loadData(teks, "text/html", "UTF-8");
		
		//inisialisasi nilai pada spinner
		semua_no_server.add("Pilih nomor server");
		//mengambil data no server
		tampil_semua_no_server();
		//untuk set item pada spinner
		sp_server.setOnItemSelectedListener(this);
		arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, semua_no_server);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_server.setAdapter(arrayAdapter);
		sp_server.setSelection(0);
		
		//get data intent
		Intent intent = getIntent();
		pin = intent.getStringExtra("pin");
		saldo = intent.getStringExtra("saldo");
		
		//aksi untuk btn_simpan
		btn_simpan.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View v) {
				if (sp_server.getSelectedItem().toString().equals("Pilih nomor server")){
					alert_salah();
				}else{
					//simpan
					String a= pin;
					String b = saldo;
					String c = sp_server.getSelectedItem().toString();
					sqLiteHelper.tambah_akun(a, b);
					sqLiteHelper.simpan_pengaturan_baru(c);
					alert_berhasil();
				}
 			}
 		});
	}
	
	public void tampil_semua_no_server(){
		ArrayList<HashMap<String, String>> data = sqLiteHelper.tampil_semua_no_server();
		for (int i = 0; i < data.size(); i++) {
			semua_no_server.add(data.get(i).get("no_server"));
		}
	}
	
	@Override
	public void onBackPressed(){
		Intent i = new Intent(getApplicationContext(), Pengaturan_awal_aplikasi_2.class);
		i.putExtra("pin", pin);
		startActivity(i);
		finish();
	}
	
	public void alert_salah(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Peringatan");
		alertDialogBuilder
	    .setMessage("Nomor server belum dipilih, mohon pilih nomor server terlebih dahulu")
	    .setCancelable(false)
	    .setNegativeButton("Mengerti",new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog,int id) {
		    	 dialog.cancel();
		     }
	    });
	    AlertDialog alertDialog = alertDialogBuilder.create();
	    alertDialog.show();
	}
	
	public void alert_berhasil(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Perberitahuan");
		alertDialogBuilder
	    .setMessage("Pengaturan telah selesai, aplikasi siap untuk digunakan.")
	    .setCancelable(false)
	    .setNegativeButton("Oke",new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog,int id) {
		    	//buka menu utama
				Intent i = new Intent(getApplicationContext(), Menu_utama.class);
 				startActivity(i);
 				finish();
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
		// TODO Auto-generated method stub
	}
}
