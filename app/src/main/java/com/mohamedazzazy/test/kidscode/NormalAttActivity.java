package com.mohamedazzazy.test.kidscode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NormalAttActivity extends AppCompatActivity implements View.OnClickListener {
 //////// stoped here last time 24/6/2015
    TextView display;
    Button byes, bno,bstop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_att);
        dec();
    }

    public void dec() {
        bstat = (Button) findViewById(R.id.bStat);
        bstart = (Button) findViewById(R.id.bStart);
        bget = (Button) findViewById(R.id.bGet);
        bget.setOnClickListener(this);
        bstart.setOnClickListener(this);
        bstat.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bStat:
                next = new Intent(getApplicationContext(), StatActivity.class);
                break;
            case R.id.bStart:
                next = new Intent(getApplicationContext(), AttActivity.class);
                break;
            case R.id.bGet:
                readFullDatabase();
                break;
        }
        startActivity(next);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_normal_att, menu);
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
