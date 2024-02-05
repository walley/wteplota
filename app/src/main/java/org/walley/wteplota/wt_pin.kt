package org.walley.wteplota

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

class wt_pin : AppCompatActivity() {

  class Size {
    @Composable
    fun height(): Int {
      val configuration = LocalConfiguration.current
      return configuration.screenHeightDp
    }

    @Composable
    fun width(): Int {
      val configuration = LocalConfiguration.current
      return configuration.screenWidthDp
    }
  }

  var pin: String = ""
  val context: Context = this

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      Screen()
    }

  }

  @Composable
  fun MyCanvas() {
    Canvas(modifier = Modifier.fillMaxSize()) {
      drawRect(color = Color.Blue, size = size)
    }
  }

  @Composable
  fun pin_view(pin: String) {
    val size = Size()
    val screenHeight = size.height()

    Row(
      modifier = Modifier
        .height(40.dp)
        .fillMaxWidth()
        .background(
          Color.Red, shape = RoundedCornerShape(4.dp)
        )
        .border(2.dp, Color.Magenta),
    ) {

      val circleRadius = 10.dp
      val circleSpacing = 10.dp
      val totalWidth = (circleRadius * 2 * 4) + (circleSpacing * 3)
      val startX = (size.width().dp - totalWidth) / 2
//      val startX = (200.dp - totalWidth) / 2
      repeat(4) { index ->
        val isFilled = index < pin.length
        val color = if (isFilled) Color.Green else Color.White
        var cspacing = 0.dp
        if (index != 1) {
          cspacing = circleSpacing
        }

        val x = startX + (index * (circleRadius * 2 + circleRadius)) - 6.dp

        Canvas(
          modifier = Modifier.offset(x, circleRadius)
        ) {
          drawCircle(color = color, radius = circleRadius.toPx())
        }
      }
    }
  }

  @Composable
  fun button_row(from: Int): String {

    Row(
      Modifier.fillMaxWidth(), Arrangement.Center
    ) {
      for (i in from .. from + 2) {
        Button(
          onClick = { pin += "$i" },
          modifier = Modifier
            .padding(8.dp)
            .size(64.dp)
        ) {
          Text(text = "$i")
        }
      }
    }
    return pin
  }

  @Preview
  @Composable
  fun Screen() {
    Surface(color = DarkOrangeColorPalette.background) {

      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {
        BackHandler {
          // Handle back button press
        }
        Text(
          color = Color.Red,
          text = "Enter your PIN",
          style = typography.headlineLarge,
          modifier = Modifier.padding(bottom = 16.dp)
        )
        pin_view("123")

        button_row(1);
        button_row(4);
        button_row(7);
        Row(
          Modifier.fillMaxWidth(), Arrangement.Center
        ) {
          Button(
            onClick = { pin = "" }, modifier = Modifier
              .padding(8.dp)
              .size(64.dp)
          ) {
            Text(text = "X")
          }
          Button(
            onClick = { pin += "0" },
            modifier = Modifier
              .padding(8.dp)
              .size(64.dp)
          ) {
            Text(text = "0")
          }
          Button(
            onClick = {
              pin = pin.dropLast(1)
              //pin.substring(1, pin.length - 1)
            },
            modifier = Modifier
              .padding(8.dp)
              .size(64.dp)
          ) {
            Text(text = "<-")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
          onClick = {
            Toast.makeText(context, pin, Toast.LENGTH_SHORT)
              .show()
          }, modifier = Modifier.fillMaxWidth()
        ) {
          Text(text = "Submit")
        }
      }
    }
  }

}
