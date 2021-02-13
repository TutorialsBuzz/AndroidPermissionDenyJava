package com.tutorialsbuzz.permissiondeny;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_WRITE_PERMISSION = 100;
    private Button allowPermissionViaSettingsBtn;
    private TextView label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        label = findViewById(R.id.label);

        Button requestPermissionBtn = findViewById(R.id.btn);
        requestPermissionBtn.setOnClickListener(this);

        allowPermissionViaSettingsBtn = findViewById(R.id.allow_permission);
        allowPermissionViaSettingsBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermissionForReadExtertalStorage()) {
            label.setText(R.string.permission_granted);
            allowPermissionViaSettingsBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:

                if (checkPermissionForReadExtertalStorage()) {
                    label.setText(R.string.permission_granted);
                } else {
                    //Make Request
                    requestPermission();
                }

                break;

            case R.id.allow_permission:

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            if (requestCode == REQUEST_WRITE_PERMISSION)
                if (grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    label.setText(R.string.permission_granted);
                    allowPermissionViaSettingsBtn.setVisibility(View.GONE);

                } else {
                    // permission denied
                    label.setText(R.string.permission_denied);
                    // Check wheather checked dont ask again
                    checkUserRequestedDontAskAgain();
                }
        }
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_PERMISSION);
        }
    }

    private void checkUserRequestedDontAskAgain() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean rationalFalgREAD = shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            boolean rationalFalgWRITE = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);

            if (!rationalFalgREAD && !rationalFalgWRITE) {
                label.setText(R.string.permission_denied_forcefully);
                allowPermissionViaSettingsBtn.setVisibility(View.VISIBLE);
            }
        }
    }

}