package com.mohamedazzazy.test.kidscode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.NotificationManager;

import com.mohamedazzazy.test.kidscode.java.DB;

public class ActionsActivity extends Activity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions);
        dec();
        DB.IS_OPENNED_BEFORE = true;
    }

    public void dec() {
        findViewById(R.id.bCoin).setOnClickListener(this);
        findViewById(R.id.bEnd).setOnClickListener(this);
        findViewById(R.id.bShowCoin).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        int mId = 5;
        switch (v.getId()) {
            case R.id.bCoin:
                i = new Intent(getApplicationContext(), CoinsActivity.class);
                startActivity(i);
                break;
            case R.id.bShowCoin:
                i = new Intent(getApplicationContext(), ShowCoinActivity.class);
                startActivity(i);
                break;
            case R.id.bEnd:
                i = new Intent(getApplicationContext(), DB.class);
                stopService(i);
                DB.IS_OPENNED_BEFORE = false;
                finish();
                i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actions, menu);
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