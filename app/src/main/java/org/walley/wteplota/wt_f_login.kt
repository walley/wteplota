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

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    button_login.setOnClickListener {
      Log.d(TAG, "klyk")
      Toast.makeText(activity, "You clicked me.", Toast.LENGTH_SHORT).show()
    }

  }


  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Log.d(TAG, "onCreateView(): start")
    val root = inflater.inflate(R.layout.fragment_login, container, false)
    button_login = root.findViewById(R.id.button_login)
    return root
  }
}
