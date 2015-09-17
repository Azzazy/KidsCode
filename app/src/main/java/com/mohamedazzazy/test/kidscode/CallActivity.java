package com.mohamedazzazy.test.kidscode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.mohamedazzazy.test.kidscode.java.DB;
import com.mohamedazzazy.test.kidscode.java.Kid;

public class CallActivity extends AppCompatActivity {
    TextView display;
    Kid k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        dec();
        DB.getCallCounter();
        showNextKid();
    }

    void dec() {
        DB.updateCallActive();
        Switch rb = (Switch) findViewById(R.id.sActive_Call);
        rb.setChecked(DB.CALL_ACTIVE);
        rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DB.CALL_ACTIVE = isChecked;
                DB.updateCallActive();
            }
        });
        findViewById(R.id.bStart_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOver();
            }
        });
        findViewById(R.id.bBack_Call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DB.CALL_COUNTER >= 2) {
                    DB.CALL_COUNTER -= 2;
                    showNextKid();
                }
            }
        });
        findViewById(R.id.bCall_Call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callkid();
                showNextKid();
            }
        });
        display = (TextView) findViewById(R.id.tvName_Call);
    }

    void showNextKid() {
      if(DB.CALL_ACTIVE) {
          do {
              if (DB.CALL_COUNTER == DB.fullList.size()) {
                  DB.CALL_COUNTER = 1;
                  finish();
                  return;
              }
              k = DB.fullList.get(DB.CALL_COUNTER++);
          } while (!k.active);
      }else{
          if (DB.CALL_COUNTER == DB.fullList.size()) {
              DB.CALL_COUNTER = 1;
              finish();
              return;
          }
          k = DB.fullList.get(DB.CALL_COUNTER++);
      }
        display.setText(DB.fullList.size() - DB.CALL_COUNTER + "-" + k.name);
    }

    void stopCalling() {
        DB.CALL_COUNTER--;
        DB.updateCallCounter();
    }

    void startOver() {
        DB.CALL_COUNTER = 0;
        showNextKid();
    }

    void callkid() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + k.mobile));
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopCalling();
        super.onDestroy();
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
