package org.walley.wteplota

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion


class wt_f_login : Fragment() {
  val TAG = "WT-F-L"

  private lateinit var button_login: Button
  private lateinit var button2_login: Button
  private lateinit var button_nextcloud: Button

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    var url: String

    super.onViewCreated(view, savedInstanceState)

    button_login.setOnClickListener {
      Log.d(TAG, "klyk 1")
      Toast.makeText(activity, "You clicked one.", Toast.LENGTH_SHORT).show()
    }

    button2_login.setOnClickListener {
      Log.d(TAG, "klyk 2")
      Toast.makeText(activity, "dvojka two.", Toast.LENGTH_SHORT).show()

      url = "https://wiot.cz/wiot/v1/username"
      val data =
        Ion.with(context).load("https://wiot.cz").setLogging("WT-F-L", Log.VERBOSE).asString().get()

      Ion.with(context).load(url).asString()
        .setCallback(FutureCallback<String?> { exception, result ->
          if (result == null) {
            Log.e(TAG, "klyk2: error")
            return@FutureCallback
          }
          Log.d(TAG, "Klyk2: " + result.toString())
          val cookies: String = CookieManager.getInstance().getCookie(url)
          Log.d(TAG, "Klyk2: " + cookies)

        })

    }

    button_nextcloud.setOnClickListener {
      Log.d(TAG, "klyk nextcloud")
      Toast.makeText(activity, "nextcloud.", Toast.LENGTH_SHORT).show()

      val i = Intent(activity, wt_webview::class.java)
      i.putExtra("message", "x");
//      Log.d("WC",class_name + "onitemclick: view id " + view_id);

      startActivity(i)

    }
  }


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
                           ): View? {
    Log.d(TAG, "onCreateView(): start")
    val root = inflater.inflate(R.layout.fragment_login, container, false)
    button_login = root.findViewById(R.id.button_login)
    button2_login = root.findViewById(R.id.button2_login)
    button_nextcloud = root.findViewById(R.id.button_nextcloud)
    return root
  }
}
