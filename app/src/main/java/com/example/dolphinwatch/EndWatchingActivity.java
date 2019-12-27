package com.example.dolphinwatch;

import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Vector;

public class EndWatchingActivity extends AppCompatActivity {
    TextView date, timeOfWatching, observerName, numberOfSightings, numberOfObservations;
    Button seeAllSightings, seeAllObservations, saveAndExit;
    String dateString, timeOfWatchingString;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_watching);
        seeAllSightings = findViewById(R.id.seeAllSightingsButton);
        seeAllObservations = findViewById(R.id.seeAllObservationsButton);

        date = findViewById(R.id.dateTextView);
        dateString = LocalDate.now().getDayOfMonth() + ". " + LocalDate.now().getMonthValue() + ". " + LocalDate.now().getYear();
        date.setText(dateString);
        LocalTime start = WatchingActivity.startOfWatching;
        LocalTime end = WatchingActivity.endOfWatching;
        timeOfWatching = findViewById(R.id.timeOfWatchingTextView);
        timeOfWatchingString = String.format("%02d:%02d - %02d:%02d", start.getHour(), start.getMinute(), end.getHour(), end.getMinute());
        timeOfWatching.setText(timeOfWatchingString);
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
                saveDataToCsv();
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

    private void saveDataToCsv() {
        try {
            String csvOverviewLine = "date, timeOfWatching, observerName\n";
            csvOverviewLine += dateString + ", " + timeOfWatchingString + ", " + MainActivity.observerName;

            String sightingFormCsvLine = "azimuth, reticulesFromHorizon, dolphinLocation, minGroupSize, estGroupSize, calves, behaviouralState, behaviouralEvent, seaState, notes\n";
            for (int i = 0; i < WatchingActivity.sightings.size(); i++) {
                SightingForm sf = (SightingForm) WatchingActivity.sightings.get(i);
                String sfTime = String.format("%02d:%02d", sf.getTime().getHour(), sf.getTime().getMinute());
                sightingFormCsvLine +=
                        sfTime + ", " +
                                sf.getAzimuth() + ", " + sf.getReticulesFromHorizon() + ", " +
                                sf.getDolphinLocation() + ", " +
                                sf.getMinGroupSize() + "," +
                                sf.getEstimatedGroupSize() + ", " +
                                (sf.isCalves() ? "Yes" : "No") + ", " +
                                sf.getBehaviouralState() + ", " +
                                sf.getBehaviouralEvent() + ", " +
                                sf.getNotes() + "\n";
            }

            String bigEyesFormCsvLine = "time, place, observingArea, equipment, seaState, visibility, vessel, trawler, sightingLocation, distanceEstimated, notes\n";
            for (int i = 0; i < WatchingActivity.bigEyesObservations.size(); i++) {
                BigEyesForm bef = (BigEyesForm) WatchingActivity.bigEyesObservations.get(i);
                String befTime = String.format("%02d:%02d - %02d:%02d", bef.getStartTime().getHour(), bef.getStartTime().getMinute(), bef.getStopTime().getHour(), bef.getStopTime().getMinute());
                bigEyesFormCsvLine +=
                        befTime + ", " +
                                bef.getPlace() + ", " +
                                bef.getObservingArea() + ", " +
                                bef.getEquipment() + ", " +
                                bef.getSeaState() + ", " +
                                bef.getVisibility() + ", " +
                                bef.getVessel() + ", " +
                                bef.getTrawler() + ", " +
                                bef.getSightingLocation() + ", " +
                                bef.getDistanceEst() + ", " +
                                bef.getNotes() + "\n";
            }

            File root = new File(Environment.getExternalStorageDirectory(), "DolphinWatch");
            if (!root.exists()) {
                root.mkdirs();
            }

            File gpxfile = new File(root, "overview.csv");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(csvOverviewLine);
            writer.flush();
            writer.close();

            gpxfile = new File(root, "sightings.csv");
            writer = new FileWriter(gpxfile);
            writer.append(sightingFormCsvLine);
            writer.flush();
            writer.close();

            gpxfile = new File(root, "bigEyesForm.csv");
            writer = new FileWriter(gpxfile);
            writer.append(bigEyesFormCsvLine);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
