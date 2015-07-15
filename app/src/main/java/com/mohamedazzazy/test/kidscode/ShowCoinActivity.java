package com.mohamedazzazy.test.kidscode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mohamedazzazy.test.kidscode.java.DB;
import com.mohamedazzazy.test.kidscode.java.Kid;

public class ShowCoinActivity extends Activity {
    ListView disp;
    boolean FROM_ACTIONS_ACTIVITY = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_coin);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            FROM_ACTIONS_ACTIVITY = extras.getBoolean("From_Actions_Activity");
        }
        dec();
        if (FROM_ACTIONS_ACTIVITY) {
            displayCoinsFromAtt();
        } else {
            displayCoinsFromFull();
        }
    }

    public void displayCoinsFromAtt() {
        disp.setAdapter(DB.getAdapterOfKidsInAtt(Kid.SHOWCASE_NAME_COINS, false));
    }

    public void displayCoinsFromFull() {
        disp.setAdapter(DB.getAdapterOfKidsInFull(Kid.SHOWCASE_ACTIVE_NAME_COINS));
    }

    public void dec() {
        disp = (ListView) findViewById(R.id.lvShowCoin);
        disp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ShowCoinActivity.this, KidInfoActivity.class);
                i.putExtra("index", position);
                i.putExtra("From_Actions_Activity", FROM_ACTIONS_ACTIVITY);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_coin, menu);
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

    @Override
    public void onBackPressed() {
        finish();
        DB.rewriteFullDB();
        super.onBackPressed();
    }
}
