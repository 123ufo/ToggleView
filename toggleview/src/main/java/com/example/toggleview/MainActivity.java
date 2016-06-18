package com.example.toggleview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toggleview.view.ToggleView;

public class MainActivity extends AppCompatActivity implements ToggleView.OnToggleViewSwitchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToggleView toggleView = (ToggleView) findViewById(R.id.tv);
        toggleView.setTextColor(Color.GREEN);
//        toggleView.setTextSize(110);

        toggleView.setOnToggleViewSiwtchListener(this);

    }

    @Override
    public void onSwitch(int stats) {
        if(stats == ToggleView.STATS_TOGGLE_MAX){
            Toast.makeText(this,"1080p",Toast.LENGTH_SHORT).show();
        }else if (stats== ToggleView.STATS_TOGGLE_MIN){
            Toast.makeText(this,"720p",Toast.LENGTH_SHORT).show();
        }
    }
}
