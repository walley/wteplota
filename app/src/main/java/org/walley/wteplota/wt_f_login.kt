package org.walley.wteplota

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.gson.JsonObject
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.async.http.Headers
import com.koushikdutta.ion.Ion
import java.net.URI
import java.util.Hashtable

class wt_f_login : Fragment() {
  val TAG = "WT-F-L"

  //  private Hashtable<String, JsonObject> data_hash;
  val data_hash = Hashtable<String, JsonObject>()

  private lateinit var button_login: Button
  private lateinit var button2_login: Button
  private lateinit var button_nextcloud: Button
  lateinit var prefs: SharedPreferences
  lateinit var api: String
  lateinit var login: String
  lateinit var tv_username: TextView

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    var url: String

    super.onViewCreated(view, savedInstanceState)

    stuff()

    button_login.setOnClickListener {
      Log.d(TAG, "klyk 1")
      Toast.makeText(activity, "You clicked one.", Toast.LENGTH_SHORT).show()
      val x = get_username()
      Log.d(TAG, "klik1: $x")

      create_form();
    }

    button2_login.setOnClickListener {
      Log.d(TAG, "klyk 2")
      Toast.makeText(activity, "dvojka two.", Toast.LENGTH_SHORT).show()

      url = "https://wiot.cz/wiot/v1/username"

      val cookies: String = android.webkit.CookieManager.getInstance().getCookie(url)
      Log.d(TAG, "Klyk2: webkit cookies:" + cookies)

      val middleware = Ion.getDefault(context).cookieMiddleware
      val ion = Ion.getDefault(context)
      ion.cookieMiddleware.clear()

      val headers = Headers()
      headers.set("Set-Cookie", "oauth2sessid=1N3XvssvvConH-1690877509")
      val uri = URI.create(url)
      middleware.put(uri, headers)
      val data = Ion.with(context).load(url).setLogging(TAG, Log.INFO).asString().get()

      Log.d(TAG, "Klyk2: data: $data")

      Ion.with(context)
        .load(url)
        .asString()
        .setCallback(FutureCallback<String?> { exception, result ->
          if (result == null) {
            Log.e(TAG, "klyk2: error")
            return@FutureCallback
          }
          Log.d(TAG, "Klyk2: " + result.toString())
        })

      val i = Intent(activity, wt_pin::class.java)
      startActivity(i)

    }

    button_nextcloud.setOnClickListener {
      Log.d(TAG, "klyk nextcloud")
      Toast.makeText(activity, "nextcloud.", Toast.LENGTH_SHORT).show()

      val i = Intent(activity, wt_webview::class.java)
      i.putExtra("message", "x");

      startActivity(i)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                           ): View? {
    Log.d(TAG, "onCreateView(): start")
    val root = inflater.inflate(R.layout.fragment_login, container, false)
    button_login = root.findViewById(R.id.button_login)
    button2_login = root.findViewById(R.id.button2_login)
    button_nextcloud = root.findViewById(R.id.button_nextcloud)
    tv_username = root.findViewById(R.id.tv_username)
    return root
  }

  fun get_username(): String {
    val url: String
    lateinit var cookies: String

    url = "https://wiot.cz/wiot/v1/username"
    Log.d(TAG, "get_username(): start")

    cookies = android.webkit.CookieManager.getInstance()
      ?.getCookie(url)
      .toString()

    if (cookies == "null") {
      Log.d(TAG, "get_username(): webkit no cookies, login .....")
    } else {
      Log.d(TAG, "get_username(): webkit cookies:" + cookies)
    }

    val middleware = Ion.getDefault(context).cookieMiddleware
    val ion = Ion.getDefault(context)
    ion.cookieMiddleware.clear()

    val headers = Headers()
    headers.set("Set-Cookie", cookies)
    val uri = URI.create(url)
    middleware.put(uri, headers)
    val data = Ion.with(context).load(url).setLogging(TAG, Log.INFO).asString().get()
    Log.d(TAG, "get_username(): returned data: $data")

    return data
  }

  fun stuff() {
    prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
    api = prefs.getString("api_type", "default_api").toString()
    Log.d(TAG, "stuff() api:" + api)

    when (api) {
      "marek" -> {
        login = prefs.getString("login", "default_api").toString()
        tv_username.setText(login)
      }

      "walley" -> {
        tv_username.setText(get_username())
      }

      else -> tv_username.text = getString(R.string.not_logged_in)
    }
  }

  private fun create_form() {
    val i = Intent(activity, wt_deviceform::class.java)
    i.putExtra("device_name", "thermostat0");
    startActivity(i)
  }
}
