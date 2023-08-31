package org.walley.wteplota

import android.content.Context
import android.content.SharedPreferences
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
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Arrays
import java.util.Hashtable

class wt_deviceform : AppCompatActivity() {
  private val TAG = "WT-DF"

  lateinit var swipeRefreshLayout: SwipeRefreshLayout

  //  private val context = applicationContext
  val data_hash = Hashtable<String, JsonObject>()
  private lateinit var layout: LinearLayout
  private lateinit var layout_row: LinearLayout
  private lateinit var label_row: TextView
  private lateinit var device_title: TextView
  private lateinit var button_submit: Button
  lateinit var item_values: Hashtable<String, Int>
  lateinit var device_name: String
  lateinit var base_url: String
  lateinit var api: String
  lateinit var prefs: SharedPreferences

  fun onRefresh() {}

  override fun onStop() {
    super.onStop()
    Log.d(TAG, "onStop() called")
    EventBus.getDefault().unregister(this)
  }

  override fun onStart() {
    super.onStart()
    Log.d(TAG, "onStart() called")
    EventBus.getDefault().register(this)
    create_form()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    prefs = PreferenceManager.getDefaultSharedPreferences(this)
    api = prefs.getString("api_type", "default_api").toString()
    base_url = prefs.getString("server_url", "localhost").toString()
    Log.d(TAG, "oncreate() api:" + api)
    Log.d(TAG, "oncreate() urli:" + base_url)

    item_values = Hashtable(1)

    Log.d(TAG, "onCreate()")

    val extras = intent.extras
    val value: String?
    if (extras != null) {
      device_name = extras.getString("device_name")!!
    }

    Log.d(TAG, "onCreate(): device name: $device_name")

    setContentView(R.layout.activity_deviceform);

    swipeRefreshLayout = findViewById(R.id.swipe_container)
    swipeRefreshLayout.setOnRefreshListener {
      reload_all()
    }

    device_title = findViewById<TextView>(R.id.tv_device_title)
    device_title.setText(device_name)

    layout = findViewById<LinearLayout>(R.id.form_layout)
  }

  override fun onCreateView(
    parent: View?, name: String, context: Context, attrs: AttributeSet
                           ): View? {
    Log.d(TAG, "onCreateView() $name")
    return super.onCreateView(parent, name, context, attrs)
  }

  private fun create_submit() {

    val url: String = "$base_url/wiot/v1/data/$device_name"
    button_submit = Button(this)
    button_submit.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    button_submit.text = getString(R.string.update_device_parameters)
    button_submit.setOnClickListener() {
      Toast.makeText(this, "buzumbura $url", Toast.LENGTH_LONG).show()

      val params = HashMap<String, List<String>>();

      for ((key, value) in item_values) {
        val v: View = findViewById(value)
        if (v is EditText) {
          params.put(key, Arrays.asList(v.text.toString()));
          Log.d(TAG, "post data: $key," + v.text.toString())
        } else if (v is ToggleButton) {
          Log.d(TAG, "$key is togglebutton")
        } else {
          Log.d(TAG, "$key is unknown")
        }
      }

      Ion.with(this).load(url).setBodyParameters(params).asString()
        .setCallback(FutureCallback<String?> { exception, result ->
          if (result == null) {
            Log.e(TAG, "create_form(): error $exception")
            return@FutureCallback
          }
          Log.d(TAG, "create_form(): " + result)
        })

    }
    layout.addView(button_submit)
  }

  private fun create_form() {

    val url: String = "https://wiot.cz/wiot/v1/form/$device_name?output=json"

    Ion.with(applicationContext).load(url).asJsonArray()
      .setCallback(FutureCallback<JsonArray?> { exception, result ->
        if (result == null) {
          Log.e(TAG, "create_form(): error $exception")
          return@FutureCallback
        }
        Log.d(TAG, "create_form(): " + result.toString())
        parse_result(result)
        create_submit()
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

          label_row.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32F)

          layout_row.addView(label_row)

          when (value.asString) {
            "int" -> {
              val item_row = EditText(this)
              item_row.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32F)
              item_row.id = generateViewId()
              item_values.put(key, item_row.id)
              item_row.setInputType(InputType.TYPE_CLASS_NUMBER)
              item_row.layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
              layout_row.addView(item_row)
              val button_plus = Button(this)
              button_plus.text = "+"
              button_plus.setOnClickListener() {
                val x: String = item_row.text.toString()
                item_row.setText((x.toInt() + 1).toString())
              }
              layout_row.addView(button_plus)
              val button_minus = Button(this)
              button_minus.text = "-"
              button_minus.setOnClickListener() {
                item_row.setText((item_row.text.toString().toInt() - 1).toString())
              }
              layout_row.addView(button_minus)

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
          var id: Int;
          id = item_values.get(key)!!
          val v: View = findViewById(id)
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
      val v: View = findViewById(value)
      if (v is EditText) {
        Log.d(TAG, "$key is edittext")
      } else if (v is ToggleButton) {
        Log.d(TAG, "$key is togglebutton")
      } else {
        Log.d(TAG, "$key is unknown")
      }
    }

    EventBus.getDefault().post(message_event("device_data_done"))
  }

  private fun reload_all() {
    layout.removeAllViews()
    create_form()
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public fun onEventMainThread(event: message_event) {
    Log.i("WC", "onEventMainThread(): got message " + event.message);
    swipeRefreshLayout.isRefreshing = false
  }
}