package org.walley.wteplota

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion


class wt_viewmodelsms(app: Application) : AndroidViewModel(app) {
  val TAG = "WT-VMSMS"
  var url: String? = null
  var prefs: SharedPreferences? = null
  val sms_data: MutableLiveData<MutableList<String>> = MutableLiveData()
  var sms_list: MutableList<String> = mutableListOf("test", "stuff")

  init {
    init_class()
  }

  private fun init_class() {
    prefs = PreferenceManager.getDefaultSharedPreferences(getApplication())
    url = prefs?.getString("server_url", "https://localhost")
    url += "sms.php"

    retrieve_sms_data()

    Log.d(TAG, "wt_viewmodelsms() url:$url")
  }

  private fun dump_data() {
    Log.d(TAG, "dump_data():dumping data")

    sms_list.forEach {
      val len = it.length
      Log.i(TAG, "* $it")
    }
  }

  private fun update_live_data() {
    Log.d(TAG, "update_live_data(): updating live data")
    sms_data.postValue(sms_list)
  }

  private fun retrieve_sms_data() {

    val context = getApplication<Application>().applicationContext

    Ion.with(context).load(url).asJsonObject()
      .setCallback(FutureCallback<JsonObject?> { exception, result ->
        var item_json: String
        if (result == null) {
          Log.e(wt_viewmodel.TAG, "get_temp(): json error")
          return@FutureCallback
        }
        sms_list.clear()
        val keys: Set<String> = result.keySet()
        for (element in keys) {
          Log.i(TAG, "  number: $element")
          val j: JsonArray = result.get(element).asJsonArray
          for (sms_text in j) {
            Log.i(TAG, "    message: $sms_text")
            sms_list.add(element + sms_text)
          }
        }
        dump_data()
        update_live_data()
      })

  }
}

