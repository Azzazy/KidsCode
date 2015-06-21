package com.mohamedazzazy.test.kidscode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class StatActivity extends AppCompatActivity implements View.OnClickListener {
    Button coin, att, all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        dec();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bCoin:

                break;
            case R.id.bAtt:

                break;
            case R.id.bAll:

                break;
        }
    }

    public void dec() {
        coin = (Button) findViewById(R.id.bCoin);
        att = (Button) findViewById(R.id.bAtt);
        all = (Button) findViewById(R.id.bAll);
        coin.setOnClickListener(this);
        att.setOnClickListener(this);
        all.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stat, menu);
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
