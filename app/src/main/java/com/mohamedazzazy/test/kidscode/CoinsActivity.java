package com.mohamedazzazy.test.kidscode;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.mohamedazzazy.test.kidscode.java.DB;
import com.mohamedazzazy.test.kidscode.java.Kid;
import com.mohamedazzazy.test.kidscode.java.Session;

public class CoinsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) k = DB.attList.get(position - 1);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void dec() {
        findViewById(R.id.bPositive).setOnClickListener(this);
        findViewById(R.id.bNegative).setOnClickListener(this);
        display = (TextView) findViewById(R.id.tvLogCoin);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(DB.getAdapterOfKidsInAtt(Kid.SHOWCASE.NAME_ONLY, true));
        spinner.setOnItemSelectedListener(this);
        NumberPicker np = (NumberPicker) findViewById(R.id.npCoinCounter_Coins);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                coins = newVal;
            }
        });
        np.setMaxValue(10);
        np.setMinValue(0);
    }

    public void AddCoinToKid(boolean CHANGE_CASE) {
        if (k == null) {
            for (Kid k:DB.attList) {
                Session s = DB.attList.get(DB.findByIdInAtt(k.id)).thisSession;
                s.coin += CHANGE_CASE ? coins : -coins;
                k.thisSession = s;
                DB.attList.set(DB.findByIdInAtt(k.id), k);
            }
            display.setText("Everybody " + (CHANGE_CASE ? '+' : '-') + coins);
        } else {
            Session s = DB.attList.get(DB.findByIdInAtt(k.id)).thisSession;
            s.coin += CHANGE_CASE ? coins : -coins;
            k.thisSession = s;
            DB.attList.set(DB.findByIdInAtt(k.id), k);
            spinner.setSelection(0);
            display.setText(k.name + "  " + (CHANGE_CASE ? '+' : '-') + coins);
            k = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bPositive:
                AddCoinToKid(true);
                break;
            case R.id.bNegative:
                AddCoinToKid(false);
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
