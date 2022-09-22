package com.example.camarademo.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.camarademo.Clases.ShowCameraSurfaceView;
import com.example.camarademo.R;

public class CameraPreviewFragment extends Fragment {

    Button btnCamara;
    Button btnMostrarCamara;
    FrameLayout frameLayout;
    ShowCameraSurfaceView showCamera;
    Camera camera;

    ActivityResultLauncher<String> cameraPermissionLauncher;
    ActivityResultLauncher<Intent> useCameraLauncher;

    private int CAMERA_PERMISSION_CODE = 1;

    public CameraPreviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =inflater.inflate(R.layout.fragment_camera_preview, container, false);

        inicializarControles();

        return v;
    }

    protected void inicializarControles(){
        btnCamara = getView().findViewById(R.id.btnMostrarFotos);
        btnMostrarCamara = getView().findViewById(R.id.btnTomarFoto);
        //visor = findViewById(R.id.iv_visor);
        frameLayout =  getView().findViewById(R.id.frameLayout);

        btnCamara.setOnClickListener(v -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA));

        btnMostrarCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarCamara();
            }
        });
    }

    protected void mostrarCamara() {
        camera = Camera.open();
        showCamera = new ShowCameraSurfaceView(this.getActivity(), camera);
        frameLayout.addView(showCamera);
    }

    protected void tomarFoto() {

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

    private void solicitarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            mostrarCamara();
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this.getActivity())
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
            }
            else
                showMessageLong("No se puede usar la cámara porque no se otorgó el permiso");
        }
        else
            showMessageLong("Faltan asignar permisos a la cámara");
    }

    private void showMessageLong(String sMessage) {
        Toast.makeText(this.getActivity(), sMessage, Toast.LENGTH_LONG).show();
    }

    private void showMessageShort(String sMessage) {
        Toast.makeText(this.getActivity(), sMessage, Toast.LENGTH_SHORT).show();
    }
}