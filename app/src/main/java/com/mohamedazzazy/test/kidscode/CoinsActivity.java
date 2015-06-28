package com.mohamedazzazy.test.kidscode;

import android.app.ExpandableListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.mohamedazzazy.test.kidscode.java.DB;

public class CoinsActivity extends AppCompatActivity implements View.OnClickListener {
    ////////////////// stopped here last time 28/6/2015

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coins);
        dec();
    }

    public void dec() {
        findViewById(R.id.bPositive).setOnClickListener(this);
        findViewById(R.id.bNegative).setOnClickListener(this);
        findViewById(R.id.bDoneCoins).setOnClickListener(this);
        ExpandableListView elv = (ExpandableListView) findViewById(R.id.elvNamesForCoins);
        elv.setAdapter(DB.getAdapterOfAtt(this)); // Needs ExpandableListAdapter
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bDoneCoins:

                break;
            case R.id.bPositive:

                break;
            case R.id.bNegative:

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coins, menu);
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
