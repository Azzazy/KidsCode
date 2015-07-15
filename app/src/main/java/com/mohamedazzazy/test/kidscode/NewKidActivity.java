package com.mohamedazzazy.test.kidscode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.mohamedazzazy.test.kidscode.java.DB;
import com.mohamedazzazy.test.kidscode.java.Kid;
import com.mohamedazzazy.test.kidscode.java.Session;

import java.util.ArrayList;
import java.util.Date;

public class NewKidActivity extends AppCompatActivity {
    static boolean FROM_ACTIONS_ACTIVITY = true;
    EditText name, mobile, id;
    char ageChar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_kid);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            FROM_ACTIONS_ACTIVITY = extras.getBoolean("From_Actions_Activity");
        }
        dec();
    }

    void add() {
        Kid k = new Kid(id.getText().toString(), name.getText().toString(), mobile.getText().toString(), ageChar);
        k.thisSession = new Session(0, new Date());
        if(FROM_ACTIONS_ACTIVITY){
            DB.newKidsList = new ArrayList<>();
            DB.newKidsList.add(k);
            DB.attList.add(k);
        }else{
            DB.addNewKidFromMain(k);
        }
        finish();
    }

    void dec() {
        ageChar = MainActivity.getAgeChar(this);
        findViewById(R.id.bAdd_NewKid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
        name = (EditText) findViewById(R.id.etNme_NewKid);
        mobile = (EditText) findViewById(R.id.etMobile_NewKid);
        id = (EditText) findViewById(R.id.etId_NewKid);
        if (!FROM_ACTIONS_ACTIVITY) {
            RadioGroup rg = (RadioGroup) findViewById(R.id.rgAgeGroup_NewKid);
            switch (ageChar) {
                case 'O':
                    rg.check(R.id.rbOld_NewKid);
                    break;
                case 'M':
                    rg.check(R.id.rbMiddle_NewKid);
                    break;
                case 'L':
                    rg.check(R.id.rbLittle_NewKid);
                    break;
            }
            rg.setVisibility(View.VISIBLE);
            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.rbOld_NewKid:
                            ageChar = 'O';
                            break;
                        case R.id.rbMiddle_NewKid:
                            ageChar = 'M';
                            break;
                        case R.id.rbLittle_NewKid:
                            ageChar = 'L';
                            break;
                    }
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_kid, menu);
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
