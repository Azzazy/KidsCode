package com.mohamedazzazy.test.kidscode;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mohamedazzazy.test.kidscode.java.DB;
import com.mohamedazzazy.test.kidscode.java.Kid;

public class ShowCoinActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    ListView disp;
    //TODO : set OnClickListener on choosing a kid to display his information
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_coin);
        dec();
        displayCoins();
    }

    public void displayCoins() {

        disp.setAdapter(DB.getAdapterOfAtt(Kid.SHOWCASE_NAME_AND_COINS,false));
    }

    public void dec() {
        disp = (ListView) findViewById(R.id.lvShowCoin);
        disp.setOnItemSelectedListener(this);
        findViewById(R.id.bDone).setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // TODO : complete this info activity
        /*Intent i = new Intent(this,KidInfoActivity.class);
        i.putExtra("index",position);
        startActivity(i);*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bDone:
                finish();
                break;
        }
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
}
