package org.walley.wteplota

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_prefs.*

class wt_f_login : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val root = inflater.inflate(R.layout.fragment_prefs, container, false)

    button?.setOnClickListener {
      Toast.makeText(activity, "You clicked me.", Toast.LENGTH_SHORT).show()
    }

    return root
  }
}
