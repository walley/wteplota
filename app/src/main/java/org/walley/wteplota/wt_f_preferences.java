package org.walley.wteplota;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class wt_f_preferences extends PreferenceFragmentCompat
{

  static final String TAG = "WT_FP";

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
  {
    setPreferencesFromResource(R.xml.preferences, rootKey);
  }
//https://developer.android.com/guide/topics/ui/settings.html
}

