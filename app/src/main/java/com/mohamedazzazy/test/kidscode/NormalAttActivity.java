package com.mohamedazzazy.test.kidscode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mohamedazzazy.test.kidscode.java.DB;

public class NormalAttActivity extends AppCompatActivity implements View.OnClickListener {
    TextView display;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_normal_att);
        dec();
        startTheService();
        displayNext();

    }

    public void startTheService() {
        Intent theServiceTntent = new Intent(getApplicationContext(), DB.class);
        startService(theServiceTntent);
    }

    public void dec() {
        display = (TextView) findViewById(R.id.tvName);
        findViewById(R.id.bStop).setOnClickListener(this);
        findViewById(R.id.bNo).setOnClickListener(this);
        findViewById(R.id.bYes).setOnClickListener(this);
        findViewById(R.id.bAll_NormalAtt).setOnClickListener(this);
        counter = 0;
    }


    public void displayNext() {
        if (counter < DB.attList.size())
            display.setText((DB.attList.size() - counter) + "-" + DB.attList.get(counter++).name);
        else finished();
    }

    public void notHereAction() {
        DB.attList.remove(--counter);
        displayNext();
    }

    public void finished() {
        Intent i = new Intent(getApplicationContext(), ActionsActivity.class);
        startActivity(i);
    }

    public void stopAtt() {
        counter--;
        while (DB.attList.size() > counter) {
            DB.attList.remove(counter);
        }
        finished();
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
            case R.id.bAll_NormalAtt:
                finished();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_normal, menu);
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
