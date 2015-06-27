package com.mohamedazzazy.test.kidscode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mohamedazzazy.test.kidscode.java.DB;

public class NormalAttActivity extends AppCompatActivity implements View.OnClickListener {
    //////// stopped here last time 27/6/2015
    TextView display;
    Button byes, bno, bstop;
    int counter;
    int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_att);
        DB.getAttDataBase(getAgeChar());
        dec();
        displayNext();
    }

    public void dec() {
        byes = (Button) findViewById(R.id.bYes);
        bno = (Button) findViewById(R.id.bNo);
        bstop = (Button) findViewById(R.id.bStop);
        display = (TextView) findViewById(R.id.tvName);
        byes.setOnClickListener(this);
        bno.setOnClickListener(this);
        bstop.setOnClickListener(this);
        counter = 0;
        size = DB.attList.size();
    }

    public char getAgeChar() {
        Bundle extras = getIntent().getExtras();
        return extras.getChar("AGE");
    }

    public void displayNext() {
        if (counter < DB.attList.size())
            display.setText((DB.attList.size() - counter) + "-" + DB.attList.get(counter++).name);
        else toTheNext();
    }

    public void notHereAction() {
        DB.attList.remove(--counter);
        displayNext();
    }

    public void toTheNext() {
        Intent i = new Intent(getApplicationContext(), ActionsActivity.class);
        startActivity(i);
    }

    public void stopAtt() {
        counter--;
        while (DB.attList.size() > counter) {
            DB.attList.remove(counter);
        }
        toTheNext();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bYes:
                displayNext();
                break;
            case R.id.bNo:
                notHereAction();
                break;
            case R.id.bStop:
                stopAtt();
                break;
        }
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
