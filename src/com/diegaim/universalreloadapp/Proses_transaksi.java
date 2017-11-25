package com.diegaim.universalreloadapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Proses_transaksi extends Activity {
	TextView txt_proses, txt_timer;
	ImageView img_proses;
	ProgressBar pb_proses;
	Button btn_catatan_transaksi, btn_transaksi_lagi;
    
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    
    String operator, nominal_pulsa, nomor, berhasilkah;
    int boleh_pergi, timer;
    
    SQLiteHelper sqLiteHelper = new SQLiteHelper(Proses_transaksi.this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_proses_transaksi);
		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#fe0000"));
	    getActionBar().setBackgroundDrawable(colorDrawable);
	    getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
	    
	    txt_proses = (TextView) findViewById(R.id.textView1);
	    txt_timer = (TextView) findViewById(R.id.textView2);
	    img_proses = (ImageView) findViewById(R.id.imageView1);
	    pb_proses = (ProgressBar) findViewById(R.id.progressBar1);
	    btn_transaksi_lagi = (Button) findViewById(R.id.button1);
	    btn_catatan_transaksi = (Button) findViewById(R.id.button2);
	    
	    btn_transaksi_lagi.setVisibility(View.GONE);
	    btn_catatan_transaksi.setVisibility(View.GONE);
	    
	    txt_timer.setText("");
	    
	    //mengambil data dari act sebelumnya
	    Intent intent = getIntent();
		operator = intent.getStringExtra("operator");
		nominal_pulsa = intent.getStringExtra("nominal_pulsa");
		nomor = intent.getStringExtra("nomor");
		
		boleh_pergi = 0;
		berhasilkah = "?";
		
		//menerapkan fungsi menerima sms
		IntentFilter filter = new IntentFilter(SMS_RECEIVED);
        registerReceiver(receiver_SMS, filter);
        
        //memulai proses kirim sms
        //ambil pin dan saldo
        String pin, saldo;
        pin = "";
        saldo = "";
        HashMap<String, String> hashMapBiodata = sqLiteHelper.info_akun();
    	for (int i = 0; i < hashMapBiodata.size(); i++) {
            pin = hashMapBiodata.get("pin");
            saldo = hashMapBiodata.get("saldo");
        }
        //ambil nomor server aktif
    	HashMap<String, String> hashMapnoserver = sqLiteHelper.info_no_server();
    	String no_server_aktif = hashMapnoserver.get("no_server"); //mengambil nomor server aktif
    	String temp_sms;
    	//cek operator
    	if (operator.equals("Telkomsel")){
    		temp_sms = "S";
    	} else if (operator.equals("As")){
    		temp_sms = "S";
    	} else if (operator.equals("XL")){
    		temp_sms = "X";
    	} else if (operator.equals("Axis")){
    		temp_sms = "X";
    	} else if (operator.equals("3")){
    		temp_sms = "T";
    	} else if (operator.equals("INDOSAT")){
    		temp_sms = "I";
    	}else{
    		temp_sms = "SM";
    	}
    	//cek nominal
    	if (nominal_pulsa.equals("5000")){
    		temp_sms = temp_sms + "5";
    	} else if (nominal_pulsa.equals("10000")){
    		temp_sms = temp_sms + "10";
    	} else if (nominal_pulsa.equals("20000")){
    		temp_sms = temp_sms + "20";
    	} else if (nominal_pulsa.equals("25000")){
    		temp_sms = temp_sms + "25";
    	} else if (nominal_pulsa.equals("50000")){
    		temp_sms = temp_sms + "50";
    	}else{
    		temp_sms = temp_sms + "100";
    	}
    	//mengformula format sms
    	temp_sms = temp_sms+"."+nomor+"."+pin;
	    
    	//mengirim sms
	    img_proses.setBackgroundResource(R.drawable.send_sms);
	    txt_proses.setText("Mengirim SMS");
	    atur_pbar(0);
	    sendSMS(no_server_aktif, temp_sms);
	    //sendSMS("082114660323", temp_sms);
	    
	    //aksi untuk btn_transaksi_lagi
	    btn_transaksi_lagi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (berhasilkah.equals("?")){
					//tambah_transaksi("Menunggu Konfirmasi");
					//sqLiteHelper.tambah_transaksi(nomor, operator, nominal_pulsa,"Menunggu konfirmasi");
				}
				Intent i = new Intent(getApplicationContext(), Transaksi_baru.class);
				startActivity(i);
				finish();
			}
		});
		
	    //aksi untuk btn_catatan_transaksi
	    btn_catatan_transaksi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (berhasilkah.equals("?")){
					//tambah_transaksi("Menunggu Konfirmasi");
					//sqLiteHelper.tambah_transaksi(nomor, operator, nominal_pulsa,"Menunggu konfirmasi");
				}
				Intent i = new Intent(getApplicationContext(), Catatan_transaksi.class);
				startActivity(i);
				finish();
			}
		});
	}
	
    BroadcastReceiver receiver_SMS = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
             if (intent.getAction().equals(SMS_RECEIVED))
             {	
            	 Bundle bundle = intent.getExtras();
            	 
                 SmsMessage[] msgs = null;
          
                 String message = "";
                 if(bundle != null) {
                     Object[] pdus = (Object[]) bundle.get("pdus");
                     msgs = new SmsMessage[pdus.length];
          
                     for(int i=0; i<msgs.length;i++) {
                         msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                         message = msgs[i].getMessageBody()+" from : "+ msgs[i].getOriginatingAddress();
                     }
                     receivedMessage(message);
                 }
             }
        }
    };
    
    private void receivedMessage(String message)
    {
		if (message.contains("SUKSES,Harga")){
			//dapetin posisi tulisan "Saldo:"
			int posisi_str_saldo=message.indexOf("Saldo:");
			
			//dapetin posisi tulisan "="
			int posisi_str_equals=message.indexOf("=");
			
			//dapetin posisi tulisan "-"
			int posisi_str_minus=message.indexOf("-");
			
			//dapetin nilai saldo awal
			String saldo_awal = message.substring(posisi_str_saldo+7, posisi_str_minus-1);
			saldo_awal = saldo_awal.replaceAll("\\.","");
			
			//dapetin nilai harga
			String harga = message.substring(posisi_str_minus+2, posisi_str_equals);
			harga = harga.replaceAll("\\.","");
			
			//dapetin sisa saldo
			int sisa_saldo = Integer.parseInt(saldo_awal) - Integer.parseInt(harga);
			
			txt_proses.setText("TRANSAKSI SUKSES!\nNomor tujuan : "+nomor+"\nOperator : "+operator+"\nNominal pulsa : Rp."+nominal_pulsa+"\n\nSaldo awal : Rp."+saldo_awal+"\nHarga Pulsa : Rp."+harga+"\nSisa saldo : Rp."+sisa_saldo);
			atur_pbar(100);
			img_proses.setBackgroundResource(R.drawable.berhasil);
			berhasilkah = "y";
			sqLiteHelper.tambah_saldo(sisa_saldo);
			
			tambah_transaksi("Berhasil");
		}else if(message.contains("SUKSES")){
			//dapetin posisi tulisan "Saldo"
			int posisi_str_saldo=message.indexOf("Saldo");
			
			//ambil tulisan "9.200 - 5.700= 3.500"
			int panjang_pesan = message.length();
			String str_math_sisa_saldo = message.substring(posisi_str_saldo+6, panjang_pesan);
			
			//dapetin saldo awal
			int posisi_minus=str_math_sisa_saldo.indexOf("-");
			String saldo_awal = str_math_sisa_saldo.substring(0, posisi_minus-1);
			saldo_awal = saldo_awal.replaceAll("\\.","");

			//dapetin harga pulsa
			int posisi_equal=str_math_sisa_saldo.indexOf("=");
			String harga = str_math_sisa_saldo.substring(posisi_minus+2, posisi_equal);
			harga = harga.replaceAll("\\.","");
			
			//dapetin sisa saldo
			int sisa_saldo = Integer.parseInt(saldo_awal) - Integer.parseInt(harga);
			
			txt_proses.setText("TRANSAKSI SUKSES!\nNomor tujuan : "+nomor+"\nOperator : "+operator+"\nNominal pulsa : Rp."+nominal_pulsa+"\n\nSaldo awal : Rp."+saldo_awal+"\nHarga Pulsa : Rp."+harga+"\nSisa saldo : Rp."+sisa_saldo);
			atur_pbar(100);
			img_proses.setBackgroundResource(R.drawable.berhasil);
			berhasilkah = "y";
			sqLiteHelper.tambah_saldo(sisa_saldo);

			tambah_transaksi("Berhasil");
		}else if(message.contains("Saldo anda tdk mencukupi")){
			//dapetin posisi tulisan "Rp"
			int posisi_str_rp=message.indexOf("Rp");
			
			//dapetin posisi tulisan "silahkan"
			int posisi_str_silahkan=message.indexOf("silahkan");
			
			//dapetin nilai saldo
			String sisa_saldo = message.substring(posisi_str_rp+3, posisi_str_silahkan-2);
			sisa_saldo = sisa_saldo.replaceAll("\\.","");
			
			txt_proses.setText("TRANSAKSI GAGAL!\nNomor tujuan : "+nomor+"\nOperator : "+operator+"\nNominal pulsa : Rp."+nominal_pulsa+"\n\nTransaksi gagal dikarenakan saldo tidak mencukupi, saldo saat ini : Rp."+sisa_saldo);
			img_proses.setBackgroundResource(R.drawable.gagal);
			berhasilkah = "n";
			sqLiteHelper.tambah_saldo(Integer.parseInt(sisa_saldo));
			
			tambah_transaksi("Gagal");
		}else if(message.contains("periksa kembali kode pin anda")){
			txt_proses.setText("TRANSAKSI GAGAL!\nNomor tujuan : "+nomor+"\nOperator : "+operator+"\nNominal pulsa : Rp."+nominal_pulsa+"\n\nTransaksi gagal dikarenakan PIN tidak sesuai, mohon perbaiki setelan PIN anda");
			img_proses.setBackgroundResource(R.drawable.gagal);
			berhasilkah = "n";
			
			tambah_transaksi("Gagal");
		}else if(message.contains("diperiksa kembali No tujuan sebelum di ulang")){
			txt_proses.setText("TRANSAKSI GAGAL!\nNomor tujuan : "+nomor+"\nOperator : "+operator+"\nNominal pulsa : Rp."+nominal_pulsa+"\n\nTransaksi gagal dikarenakan nomor tujuan salah atau tidak sesuai dengan operator");
			img_proses.setBackgroundResource(R.drawable.gagal);
			berhasilkah = "n";
			
			tambah_transaksi("Gagal");
		}
    }
	
	public void sendSMS(String phoneNumber, String message) {
		// Intent Filter Tags for SMS SEND and DELIVER
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";
		// STEP-1___
		// SEND PendingIntent
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);

		// DELIVER PendingIntent
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);
		// STEP-2___
		// SEND BroadcastReceiver
		BroadcastReceiver sendSMS = new BroadcastReceiver() {
			int hitung;
			
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					txt_proses.setText("SMS terkirim");
					btn_transaksi_lagi.setVisibility(View.GONE);
					btn_catatan_transaksi.setVisibility(View.GONE);
					atur_pbar(30);
					//untuk timer
					hitung = 120;
					new CountDownTimer(121000, 1000) {						
					    public void onTick(long millisUntilFinished) {
					    	txt_timer.setText("Waktu tunggu : "+hitung+"s");
					    	hitung--;
					    	if (berhasilkah.equals("n") || berhasilkah.equals("y")){
					    		txt_timer.setText("");
					    		cancel();
					    	}
					    }

					    public void onFinish() {
				    		txt_timer.setText("Waktu tunggu : 0s");
					    	txt_proses.setText("TRANSAKSI MENUNGGU KONFIRMASI!\nNomor tujuan : "+nomor+"\nOperator : "+operator+"\nNominal pulsa : Rp."+nominal_pulsa+"\n\nWaktu menunggu repon server habis, server mungkin sedang sibuk. Transaksi mungkin berhasil atau gagal, mohon lakukan konfirmasi transaksi secara manual pada halaman Catatan Transaksi Belum Terkonfirmasi.");
					    	try{
					    		tambah_transaksi("Menunggu konfirmasi");
					    	}catch (Exception e){
					    		Toast.makeText(getApplicationContext(), "Kesalahan\n"+e.toString(), Toast.LENGTH_LONG).show();
					    	}
					    	atur_pbar(100);
					    	boleh_pergi = 1;
							btn_transaksi_lagi.setVisibility(View.VISIBLE);
							btn_catatan_transaksi.setVisibility(View.VISIBLE);
					    }
					}.start();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					txt_proses.setText("TRANSAKSI GAGAL!\nNomor tujuan : "+nomor+"\nOperator : "+operator+"\nNominal pulsa : Rp."+nominal_pulsa+"\n\nKesalahan internal saat mengirim sms, mungkin tidak ada cukup pulsa untuk mengirim SMS.");
					img_proses.setBackgroundResource(R.drawable.gagal);
					
					tambah_transaksi("Gagal");
					
					atur_pbar(100);
					berhasilkah = "n";
					boleh_pergi = 1;
					btn_transaksi_lagi.setVisibility(View.VISIBLE);
					btn_catatan_transaksi.setVisibility(View.VISIBLE);
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					txt_proses.setText("TRANSAKSI GAGAL!\nNomor tujuan : "+nomor+"\nOperator : "+operator+"\nNominal pulsa : Rp."+nominal_pulsa+"\n\nTidak dapat mengirim sms dikarenakan kesalahan pada jaringan.");
					img_proses.setBackgroundResource(R.drawable.gagal);
					
					tambah_transaksi("Gagal");
					
					atur_pbar(100);
					berhasilkah = "n";
					boleh_pergi = 1;
					btn_transaksi_lagi.setVisibility(View.VISIBLE);
					btn_catatan_transaksi.setVisibility(View.VISIBLE);
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					txt_proses.setText("TRANSAKSI GAGAL!\nNomor tujuan : "+nomor+"\nOperator : "+operator+"\nNominal pulsa : Rp."+nominal_pulsa+"\n\nTidak dapat mengirim sms dikarenakan terjadi galat pada proses PDU.");
					img_proses.setBackgroundResource(R.drawable.gagal);
					
					tambah_transaksi("Gagal");
					
					atur_pbar(100);
					berhasilkah = "n";
					boleh_pergi = 1;
					btn_transaksi_lagi.setVisibility(View.VISIBLE);
					btn_catatan_transaksi.setVisibility(View.VISIBLE);
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					txt_proses.setText("TRANSAKSI GAGAL!\nNomor tujuan : "+nomor+"\nOperator : "+operator+"\nNominal pulsa : Rp."+nominal_pulsa+"\n\nTidak dapat mengirim sms dikarenakan kesalahan pada jaringan.");
					img_proses.setBackgroundResource(R.drawable.gagal);
					
					tambah_transaksi("Gagal");
					
					atur_pbar(100);
					berhasilkah = "n";
					boleh_pergi = 1;
					btn_transaksi_lagi.setVisibility(View.VISIBLE);
					btn_catatan_transaksi.setVisibility(View.VISIBLE);
					break;
				}
			}
		};

		BroadcastReceiver deliverSMS = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					txt_proses.setText("SMS telah diterima, transaksi sedang diproses");
					img_proses.setBackgroundResource(R.drawable.menunggu);
					atur_pbar(70);
					break;
				case Activity.RESULT_CANCELED:
					txt_proses.setText("TRANSAKSI GAGAL!\nNomor tujuan : "+nomor+"\nOperator : "+operator+"\nNominal pulsa : Rp."+nominal_pulsa+"\n\nSMS tidak diterima oleh server. Transaksi gagal!");
					img_proses.setBackgroundResource(R.drawable.gagal);
					berhasilkah = "n";
					atur_pbar(100);
					tambah_transaksi("Gagal");
					boleh_pergi = 1;
					btn_transaksi_lagi.setVisibility(View.VISIBLE);
					btn_catatan_transaksi.setVisibility(View.VISIBLE);
					break;
				}
			}
		};
		registerReceiver(sendSMS, new IntentFilter(SENT));
		registerReceiver(deliverSMS, new IntentFilter(DELIVERED));
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	}
	
	public void tambah_transaksi(String str_status_trans){
		try{
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
			//cek total transaksi hari ini
			waktu = year+"-"+str_month+"-"+str_day;
	    	ArrayList<HashMap<String, String>> hashMapHasil = sqLiteHelper.total_transaksi_hari_ini(waktu);
	    	String tot_hari_ini =  String.valueOf(hashMapHasil.size()+1);
	    	//susun id
	    	if (Integer.parseInt(tot_hari_ini) <= 10){
	    		tot_hari_ini = "00"+tot_hari_ini;
	    	}else if(Integer.parseInt(tot_hari_ini) <= 99){
	    		tot_hari_ini = "0"+tot_hari_ini;
	    	}
	    	
	    	/*String str_year_temp = String.valueOf(year);//2017
	    	String str_year = str_year_temp.substring(2);
	    	String id = str_year+str_month+str_day+tot_hari_ini;*/
	    	
	    	String str_year = String.valueOf(year);
	    	String id = str_year+str_month+str_day+tot_hari_ini;
	    	//Toast.makeText(getApplicationContext(), "id : \n"+id, Toast.LENGTH_LONG).show();
	    	
			//tambah data ketabel transaksi
	    	sqLiteHelper.tambah_transaksi(id, nomor, operator, nominal_pulsa, str_status_trans);
	    	
	    	//tampil tombol
	    	btn_transaksi_lagi.setVisibility(View.VISIBLE);
			btn_catatan_transaksi.setVisibility(View.VISIBLE);
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
	}
	
	public void atur_pbar(int progrs){
		pb_proses.setMax(100);
		pb_proses.setProgress(progrs);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.transaksi_baru, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.men_menu_utama) {
			if (boleh_pergi == 1){
				Intent i = new Intent(getApplicationContext(), Menu_utama.class);
				startActivity(i);
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Mohon tunggu sampai proses transaksi selesai!", Toast.LENGTH_SHORT).show();
			}
		}else if (id == R.id.men_catatan_transaksi) {
			if (boleh_pergi == 1){
				Intent i = new Intent(getApplicationContext(), Catatan_transaksi.class);
				startActivity(i);
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Mohon tunggu sampai proses transaksi selesai!", Toast.LENGTH_SHORT).show();
			}
		}else if (id == R.id.men_pengaturan) {
			if (boleh_pergi == 1){
				Intent i = new Intent(getApplicationContext(), Pengaturan.class);
				startActivity(i);
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Mohon tunggu sampai proses transaksi selesai!", Toast.LENGTH_SHORT).show();
			}
		}else if (id == R.id.men_bantuan) {
			if (boleh_pergi == 1){
				Intent i = new Intent(getApplicationContext(), Bantuan.class);
				startActivity(i);
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Mohon tunggu sampai proses transaksi selesai!", Toast.LENGTH_SHORT).show();
			}
		}else if (id == R.id.men_tentang) {
			if (boleh_pergi == 1){
				Intent i = new Intent(getApplicationContext(), Tentang.class);
				startActivity(i);
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Mohon tunggu sampai proses transaksi selesai!", Toast.LENGTH_SHORT).show();
			}
		}else if (id == R.id.men_keluar) {
			if (boleh_pergi == 1){
				keluar_alert();
			}else{
				Toast.makeText(getApplicationContext(), "Mohon tunggu sampai proses transaksi selesai!", Toast.LENGTH_SHORT).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed(){
		if (boleh_pergi == 1){
			Intent i = new Intent(getApplicationContext(), Menu_utama.class);
			startActivity(i);
			finish();
		}else{
			Toast.makeText(getApplicationContext(), "Mohon tunggu sampai proses transaksi selesai!", Toast.LENGTH_SHORT).show();
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
