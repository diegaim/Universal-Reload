package com.diegaim.universalreloadapp;

import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.InputType;

import java.util.ArrayList;
import java.util.Calendar;
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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Catatan_transaksi extends ActionBarActivity {
	Button btn_cari_k, btn_sudah_k, btn_belum_k, btn_hapus_transaksi;
	String kata_kunci_cari;
	WebView txt_rangkuman;
	
	SQLiteHelper sqLiteHelper = new SQLiteHelper(Catatan_transaksi.this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catatan_transaksi);
		
		btn_cari_k = (Button) findViewById(R.id.button4);
		btn_sudah_k = (Button) findViewById(R.id.button1);
		btn_belum_k = (Button) findViewById(R.id.button2);
		btn_hapus_transaksi = (Button) findViewById(R.id.button3);
		txt_rangkuman = (WebView) findViewById(R.id.webView1);
		
		//Untuk mengatur warna title bar agar berubah menjadi merah
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#fe0000"));
	    getActionBar().setBackgroundDrawable(colorDrawable);
	    
	    //buat rangkuman total transaksi
	    cek_jml_transaksi();
	    
	    //cek apakah ada transaksi yang tercatat
	    ArrayList<HashMap<String, String>>  t_konf = sqLiteHelper.tampil_transaksi_sudah_k();
	    ArrayList<HashMap<String, String>> t_blm_konf = sqLiteHelper.tampil_transaksi_belum_k();
	    if (t_konf.size() == 0 && t_blm_konf.size() == 0){
	    	btn_hapus_transaksi.setVisibility(View.GONE);
	    }
	    
	    //aksi untuk btn_cari_k
	    btn_cari_k.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View v) {
				cari_trans();
 			}
 		});
	    
	    //aksi untuk btn_sudah_k
	    btn_sudah_k.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View v) {
 				Intent i = new Intent(getApplicationContext(), Transaksi_sudah_terkonfirmasi.class);
 				startActivity(i);
 				finish();
 			}
 		});
	    
	    //aksi untuk btn_belum_k
	    btn_belum_k.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View v) {
 				Intent i = new Intent(getApplicationContext(), Transaksi_belum_terkonfirmasi.class);
 				startActivity(i);
 				finish();
 			}
 		});
	    
	    //aksi untuk btn_hapus_transaksi
	    btn_hapus_transaksi.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View v) {
 				yakin_hapus();
 			}
 		});
	}
	
	public void cari_trans() {
        LinearLayout layoutInput = new LinearLayout(this);
        layoutInput.setOrientation(LinearLayout.VERTICAL);

        final EditText txt_tambah_saldo = new EditText(this);
        txt_tambah_saldo.setHint("Nomor Telepon");
        txt_tambah_saldo.setInputType(InputType.TYPE_CLASS_NUMBER);
        txt_tambah_saldo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        layoutInput.addView(txt_tambah_saldo);

        AlertDialog.Builder builderInsertBiodata = new AlertDialog.Builder(this);
        builderInsertBiodata.setTitle("Masukkan kata kunci pencarian");
        builderInsertBiodata.setView(layoutInput);
        builderInsertBiodata.setNegativeButton("Cari",new DialogInterface.OnClickListener() {
        	@Override
            public void onClick(DialogInterface dialog, int which) {
            	//cek apakah ada inputan yg dimasukkan
        		if (txt_tambah_saldo.getText().toString().equals("")){
            		kata_kunci_cari = "";
            	}else{
            		kata_kunci_cari = txt_tambah_saldo.getText().toString();
            	}
        		//buka halaman cari transaksi
        		Intent i = new Intent(getApplicationContext(), Cari_transaksi.class);
 				i.putExtra("kata_kunci_cari", kata_kunci_cari);
 				startActivity(i); 				
 				finish();
            }
        });

        builderInsertBiodata.setPositiveButton("Batal",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builderInsertBiodata.show();
    }
	
	public void cek_jml_transaksi(){
		//dapetin tanggal hari ini
		Calendar cc = Calendar.getInstance();
		int year=cc.get(Calendar.YEAR);
		int month=cc.get(Calendar.MONTH) + 1;
		String str_month;
		if (month < 10){
			str_month = "0"+String.valueOf(month);
		}else{
			str_month = String.valueOf(month);
		}
		int mDay = cc.get(Calendar.DAY_OF_MONTH);
		String str_day;
		if (mDay < 10){
			str_day = "0"+String.valueOf(mDay);
		}else{
			str_day = String.valueOf(mDay);
		}
		String waktu;
		
		//cek hari ini
		waktu = year+"-"+str_month+"-"+str_day;
    	ArrayList<HashMap<String, String>> hashMapHasil = sqLiteHelper.total_transaksi_hari_ini(waktu);
    	String tot_hari_ini =  String.valueOf(hashMapHasil.size());
    	
    	//cek bulan ini
    	hashMapHasil = sqLiteHelper.total_transaksi_bulan_ini(str_month, String.valueOf(year));
    	String tot_bulan_ini =  String.valueOf(hashMapHasil.size());
    	
    	//cek bulan kemarin
    	if (month < 10){
    		month = month - 1;
			str_month = "0"+String.valueOf(month);
		}else{
			month = month - 1;
			str_month = String.valueOf(month);
		}
    	hashMapHasil = sqLiteHelper.total_transaksi_bulan_kemarin(str_month, String.valueOf(year));
    	String tot_bulan_kemarin =  String.valueOf(hashMapHasil.size());
    	
    	//cek semua transaksi
    	hashMapHasil = sqLiteHelper.total_transaksi();
    	String tot_transaksi =  String.valueOf(hashMapHasil.size());
    	
    	//tampilkan di webview
    	String teks="<div align=center>"
	    		+ "<h4>JUMLAH TRANSAKSI</h4></div>"
	    		+ "<table border=0 width=270>"
	    		+ "<tr>"
	    		+ "<td>Hari ini</td>"
	    		+ "<td>"+tot_hari_ini+"</td>"
	    		+ "</tr>"
	    		+ "<tr>"
	    		+ "<td>Bulan ini</td>"
	    		+ "<td>"+tot_bulan_ini+"</td>"
	    		+ "</tr>"
	    		+ "<tr>"
	    		+ "<td>Bulan  Kemarin</td>"
	    		+ "<td>"+tot_bulan_kemarin+"</td>"
	    		+ "</tr>"
	    		+ "<tr>"
	    		+ "<td>Total Transaksi</td>"
	    		+ "<td>"+tot_transaksi+"</td>"
	    		+ "</tr>"
	    		+ "</table>";
    	txt_rangkuman.loadData(teks, "text/html", "UTF-8");
	}
	
	public void yakin_hapus(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Konfirmasi");
		alertDialogBuilder
	    .setMessage("Hapus semua catatan transaksi?")
	    .setCancelable(false)
	    .setPositiveButton("Iya",new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog,int id) {
		    	sqLiteHelper.hapus_semua_transaksi();
		    	btn_hapus_transaksi.setVisibility(View.GONE);
		    	Toast.makeText(getApplicationContext(), "Berhasil mengahapus semua transaksi", Toast.LENGTH_SHORT).show();
		 	    cek_jml_transaksi();
		    	dialog.cancel();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.catatan_transaksi, menu);
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
