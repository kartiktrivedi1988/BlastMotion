package com.blastmotion;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blastmotion.com.blastmotion.BarChartActivity;
import com.blastmotion.com.blastmotion.LineChartActivity1;
import com.blastmotion.com.blastmotion.PieChartActivity;
import com.blastmotion.com.blastmotion.file.FileChooserActivity;
import com.blastmotion.com.blastmotion.helper.ApplicationHelper;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends Activity {

    private ArrayList<String> timestamp = new ArrayList<String>(),
            sweepSpeed = new ArrayList<String>(),
            showPlane = new ArrayList<String>(),
            timing = new ArrayList<String>();
    private int timestampIndex = -1, sweepSpeedIndex = -1, showPlaneIndex = -1, timingIndex = -1;


    private String FilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.Browse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FileChooserActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        findViewById(R.id.Next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) findViewById(R.id.tvCSVPath);

                if (textView.getText().toString().toLowerCase().endsWith(".csv")) {

                    FilePath = textView.getText().toString();

                    new ReadCSV().execute();
                }
            }
        });

        findViewById(R.id.lineChart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LineChartActivity1.class);
                intent.putStringArrayListExtra("X", timestamp);
                intent.putStringArrayListExtra("Y", sweepSpeed);
                startActivity(intent);
            }
        });


        findViewById(R.id.pieChart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PieChartActivity.class);
                intent.putStringArrayListExtra("Y", showPlane);
                startActivity(intent);
            }
        });

        findViewById(R.id.barChart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BarChartActivity.class);
                intent.putStringArrayListExtra("X", timestamp);
                intent.putStringArrayListExtra("Y", timing);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    File selectedFile = (File) bundle.getSerializable(FileChooserActivity.OUTPUT_FILE_OBJECT);
                    if (selectedFile.getAbsolutePath().toLowerCase().endsWith("csv"))
                        ((TextView) findViewById(R.id.tvCSVPath)).setText(selectedFile.getAbsolutePath());
                    else
                        Toast.makeText(MainActivity.this, "Select CSV file", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class ReadCSV extends AsyncTask<Void, Void, Void> {
        private Dialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ApplicationHelper(MainActivity.this).showProgressDialog(MainActivity.this, false);
            progressDialog.show();


        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                CSVReader reader = new CSVReader(new FileReader(FilePath));
                String[] nextLine;

                boolean isSetIndex = false;
                while ((nextLine = reader.readNext()) != null) {
                    // nextLine[] is an array of values from the line
                    if (nextLine.length > 1) {
                        if (isSetIndex == false) {
                            System.out.println(nextLine[0] + nextLine[1] + "etc...");

                            for (int i = 0; i < nextLine.length; ++i) {

                                if (nextLine[i].trim().equals("timestamp"))
                                    timestampIndex = i;
                                else if (nextLine[i].trim().equals("Swing Speed"))
                                    sweepSpeedIndex = i;
                                else if (nextLine[i].trim().equals("Swing Plane"))
                                    showPlaneIndex = i;
                                else if (nextLine[i].trim().equals("Timing"))
                                    timingIndex = i;

                            }

                            isSetIndex = true;
                        } else {
                            timestamp.add(nextLine[timestampIndex]);
                            sweepSpeed.add(nextLine[sweepSpeedIndex]);
                            showPlane.add(nextLine[showPlaneIndex]);
                            timing.add(nextLine[timingIndex]);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

//            Intent intent = new Intent(MainActivity.this, LineChartActivity1.class);
//            intent.putStringArrayListExtra("X", timestamp);
//            intent.putStringArrayListExtra("Y", sweepSpeed);
//            startActivity(intent);

            findViewById(R.id.layCharts).setVisibility(View.VISIBLE);
            findViewById(R.id.layReadCSV).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (findViewById(R.id.layCharts).isShown()) {
                findViewById(R.id.layCharts).setVisibility(View.GONE);
                findViewById(R.id.layReadCSV).setVisibility(View.VISIBLE);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
