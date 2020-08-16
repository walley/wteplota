package org.walley.wteplota

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class wt_f_preferences : PreferenceFragmentCompat() {
  private val TAG = "WT_FP"
  override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
    setPreferencesFromResource(R.xml.preferences, rootKey)
  }

  //https://developer.android.com/guide/topics/ui/settings.html
}