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
import org.greenrobot.eventbus.EventBus


class wt_viewmodelsms(app: Application) : AndroidViewModel(app) {
  val TAG = "WT-VMSMS"
  var url: String? = null
  var prefs: SharedPreferences? = null
  private val sms_data: MutableLiveData<List<String>>? = null
  var sms_list: MutableList<String> = mutableListOf()

  init {
    init_class()
  }

  private fun init_class() {
    prefs = PreferenceManager.getDefaultSharedPreferences(getApplication())
    url = prefs?.getString("server_url", "https://localhost")
    url += "sms.php"
    get_data()
    Log.d(TAG, "wt_viewmodelsms() url:$url")
  }

  private fun get_data() {
    Log.d(TAG, "get_data():adding data")

    sms_list.add("777623017:bazar");
    sms_list.add("800123456:free");

    retrieve_sms_data()

    sms_list.forEach {
      val len = it.length
      Log.i(TAG, it)
    }
  }

  fun get_sms_data(): MutableLiveData<List<String>>? {
    get_data()
    retrieve_sms_data()
    sms_data?.value = sms_list
    return sms_data
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

        val keys: Set<String> = result.keySet()
        for (element in keys) {
          Log.i(TAG, "  number: $element")
          val j: JsonArray = result.get(element).asJsonArray
          for (sms_text in j) {
            Log.i(TAG, "    message: $sms_text")
            sms_list.add(element + sms_text)
          }
        }
        EventBus.getDefault().post(MessageEvent("sms_data_done"))
      })

  }
}

