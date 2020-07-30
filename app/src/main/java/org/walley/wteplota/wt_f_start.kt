package org.walley.wteplota

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_start.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class wt_f_start : Fragment(), adapter_RecyclerView.ItemClickListener {

  private val TAG = "WT-S"
  private var adapter: adapter_RecyclerView? = null
  private val data: Array<String> = arrayOf("1", "2", "3", "wr4r", "a", "::")
  private val number_of_columns: Int = 3
  private var wtviewmodel: wt_viewmodel? = null
  var data_hash: Hashtable<String, JsonObject>? = Hashtable()
  var devices_array: java.util.ArrayList<wt_device> = java.util.ArrayList()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

    val root = inflater.inflate(R.layout.fragment_start, container, false)
    wtviewmodel = ViewModelProviders.of(this).get(wt_viewmodel::class.java)

    wtviewmodel!!._server_data.observe(viewLifecycleOwner, Observer {
      Log.i(TAG, "vm observer onchanged()")
      update_temp()
    })

    data_hash = wtviewmodel!!._server_data.value

    val x: wt_device = wt_device("a", "b", "c");
    devices_array.add(x);

    adapter = adapter_RecyclerView(context = context, data = devices_array)
//   adapter = adapter_RecyclerView(context, this.devices_array)
    adapter!!.setClickListener(this)

    root.rv_start.layoutManager = GridLayoutManager(context, number_of_columns)
    root.rv_start.adapter = adapter

    root.rv_button.setOnClickListener {
      // your code to perform when the user clicks on the button
      Toast.makeText(context, "You clicked me.", Toast.LENGTH_SHORT).show()
      val xx: wt_device = wt_device("jouda", "b", "c");
      devices_array.add(xx);
      show_listview("Uvod");
      dump_devices_array()
    }

    return root
  }

  override fun onItemClick(view: View?, position: Int) {
    //TODO("Not yet implemented")
  }

  override fun onStart() {
    super.onStart()
    EventBus.getDefault().register(this)
  }

  override fun onStop() {
    EventBus.getDefault().unregister(this)
    super.onStop()
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  fun onMessageEvent(event: MessageEvent) {
    Toast.makeText(getActivity(), TAG + " got " + event.message, Toast.LENGTH_SHORT).show();
    Log.i(TAG, event.message)
    when (event.message) {
      "data_done" -> {
//        update_temp()
        show_listview("Uvod")
      }
    }
  }

  private fun update_temp() {
    val temps = ""
    Log.i(TAG, "update_temp() start.")
    for (key in data_hash!!.keys) {
      val element = data_hash!![key] as JsonElement?
      val jo = element!!.asJsonObject
      for ((key1, value) in jo.entrySet()) {
        if (key1 == "Nazev") {
          val room_name = value.toString().replace("\"", "")
          Log.i(TAG, "update_temp() name: $value")
        }
      }
    }
  }

  private fun dump_devices_array() {
    for (device in devices_array) {
      Log.i(TAG, "devices: (" + device.name + "," + device.value + "," + device.type + ")")
    }
  }

  private fun show_listview(room_name: String) {
    val room_data = data_hash!![room_name]
    var device: wt_device
//    devices_array?.clear()
    if (room_data != null) {
      for ((key, value) in room_data.entrySet()) {
        if (key != "Nazev") {
          device = wt_device(key, value.toString())
          add_to_devices_list(key, value.toString())
        }
        Log.i(TAG, "added $key")
      }
    }
    dump_devices_array()
    adapter?.notifyDataSetChanged()
    adapter?.dump_data()
  }

  private fun add_to_devices_list(name: String, value: String) {

    val device_name: String
    val device_type: String
    val device_value: String
    var device_index = 0
    var has_name = false
    var has_type = false
    var has_value = false

    if (name.contains(".typ")) {
      device_name = name.substring(0, name.length - 4).replace("\"", "")
      device_type = value.replace("\"", "")
      device_value = ""
    } else {
      device_name = name.replace("\"", "")
      device_type = ""
      device_value = value.replace("\"", "")
    }
    for (i in devices_array!!.indices) {
      val x = devices_array!![i]
      if (x.name == device_name) {
        has_name = true
        device_index = i
        if (x.type != null && !x.type.isEmpty()) {
          has_type = true
        }
        if (x.value != null && !x.value.isEmpty()) {
          has_value = true
        }
        break
      }
    }
    Log.i(TAG, "a_t_d_l($name,$value): has_value:$has_value, has_type:$has_type")
    if (has_name) {
      Log.i(TAG, "a_t_d_l($name,$value): existing :$device_name,$device_value,$device_type")
      devices_array[device_index].name = device_name
      if (!has_type) {
        devices_array[device_index].type = device_type
      }
      if (!has_value) {
        devices_array[device_index].value = device_value
      }
    } else {
      Log.i(TAG, "a_t_d_l($name,$value): new :$device_name,$device_value,$device_type")
      val x = wt_device(device_name, device_value, device_type)
      devices_array.add(x)
    }
  }

}
