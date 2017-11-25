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

public class Pengaturan_awal_aplikasi_2 extends Activity {
	Button btn_selanjutnya;
	WebView wb_info;
	EditText txt_saldo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pengaturan_awal_aplikasi_2);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#fe0000"));
		getActionBar().setBackgroundDrawable(colorDrawable);
		
		wb_info = (WebView) findViewById(R.id.webView1);
		btn_selanjutnya = (Button) findViewById(R.id.button1);
		txt_saldo = (EditText) findViewById(R.id.editText1);
		txt_saldo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
		
		//inisialisasi pesan
		String teks="<h4>Petunjuk</h4>"
				+ "<p align=justify>Jumlah saldo dapat diketahui dengan cara mengirimkan SMS dengan format SAL.[PIN ANDA] ke nomor server Universal Reload. Nomor server dapat dilihat dibawah ini :</p>"
				+ "<ol> <li>081-165-2829</li> <li>0821-6844-4555</li> <li>0852-8687-0888</li> <li>0852-6212-0555</li> <li>0819-637-900</li> <li>0819-61-3300</li> <li>0857-6388-7555</li> <li>0899-0058-111</li> ";
		wb_info.loadData(teks, "text/html", "UTF-8");
		
		//get data intent
		Intent intent = getIntent();
		final String pin = intent.getStringExtra("pin");
		
		//aksi untuk btn_selanjutnya
		btn_selanjutnya.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View v) {
				if (txt_saldo.getText().toString().equals("")){
					alert_salah();
				}else{
					Intent i = new Intent(getApplicationContext(), Pengaturan_awal_aplikasi_3.class);
					i.putExtra("pin", pin);
	 				i.putExtra("saldo", txt_saldo.getText().toString());
	 				startActivity(i);
	 				finish();
				}
 			}
 		});
		
	}
	
	@Override
	public void onBackPressed(){
		Intent i = new Intent(getApplicationContext(), Pengaturan_awal_aplikasi.class);
		i.putExtra("pesan", "dibaca");
		startActivity(i);
		finish();
	}
	
	public void alert_salah(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Peringatan");
		alertDialogBuilder
	    .setMessage("Jumlah saldo salah, mohon pastikan mengisi jumlah saldo dengan benar")
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
