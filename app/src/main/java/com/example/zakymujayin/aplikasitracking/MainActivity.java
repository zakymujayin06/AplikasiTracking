package com.example.zakymujayin.aplikasitracking;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements LocationListener{
    LocationManager locationManager ;
    String provider;
    Location location;
    public TextView Lat;
    public TextView Long;
    public TextView Simpan;
    public LinearLayout layerBawah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cekDrive();
        Lat = (TextView) findViewById(R.id.lat_view);
        Long = (TextView) findViewById(R.id.long_view);
        Simpan = (TextView) findViewById(R.id.nama_lokasi);
        layerBawah = (LinearLayout) findViewById(R.id.layerbawah);
        hideLayerBawah();
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if(provider!=null && !provider.equals("")){
            location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 20000, 1, this);
            if(location!=null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }
    }
    public void hideLayerBawah(){
        layerBawah.setVisibility(View.GONE);
    }
    public void showLayerBawah(){
        layerBawah.setVisibility(View.VISIBLE);
    }



    @Override
    public void onLocationChanged(Location location) {
    }
    @Override
    public void onProviderDisabled(String provider) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Long.setText(String.valueOf(location.getLongitude()));
        Lat.setText(String.valueOf(location.getLatitude()));
    }
    public void getLokasi(View v){
        Long.setText(String.valueOf(location.getLongitude()));
        Lat.setText(String.valueOf(location.getLatitude()));
        showLayerBawah();
    }

    public void getMap (View v) {

        Intent i = new Intent (MainActivity.this, MapsActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        System.exit(1);
    }
    public void cekDrive(){
        File folder = new File(Environment.getExternalStorageDirectory() + "/dataSkripsi");
        boolean success;
        if (!folder.exists()) {
            success = folder.mkdir();
            if(success){
                Toast.makeText(getApplicationContext(), "Phone is Ready for storing data", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Phone is not Ready for storing data", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Phone is Ready!",  Toast.LENGTH_LONG).show();
        }
    }
    public void saveLokasi(View v){
        String simpan = Simpan.getText().toString();
        writeToFile(simpan);
    }
    public void writeToFile(String data) {
        String filex = data + "\nLatitude: " + Lat.getText() + "\nLongitude: " + Long.getText();
        File root = new File(Environment.getExternalStorageDirectory(), "dataSkripsi");
        String namaFile = data + ".txt";
        try {
            File gpxfile = new File(root, namaFile);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(filex);
            writer.flush();
            writer.close();
            Toast.makeText(getApplicationContext(), "Data " + data + " sudah Tersimpan", Toast.LENGTH_LONG).show();
            clear();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public void clear(){
        Lat.setText("0.0");
        Long.setText("0.0");
        Simpan.setText("");
        hideLayerBawah();
    }
}