package com.protosaider.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private static final String TAG = "Logger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreateMain");
        //Button btnMain = (Button) findViewById(R.id.);
        //btnMain.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        // пункты меню с ID группы = 1 видны, если в CheckBox стоит галка
        menu.setGroupVisible(1, chb.isChecked());
        return super.onPrepareOptionsMenu(menu);
    } */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_settings:
               Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);//вызов активити
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v)
    {
//для нескольких кнопок = v.getId => switch
        Toast.makeText(this, "onCLick", Toast.LENGTH_LONG).show();
    }
}

/*
Button btnMain;

    Button btnMain = (Button) findViewById(R.id.);

    btnMain.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View v)
    {
    }
    };)
}
*/