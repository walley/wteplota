package org.walley.wteplota;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class wt_preferences extends PreferenceActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
