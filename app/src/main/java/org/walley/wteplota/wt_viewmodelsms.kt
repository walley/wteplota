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
  private val sms_data: MutableLiveData<List<String>>? = null
  val sms_list: MutableList<String>? = null

  init {
    prefs = PreferenceManager.getDefaultSharedPreferences(getApplication())
    url = prefs?.getString("server_url", "https://localhost")
    Log.d(TAG, "before")
    Log.d(TAG, "wt_viewmodelsms() url:$url")
    Log.d(TAG, "before")

    get_data()
    Log.d(TAG, "after")
  }

  private fun get_data() {
    Log.d(TAG, "adding data")

    sms_list?.add("777623017:nazdar");
    sms_list?.add("777623017:bazar");
    sms_list?.add("800123456:free");

    sms_list?.forEach {
      val len = it.length
      Log.i(TAG, it)
    }

    sms_data?.value = sms_list
  }

  fun get_sms_data(): MutableLiveData<List<String>>? {
//    get_temp()
    get_data()
    return sms_data
  }

  private fun get_temp() {

    val context = getApplication<Application>().applicationContext

    Log.i(TAG, "sms_send: $url")

    Ion.with(context).load(url).asJsonObject()
      .setCallback(FutureCallback<JsonObject?> { exception, result ->
        var item_json: String
        if (result == null) {
          Log.e(wt_viewmodel.TAG, "get_temp(): json error")
          return@FutureCallback
        }

        var temps = ""

        val keys: Set<String> = result.keySet()
        for (element in keys) {
          Log.i(TAG, "key stuff $element")
          val j: JsonArray = result.get(element).asJsonArray
          println(element)
          for (sms_text in j) {
            Log.i(TAG, "val stuff $sms_text")
          }
        }
//          EventBus.getDefault().post(MessageEvent("data_done"))

        Log.d(TAG, "parsed stuff $temps")
      })

  }
}

