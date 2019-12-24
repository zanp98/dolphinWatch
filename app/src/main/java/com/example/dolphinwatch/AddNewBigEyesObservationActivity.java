package com.example.dolphinwatch;

import android.annotation.SuppressLint;
import android.location.Location;
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

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.time.LocalTime;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AddNewBigEyesObservationActivity extends AppCompatActivity {
    TextView timeStartedTextView;
    Button endObservationButton, getLocationButton;
    EditText place, observingArea, equipment, seaState, vessel, trawler, sightingLocationDescription, distanceEst, notes, visibility;
    BigEyesForm bef;
    private FusedLocationProviderClient fusedLocationClient;

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_big_eyes_observation);
        timeStartedTextView = findViewById(R.id.timeStartedTextView);
        LocalTime timeStarted = LocalTime.now();
        timeStartedTextView.setText(String.format("%02d:%02d", timeStarted.getHour(), timeStarted.getMinute()));
        endObservationButton = findViewById(R.id.endBigEyesObservationButton);
        getLocationButton = findViewById(R.id.getLocation);

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

        requestPermission();

        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocation();
            }
        });


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
                    AddNewBigEyesObservationActivity.this.finish();
                }
            }
        });

    }
    @SuppressLint("MissingPermission")
    public void setLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if(place == null)
            place = findViewById(R.id.placeEditText);
        fusedLocationClient.getLastLocation()
            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        place.setText(location.toString());
                    }
                }
            });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}
