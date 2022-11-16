package org.walley.wteplota

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager


class wt_viewmodelsms(app: Application) : AndroidViewModel(app) {
  val TAG = "WT-VMSMS"
  var url: String? = null
  var prefs: SharedPreferences? = null
  private val sms_data: MutableLiveData<List<String>>? = null

  fun wt_viewmodelsms() {
//    server_data = MutableLiveData<Hashtable<String, JsonObject>>()
//    data_hash = Hashtable<String, JsonObject>()
    prefs = PreferenceManager.getDefaultSharedPreferences(getApplication())
    url = prefs?.getString("server_url", "https://localhost")
    Log.d(wt_viewmodel.TAG, "wt_viewmodelsms() url:$url")
  }
}
