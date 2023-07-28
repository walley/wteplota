package org.walley.wteplota

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class wt_webview : AppCompatActivity() {
  private lateinit var wv: WebView
  private val TAG: String = "WT_WV";

  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_webview);
    wv = findViewById(R.id.webview)
    wv.settings.javaScriptEnabled = true
    wv.settings.domStorageEnabled = true;

    wv.webViewClient = object : WebViewClient() {
      override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url != null) {
          view?.loadUrl(url)
        }
        return true
      }

      override fun onPageFinished(view: WebView, url: String) {
        Log.d(TAG, "onPageFinished(): done ");
        val cookies: String = CookieManager.getInstance().getCookie(url)
        Log.d(TAG, "onPageFinished(): " + cookies)
      }
    }

    var bundle: Bundle? = intent.extras
    var message = bundle!!.getString("message")
    Log.d(TAG, "wt_webview onCreate(): bundle " + message);

    //var client_id = getConfigString("client_id")
    //var client_id = "Jjd2YSV1KK9zktD9gaDbHInooLN8QsdvwwKilvwKf4DNcRpc4HeWrsbYg1al8hw7"
    val client_id = "q19BJMSducGS6Msosga9reb5r3BaWL1DOdaT116hwtEYTAMYolpsOsSFyDRvzBZn"

    var uri_redirect: String
    uri_redirect = "https://cloud.grezl.eu/index.php/apps/oauth2/authorize?";
    uri_redirect += "response_type=code&";
    uri_redirect += "client_id=$client_id&";
    uri_redirect += "state=yo&";
    uri_redirect += "redirect_uri=https://wiot.cz/wiot/v1/oknextcloud";

    wv.loadUrl(uri_redirect)

  }
}
