package com.mohamedazzazy.test.kidscode;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.mohamedazzazy.test.kidscode.java.DB;
import com.mohamedazzazy.test.kidscode.java.Kid;

public class KidInfoActivity extends Activity {
    TextView mobile;
    Kid k;
    ListView list;
    static boolean FROM_ACTIONS_ACTIVITY = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_info);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            FROM_ACTIONS_ACTIVITY = extras.getBoolean("From_Actions_Activity");
        }
        dec();
    }

    void dec() {
        final int index = getIntent().getExtras().getInt("index");
        if (FROM_ACTIONS_ACTIVITY) {
            k = DB.attList.get(index);
        } else {
            k = DB.fullList.get(index);
        }
        ((TextView) findViewById(R.id.tvName_KidInfo)).setText(k.name);
        ((TextView) findViewById(R.id.tvCoins_KidInfo)).setText("Coins : " + k.getTotalCoins());
        ((TextView) findViewById(R.id.tvId_KidInfo)).setText("ID : " + k.id);
        if (k.mobile == null) {
            ((TextView) findViewById(R.id.tvMobile_KidInfo)).setText("Mobile : Not available");
        } else {
            ((TextView) findViewById(R.id.tvMobile_KidInfo)).setText("Mobile : " + k.mobile);
            findViewById(R.id.tvMobile_KidInfo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callkid();
                }
            });
        }
        if (!FROM_ACTIONS_ACTIVITY) {
            list = (ListView) findViewById(R.id.lvCoins_KidInfo);
            list.setVisibility(View.VISIBLE);
            list.setAdapter(DB.getAdapterOfSessions(index));
            Switch rb = (Switch) findViewById(R.id.rbActive_KidInfo);
            rb.setVisibility(View.VISIBLE);
            rb.setChecked(k.active);
            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    k.active=isChecked;
                    DB.fullList.set(index,k);
                    DB.NEED_REWRITE=true;
                }
            });
        }
    }

    void callkid() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + k.mobile));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kid_info, menu);
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
