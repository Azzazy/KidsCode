package com.mohamedazzazy.test.kidscode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AttActivity extends AppCompatActivity implements View.OnClickListener {

    Intent next;
    char ageChar = 'O';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_att);
        dec();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bNormal:
                next = new Intent(getApplicationContext(), NormalAttActivity.class);
                next.putExtra("AGE", ageChar);
                startActivity(next);
                break;
            case R.id.bQr:
                next = new Intent(getApplicationContext(), NormalAttActivity.class);
                startActivity(next);
                break;
            case R.id.bOld:
                ageChar = 'O';
                break;
            case R.id.bMeddle:
                ageChar = 'M';
                break;
            case R.id.bLittle:
                ageChar = 'L';
                break;
        }


    }

    public void dec() {
        findViewById(R.id.bNormal).setOnClickListener(this);
        findViewById(R.id.bQr).setOnClickListener(this);
        findViewById(R.id.bOld).setOnClickListener(this);
        findViewById(R.id.bMeddle).setOnClickListener(this);
        findViewById(R.id.bLittle).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_att, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
