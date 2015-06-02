package com.blastmotion;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blastmotion.com.blastmotion.LineChartActivity1;
import com.blastmotion.com.blastmotion.file.FileChooserActivity;
import com.blastmotion.com.blastmotion.helper.ApplicationHelper;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dhaval on 6/1/2015.
 */
public class ReadCSVFileActivity extends Activity{
    private ArrayList<String> timestamp = new ArrayList<String>(), sweepSpeed = new ArrayList<String>();
    private int timestampIndex = -1, sweepSpeedIndex = -1;
    private Dialog progressDialog;

    private String FilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_selection);
        FilePath = getIntent().getStringExtra(FileChooserActivity.OUTPUT_NEW_FILE_NAME);
        progressDialog = new ApplicationHelper(this).showProgressDialog(this, true);
//        new ReadCSV().execute();

        findViewById(R.id.lineChart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
