package org.walley.wteplota

import android.content.res.Configuration
import androidx.fragment.app.Fragment

open class wt_f_base : Fragment() {
  public fun is_dark_theme(): Boolean {
    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
      Configuration.UI_MODE_NIGHT_YES -> return true
      Configuration.UI_MODE_NIGHT_NO  -> return false
    }
    return true
  }
}