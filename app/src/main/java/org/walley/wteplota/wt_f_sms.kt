package org.walley.wteplota

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_sms.*
import kotlinx.android.synthetic.main.fragment_sms.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class wt_f_sms : Fragment() {
  private val TAG = "WT_SMS"
  var url: String? = null
  var prefs: SharedPreferences? = null
  var sms_list: MutableList<String> = ArrayList()
  private var adapter: adapter_sms? = null

  private val vm: wt_viewmodelsms by viewModels()

  private var param1: String? = null
  private var param2: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      param1 = it.getString(ARG_PARAM1)
      param2 = it.getString(ARG_PARAM2)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                           ): View? {

    val root = inflater.inflate(R.layout.fragment_sms, container, false)
    Log.i(TAG, "wt_f_sms oncreateview()")

    sms_list.add("a")

    adapter = adapter_sms(context, sms_list)

    root.rv_sms.layoutManager = LinearLayoutManager(context)
    root.rv_sms.adapter = adapter

    vm.sms_data.observe(viewLifecycleOwner, Observer {
      Log.i(TAG, "viewmodelsms observer onchanged()")
      sms_list.clear()
//      sms_list = vm.sms_data.value!!
      vm.sms_data.value!!.forEach {
        Log.i(TAG, "wt_f_sms list content: $it")
        sms_list.add(it)
      }

      sms_list.add("b")
      dump_sms_list()
      show_listview()
    })

    return root
  }

  override fun onStart() {
    super.onStart()
  }

  override fun onStop() {
    super.onStop()
  }

  private lateinit var button: Button

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    bt_sms_send?.setOnClickListener {
      Log.d(TAG, "klyk sms")
      Toast.makeText(activity, "You sms clicked me. $url", Toast.LENGTH_SHORT).show()
//     sms_send()
    }


  }

  fun sms_send() {
    val intent = Intent(Intent.ACTION_SEND)
    val shareBody = "Here is the share content body"
    intent.type = "text/plain"
    intent.putExtra("address", "smsto:9839098390")
    intent.putExtra(Intent.EXTRA_TEXT, shareBody)
    startActivity(Intent.createChooser(intent, "nazdar"))
  }

  private fun dump_sms_list() {
    sms_list!!.forEach {
      val len = it.length
      Log.i(TAG, "wt_f_sms list content: $it")
    }
  }

  private fun show_listview() {
    dump_sms_list()
    adapter?.notifyDataSetChanged()
    adapter?.dump_data()
  }

  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment wt_f_sms.
     */
    // TODO: Rename and change types and number of parameters
    @JvmStatic
    fun newInstance(param1: String, param2: String) = wt_f_sms().apply {
      arguments = Bundle().apply {
        putString(ARG_PARAM1, param1)
        putString(ARG_PARAM2, param2)
      }
    }
  }


}