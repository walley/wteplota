package org.walley.wteplota

import android.content.res.Configuration
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager

open class wt_f_base : Fragment() {

  open lateinit var base_api:String
  open lateinit var base_url:String

  public fun is_dark_theme(): Boolean {
    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
      Configuration.UI_MODE_NIGHT_YES -> return true
      Configuration.UI_MODE_NIGHT_NO  -> return false
    }
    return true
  }

  fun add_trailing_slash(input: String): String {
    return if (input.endsWith("/")) input else "$input/"
  }

  fun stuff() {
    val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
    base_url = prefs.getString("server_url", "https://localhost/")!!
    if (!base_url.endsWith("/")) {
      base_url += "/"
    }
    base_api = prefs.getString("api_type", "default_api")!!

    if (base_api == "marek") {
      base_url += "android.php"
    }

    if (base_api == "walley") {
//      url += "wiot/v1/devices?output=json";
      base_url += "wiot/v1/house?output=json"
    }

    android.util.Log.d(wt_viewmodel.TAG, "stuff() url:" + base_url)
    android.util.Log.d(wt_viewmodel.TAG, "stuff() api:" + base_api)
  }
}