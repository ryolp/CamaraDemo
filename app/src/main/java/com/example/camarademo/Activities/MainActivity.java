package com.example.camarademo.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.camarademo.Clases.ShowCameraSurfaceView;
import com.example.camarademo.R;

public class MainActivity extends AppCompatActivity {

    private Button btnTomarFoto = null;
    private Button btnMostrarFotos = null;
    private FrameLayout frameLayout = null;
    private ShowCameraSurfaceView showCamera = null;
    private Camera camera;
    private Camera.PictureCallback _pictureCallback;
    private byte[] _imageTaken;

    ActivityResultLauncher<String> cameraPermissionLauncher;
    ActivityResultLauncher<Intent> useCameraLauncher;

    private final static int CAMERA_PERMISSION_CODE = 1;

    private final static int MOSTRAR_IMAGEN = 1;
    private final static int FOTOS = 3;
    private final static int VER_FOTOS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registrarValidacionPermisos();
        inicializarControles();
        inicializarEventosControles();
        //inicializarCamara();
    }



    protected void inicializarControles() {
        btnMostrarFotos = findViewById(R.id.btnMostrarFotos);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);
        //visor = findViewById(R.id.iv_visor);
        frameLayout = findViewById(R.id.frameLayout);

        //btnMostrarFotos.setOnClickListener(v -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA));
    }

    private void inicializarEventosControles() {
        btnMostrarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarFotos();
            }
        });

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto();
            }
        });
    }

    private void mostrarFotos() {
        Intent intentVerFotos = new Intent(MainActivity.this, CustomGalleryActivity.class);

        startActivityForResult(intentVerFotos, VER_FOTOS);
    }

    protected void inicializarCamara() {
        _pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                _imageTaken = bytes;

                if (bytes != null) {
                    if (Build.VERSION.SDK_INT >= 11)
                        camera.stopPreview();
                    mostrarImage(bytes);
                }
            }
        };

        showCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessageLong("Click");
            }
        });
    }

    protected void mostrarCamara() {
        if (showCamera == null) {
            camera = Camera.open();
            showCamera = new ShowCameraSurfaceView(this, camera);
            frameLayout.addView(showCamera);
            inicializarCamara();
        }
    }

    protected void tomarFoto() {
        // camera.takePicture(null, null, _pictureCallback);
        Intent intentCamara = new Intent(MainActivity.this, Camera2Activity.class);

        startActivityForResult(intentCamara, FOTOS);
    }

    protected void mostrarImage(byte[] imagen) {
        byte[] imagenAux;

        imagenAux = imagen;

        Intent intent = new Intent(MainActivity.this, ShowImageActivity.class);

        startActivityForResult(intent, MOSTRAR_IMAGEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == MOSTRAR_IMAGEN) {

        }
    }

    protected void registrarValidacionPermisos() {
        cameraPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                result -> {
                    if (result) {
                        mostrarCamara();
                    } else {
                        solicitarPermisoCamara();
                    }
                });
    }

//    protected void validarPermisos() {
//        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            btnCamara.setEnabled(false);
//            Toast.makeText(this, "No tiene los permisos para usar la cámara", Toast.LENGTH_LONG).show();
//        } else
//            solicitarPermisoCamara();
//    }

    private void solicitarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            mostrarCamara();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permiso necesario")
                        .setMessage("Permiso requerido para la aplicación")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else
                showMessageLong("No se puede usar la cámara porque no se otorgó el permiso");
        } else
            showMessageLong("Faltan asignar permisos a la cámara");
    }

    private void showMessageLong(String sMessage) {
        Toast.makeText(this, sMessage, Toast.LENGTH_LONG).show();
    }

    private void showMessageShort(String sMessage) {
        Toast.makeText(this, sMessage, Toast.LENGTH_SHORT).show();
    }

//    // Abre o activa la cámara
//    protected void camara() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, 1);
//        }
//    }
//
//    // Este bloque captura la imagen
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//
//            Bitmap imgBitmap = (Bitmap) extras.get("data");
//            visor.setImageBitmap(imgBitmap);
//        }
//    }
}