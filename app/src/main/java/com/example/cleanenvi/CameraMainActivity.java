package com.example.cleanenvi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class CameraMainActivity extends AppCompatActivity {
    private SurfaceView cameraView;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private TextView barcodeText;
    private String barcodeData;
    public static String EAN_CAMERA, EAN_CAM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.camera_main);
        this.setTitle("Produktsuche");


        cameraView = findViewById(R.id.camera_view);
        barcodeText = findViewById(R.id.barcode_text); //Textfeld für spätere Fehlersuche
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_camera);

        //Wenn Kameraberechtigung erteilt -> initialisieren des Kamerabilds etc., ansonsten wird User nach Berechtigung gefragt
        if(ActivityCompat.checkSelfPermission(CameraMainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initialiseDetectorsAndSources();
        } else {
            ActivityCompat.requestPermissions(CameraMainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            finish();
            overridePendingTransition(0, 0);
            this.recreate();
            overridePendingTransition(0, 0);
        }

        //Verhindern des Layoutflackerns
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_home) {
                    CameraMainActivity.this.startActivity(new Intent(CameraMainActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                } else if(id == R.id.action_search) {
                    CameraMainActivity.this.startActivity(new Intent(CameraMainActivity.this, ProductSearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                } else if(id == R.id.action_camera) {
                    CameraMainActivity.this.startActivity(new Intent(CameraMainActivity.this, CameraMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                } else if (id == R.id.action_hofkarte) {
                    CameraMainActivity.this.startActivity(new Intent(CameraMainActivity.this, MapActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                }
                return true;
            }
        });
    }



    //Initialisierung des BarcodeDetectors
    private void initialiseDetectorsAndSources() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true).build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if(ActivityCompat.checkSelfPermission(CameraMainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(cameraView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(CameraMainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            //Verarbeitung des erkannten Bilds/Barcodes
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if(barcodes.size() != 0) {
                    barcodeText.post(new Runnable() {
                        @Override
                        public void run() {
                            if(barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                            } else {
                                barcodeData = barcodes.valueAt(0).displayValue;
                            }
                            EAN_CAM = barcodeData;
                            EAN_CAMERA = EAN_CAM.trim();
                            CameraMainActivity.this.startActivity(new Intent(CameraMainActivity.this, com.example.cleanenvi.productmanager.ProductShowActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

                            // Textänderung von BarcodeText, bleibt drin für mögliche spätere Problemänderungen
                            // barcodeText.setText(barcodeData);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSupportActionBar().hide();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().hide();
        initialiseDetectorsAndSources();
    }
}