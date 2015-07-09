package com.mohamedazzazy.test.kidscode;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mohamedazzazy.test.kidscode.java.DB;
import com.mohamedazzazy.test.kidscode.java.Kid;

public class KidInfoActivity extends AppCompatActivity {
    TextView mobile;
    Kid k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_info);
        dec();
    }

    void dec() {
        int index = getIntent().getExtras().getInt("index");
        k = DB.attList.get(index);
        ((TextView) findViewById(R.id.tvName_KidInfo)).setText(k.name);
        ((TextView) findViewById(R.id.tvCoins_KidInfo)).setText("Coins : " + k.thisSession.coin);
        ((TextView) findViewById(R.id.tvId_KidInfo)).setText("ID : " + k.id);
        ((TextView) findViewById(R.id.tvMobile_KidInfo)).setText("Mobile : " + k.mobile);
        findViewById(R.id.tvMobile_KidInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callkid();
            }
        });
    }

    void callkid() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + k.mobile));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, ShowCoinActivity.class);
        startActivity(intent);
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
