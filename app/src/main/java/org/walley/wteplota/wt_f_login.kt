package org.walley.wteplota

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class wt_f_login : Fragment() {
  val TAG = "WT-L"

  private lateinit var button_login: Button
  private lateinit var button_nextcloud: Button

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    button_login.setOnClickListener {
      Log.d(TAG, "klyk")
      Toast.makeText(activity, "You clicked me.", Toast.LENGTH_SHORT).show()
    }
    button_nextcloud.setOnClickListener {
      Log.d(TAG, "klyk")
      Toast.makeText(activity, "nextcloud.", Toast.LENGTH_SHORT).show()
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
    button_nextcloud = root.findViewById(R.id.button_nextcloud)
    return root
  }

  /*
    my $uri_redirect = "https://cloud.grezl.eu/index.php/apps/oauth2/authorize?";
  $uri_redirect .= "response_type=code&";
  $uri_redirect .= "client_id=$client_id&";
  $uri_redirect .= "state=yo&";
  $uri_redirect .= "redirect_uri=https://wiot.cz/wiot/v1/oknextcloud";



   */
}
