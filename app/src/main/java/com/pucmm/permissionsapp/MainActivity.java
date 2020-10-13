package com.pucmm.permissionsapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private Switch switch_storage, switch_location, switch_camera, switch_phone, switch_contacts;
    private Button btn_continue, btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switch_storage = findViewById(R.id.switch_storage);
        switch_location = findViewById(R.id.switch_location);
        switch_camera = findViewById(R.id.switch_camera);
        switch_phone = findViewById(R.id.switch_phone);
        switch_contacts = findViewById(R.id.switch_contacts);

        btn_continue = findViewById(R.id.btn_continue);
        btn_cancel = findViewById(R.id.btn_cancel);

        switch_storage.setOnCheckedChangeListener(this);
        switch_location.setOnCheckedChangeListener(this);
        switch_camera.setOnCheckedChangeListener(this);
        switch_phone.setOnCheckedChangeListener(this);
        switch_contacts.setOnCheckedChangeListener(this);

        btn_continue.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final String [] access = getAccess();
                ActivityCompat.requestPermissions(MainActivity.this, access, PERMISSION_REQUEST_CODE);
            }
        });


        btn_cancel.setOnClickListener(v -> {
            finish();
            System.exit(0);
        });

        checkSwitch();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String[] getAccess(){
        ArrayList<String> list = new ArrayList<>();
        list.add(switch_storage.isChecked() ? READ_EXTERNAL_STORAGE : "");
        list.add(switch_location.isChecked() ? ACCESS_FINE_LOCATION : "");
        list.add(switch_camera.isChecked() ? CAMERA : "");
        list.add(switch_phone.isChecked() ? CALL_PHONE : "");
        list.add(switch_contacts.isChecked() ? READ_CONTACTS : "");

        list.stream().filter(f -> !f.isEmpty()).collect(Collectors.toList());

        return list.toArray(new String[list.size()]);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
        startActivity(intent);
    }

    private void checkSwitch(){
        if (!switch_storage.isChecked() && ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            switch_storage.setChecked(true); }
        if (!switch_location.isChecked() && ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            switch_location.setChecked(true); }
        if (!switch_camera.isChecked() && ActivityCompat.checkSelfPermission(this, CAMERA) == PackageManager.PERMISSION_GRANTED){
            switch_camera.setChecked(true); }
        if (!switch_phone.isChecked() && ActivityCompat.checkSelfPermission(this, CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            switch_phone.setChecked(true); }
        if (!switch_contacts.isChecked() && ActivityCompat.checkSelfPermission(this, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            switch_contacts.setChecked(true); }
    }


    @Override
    public void onCheckedChanged(CompoundButton v, boolean b) {
        Switch view;
        String access;

        switch(v.getId()){
            case R.id.switch_storage:
                view = findViewById(R.id.switch_storage);
                access = READ_EXTERNAL_STORAGE;
                break;
            case R.id.switch_location:
                view = findViewById(R.id.switch_location);
                access = ACCESS_FINE_LOCATION;
                break;
            case R.id.switch_camera:
                view = findViewById(R.id.switch_camera);
                access = CAMERA;
                break;
            case R.id.switch_phone:
                view = findViewById(R.id.switch_phone);
                access = CALL_PHONE;
                break;
            case R.id.switch_contacts:
                view = findViewById(R.id.switch_contacts);
                access = READ_CONTACTS;
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }

        if (!view.isChecked() && ActivityCompat.checkSelfPermission(this, access) == PackageManager.PERMISSION_GRANTED){
            view.setChecked(true);
            Snackbar.make(view, "Permission granted.", Snackbar.LENGTH_LONG).show();
        }

    }

}