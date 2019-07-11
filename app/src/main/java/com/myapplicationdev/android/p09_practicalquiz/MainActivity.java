package com.myapplicationdev.android.p09_practicalquiz;

import android.Manifest;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    String folderLocation;
    EditText et;
    Button btnSave, btnRead;
    TextView tvRead;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = findViewById(R.id.etCoordinates);
        btnSave = findViewById(R.id.btnSave);
        btnRead = findViewById(R.id.btnRead);
        tvRead = findViewById(R.id.tvRead);

        if (checkPermission()) {

            folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/quiz";

            File folder = new File(folderLocation);
            if (folder.exists() == false) {
                boolean result = folder.mkdir();
                if (result == true) {
                    Log.d("File Read/Write", "Folder created");
                }
            }

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Code for file writing
                    try {
                        File file = new File(folderLocation, "quiz.txt");
                        FileWriter fileWriter = new FileWriter(file, false);
                        fileWriter.write(et.getText().toString() + "\n");
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            btnRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File targetFile = new File(folderLocation, "quiz.txt");
                    if (targetFile.exists() == true) {
                        String data = "";
                        try {
                            FileReader reader = new FileReader(targetFile);
                            BufferedReader br = new BufferedReader(reader);

                            String line = br.readLine();
                            while (line != null) {
                                data += line + "\n";
                                line = br.readLine();
                            }

                            br.close();
                            reader.close();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Failed to read!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        Log.d("Content", data);
                        tvRead.setText(data);
                    } else{
                        Toast.makeText(MainActivity.this, "File does not exist!", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }

    private boolean checkPermission() {
        int permissionCheck_Write = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck_Read = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck_Write == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Read == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}
