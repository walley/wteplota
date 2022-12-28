package org.walley.wteplota;

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
//  private var mClickListener: adapter_sms.ItemClickListener? = null;

  // parent activity will implement this method to respond to click events
//  interface ItemClickListener {
//    fun onItemClick(view: View?, position: Int)
//  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(context).inflate(R.layout.item_sms_cardview, parent, false)
    val recyclerview = parent.findViewById<RecyclerView>(R.id.rv_sms)
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
////
    holder.itemView.setOnClickListener { listener(position) }
  }

  override fun getItemCount(): Int {
    return sms_list.size
  }


  // stores and recycles views as they are scrolled off screen
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
/*      if (mClickListener != null) {
        Log.i(TAG, "listener on  $bindingAdapterPosition")
//        mClickListener!!.onItemClick(view, getAdapterPosition())
        mClickListener!!.onItemClick(view, bindingAdapterPosition)
      } else {
        Log.i(TAG, "listener null")
      }*/
    }
  }

  // convenience method for getting data at click position
  fun getItem(id: Int): String {
    return sms_list[id]
  }

  /*
  allows clicks events to be caught
  fun setClickListener(itemClickListener: ItemClickListener?) {
  this.mClickListener = itemClickListener
  Log.i(TAG, "setClickListener() set")
  }
  */


  public fun dump_data() {
    sms_list.forEach {
      val len = it.length
      Log.i(TAG, "adapter list content: $it")
    }
  }
}
