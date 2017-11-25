package com.diegaim.universalreloadapp;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Splash_screen extends Activity {
	SQLiteHelper sqLiteHelper = new SQLiteHelper(Splash_screen.this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash_screen);
		
		new CountDownTimer(2000, 1000) {						
		    public void onTick(long millisUntilFinished) {
		    }

		    public void onFinish() {
		    	cek_pengaturan();
		    }
		}.start();
	}
	
	public void cek_pengaturan(){
		//ambil data pin dan saldo
    	HashMap<String, String> hashMapHasil = sqLiteHelper.info_akun();
    	int hasil_baca =  hashMapHasil.size();
		
    	if (hasil_baca == 0){
    		String pin = null, saldo = null;
          	HashMap<String, String> hashMapInput = sqLiteHelper.info_akun();
           	if (hashMapInput.get("pin") == null){
           		try{
           			sqLiteHelper.hapus_isi_table();
           			
           			sqLiteHelper.tambah_no_server("0811652829", "tdk_dipilih");
					sqLiteHelper.tambah_no_server("082168444555", "tdk_dipilih");
					sqLiteHelper.tambah_no_server("085286870888", "tdk_dipilih");
					sqLiteHelper.tambah_no_server("085262120555", "tdk_dipilih");
					sqLiteHelper.tambah_no_server("0819637900", "tdk_dipilih");
					sqLiteHelper.tambah_no_server("0819613300", "tdk_dipilih");
					sqLiteHelper.tambah_no_server("085763887555", "tdk_dipilih");
					sqLiteHelper.tambah_no_server("08990058111", "tdk_dipilih");
					sqLiteHelper.tambah_no_server("083194653222", "tdk_dipilih");
           	    	
           	    	sqLiteHelper.tambah_transaksi("20170829001","081298930755", "XL", "50000","Berhasil");
           	    	sqLiteHelper.tambah_transaksi("20170829002","081298930756", "Axis", "10000","Berhasil");
           	    	sqLiteHelper.tambah_transaksi("20170829003","081298930757", "Telkomsel", "25000","Berhasil");
           	    	sqLiteHelper.tambah_transaksi("20170829004","081298930758", "XL", "50000","Berhasil");
           	    	sqLiteHelper.tambah_transaksi("20170829005","081298930759", "Axis", "10000","Berhasil");
           	    	sqLiteHelper.tambah_transaksi("20170829006","081298930750", "Telkomsel", "25000","Berhasil");
           	    	sqLiteHelper.tambah_transaksi("20170829007","081298930751", "XL", "50000","Gagal");
           	    	sqLiteHelper.tambah_transaksi("20170829008","081298930752", "Axis", "10000","Gagal");
           	    	sqLiteHelper.tambah_transaksi("20170829009","081298930754", "Telkomsel", "25000","Gagal");
           	    	sqLiteHelper.tambah_transaksi("20170829010","081298930755", "XL", "50000","Gagal");
           	    	sqLiteHelper.tambah_transaksi("20170829011","021298930756", "3", "10000","Gagal");
           	    	sqLiteHelper.tambah_transaksi("20170829012","021298930757", "IM3", "25000","Gagal");
           	    	sqLiteHelper.tambah_transaksi("20170829013","021298930755", "As", "50000","Gagal");
           	    	sqLiteHelper.tambah_transaksi("20170829014","021298930756", "Smartfren", "10000","Menunggu konfirmasi");
           	    	sqLiteHelper.tambah_transaksi("20170829015","021298930757", "3", "25000","Menunggu konfirmasi"); 
           		}catch(Exception e){
           	    	Toast.makeText(getApplicationContext(), "Database gagal dibuat, Aplikasi tidak berjalan dengan normal!" + e.toString(), Toast.LENGTH_SHORT).show();
           	    }
           	}
    		
    		Intent i = new Intent(getApplicationContext(), Pengaturan_awal_aplikasi.class);
    		i.putExtra("pesan", "belum dibaca");
    		startActivity(i);
    		finish();
    	}else{
    		Intent i = new Intent(getApplicationContext(), Menu_utama.class);
    		startActivity(i);
    		finish();
    	}
	}
	
	@Override
	public void onBackPressed(){
		
	}
}
