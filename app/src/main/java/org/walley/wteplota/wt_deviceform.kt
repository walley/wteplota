package org.walley.wteplota

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat.LayoutParams
import androidx.appcompat.widget.LinearLayoutCompat.generateViewId
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion
import java.util.Hashtable

class wt_deviceform : AppCompatActivity() {
  private val TAG = "WT-DF"

  //  private val context = applicationContext
  val data_hash = Hashtable<String, JsonObject>()
  private lateinit var layout: LinearLayout
  private lateinit var layout_row: LinearLayout
  private lateinit var label_row: TextView
  private lateinit var device_title: TextView
  lateinit var item_values: Hashtable<String, Int>
  lateinit var device_name: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    item_values = Hashtable(1)

    Log.d(TAG, "onCreate()")

    val extras = intent.extras
    val value: String?
    if (extras != null) {
      device_name = extras.getString("device_name")!!
    }

    Log.d(TAG, "onCreate(): device name: $device_name")


    setContentView(R.layout.activity_deviceform);

    device_title = findViewById<TextView>(R.id.tv_device_title)
    device_title.setText(device_name)

    layout = findViewById<LinearLayout>(R.id.form_layout)
    val btnTag = Button(this)
    btnTag.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    btnTag.text = "Button"
    btnTag.setOnClickListener() {
      Toast.makeText(this, "buzumbura", Toast.LENGTH_LONG).show()
    }

    layout.addView(btnTag)

    create_form()

//    actionBar?.setDisplayHomeAsUpEnabled(true);
  }

  override fun onCreateView(
    parent: View?, name: String, context: Context, attrs: AttributeSet
                           ): View? {
    Log.d(TAG, "onCreateView() $name")

    return super.onCreateView(parent, name, context, attrs)
  }

  private fun create_form() {

    val url: String = "https://wiot.cz/wiot/v1/form/$device_name?output=json"

    Ion.with(applicationContext).load(url).asJsonArray()
      .setCallback(FutureCallback<JsonArray?> { exception, result ->
        if (result == null) {
          Log.e(TAG, "create_form(): error")
          return@FutureCallback
        }
        Log.d(TAG, "create_form(): " + result.toString())
        parse_result(result)
//add submit
      })
  }

  private fun parse_result(result: JsonArray) {
    val it: Iterator<JsonElement> = result.iterator()
    var run = true;

    data_hash.clear()
    while (it.hasNext()) {
      var temps = ""
      val element = it.next()
      Log.d(TAG, "element " + element.asJsonObject.toString())
      val jo = element.asJsonObject

      for ((key, value) in jo.asMap()) {
        temps += key + " = " + value + "\n"
      }

      Log.d(TAG, "parsed stuff $temps")
      if (run) {
        run = !run
        //build view
        Log.d(TAG, "building view")
        for ((key, value) in jo.asMap()) {

          layout_row = LinearLayout(this)
          layout_row.layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
          layout_row.setOrientation(LinearLayout.HORIZONTAL);

          label_row = TextView(this)
          label_row.text = "$key"
          label_row.id = generateViewId()
          label_row.setTextColor(Color.RED);

          //label_row.setTextSize(TypedValue.COMPLEX_UNIT_PX, 24F)
          label_row.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32F)

          layout_row.addView(label_row)

          when (value.asString) {
            "int" -> {
              val item_row = EditText(this)
              item_row.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32F)
              item_row.id = generateViewId()
              item_values.put(key, item_row.id)
              item_row.setInputType(InputType.TYPE_CLASS_NUMBER)
              //item_row.layoutParams = LayoutParams(200,100)
              item_row.layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
              layout_row.addView(item_row)
            }

            "bool" -> {
              val item_row = ToggleButton(this)
              item_row.id = generateViewId()
              item_values.put(key, item_row.id)
              layout_row.addView(item_row)
            }
          }

          layout.addView(layout_row);
          Log.d(TAG, "added $key $value $label_row.id ")

        }
      } else {
        //set values
        Log.d(TAG, "setting values")
        for ((key, value) in jo.asMap()) {
          var id: Int = 0;
          id = item_values.get(key)!!
          var v: View = findViewById(id)
          if (v is EditText) {
            Log.d(TAG, "$key is edittext")
            v.setText(value.toString())
          } else if (v is ToggleButton) {
            Log.d(TAG, "$key is togglebutton")
          } else {
            Log.d(TAG, "$key is unknown")
          }

        }
      }
    }

    for ((key, value) in item_values) {
      var v: View = findViewById(value)
      if (v is EditText) {
        Log.d(TAG, "$key is edittext")
      } else if (v is ToggleButton) {
        Log.d(TAG, "$key is togglebutton")
      } else {
        Log.d(TAG, "$key is unknown")
      }
    }

    // EventBus.getDefault().post(MessageEvent("data_done"))
  }

}


