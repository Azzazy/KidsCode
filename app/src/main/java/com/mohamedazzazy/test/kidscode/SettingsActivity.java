package com.mohamedazzazy.test.kidscode;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mohamedazzazy.test.kidscode.java.DB;


/**
 * Created by Mohamed Azzazy on 04/07/2015
 * within project KidsCode.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefrences);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        PreferenceManager.getDefaultSharedPreferences(DB.mainActivity)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        PreferenceManager.getDefaultSharedPreferences(DB.mainActivity)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("age_group")) {
            DB.AGE_CHAR = PreferenceManager.getDefaultSharedPreferences(DB.mainActivity).getString("age_group", "A").charAt(0);
            DB.fullList = null;
            DB.CALL_COUNTER=0;
            DB.updateCallCounter();
            Log.e("TEST", "onSharedPreferenceChanged : DB.AGE_CHAR = " + DB.AGE_CHAR);      //test
        }
    }
}