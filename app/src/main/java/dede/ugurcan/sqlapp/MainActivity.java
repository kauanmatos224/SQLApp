package dede.ugurcan.sqlapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String isim, soyAd, yas;
    SQLiteDatabase database;
    ListView listView;
    Button button;
    EditText edtIsim, edtSoyIsim, edtYas;
    ArrayAdapter arrayAdapter;
    Cursor cursor;
    final ArrayList<String> ogrenciList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtIsim = findViewById(R.id.editTextIsim);
        edtSoyIsim = findViewById(R.id.editTextSoyIsım);
        edtYas = findViewById(R.id.editTextYas);
        listView = findViewById(R.id.listView);
        button = findViewById(R.id.buttonKaydet);
        button = findViewById(R.id.buttonSil);
        edtIsim.setText(isim);
        edtSoyIsim.setText(soyAd);
        edtYas.setText(yas);
        //final ArrayList<String> ogrenciList = new ArrayList<>();
        try {
            temizleBox();

            database = this.openOrCreateDatabase("ogrenciler", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS ogrenci(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "isim VARCHAR, " +
                    "soyisim  VARCHAR, " +
                    "yas VARCHAR) ");
            //database.execSQL("DELETE FROM ogrenci");
            guncelle();

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "HATA OLUŞTU\n" + e, Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(ogrenciList.get(position));
                Toast.makeText(getApplicationContext(), ogrenciList.get(position), Toast.LENGTH_LONG).show();
                String s = ogrenciList.get(position);
                String[] parser = s.split(" ");
                System.out.println("Parser İsim: " + parser[0]);
                System.out.println("Parser Soyisim: " + parser[1]);
                System.out.println("Parser Yaş: " + parser[2]);
                //isim = parser[0];
                //soyAd = parser[1];
                //yas = parser[2];
                edtIsim.setText(parser[0]);
                edtSoyIsim.setText(parser[1]);
                edtYas.setText(parser[2]);
            }
        });
    }


    public void ekle(View v) {
        isim = edtIsim.getText().toString();
        soyAd = edtSoyIsim.getText().toString();
        yas = edtYas.getText().toString();

        temizleBox();
        try {
            /*
            database = this.openOrCreateDatabase("ogrenciler", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS ogrenci(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "isim VARCHAR, " +
                    "soyisim  VARCHAR, " +
                    "yas VARCHAR) ");
             */

            database.execSQL("INSERT INTO ogrenci(isim,soyisim,yas) VALUES ('" + isim + "','" + soyAd + "','" + yas + "')");
            if (cursor != null) {
                cursor.moveToFirst();
                guncelle();
                System.out.println(isim + " " + soyAd + " " + yas + " " + " Eklendi\n");
                Toast.makeText(getApplicationContext(), "Veri Başarıyla Eklendi ", Toast.LENGTH_SHORT).show();
            }
            cursor.close();

        } catch (CursorIndexOutOfBoundsException e) {
            //Toast.makeText(getApplicationContext(), "Veri Kaydedilirken Hata Oluştu", Toast.LENGTH_LONG).show();
            System.out.println("Kaydetme hatası\t" + e);
        }
    }

    public void sil(View v) {
        isim = edtIsim.getText().toString();
        soyAd = edtSoyIsim.getText().toString();
        yas = edtYas.getText().toString();
        temizleBox();
        try {
         /* database = this.openOrCreateDatabase("ogrenciler", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS ogrenci(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "isim VARCHAR, " +
                    "soyisim  VARCHAR, " +
                    "yas VARCHAR) ");
         */
            database.execSQL("DELETE FROM ogrenci WHERE isim='" + isim + "' AND  soyisim='" + soyAd + "' AND  yas='" + yas + "'");
            if (cursor != null) {
                cursor.moveToFirst();
                guncelle();
                System.out.println(isim + " " + soyAd + " " + yas + " " + " Silindi\n");
                Toast.makeText(getApplicationContext(), "Veri Başarıyla Silindi", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } catch (CursorIndexOutOfBoundsException e) {
            //Toast.makeText(getApplicationContext(), "Veri Silinirken Hata Oluştu", Toast.LENGTH_LONG).show();
            System.out.print("Silme hatası\t" + e);
        }
    }

    public void temizleBox() {
        edtIsim.getText().clear();
        edtSoyIsim.getText().clear();
        edtYas.getText().clear();
        ogrenciList.clear();
        ogrenciList.addAll(ogrenciList);
    }

    public void guncelle() {
        cursor = database.rawQuery("SELECT * FROM ogrenci", null);
        //int idIndex = cursor.getColumnIndex("ID");
        int isimIndex = cursor.getColumnIndex("isim");
        int soyadIndex = cursor.getColumnIndex("soyisim");
        int yasIndex = cursor.getColumnIndex("yas");

        if (cursor != null && cursor.moveToFirst()) {
            while (cursor != null) {
                System.out.println("İsim: " + cursor.getString(isimIndex) +
                        " Soyisim: " + cursor.getString(soyadIndex) +
                        " Yas: " + cursor.getString(yasIndex));
                arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, ogrenciList);
                listView.setAdapter(arrayAdapter);
                if (cursor.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), "Veri Yok", Toast.LENGTH_LONG).show();
                } else {
                    ogrenciList.add(cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3));
                    arrayAdapter.notifyDataSetChanged();
                    // arrayAdapter.add(cursor.getString(1));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
    }
}
