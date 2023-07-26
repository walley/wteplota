package org.walley.wteplota

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class wt_webview : AppCompatActivity() {
  private lateinit var wv: WebView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_webview);
    wv = findViewById(R.id.webview)
    wv.settings.setJavaScriptEnabled(true)

    wv.webViewClient = object : WebViewClient() {
      override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url != null) {
          view?.loadUrl(url)
        }
        return true
      }
    }

    var bundle: Bundle? = intent.extras
    var message = bundle!!.getString("value")
    Log.d("WC", "wt_webview onCreate(): " + message);

//    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    //var client_id = getConfigString("client_id")

    var client_id = "Jjd2YSV1KK9zktD9gaDbHInooLN8QsdvwwKilvwKf4DNcRpc4HeWrsbYg1al8hw7"

    var uri_redirect: String
    uri_redirect = "https://cloud.grezl.eu/index.php/apps/oauth2/authorize?";
    uri_redirect += "response_type=code&";
    uri_redirect += "client_id=$client_id&";
    uri_redirect += "state=yo&";
    uri_redirect += "redirect_uri=https://wiot.cz/wiot/v1/oknextcloud";

    wv.loadUrl(uri_redirect)

  }
}
