package com.example.comquimes.myapplicationd;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.Image;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    NotificationManager mNotifyMgr;
    Notification notification;
    TextView tvmetros;
    RelativeLayout rl_info;
    RelativeLayout rl_distance;
    Dictionary points;
    ArrayList<CheckPoint> arrayCheckPoint;
    ImageView monumentImg;
    TextView txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification = new Notification(this);

        tvmetros =(TextView) findViewById(R.id.TV_DISTANCIA);

        rl_info = (RelativeLayout) findViewById(R.id.RL_INFO);
        rl_distance = (RelativeLayout) findViewById(R.id.RL_DISTANCE);
        monumentImg = (ImageView) findViewById(R.id.iv);
        txtDescription = (TextView) findViewById(R.id.txtinfo);

        arrayCheckPoint = new ArrayList<CheckPoint>();

        arrayCheckPoint.add(new CheckPoint("Sagrada Familia",
                41.403743,2.174356,
                "El Templo Expiatorio de la Sagrada Familia (en catalán," +
                        " Temple Expiatori de la Sagrada Família), conocido simplemente como la Sagrada Familia, es una basílica católica de Barcelona (España)," +
                        " diseñada por el arquitecto Antoni Gaudí.",getResources().getDrawable(R.drawable.sf)));

        arrayCheckPoint.add(new CheckPoint("Esports UPC",
                41.387727,2.113386,
                "Esports al costat de la UPC :D",getResources().getDrawable(R.drawable.esportsupc)));

        arrayCheckPoint.add(new CheckPoint("CaixaBank Baloon",
                41.38895083,2.1135003,
                "Mitic globus de CaixaBank",getResources().getDrawable(R.drawable.imagin)));

        arrayCheckPoint.add(new CheckPoint("FIB",
                41.389479,2.113377,
                "Facultat d'Informatica de Barcelona LA UB ES MILLOR",getResources().getDrawable(R.drawable.fib)));



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }

    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);


    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                   // mensaje2.setText("Mi direccion es: \n"
                     //       + DirCalle.getAddressLine(0));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        MainActivity mainActivity;

        public MainActivity getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            double dmin = Double.POSITIVE_INFINITY;
            CheckPoint cmin = arrayCheckPoint.get(0);

            for(CheckPoint c : arrayCheckPoint) {

                double radioTierra = 6371;//en kilómetros
                double dLat = Math.toRadians(c.getLatitud() - loc.getLatitude());
                double dLng = Math.toRadians(c.getLongitud() - loc.getLongitude());
                double sindLat = Math.sin(dLat / 2);
                double sindLng = Math.sin(dLng / 2);
                double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                        * Math.cos(Math.toRadians(loc.getLatitude())) * Math.cos(Math.toRadians(c.getLatitud()));
                double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
                double distancia = radioTierra * va2 * 1000;
                if(distancia<dmin){
                    dmin=distancia;
                    cmin=c;
                }
            }

            String Text = "Mi ubicacion actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();
            //mensaje1.setText(Text);
            this.mainActivity.setLocation(loc);
            if (dmin <= 15) {
                rl_distance.setVisibility(View.GONE);
                rl_info.setVisibility(View.VISIBLE);

                //  mensaje3.setText("Estas al punt d'interes");
                mNotifyMgr.notify(notification.getID(), notification.getmBuilder().build());
                monumentImg.setImageDrawable(cmin.getImage());
                txtDescription.setText(cmin.getID()+"\n"+cmin.getInfo());
            } else {
                rl_distance.setVisibility(View.VISIBLE);
                rl_info.setVisibility(View.GONE);
                tvmetros.setText(Double.toString(Math.round(dmin)) + " m");
                mNotifyMgr.cancel(notification.getID());
            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //mensaje1.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            //mensaje1.setText("GPS Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
}
