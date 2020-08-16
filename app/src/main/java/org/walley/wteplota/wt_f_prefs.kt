package org.walley.wteplota

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class wt_f_prefs : Fragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val root = inflater.inflate(R.layout.fragment_prefs, container, false)
    return root
  }
}

/*
class wt_f_prefs : Fragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val root = inflater.inflate(R.layout.fragment_prefs, container, false)

//    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, SettingsFragment()).commit()
    return root
  }

  private inner class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
      setPreferencesFromResource(org.walley.wteplota.R.xml.preferences, rootKey)
    }
  }
}
*/