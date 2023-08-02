package org.walley.wteplota

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class wt_f_about : wt_f_base() {

  lateinit var tv_about: TextView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
                           ): View? {
    val root = inflater.inflate(R.layout.fragment_about, container, false)
    tv_about = root.findViewById<TextView>(R.id.tv_about)
    return root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    tv_about.setText("nazdar bazar")
  }
}
