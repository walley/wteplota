package org.walley.wteplota

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity

open class wt_base : AppCompatActivity() {

  /*
   * TUYA:
   * AppKey: phkpm85jvrrhndp88yxf
   * AppSecret: drkxrw9skw77extwsf58uu7p7qd93u4k Copy
   *
   */
  fun is_dark_theme(): Boolean {
    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
      Configuration.UI_MODE_NIGHT_YES -> return true
      Configuration.UI_MODE_NIGHT_NO  -> return false
    }
    return true
  }

  open fun add_trailing_slash(input: String): String {
    return if (input.endsWith("/")) input else "$input/"
  }
}