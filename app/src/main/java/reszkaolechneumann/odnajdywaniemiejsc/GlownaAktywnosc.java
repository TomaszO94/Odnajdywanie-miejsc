package reszkaolechneumann.odnajdywaniemiejsc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class GlownaAktywnosc extends AppCompatActivity {
    /* - Pola - */
    private LocationManager ZarzadcaLokalizacji;
    private LocationListener NasluchiwaczLokalizacji;
    private TextView PobierzMiejsce;

    int ZadaniePoszukiwaczaMiejsc = 1;
    /* - Metody - */
    /* Metody chronione */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glowna_aktywnosc);

        PobierzMiejsce = (TextView) findViewById(R.id.PobierzMiejsceP);
        PobierzMiejsce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder Budowniczy = new PlacePicker.IntentBuilder();

                Intent Intencja;

                try
                {
                    Intencja = Budowniczy.build(getApplicationContext());
                    startActivityForResult(Intencja,ZadaniePoszukiwaczaMiejsc);
                }
                catch (GooglePlayServicesRepairableException e)
                {
                    e.printStackTrace();
                }
                catch (GooglePlayServicesNotAvailableException e)
                {
                    e.printStackTrace();
                }
            }
        });


        ZarzadcaLokalizacji = (LocationManager) getSystemService(LOCATION_SERVICE);
        NasluchiwaczLokalizacji = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        // Jeśli przekroczymy określoną wersję SDK, przeprowadzamy osobne sprawdzenie, czy można pobierać informacje na temat lokalizacji
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            // Jeśli nie posiadamy pozwolenia na pobieranie informacji, wysyłamy żądanie do systemu, aby nam go udzielił
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET},10);
                return;
            }
        }
        else
        {
            KonfigurujOdczytywanieLokalizacji();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == ZadaniePoszukiwaczaMiejsc)
        {
            if (resultCode == RESULT_OK)
            {
                Place Miejsce = PlacePicker.getPlace(data,this);
                String Adres = String.format("Place: %s",Miejsce.getAddress());
                PobierzMiejsce.setText(Adres);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    KonfigurujOdczytywanieLokalizacji();
                    return;
                }
        }
    }

    private void KonfigurujOdczytywanieLokalizacji()
    {
        ZarzadcaLokalizacji.requestLocationUpdates("gps", 5000, 0, NasluchiwaczLokalizacji);
    }
}
