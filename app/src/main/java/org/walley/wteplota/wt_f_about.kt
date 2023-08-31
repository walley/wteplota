package org.walley.wteplota

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView

class wt_f_about : wt_f_base() {

  lateinit var tv_about: TextView
  lateinit var compost: ComposeView

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    val root = inflater.inflate(R.layout.fragment_about, container, false)
    tv_about = root.findViewById<TextView>(R.id.tv_about)
    compost = root.findViewById<ComposeView>(R.id.compose_view)
    return root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    val manager = requireContext().packageManager
    val info = manager.getPackageInfo(
      requireContext().packageName, 0
    )
    val version = info.versionName
    val packageName = info.packageName
    val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      info.getLongVersionCode()
    } else {
      TODO("VERSION.SDK_INT < P")
    }

    tv_about.setText("nazdar bazar $version $versionCode $packageName \n ${BuildConfig.BUILD_TYPE} ${get_app_name()}")
    compost.setContent { SimpleScreen() }
  }

  fun get_app_name(): String? {
    val applicationInfo = requireContext().applicationInfo
    val stringId = applicationInfo.labelRes
    if (stringId == 0) {
      return applicationInfo.nonLocalizedLabel.toString()
    } else {
      return requireContext().getString(stringId)
    }
  }

  @Composable
  fun SimpleScreen() {
    Column(Modifier.fillMaxSize()) {
      Text(
        text = "x", style = MaterialTheme.typography.headlineMedium
        //, color =
      )
      Text(
        text = "xx", style = MaterialTheme.typography.headlineSmall
      )
      Text(
        text = "xxx", style = MaterialTheme.typography.bodyMedium
      )
      Spacer(modifier = Modifier.weight(1f))
      Button(onClick = { /* Handle click */ }, Modifier.fillMaxWidth()) {
        Text(text = "cudl")
      }
    }
  }
}
