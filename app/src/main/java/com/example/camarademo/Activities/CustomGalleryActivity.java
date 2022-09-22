package com.example.camarademo.Activities;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.camarademo.R;
import com.example.camarademo.adapters.ViewPagerAdapter;

import java.io.File;
import java.util.ArrayList;

public class CustomGalleryActivity extends AppCompatActivity {

    ArrayList<String> f= new ArrayList<>();
    File[] listFile;
    private String folderName = "MyPhotoDir";
    ViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        getFromSdCard();
        mViewPager=findViewById(R.id.viewPagerMain);
        mViewPagerAdapter=new ViewPagerAdapter(this, f);
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    public void     getFromSdCard() {
        File file = new File(getExternalFilesDir(folderName), "/");

        if (file.isDirectory() ){
            listFile = file.listFiles();

            for (int i=0; i< listFile.length; i++){
                f.add(listFile[i].getAbsolutePath());
            }
        }
    }
}
