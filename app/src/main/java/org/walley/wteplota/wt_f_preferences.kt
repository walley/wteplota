package org.walley.wteplota

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class wt_f_preferences : PreferenceFragmentCompat() {
  private val TAG = "WT_FP"

  override fun onCreatePreferences(
    savedInstanceState: Bundle?,
    rootKey: String?,
                                  ) {
    setPreferencesFromResource(R.xml.preferencesx, rootKey)
  }

/*  public static void showListPreference(PreferenceFragmentCompat fragment, String preferenceKey,
  int valuesR, int entriesR, int summaryR) {
    ListPreference listPref = (ListPreference) fragment.findPreference(preferenceKey);
    if (listPref != null) {
      listPref.setSummary(getSummaryForListPreference(fragment.getActivity(),
        listPref.getValue(), valuesR, entriesR, summaryR));
    }*/

  fun showListPreference(
    fragment: PreferenceFragmentCompat,
    preferenceKey: String,
    valuesR: Int,
    entriesR: Int,
    summaryR: Int
  ) {
    val listPref = fragment.findPreference<ListPreference>(preferenceKey)
    Log.i(TAG, listPref.toString() +  listPref?.entries.toString());
    for (x in listPref?.entries!!) {
      Log.i(TAG, x.toString())
    }
   /* if (listPref != null) {
      listPref.summary = getSummaryForListPreference(
        fragment.requireActivity(),
        listPref.value,
        valuesR,
        entriesR,
        summaryR
      )
      Log.i(TAG, listPref.summary as String);
    }*/
  }

  private fun getSummaryForListPreference(
    context: Context,
    value: String,
    valuesR: Int,
    entriesR: Int,
    summaryR: Int
  ): String {
    val values = context.resources.getStringArray(valuesR)
    val entries = context.resources.getStringArray(entriesR)
    val index = values.indexOf(value)
    return if (index >= 0) {
      context.getString(summaryR, entries[index])
    } else {
      ""
    }
  }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
    Log.i(TAG,"click $preference")
    Log.i(TAG, "click2 " + preference.key)

      showListPreference(this,"api_selection",1,1,1)
    return super.onPreferenceTreeClick(preference)
  }

  override fun onDisplayPreferenceDialog(preference: Preference) {
    Log.i(TAG, "dialog $preference")
    Log.i(TAG, "dialog2 "+preference.key);

    super.onDisplayPreferenceDialog(preference)
  }
}
