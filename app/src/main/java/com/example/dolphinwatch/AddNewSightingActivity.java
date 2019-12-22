package com.example.dolphinwatch;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.time.LocalTime;

public class AddNewSightingActivity extends AppCompatActivity {
    EditText azimuth, reticulesFromHorizon, dolphinLocation, minGroupSize, estGroupSize, behaviouralState, behaviouralEvent, seaState, notes;
    Button save;
    Switch calves;
    SightingForm sf;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_sighting);
        LocalTime time = LocalTime.now();
        sf = new SightingForm(time);

        TextView sightingTitle = findViewById(R.id.sightingTitle);
        sightingTitle.setText("Sighting no. " + (WatchingActivity.sightings.size() + 1));
        TextView timeOfSighting = findViewById(R.id.timeOfSighting);
        timeOfSighting.setText(String.format("%02d:%02d", time.getHour(), time.getMinute()));

        save = findViewById(R.id.saveSightingButton);
        azimuth = findViewById(R.id.azimuthEditText);
        reticulesFromHorizon = findViewById(R.id.reticulesEditText);
        dolphinLocation = findViewById(R.id.dolphinLocationEditText);
        minGroupSize = findViewById(R.id.minGroupSizeEditText);
        estGroupSize = findViewById(R.id.estGroupSizeEditText);
        behaviouralEvent = findViewById(R.id.behaviouralEventEditText);
        behaviouralState = findViewById(R.id.behaviouralStateEditText);
        seaState = findViewById(R.id.seaStateEditText);
        notes = findViewById(R.id.notesEditText);
        calves = findViewById(R.id.calvesSwitch);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dolphinLocation.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Dolphin location is required", Toast.LENGTH_SHORT).show();
                } else {
                    sf.setAzimuth(azimuth.getText().toString());
                    sf.setReticulesFromHorizon(reticulesFromHorizon.getText().toString());
                    sf.setDolphinLocation(dolphinLocation.getText().toString());
                    sf.setMinGroupSize(minGroupSize.getText().toString());
                    sf.setEstimatedGroupSize(estGroupSize.getText().toString());
                    sf.setCalves(calves.isChecked());
                    sf.setBehaviouralState(behaviouralState.getText().toString());
                    sf.setBehaviouralEvent(behaviouralEvent.getText().toString());
                    sf.setSeaState(seaState.getText().toString());
                    sf.setNotes(notes.getText().toString());

                    WatchingActivity.sightings.add(sf);
                    Toast.makeText(getApplicationContext(), "Saved succesfully", Toast.LENGTH_SHORT).show();
                    AddNewSightingActivity.this.finish();
                }
            }
        });
    }
}
