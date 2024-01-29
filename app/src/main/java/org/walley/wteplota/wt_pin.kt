package org.walley.wteplota

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class wt_pin : AppCompatActivity() {

  val pink700 = Color(0XFFD74A5B);

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
//      Surface(color = MaterialTheme.colors.background) {
      Surface(color = pink700) {
        var pin by remember { mutableStateOf("") }
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
          BackHandler {
            // Handle back button press
          }
          Text(
            text = "Enter your PIN",
//            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
          )
          Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
          ) {
            for (i in 1 .. 9) {
              Button(
                onClick = { pin += i },
                modifier = Modifier
                  .padding(8.dp)
                  .size(64.dp)
              ) {
                Text(text = "$i")
              }
            }
            Button(
              onClick = { pin += "0" },
              modifier = Modifier
                .padding(8.dp)
                .size(64.dp)
            ) {
              Text(text = "0")
            }
          }
          Spacer(modifier = Modifier.height(16.dp))
          Button(
            onClick = {
              // TODO: Implement your logic to verify the entered PIN.
            }, modifier = Modifier.fillMaxWidth()
          ) {
            Text(text = "Submit")
          }
        }
      }
    }
  }
}
