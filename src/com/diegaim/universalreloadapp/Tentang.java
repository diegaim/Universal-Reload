package com.diegaim.universalreloadapp;

import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

@SuppressLint("NewApi")
public class Tentang extends ActionBarActivity {
	WebView wb_desc;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tentang);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#fe0000"));
	    getActionBar().setBackgroundDrawable(colorDrawable);
	    
		wb_desc = (WebView) findViewById(R.id.webView1);		
	    
	    /*String teks="Universal Reload <br><div align=justify>Merupakan salah satu agen pulsa yang ada diIndonesia yang beralamat di : Jl. Bilal Ujung No.1C, Pulo Brayan Darat I, Medan Tim., Kota Medan, Sumatera Utara 20116. </div>"
	    		+ "<br><hr><div align=center>Aplikasi ini dibuat oleh :"
	    		+ "<br>Diega Iqbal Mardana "
	    		+ "<br>12113441"
	    		+ "<br>Sistem Informasi</div>";*/
		String teks="<p align=justify>Aplikasi ini dibuat untuk membantu penjual pulsa yang menggunakan jasa Agen pulsa Universal Reload melakukan penjualan pulsa.</p>"
				+ "<hr><div align=center>Aplikasi ini dibuat oleh :"
	    		+ "<br><br>Diega Iqbal Mardana "
	    		+ "<br>12113441"
	    		+ "<br>Sistem Informasi"
	    		+ "<br>Universitas Gunadarma</div>";
	    wb_desc.loadData(teks, "text/html", "UTF-8");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tentang, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.men_menu_utama) {
			Intent i = new Intent(getApplicationContext(), Menu_utama.class);
			startActivity(i);
			finish();
		}else if (id == R.id.men_transaksi_baru) {
			Intent i = new Intent(getApplicationContext(), Transaksi_baru.class);
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
		}else if (id == R.id.men_catatan_transaksi) {
			Intent i = new Intent(getApplicationContext(), Catatan_transaksi.class);
			startActivity(i);
			finish();
		}else if (id == R.id.men_keluar) {
			keluar_alert();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed(){
		Intent i = new Intent(getApplicationContext(), Menu_utama.class);
		startActivity(i);
		finish();
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
}
