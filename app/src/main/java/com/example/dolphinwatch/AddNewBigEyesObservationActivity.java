package com.example.dolphinwatch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.time.LocalTime;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AddNewBigEyesObservationActivity extends AppCompatActivity {
    TextView timeStartedTextView;
    Button endObservationButton;
    EditText place, observingArea, equipment, seaState, vessel, trawler, sightingLocationDescription, distanceEst, notes, visibility;
    BigEyesForm bef;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final String TAG = MainActivity.class.getSimpleName();
    protected Location mLastLocation;

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

        if (!checkPermissions()) {
            requestPermissions();
        }

        setLocation();


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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            place.setText(mLastLocation.getLatitude() + " " + mLastLocation.getLongitude() + "");
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                            showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });

    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(AddNewBigEyesObservationActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = findViewById(R.id.main_activity_container);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }
}
