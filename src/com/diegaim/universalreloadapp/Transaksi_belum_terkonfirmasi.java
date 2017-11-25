package com.diegaim.universalreloadapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Transaksi_belum_terkonfirmasi extends Activity implements OnClickListener {
	TableLayout tableData;
	Button loadmore, btn_cari;
	TableRow barisTabel;
	TextView viewHeader1,viewHeader2,txt_kosong;
	EditText txt_cari;
	
	ArrayList<Button> buttonKonfirmasi = new ArrayList<Button>();
	
	//membuat objek untuk memanggil class sqlite helper
	SQLiteHelper sqLiteHelper = new SQLiteHelper(Transaksi_belum_terkonfirmasi.this);
	ArrayList<HashMap<String, String>> array_data_tampung;
	
	ArrayList<Long> id_btn_konfr = new ArrayList<Long>();
	Integer limit, counter_data, jml_btn_konfr;
	String update_saldo, saldo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaksi_belum_terkonfirmasi);
		
		tableData = (TableLayout) findViewById(R.id.table_data);
		loadmore = (Button) findViewById(R.id.button1);
		btn_cari = (Button) findViewById(R.id.button2);
		txt_cari = (EditText) findViewById(R.id.editText1);
		txt_kosong = (TextView) findViewById(R.id.textView1);
		
		//Untuk mengatur warna title bar agar berubah menjadi merah
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#fe0000"));
	    getActionBar().setBackgroundDrawable(colorDrawable);
	    
	    //set nilai awal jml_btn_konfr
	    jml_btn_konfr = 0;
	    
	    //set listener untuk btn_cari
	    btn_cari.setOnClickListener(this);
	    
	    //ambil data
	    array_data_tampung = sqLiteHelper.tampil_transaksi_belum_k();
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
	        viewHeader2.setText("Aksi"); viewHeader2.setTextColor(Color.parseColor("#FFFFFF"));viewHeader2.setGravity(Gravity.CENTER);
	        
	        viewHeader1.setPadding(0, 10, 0, 10);
	        viewHeader2.setPadding(0, 10, 0, 10);
	        
	        barisTabel.addView(viewHeader1);
	        barisTabel.addView(viewHeader2);

	        tableData.addView(barisTabel, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		    
	        limit = 10;	//atur limit tampil
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
		viewId.setText("Nomor : "+nomor+"\nOperator : "+operator+"\nNominal : "+nominal+"\nStatus :\n"+status+"\n\n"+waktu_tanggal);
		viewId.setPadding(20, 20, 25, 40);
		barisTabel.addView(viewId);
		
		TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.topMargin = 55;
		params.leftMargin = 20;
		params.rightMargin = 20;
		
		//simpan id ke array list
		id_btn_konfr.add(Long.parseLong(id));
		
		buttonKonfirmasi.add(counter_data, new Button(this));
		buttonKonfirmasi.get(counter_data).setId(jml_btn_konfr);
		jml_btn_konfr++;
		buttonKonfirmasi.get(counter_data).setTag("Konfirmasi");
		buttonKonfirmasi.get(counter_data).setText("Konfirmasi");
		buttonKonfirmasi.get(counter_data).setTextSize(15);
		buttonKonfirmasi.get(counter_data).setBackgroundColor(Color.parseColor("#0146fe"));
		buttonKonfirmasi.get(counter_data).setTextColor(Color.parseColor("#FFFFFF"));
		buttonKonfirmasi.get(counter_data).setLayoutParams(params);
		buttonKonfirmasi.get(counter_data).setOnClickListener(this);
		barisTabel.addView(buttonKonfirmasi.get(counter_data));
		tableData.addView(barisTabel, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	@Override
    public void onClick(View view) {
		if (view == btn_cari){
			//reset array list
			buttonKonfirmasi.clear();
			txt_kosong.setText("Hasil pencarian kosong");
			loadmore.setVisibility(View.VISIBLE);			
			
			//ambil data
			String temp;
			if (txt_cari.getText().toString().equals("")){
				temp = "0";
				Toast.makeText(getApplicationContext(), "Menampilkan semua transaksi", Toast.LENGTH_SHORT).show();
			}else{
				temp = txt_cari.getText().toString();
				Toast.makeText(getApplicationContext(), "Menampilkan hasil pencarian dari : "+temp, Toast.LENGTH_SHORT).show();
			}
		    
			array_data_tampung = sqLiteHelper.cari_transaksi(temp,"Menunggu konfirmasi");
			
			if (array_data_tampung.size() == 0){
				tableData.removeAllViews();
				loadmore.setVisibility(View.GONE);
				txt_kosong.setVisibility(View.VISIBLE);
			}else{
				loadmore.setVisibility(View.VISIBLE);
				txt_kosong.setVisibility(View.GONE);
				
				//menghapus isi table 
			    tableData.removeAllViews();
			    
			    //tampil data
			    barisTabel = new TableRow(this);
		        barisTabel.setBackgroundColor(Color.RED);

		        viewHeader1 = new TextView(this);
		        viewHeader2 = new TextView(this);

		        viewHeader1.setText("Transaksi"); viewHeader1.setTextColor(Color.parseColor("#FFFFFF"));viewHeader1.setGravity(Gravity.CENTER);
		        viewHeader2.setText("Aksi"); viewHeader2.setTextColor(Color.parseColor("#FFFFFF"));viewHeader2.setGravity(Gravity.CENTER);
		        
		        viewHeader1.setPadding(0, 10, 0, 10);
		        viewHeader2.setPadding(0, 10, 0, 10);
		        
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
		}else{
			for (int i = 0; i < buttonKonfirmasi.size(); i++) {
				/* jika yang diklik adalah button edit */
				if (view.getId() == buttonKonfirmasi.get(i).getId() && view.getTag().toString().trim().equals("Konfirmasi")) {
	                final int id_data = buttonKonfirmasi.get(i).getId();
	                
	        		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	        		alertDialogBuilder.setTitle("Pemberitahuan");
	        		alertDialogBuilder
	    		    .setMessage("Transaksi berhasil dikonfirmasi")
	    		    .setCancelable(false)
	    		    .setNegativeButton("Oke",new DialogInterface.OnClickListener() {
		   			     public void onClick(DialogInterface dialog,int id) {
		   			    	buttonKonfirmasi.clear();
			   			    konfirm(String.valueOf(id_btn_konfr.get(id_data)), "Berhasil");
			   			    
			   			    //update saldo setelah konfirmasi.
			   				HashMap<String, String> hasMapKonfirmSaldo = sqLiteHelper.get_info_transaksi_belum_k(String.valueOf(id_btn_konfr.get(id_data)));
			   				for (int i = 0; i < hasMapKonfirmSaldo.size(); i++) {
			   		            update_saldo = hasMapKonfirmSaldo.get("nominal");
			   		        }
			   				//ambil saldo sekarang
			   				HashMap<String, String> hashMapBiodata = sqLiteHelper.info_akun();
			   		    	for (int i = 0; i < hashMapBiodata.size(); i++) {
			   		            saldo = hashMapBiodata.get("saldo");
			   		        }
			   		    	//hitung
			   		    	int hasil_saldo = Integer.parseInt(saldo) - Integer.parseInt(update_saldo) - 800;
			   		    	//update saldo kedatabase
			   		    	sqLiteHelper.tambah_saldo(hasil_saldo);
		   			     }
	    		    });
	    		    AlertDialog alertDialog = alertDialogBuilder.create();
	    		    alertDialog.show();
            	}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.transaksi_belum_terkonfirmasi, menu);
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
		}else if (id == R.id.men_catatan_transaksi_sudah_k) {
			Intent i = new Intent(getApplicationContext(), Transaksi_sudah_terkonfirmasi.class);
			i.putExtra("kata_kunci_cari", "");
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
	
	public void konfirm(String id, String status){
		loadmore.setVisibility(View.VISIBLE);
		
		array_data_tampung = sqLiteHelper.konfirmasi_manual_transaksi(id,status);
		if (array_data_tampung.size() == 0){
			tableData.removeAllViews();
			loadmore.setVisibility(View.GONE);
			txt_kosong.setVisibility(View.VISIBLE);
		}else{
			loadmore.setVisibility(View.VISIBLE);
			txt_kosong.setVisibility(View.GONE);
			
			//menghapus isi table 
		    tableData.removeAllViews();
		    
		    //mencetak isi table
		    barisTabel = new TableRow(this);
	        barisTabel.setBackgroundColor(Color.RED);

	        viewHeader1 = new TextView(this);
	        viewHeader2 = new TextView(this);

	        viewHeader1.setText("Transaksi"); viewHeader1.setTextColor(Color.parseColor("#FFFFFF"));
	        viewHeader2.setText("Aksi"); viewHeader2.setTextColor(Color.parseColor("#FFFFFF"));
	        
	        viewHeader1.setPadding(120, 10, 20, 20);
	        viewHeader2.setPadding(100, 10, 15, 20);
	        
	        barisTabel.addView(viewHeader1);
	        barisTabel.addView(viewHeader2);

	        tableData.addView(barisTabel, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		    
	        limit = 10;
		    counter_data = 0;
	        //mencetak data
		    for (int i = 0; i < limit; i++){
				if (counter_data < array_data_tampung.size()){
					txt_kosong.setVisibility(View.GONE);
					tampil_data(array_data_tampung);
					counter_data++;
					if (counter_data == array_data_tampung.size()) loadmore.setVisibility(View.GONE);
				}
			}
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
}