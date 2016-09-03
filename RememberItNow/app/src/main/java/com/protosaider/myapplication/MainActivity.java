package com.protosaider.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "Logger";
    Button btnMain;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreateMain");
        btnMain = (Button) findViewById(R.id.btn_main);
        btnMain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(MainActivity.this, spinner.getSelectedItem().toString() + " selected", Toast.LENGTH_LONG).show();
            }
        });
        spinner = (Spinner) findViewById(R.id.spinner_main);
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

}
