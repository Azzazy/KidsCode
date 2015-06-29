package com.mohamedazzazy.test.kidscode;

import android.app.ExpandableListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.mohamedazzazy.test.kidscode.java.DB;
import com.mohamedazzazy.test.kidscode.java.Kid;
import com.mohamedazzazy.test.kidscode.java.Session;

public class CoinsActivity extends AppCompatActivity implements View.OnClickListener, View.OnItemSelectedListener, View.RadioGroup.OnCheckedChangeListener {
    ////////////////// stopped here last time 28/6/2015

    Spinner spinner;
    TextView display;
    Kid k = null;
    int coins = 0;

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
        display = (TextView) findViewById(R.id.tvLogCoin);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(DB.getAdapterOfAtt(this, Kid.SHOWMODE_NAME_ONLY));

    }

    public void changeCoin(boolean CHANGE_CASE) {
        if (k != null && coins > 0) {
            Session s = DB.attList.get(DB.findInAtt(k)).sessions.get(0);
            s.coin += CHANGE_CASE ? coins : -coins;
            k.sessions.set(0, s);
            DB.attList.set(DB.findInAtt(k), k);
            spinner.setAdapter(0);
            display.setText(k.name + "  " + (CHANGE_CASE ? '+' : '-') + coins);
            k = null;
        }
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        coins = Integer.parseInt(findViewById(checkedId).getText());
    }

    public void onItemSelected(AdapterView<?> parent, View view,
            int pos, long id) {
        k = DB.attList.get(pos);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bDoneCoins:
                Intent i = new Intent(getApplicationContext(), ActionsActivity.class);
                startActivity(i);
                break;
            case R.id.bPositive:
                changeCoin(true);
                break;
            case R.id.bNegative:
                changeCoin(false);
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
