package com.example.dolphinwatch;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalTime;

public class EndWatchingActivity extends AppCompatActivity {
    TextView date, timeOfWatching, observerName, numberOfSightings, numberOfObservations;
    Button seeAllSightings, seeAllObservations, saveAndExit;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_watching);
        seeAllSightings = findViewById(R.id.seeAllSightingsButton);
        seeAllObservations = findViewById(R.id.seeAllObservationsButton);

        date = findViewById(R.id.dateTextView);
        date.setText(LocalDate.now().getDayOfMonth() + ". " + LocalDate.now().getMonthValue() + ". " + LocalDate.now().getYear());
        LocalTime start = WatchingActivity.startOfWatching;
        LocalTime end = WatchingActivity.endOfWatching;
        timeOfWatching = findViewById(R.id.timeOfWatchingTextView);
        timeOfWatching.setText(String.format("%02d:%02d - %02d:%02d", start.getHour(), start.getMinute(), end.getHour(), end.getMinute()));
        observerName = findViewById(R.id.observerTextView);
        observerName.setText(MainActivity.observerName);

        numberOfSightings = findViewById(R.id.numberOfSightingsTextView);
        if (WatchingActivity.sightings.size() == 0) {
            numberOfSightings.setText("No sightings recorded");
            seeAllSightings.setVisibility(View.INVISIBLE);
        } else if (WatchingActivity.sightings.size() == 1) {
            numberOfSightings.setText("1 sighting");
        } else {
            numberOfSightings.setText(WatchingActivity.sightings.size() + " sightings");
        }

        numberOfObservations = findViewById(R.id.numberOfObservationsTextView);
        if (WatchingActivity.bigEyesObservations.size() == 0) {
            numberOfObservations.setText("No Big Eyes observations recorded");
            seeAllObservations.setVisibility(View.INVISIBLE);
        } else if (WatchingActivity.bigEyesObservations.size() == 1) {
            numberOfObservations.setText("1 Big Eyes observation");
        } else {
            numberOfObservations.setText(WatchingActivity.bigEyesObservations.size() + " Big Eyes observations");
        }

        saveAndExit = findViewById(R.id.saveAndExitButton);
        saveAndExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                moveTaskToBack(true);
                System.exit(0);
            }
        });

        seeAllSightings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EndWatchingActivity.this, AllSightingsActivity.class));
            }
        });

        seeAllObservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EndWatchingActivity.this, AllObservationsActivity.class));
            }
        });
    }
}
