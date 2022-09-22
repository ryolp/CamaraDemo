package com.example.camarademo.Clases;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.PipedInputStream;

public class ShowCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private Camera _camera;
    private SurfaceHolder _holder;
    private Context _context;

    public ShowCameraSurfaceView(Context context, Camera camera) {
        super(context);

        this._context = context;
        this._camera = camera;
        this._holder = getHolder();
        _holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        Camera.Parameters params = _camera.getParameters();

        // change de orientation

        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            params.set("orientation", "portrait");
            _camera.setDisplayOrientation(90);
            params.setRotation(90);
        } else {
            params.set("orientation", "landscape");
            _camera.setDisplayOrientation(0);
            params.setRotation(0);
        }

        _camera.setParameters(params);
        try {
            _camera.setPreviewDisplay(_holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        _camera.startPreview();

    }

    private Camera.Size getBestPreviewSize(int width, int height,Camera.Parameters parameters){
        Camera.Size result=null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes())
        {
            if (size.width<=width && size.height<=height)
            {
                if (result==null) {
                    result=size;
                }   else {
                    int resultArea=result.width*result.height;
                    int newArea=size.width*size.height;
                    if (newArea>resultArea) {
                        result=size;
                    }
                }
            }
        }
        return(result);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (surfaceHolder.getSurface() == null) {
            // Return if preview surface does not exist
            return;
        }

        if (_camera != null) {
            // Stop if preview surface is already running.
            _camera.stopPreview();
            try {
                // Set preview display
                _camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Start the camera preview...
            _camera.startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        if (_camera != null) {
            _camera.stopPreview();
            _camera.release();
            _camera = null;
        }
    }

//    @Override
//    public void onClick(View view) {
//
//    }
}
