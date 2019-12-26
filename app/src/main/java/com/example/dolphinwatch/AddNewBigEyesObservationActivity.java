package com.example.dolphinwatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalTime;

public class AddNewBigEyesObservationActivity extends AppCompatActivity {
    TextView timeStartedTextView;
    Button endObservationButton;
    EditText place, observingArea, equipment, seaState, vessel, trawler, sightingLocationDescription, distanceEst, notes, visibility;
    BigEyesForm bef;
    LocationManager locationManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_big_eyes_observation);
        timeStartedTextView = findViewById(R.id.timeStartedTextView);
        LocalTime timeStarted = LocalTime.now();
        timeStartedTextView.setText(String.format("%02d:%02d", timeStarted.getHour(), timeStarted.getMinute()));
        endObservationButton = findViewById(R.id.endBigEyesObservationButton);

        bef = new BigEyesForm(timeStarted);

        place = findViewById(R.id.placeEditText);
        observingArea = findViewById(R.id.observingAreaEditText);
        equipment = findViewById(R.id.equipmentEditText);
        seaState = findViewById(R.id.seaStateEditText);
        vessel = findViewById(R.id.vesselEditText);
        trawler = findViewById(R.id.trawlerEditText);
        sightingLocationDescription = findViewById(R.id.sightingLocationEditText);
        distanceEst = findViewById(R.id.distanceEstEditText);
        notes = findViewById(R.id.notesEditText);
        visibility = findViewById(R.id.visibilityEditText);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitutde = location.getLatitude();

        place.setText(String.format("Latitude: %.2f, Longitude: %.2f", latitutde, longitude));
        place.setEnabled(false);

        endObservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (place.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Place is required", Toast.LENGTH_SHORT).show();
                } else {

                    bef.setPlace(place.getText().toString());
                    bef.setEquipment(equipment.getText().toString());
                    bef.setSightingLocation(sightingLocationDescription.getText().toString());
                    bef.setNotes(notes.getText().toString());
                    bef.setVisibility(visibility.getText().toString());
                    bef.setObservingArea(observingArea.getText().toString());
                    bef.setSeaState(seaState.getText().toString());
                    bef.setVessel(vessel.getText().toString());
                    bef.setTrawler(trawler.getText().toString());
                    bef.setDistanceEst(distanceEst.getText().toString());
                    bef.setStopTime(LocalTime.now());

                    WatchingActivity.bigEyesObservations.add(bef);

                    Toast.makeText(getApplicationContext(), "Observation finished", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
