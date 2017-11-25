package com.diegaim.universalreloadapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Transaksi_sudah_terkonfirmasi extends Activity implements OnClickListener {
	TableLayout tableData;
	Button loadmore,btn_cari;
	TableRow barisTabel;
	TextView viewHeader1,viewHeader2, txt_kosong;
	EditText txt_cari;
	
	ArrayList<Button> buttonEdit = new ArrayList<Button>();
    ArrayList<Button> buttonDelete = new ArrayList<Button>();
	
	//membuat objek untuk memanggil class sqlite helper
	SQLiteHelper sqLiteHelper = new SQLiteHelper(Transaksi_sudah_terkonfirmasi.this);
	ArrayList<HashMap<String, String>> array_data_tampung;
	
	Integer limit, counter_data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaksi_sudah_terkonfirmasi);
		
		tableData = (TableLayout) findViewById(R.id.table_data);
		loadmore = (Button) findViewById(R.id.button1);
		btn_cari = (Button) findViewById(R.id.button2);
		txt_cari = (EditText) findViewById(R.id.editText1);
		txt_kosong = (TextView) findViewById(R.id.textView1);
		
		//tambah listener untuk btn_cari
		btn_cari.setOnClickListener(this);
		//Untuk mengatur warna title bar agar berubah menjadi merah
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#fe0000"));
	    getActionBar().setBackgroundDrawable(colorDrawable);
	    
	    //ambil data
	    array_data_tampung = sqLiteHelper.tampil_transaksi_sudah_k();
	    if (array_data_tampung.size() == 0){
	    	loadmore.setVisibility(View.GONE);
			txt_kosong.setVisibility(View.VISIBLE);
	    }else{
	    	txt_kosong.setVisibility(View.GONE);
	    	
	    	//tampil data
		    barisTabel = new TableRow(this);
	        barisTabel.setBackgroundColor(Color.RED);

	        viewHeader1 = new TextView(this);
	        viewHeader2 = new TextView(this);

	        viewHeader1.setText("Transaksi"); viewHeader1.setTextColor(Color.parseColor("#FFFFFF"));viewHeader1.setGravity(Gravity.CENTER);
	        viewHeader2.setText("Status"); viewHeader2.setTextColor(Color.parseColor("#FFFFFF"));viewHeader2.setGravity(Gravity.CENTER);
	        
	        viewHeader1.setPadding(0, 10, 0, 10);
	        viewHeader2.setPadding(19, 10, 0, 10);
	        
	        barisTabel.addView(viewHeader1);
	        barisTabel.addView(viewHeader2);

	        tableData.addView(barisTabel, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		    
	        limit = 10;
		    counter_data = 0;
		    if (counter_data == array_data_tampung.size()) ((ViewGroup)loadmore.getParent()).removeView(loadmore);
	        
	        //mencetak data
		    for (int i = 0; i < limit; i++){
				if (counter_data < array_data_tampung.size()){
					tampil_data(array_data_tampung);
					counter_data++;
					if (counter_data == array_data_tampung.size()) loadmore.setVisibility(View.GONE);
				}
			}
	    }
	    
 		//aksi untuk loadmore
 		loadmore.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View v) {
				for (int i = 0; i < limit; i++){
					if (counter_data < array_data_tampung.size()){
						tampil_data(array_data_tampung);
						counter_data++;
						if (counter_data == array_data_tampung.size()) loadmore.setVisibility(View.GONE);						
					}
				}
 			}
 		});
	}
	
	public void tampil_data(ArrayList<HashMap<String, String>> array_data_tampung){
		// ambil masing-masing hasmap dari array_data_tampung
        HashMap<String, String> hashMapRecordData = array_data_tampung.get(counter_data);
        String id = hashMapRecordData.get("id");
        String waktu_tanggal = hashMapRecordData.get("waktu_tanggal");
        String nomor = hashMapRecordData.get("nomor");
        String operator = hashMapRecordData.get("operator");
        String nominal = hashMapRecordData.get("nominal");
        String status = hashMapRecordData.get("status");
		
		//cetak di logCat untuk percobaan saja
        System.out.println("id :" + id);
        System.out.println("waktu_tanggal :" + waktu_tanggal);
        System.out.println("nomor : " + nomor);
        System.out.println("operator :" + operator);
        System.out.println("nominal :" + nominal);
		
		//memulai cetak data
		barisTabel = new TableRow(this);

        if (counter_data % 2 == 0) {
            barisTabel.setBackgroundColor(Color.LTGRAY);
        }
		
		TextView viewId = new TextView(this);
		viewId.setText("Nomor : "+nomor+"\nOperator : "+operator+"\nNominal : "+nominal+"\n\n"+waktu_tanggal);
		viewId.setPadding(20, 20, 20, 40);
		barisTabel.addView(viewId);
		
		TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.topMargin = 55;
		params.leftMargin = 50;
		params.rightMargin = 20;
		
		TextView viewId2 = new TextView(this);
		viewId2.setText(status);
		viewHeader2.setGravity(Gravity.CENTER);
		//viewId2.setPadding(100, 20, 100, 40);
		viewId2.setLayoutParams(params);
		barisTabel.addView(viewId2);
		tableData.addView(barisTabel, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	@Override
    public void onClick(View view) {
		if (view == btn_cari){			
			loadmore.setVisibility(View.VISIBLE);
			txt_kosong.setText("Hasil pencarian kosong");
			
			//ambil data
			String temp;
			if (txt_cari.getText().toString().equals("")){
				temp = "0";
				Toast.makeText(getApplicationContext(), "Menampilkan semua transaksi", Toast.LENGTH_SHORT).show();				
			}else{
				temp = txt_cari.getText().toString();
				Toast.makeText(getApplicationContext(), "Menampilkan hasil pencarian dari : "+temp, Toast.LENGTH_SHORT).show();
			}
			
		    array_data_tampung = sqLiteHelper.cari_transaksi(temp,"Berhasil");
		    
		    if (array_data_tampung.size() == 0){
		    	loadmore.setVisibility(View.GONE);
		    	txt_kosong.setVisibility(View.VISIBLE);
		    	//menghapus isi table 
			    tableData.removeAllViews();
		    }else{
		    	txt_kosong.setVisibility(View.GONE);
		    	
		    	//menghapus isi table 
			    tableData.removeAllViews();
		    	//tampil data
			    barisTabel = new TableRow(this);
		        barisTabel.setBackgroundColor(Color.RED);
		
		        viewHeader1 = new TextView(this);
		        viewHeader2 = new TextView(this);
		
		        viewHeader1.setText("Transaksi"); viewHeader1.setTextColor(Color.parseColor("#FFFFFF"));viewHeader1.setGravity(Gravity.CENTER);
		        viewHeader2.setText("Status"); viewHeader2.setTextColor(Color.parseColor("#FFFFFF"));viewHeader2.setGravity(Gravity.CENTER);
		        
		        viewHeader1.setPadding(0, 10, 0, 10);
		        viewHeader2.setPadding(19, 10, 0, 10);
		        
		        barisTabel.addView(viewHeader1);
		        barisTabel.addView(viewHeader2);
		
		        tableData.addView(barisTabel, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			    
		        limit = 10;	//atur limit tampil
			    counter_data = 0;
		        //mencetak data
			    for (int i = 0; i < limit; i++){
					if (counter_data < array_data_tampung.size()){
						tampil_data(array_data_tampung);
						counter_data++;
						if (counter_data == array_data_tampung.size()) loadmore.setVisibility(View.GONE);
					}
				}
		    }		    
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.transaksi_sudah_terkonfirmasi, menu);
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
		}else if (id == R.id.men_catatan_transaksi) {
			Intent i = new Intent(getApplicationContext(), Catatan_transaksi.class);
			startActivity(i);
			finish();
		}else if (id == R.id.men_cari_transaksi) {
			cari_trans();
		}else if (id == R.id.men_catatan_transaksi_belum_k) {
			Intent i = new Intent(getApplicationContext(), Transaksi_belum_terkonfirmasi.class);
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
        		String kata_kunci_cari;
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
	
	@Override
	public void onBackPressed(){
		Intent i = new Intent(getApplicationContext(), Catatan_transaksi.class);
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
