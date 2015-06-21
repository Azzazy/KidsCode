package com.mohamedazzazy.test.kidscode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AttActivity extends AppCompatActivity implements View.OnClickListener {
    Button norm;
    Button qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_att);
        dec();
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bNormal:

                break;
            case R.id.bQr:

                break;
        }
    }
    public void dec() {
        norm = (Button) findViewById(R.id.bNormal);
        qr = (Button) findViewById(R.id.bQr);
        norm.setOnClickListener(this);
        qr.setOnClickListener(this);

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
