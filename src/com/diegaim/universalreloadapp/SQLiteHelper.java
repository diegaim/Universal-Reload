package com.diegaim.universalreloadapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String nama_database = "db.s3db";
    private static final int versi_database = 3;
    private static final String query_buat_table_akun = "CREATE TABLE IF NOT EXISTS akun(pin varchar(4), saldo integer(8))";
    private static final String query_buat_table_no_server = "CREATE TABLE IF NOT EXISTS no_server(no_server varchar(15), status varchar(11))";
    private static final String query_buat_table_transaksi = "CREATE TABLE IF NOT EXISTS transaksi(id varchar(11), waktu_tanggal TIMESTAMP DEFAULT CURRENT_TIMESTAMP, nomor varchar(15), operator varchar(15), nominal varchar(8), status varchar(19))";

    public SQLiteHelper(Context context) {
        super(context, nama_database, null, versi_database);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    	sqLiteDatabase.execSQL(query_buat_table_akun);
	    sqLiteDatabase.execSQL(query_buat_table_no_server);
	    sqLiteDatabase.execSQL(query_buat_table_transaksi);
    }
    
    public void hapus_isi_table(){
    	SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("DELETE FROM transaksi", null);
        if (cursor.moveToFirst()) {
            do {
            } while (cursor.moveToNext());
        }
        
        cursor = database.rawQuery("DELETE FROM akun", null);
        if (cursor.moveToFirst()) {
            do {
            } while (cursor.moveToNext());
        }
        
        cursor = database.rawQuery("DELETE FROM no_server", null);
        if (cursor.moveToFirst()) {
            do {
            } while (cursor.moveToNext());
        }
    }

    public void tambah_akun(String pin, String saldo) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pin", pin);
        values.put("saldo", saldo);
        database.insert("akun", null, values);
        database.close();
    }
    
    public void tambah_no_server(String no_server, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("no_server", no_server);
        values.put("status", status);
        database.insert("no_server", null, values);
        database.close();
    }
    
    /*public void tambah_transaksi(String nomor, String operator, String nominal, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.putNull("id");
        values.put("waktu_tanggal", getDateTime());
        values.put("nomor", nomor);
        values.put("operator", operator);
        values.put("nominal", nominal);
        values.put("status", status);
        database.insert("transaksi", null, values);
        database.close();
    }*/
    
    public void tambah_transaksi(String id, String nomor, String operator, String nominal, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("waktu_tanggal", getDateTime());
        values.put("nomor", nomor);
        values.put("operator", operator);
        values.put("nominal", nominal);
        values.put("status", status);
        database.insert("transaksi", null, values);
        database.close();
    }
    
    public ArrayList<HashMap<String, String>> total_transaksi_hari_ini(String waktu) {
        ArrayList<HashMap<String, String>> arrayListBiodata = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM transaksi WHERE DATE(waktu_tanggal) = '"+waktu+"'", null);
        //Cursor cursor = database.rawQuery("SELECT * FROM transaksi WHERE DATE(waktu_tanggal) = '2017-05-27'", null);
        if (cursor.moveToFirst()) {
            do {
                // deklarasikan sebuah hashmap, yang bisa menamp
                HashMap<String, String> hashMapBiodata = new HashMap<String, String>();
                hashMapBiodata.put("id", cursor.getString(0));
                arrayListBiodata.add(hashMapBiodata);
            } while (cursor.moveToNext());
        }
        return arrayListBiodata;
    }
    
    public ArrayList<HashMap<String, String>> total_transaksi_bulan_ini(String bulan, String tahun) {
        ArrayList<HashMap<String, String>> arrayListBiodata = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM transaksi WHERE strftime('%m', waktu_tanggal) = '"+bulan+"' AND strftime('%Y', waktu_tanggal) = '"+tahun+"'", null);
        if (cursor.moveToFirst()) {
            do {
                // deklarasikan sebuah hashmap, yang bisa menamp
                HashMap<String, String> hashMapBiodata = new HashMap<String, String>();
                hashMapBiodata.put("id", cursor.getString(0));
                arrayListBiodata.add(hashMapBiodata);
            } while (cursor.moveToNext());
        }
        return arrayListBiodata;
    }
    
    public ArrayList<HashMap<String, String>> total_transaksi_bulan_kemarin(String bulan, String tahun) {
        ArrayList<HashMap<String, String>> arrayListBiodata = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM transaksi WHERE strftime('%m', waktu_tanggal) = '"+bulan+"' AND strftime('%Y', waktu_tanggal) = '"+tahun+"'", null);
        if (cursor.moveToFirst()) {
            do {
                // deklarasikan sebuah hashmap, yang bisa menamp
                HashMap<String, String> hashMapBiodata = new HashMap<String, String>();
                hashMapBiodata.put("id", cursor.getString(0));
                arrayListBiodata.add(hashMapBiodata);
            } while (cursor.moveToNext());
        }
        return arrayListBiodata;
    }
    
    public ArrayList<HashMap<String, String>> total_transaksi() {
        ArrayList<HashMap<String, String>> arrayListBiodata = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM transaksi", null);
        if (cursor.moveToFirst()) {
            do {
                // deklarasikan sebuah hashmap, yang bisa menamp
                HashMap<String, String> hashMapBiodata = new HashMap<String, String>();
                hashMapBiodata.put("id", cursor.getString(0));
                arrayListBiodata.add(hashMapBiodata);
            } while (cursor.moveToNext());
        }
        return arrayListBiodata;
    }
    
    private String getDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    public void hapus_no_server(String no_server){
    	SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("DELETE FROM no_server where no_server='"+no_server+"'", null);
        if (cursor.moveToFirst()) {
            do {
            } while (cursor.moveToNext());
        }
    }
    
    public void bersihkan_table_no_server(){
    	SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("DELETE FROM no_server", null);
        if (cursor.moveToFirst()) {
            do {
            } while (cursor.moveToNext());
        }
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase database, int versi_lama,
            int versi_baru) {
    }
    
    public HashMap<String, String> info_akun() {
    	SQLiteDatabase database = this.getReadableDatabase();
        HashMap<String, String> hashMapBiodata = new HashMap<String, String>();
        Cursor cursor = database.rawQuery("SELECT * FROM akun", null);

        if (cursor.moveToFirst()) {
            do {
                hashMapBiodata.put("pin", cursor.getString(0));
                hashMapBiodata.put("saldo", cursor.getString(1));
            } while (cursor.moveToNext());
        }

        return hashMapBiodata;
    }
    
    public HashMap<String, String> info_no_server() {
    	SQLiteDatabase database = this.getReadableDatabase();
        HashMap<String, String> hashMapnoserver = new HashMap<String, String>();
        Cursor cursor = database.rawQuery("SELECT * FROM no_server where status='dipilih'", null);
        if (cursor.moveToFirst()) {
            do {
            	hashMapnoserver.put("no_server", cursor.getString(0));
            }while (cursor.moveToNext());
    	}
        
        return hashMapnoserver;
    }
    
    public ArrayList<HashMap<String, String>> tampil_semua_no_server() {
        // deklarasikan sebuah arraylist yang bisa menampung hashmap
        ArrayList<HashMap<String, String>> arrayListAllServer = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM no_server where status='tdk_dipilih'", null);
        // kursor langsung diarkan ke posisi paling awal data pada tabel_biodata
        if (cursor.moveToFirst()) {
            do {
                // deklarasikan sebuah hashmap, yang bisa menamp
                HashMap<String, String> hashMapAllServer = new HashMap<String, String>();
                // masukkan masing-masing field dari tabel_biodata ke dalam hashMapBiodata
                //pastikan id_biodata, nama dan alamat sama persis dengan field yang ada pada tabel_biodata
                hashMapAllServer.put("no_server", cursor.getString(0));
                // masukkan hashMapBiodata ke dalam arrayListBiodata
                arrayListAllServer.add(hashMapAllServer);
            } while (cursor.moveToNext());
        }
        return arrayListAllServer;
    }
    
    public ArrayList<HashMap<String, String>> cari_transaksi(String nomor, String status) {
    	// deklarasikan sebuah arraylist yang bisa menampung hashmap
        ArrayList<HashMap<String, String>> arrayListBiodata = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = this.getWritableDatabase();
        
        if (status.equals("Berhasil")){
        	Cursor cursor = database.rawQuery("SELECT * FROM transaksi WHERE nomor LIKE '%"+nomor+"%' AND status='"+status+"' ORDER BY id DESC", null);
        	// kursor langsung diarahkan ke posisi paling awal data pada tabel_biodata
            if (cursor.moveToFirst()) {
                do {
                    // deklarasikan sebuah hashmap, yang bisa menamp
                    HashMap<String, String> hashMapBiodata = new HashMap<String, String>();
                    // masukkan masing-masing field dari tabel_biodata ke dalam hashMapBiodata
                    //pastikan id_biodata, nama dan alamat sama persis dengan field yang ada pada tabel_biodata
                    hashMapBiodata.put("id", cursor.getString(0));
                    hashMapBiodata.put("waktu_tanggal", cursor.getString(1));
                    hashMapBiodata.put("nomor", cursor.getString(2));
                    hashMapBiodata.put("operator", cursor.getString(3));
                    hashMapBiodata.put("nominal", cursor.getString(4));
                    hashMapBiodata.put("status", cursor.getString(5));
                    // masukkan hashMapBiodata ke dalam arrayListBiodata
                    arrayListBiodata.add(hashMapBiodata);
                } while (cursor.moveToNext());
            }
            return arrayListBiodata;
        }else if (status.equals("Semua")){
        	Cursor cursor = database.rawQuery("SELECT * FROM transaksi WHERE nomor LIKE '%"+nomor+"%' ORDER BY id DESC", null);
        	// kursor langsung diarahkan ke posisi paling awal data pada tabel_biodata
            if (cursor.moveToFirst()) {
                do {
                    // deklarasikan sebuah hashmap, yang bisa menamp
                    HashMap<String, String> hashMapBiodata = new HashMap<String, String>();
                    // masukkan masing-masing field dari tabel_biodata ke dalam hashMapBiodata
                    //pastikan id_biodata, nama dan alamat sama persis dengan field yang ada pada tabel_biodata
                    hashMapBiodata.put("id", cursor.getString(0));
                    hashMapBiodata.put("waktu_tanggal", cursor.getString(1));
                    hashMapBiodata.put("nomor", cursor.getString(2));
                    hashMapBiodata.put("operator", cursor.getString(3));
                    hashMapBiodata.put("nominal", cursor.getString(4));
                    hashMapBiodata.put("status", cursor.getString(5));
                    // masukkan hashMapBiodata ke dalam arrayListBiodata
                    arrayListBiodata.add(hashMapBiodata);
                } while (cursor.moveToNext());
            }
            return arrayListBiodata;
        }else{
        	Cursor cursor = database.rawQuery("SELECT * FROM transaksi WHERE nomor LIKE '%"+nomor+"%' AND status<>'Berhasil' ORDER BY id DESC", null);
        	// kursor langsung diarahkan ke posisi paling awal data pada tabel_biodata
            if (cursor.moveToFirst()) {
                do {
                    // deklarasikan sebuah hashmap, yang bisa menamp
                    HashMap<String, String> hashMapBiodata = new HashMap<String, String>();
                    // masukkan masing-masing field dari tabel_biodata ke dalam hashMapBiodata
                    //pastikan id_biodata, nama dan alamat sama persis dengan field yang ada pada tabel_biodata
                    hashMapBiodata.put("id", cursor.getString(0));
                    hashMapBiodata.put("waktu_tanggal", cursor.getString(1));
                    hashMapBiodata.put("nomor", cursor.getString(2));
                    hashMapBiodata.put("operator", cursor.getString(3));
                    hashMapBiodata.put("nominal", cursor.getString(4));
                    hashMapBiodata.put("status", cursor.getString(5));
                    // masukkan hashMapBiodata ke dalam arrayListBiodata
                    arrayListBiodata.add(hashMapBiodata);
                } while (cursor.moveToNext());
            }
            return arrayListBiodata;
        }
    }
    
    public void simpan_pengaturan_baru(String no_server_baru){
    	SQLiteDatabase database = this.getWritableDatabase();
    	
        Cursor cursor2 = database.rawQuery("UPDATE no_server SET status='dipilih' WHERE no_server='"+no_server_baru+"'", null);
        if (cursor2.moveToFirst()) {
            do {
            } while (cursor2.moveToNext());
        }
    }
    
    public void hapus_semua_transaksi(){
    	SQLiteDatabase database = this.getWritableDatabase();
    	
        Cursor cursor2 = database.rawQuery("DELETE FROM transaksi", null);
        if (cursor2.moveToFirst()) {
            do {
            } while (cursor2.moveToNext());
        }
    }
    
    public void simpan_pengaturan(String pin, int saldo, String no_server_lama, String no_server_baru){
    	SQLiteDatabase database = this.getWritableDatabase();
    	
    	Cursor cursor = database.rawQuery("UPDATE no_server SET status='tdk_dipilih' WHERE no_server='"+no_server_lama+"'", null);
        if (cursor.moveToFirst()) {
            do {
            } while (cursor.moveToNext());
        }
        Cursor cursor2 = database.rawQuery("UPDATE no_server SET status='dipilih' WHERE no_server='"+no_server_baru+"'", null);
        if (cursor2.moveToFirst()) {
            do {
            } while (cursor2.moveToNext());
        }
        Cursor cursor3 = database.rawQuery("UPDATE akun SET pin='"+pin+"', saldo="+saldo, null);
        if (cursor3.moveToFirst()) {
            do {
            } while (cursor3.moveToNext());
        }
    }
    
    public ArrayList<HashMap<String, String>> konfirmasi_manual_transaksi(String id, String status){
    	System.out.println("UPDATE transaksi SET status='"+status+"' WHERE id="+id);
    	SQLiteDatabase database = this.getWritableDatabase();
    	Cursor cursor1 = database.rawQuery("UPDATE transaksi SET status='"+status+"' WHERE id="+id, null);
    	if (cursor1.moveToFirst()) {
            do {
            } while (cursor1.moveToNext());
        }
    	
    	ArrayList<HashMap<String, String>> arrayListBiodata = new ArrayList<HashMap<String, String>>();
    	Cursor cursor = database.rawQuery("SELECT * FROM transaksi WHERE status='Menunggu konfirmasi' OR status='Gagal' ORDER BY id DESC", null);
        if (cursor.moveToFirst()) {
            do {
            	HashMap<String, String> hashMapBiodata = new HashMap<String, String>();
            	hashMapBiodata.put("id", cursor.getString(0));
                hashMapBiodata.put("waktu_tanggal", cursor.getString(1));
                hashMapBiodata.put("nomor", cursor.getString(2));
                hashMapBiodata.put("operator", cursor.getString(3));
                hashMapBiodata.put("nominal", cursor.getString(4));
                hashMapBiodata.put("status", cursor.getString(5));
                // masukkan hashMapBiodata ke dalam arrayListBiodata
                arrayListBiodata.add(hashMapBiodata);
            } while (cursor.moveToNext());
        }
        return arrayListBiodata;
    } 
    
    public void tambah_saldo(int saldo){
    	SQLiteDatabase database = this.getWritableDatabase();
    	Cursor cursor = database.rawQuery("UPDATE akun SET saldo="+saldo, null);
        if (cursor.moveToFirst()) {
            do {
            } while (cursor.moveToNext());
        }
    }
    
    public HashMap<String, String> get_info_transaksi_belum_k(String id) {
    	SQLiteDatabase database = this.getReadableDatabase();
        HashMap<String, String> hashMapBiodata = new HashMap<String, String>();
        Cursor cursor = database.rawQuery("SELECT * FROM transaksi WHERE id='"+id+"'", null);

        if (cursor.moveToFirst()) {
            do {
                hashMapBiodata.put("id", cursor.getString(0));
                hashMapBiodata.put("waktu_tanggal", cursor.getString(1));
                hashMapBiodata.put("nomor", cursor.getString(2));
                hashMapBiodata.put("operator", cursor.getString(3));
                hashMapBiodata.put("nominal", cursor.getString(4));
                hashMapBiodata.put("status", cursor.getString(5));
            } while (cursor.moveToNext());
        }

        return hashMapBiodata;
    }
    
    public ArrayList<HashMap<String, String>> tampil_transaksi_sudah_k() {
        // deklarasikan sebuah arraylist yang bisa menampung hashmap
        ArrayList<HashMap<String, String>> arrayListBiodata = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM transaksi WHERE status='Berhasil' ORDER BY id DESC", null);
        // kursor langsung diarkan ke posisi paling awal data pada tabel_biodata
        if (cursor.moveToFirst()) {
            do {
                // deklarasikan sebuah hashmap, yang bisa menamp
                HashMap<String, String> hashMapBiodata = new HashMap<String, String>();
                // masukkan masing-masing field dari tabel_biodata ke dalam hashMapBiodata
                //pastikan id_biodata, nama dan alamat sama persis dengan field yang ada pada tabel_biodata
                hashMapBiodata.put("id", cursor.getString(0));
                hashMapBiodata.put("waktu_tanggal", cursor.getString(1));
                hashMapBiodata.put("nomor", cursor.getString(2));
                hashMapBiodata.put("operator", cursor.getString(3));
                hashMapBiodata.put("nominal", cursor.getString(4));
                hashMapBiodata.put("status", cursor.getString(5));
                // masukkan hashMapBiodata ke dalam arrayListBiodata
                arrayListBiodata.add(hashMapBiodata);
            } while (cursor.moveToNext());
        }
        return arrayListBiodata;
    }
    
    public ArrayList<HashMap<String, String>> tampil_transaksi_belum_k() {
        // deklarasikan sebuah arraylist yang bisa menampung hashmap
        ArrayList<HashMap<String, String>> arrayListBiodata = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM transaksi WHERE status='Menunggu konfirmasi' OR status='Gagal'  ORDER BY id DESC", null);
        // kursor langsung diarkan ke posisi paling awal data pada tabel_biodata
        if (cursor.moveToFirst()) {
            do {
                // deklarasikan sebuah hashmap, yang bisa menamp
                HashMap<String, String> hashMapBiodata = new HashMap<String, String>();
                // masukkan masing-masing field dari tabel_biodata ke dalam hashMapBiodata
                //pastikan id_biodata, nama dan alamat sama persis dengan field yang ada pada tabel_biodata
                hashMapBiodata.put("id", cursor.getString(0));
                hashMapBiodata.put("waktu_tanggal", cursor.getString(1));
                hashMapBiodata.put("nomor", cursor.getString(2));
                hashMapBiodata.put("operator", cursor.getString(3));
                hashMapBiodata.put("nominal", cursor.getString(4));
                hashMapBiodata.put("status", cursor.getString(5));
                // masukkan hashMapBiodata ke dalam arrayListBiodata
                arrayListBiodata.add(hashMapBiodata);
            } while (cursor.moveToNext());
        }
        return arrayListBiodata;
    }
}
