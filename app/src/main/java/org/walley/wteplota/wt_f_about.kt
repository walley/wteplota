package org.walley.wteplota

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.Navigation

class wt_f_about : wt_f_base() {

  lateinit var tv_about: TextView
  lateinit var compost: ComposeView
  lateinit var navController: NavController

  var version: String = ""
  var packageName: String = ""
  var versionCode: Long = 0

  var ver_string = ""

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    val root = inflater.inflate(R.layout.fragment_about, container, false)
    tv_about = root.findViewById<TextView>(R.id.tv_about)
    compost = root.findViewById<ComposeView>(R.id.compose_view)
    navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

    return root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    val manager = requireContext().packageManager
    val info = manager.getPackageInfo(
      requireContext().packageName, 0
    )
    version = info.versionName
    packageName = info.packageName
    versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      info.longVersionCode
    } else {
      TODO("VERSION.SDK_INT < P")
    }

    ver_string =
      "WTeplota $version\n" + "versioncode:$versionCode\n" + "package:$packageName\n" + "build:${BuildConfig.BUILD_TYPE}\n" + "app:${get_app_name()}"

    tv_about.setText("ABOUT")

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

  val red200 = Color(0XFFEF9A9A)
  val red500 = Color(0XFFF44336)
  val red700 = Color(0XFFD32F2F)
  val pink200 = Color(0XFFF27584)
  val pink500 = Color(0XFFEF5366)

  val pink700 = Color(0XFFD74A5B)
  val purple200 = Color(0XFFCE93D8)
  val purple500 = Color(0XFF9C27B0)
  val purple700 = Color(0XFF7B1FA2)

  val indigo200 = Color(0XFF9FA8DA)
  val indigo500 = Color(0XFF3F51B5)
  val indigo700 = Color(0XFF303f9f)
  val blue200 = Color(0XFF90CAF9)
  val blue500 = Color(0xFF2195F2)
  val blue700 = Color(0xFF1976D2)

  val teal200 = Color(0XFF80DEEA)

  val green200 = Color(0XFFA5D6A7)
  val green500 = Color(0XFF4CAF50)
  val green700 = Color(0XFF388E3C)

  val yellow200 = Color(0XFFFFF59D)
  val yellow500 = Color(0XFFFFEB3B)
  val yellow700 = Color(0XFFFBC02D)

  val orange200 = Color(0XFFFFCC80)
  val orange500 = Color(0XFFFF9800)
  val orange700 = Color(0XFFF57C00)

  val brown200 = Color(0XFFBCAAA4)
  val brown500 = Color(0XFF795548)
  val brown700 = Color(0XFF5D4037)
  val grey200 = Color(0XFFEEEEEE)
  val grey500 = Color(0XFF9E9E9E)
  val grey700 = Color(0XFF616161)

  private val DarkColorScheme = darkColorScheme(
    primary = red500,
    secondary = red200,
    tertiary = androidx.compose.ui.graphics.Color.White,
    background = Black,
  )

  private val DarkOrangeColorPalette = darkColorScheme(
    primary = orange200,
    secondary = orange700,
    tertiary = teal200,
    background = Black,
    surface = Black,
    onPrimary = Black,
    onTertiary = White,
    onBackground = White,
    onSurface = White,
    error = Red,
  )

  val wt_t_style = TextStyle(
    color = DarkOrangeColorPalette.primary
  )

  @Composable
  fun MyTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
  ) {
    MaterialTheme(
      colorScheme = DarkOrangeColorPalette, content = content
    )
  }

  @Composable
  @Preview
  fun SimpleScreen() {
    MyTheme() {
      Column(
        modifier = Modifier
          .padding(30.dp)
          .fillMaxWidth()
          .wrapContentSize(Alignment.Center)
      ) {
        Text(
          text = ver_string,
          textAlign = TextAlign.Center,
          style = wt_t_style,
          modifier = Modifier.fillMaxWidth(),
        )
        Icon(
          painter = painterResource(id = R.drawable.ic_dragon), contentDescription = null
        )
        Image(
          painter = painterResource(id = R.drawable.ic_dragon_24), contentDescription = "buzumbura"
        )
        Box(
          modifier = Modifier
            .border(width = 4.dp, color = Gray, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth(),
        ) {
          Text(
            modifier = Modifier.fillMaxWidth(),
            text = "TEST",
            textAlign = TextAlign.Center,
            style = wt_t_style,
          )
        }/* AsyncImage(
          model = "http://placekitten.com/200/300",
          contentDescription = "buzumbura"
        )*/
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {

          navController.navigate(R.id.nav_start)

        }, Modifier.fillMaxWidth()) {
          Text(text = stringResource(R.string.back))
        }
      }
    }
  }
}
