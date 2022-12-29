package org.walley.wteplota

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapter_sms(
  private val context: Context?,
  private val sms_list: MutableList<String>,
  private val listener: (i: Int) -> Unit
                 ) : RecyclerView.Adapter<adapter_sms.ViewHolder>() {

  var TAG = "WT-ASMS"

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(context).inflate(R.layout.item_sms_cardview, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val sms_number: String
    val sms_text: String

    holder.imageView.setImageResource(R.drawable.ic_cat_24)
    if (sms_list[position].contains('"')) {
      sms_number = sms_list[position].split('"')[0]
      sms_text = sms_list[position].split('"')[1]
    } else {
      sms_number = sms_list[position]
      sms_text = "nic"
    }

    Log.i(TAG, "onBindViewHolder(): $position: number,text:$sms_number,$sms_text")

    holder.textView.text = sms_number
    holder.textView2.text = sms_text
    holder.itemView.setOnClickListener { listener(position) }
  }

  override fun getItemCount(): Int {
    return sms_list.size
  }

  fun getItem(id: Int): String {
    return sms_list[id]
  }

  fun dump_data() {
    sms_list.forEach {
      Log.i(TAG, "adapter list content: $it")
    }
  }

  /****** stores and recycles views as they are scrolled off screen ******/
  inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
                                                                View.OnClickListener {

    val imageView: ImageView = itemView.findViewById(R.id.iv_card)
    val textView: TextView = itemView.findViewById(R.id.textView)
    val textView2: TextView = itemView.findViewById(R.id.textView2)

    init {
      itemView.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
      Log.i(TAG, "adapter onclick")
      Log.i(TAG, "listener on  $bindingAdapterPosition")
    }
  }
}
