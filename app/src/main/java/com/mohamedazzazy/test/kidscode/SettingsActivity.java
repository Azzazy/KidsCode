package com.mohamedazzazy.test.kidscode;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;


/**
 * Created by Mohamed Azzazy on 04/07/2015
 * within project KidsCode.
 */
public class SettingsActivity extends Activity {
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
}