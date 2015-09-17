package com.mohamedazzazy.test.kidscode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mohamedazzazy.test.kidscode.java.DB;
import com.mohamedazzazy.test.kidscode.java.Kid;

public class ShowCoinActivity extends AppCompatActivity {
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
            if(DB.fullList!=null)
            displayCoinsFromFull();
        }
    }

    public void displayCoinsFromAtt() {
        disp.setAdapter(DB.getAdapterOfKidsInAtt(Kid.SHOWCASE.NAME_COINS, false));
    }

    public void displayCoinsFromFull() {
        if (DB.AGE_CHAR == 'A') {
            disp.setAdapter(DB.getAdapterOfKidsInFull(Kid.SHOWCASE.AGE_ACTIVE_NAME_COINS));

        } else {
            disp.setAdapter(DB.getAdapterOfKidsInFull(Kid.SHOWCASE.ACTIVE_NAME_COINS));
        }
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
        if (FROM_ACTIONS_ACTIVITY)
            getMenuInflater().inflate(R.menu.menu_normal, menu);
        else
            getMenuInflater().inflate(R.menu.menu_show_coin, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_call_showcoin:
                Intent i = new Intent(this, CallActivity.class);
                startActivity(i);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        DB.rewriteFullDB();
        super.onBackPressed();
    }
}
