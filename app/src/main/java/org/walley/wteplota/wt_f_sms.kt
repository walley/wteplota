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
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.fragment_sms.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class wt_f_sms : Fragment() {
  private val TAG = "WT_SMS"
  var url: String? = null
  var prefs: SharedPreferences? = null
  var sms_list: List<String>? = null

  private val wtviewmodel: wt_viewmodelsms by viewModels()

  // TODO: Rename and change types of parameters
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
    val root = inflater.inflate(org.walley.wteplota.R.layout.fragment_sms, container, false)

    Log.i(TAG, "oncreateview")

    wtviewmodel.get_sms_data()?.observe(viewLifecycleOwner, Observer {
      Log.i(TAG, "viewmodel observer onchanged()")
    })

    sms_list = wtviewmodel.get_sms_data()?.value

    sms_list?.forEach {
      val len = it.length
      Log.i(TAG, it)
    }
    return root
  }

  private lateinit var button: Button

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

//    url = this.prefs?.getString("server_url", "https://localhost")
    url = "http://mn-servispc.eu/android/"
    url += "sms.php"

    bt_sms_send?.setOnClickListener {
      Log.d(TAG, "klyk sms")
      Toast.makeText(activity, "You sms clicked me. $url", Toast.LENGTH_SHORT).show()
      get_temp()
//      sms_send()
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

  private fun get_temp() {
//    val context: Context = container.getContext();
    Log.i(TAG, "sms_send: $url")

    Ion.with(getContext()).load(url).asJsonObject()
      .setCallback(FutureCallback<JsonObject?> { exception, result ->
        var item_json: String
        if (result == null) {
          Log.e(wt_viewmodel.TAG, "get_temp(): json error")
          return@FutureCallback
        }

        var temps = ""

        val keys: Set<String> = result.keySet()
        for (element in keys) {
          Log.i(TAG, "key stuff $element")
          val j: JsonArray = result.get(element).asJsonArray
          println(element)
          for (sms_text in j) {
            Log.i(TAG, "val stuff $sms_text")
          }
        }
//          EventBus.getDefault().post(MessageEvent("data_done"))

        Log.d(TAG, "parsed stuff $temps")
      })
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