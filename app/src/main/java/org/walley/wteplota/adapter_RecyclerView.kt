package org.walley.wteplota;

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

class adapter_RecyclerView : RecyclerView.Adapter<adapter_RecyclerView.ViewHolder?> {

  internal constructor(context: Context?, data: java.util.ArrayList<wt_device>) : super() {
    this.mData = data
    this.mInflater = LayoutInflater.from(context)
    dump_data()
  }

  var TAG = "WT-RV"

  private val mData: java.util.ArrayList<wt_device>
  private val mInflater: LayoutInflater
  private var mClickListener: ItemClickListener? = null;

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view: View = mInflater.inflate(R.layout.item_recyclerview, parent, false)

    return ViewHolder(view)
  }

  public fun dump_data() {
    for (device in mData) {
      Log.i(TAG, "devices: (" + device.name + "," + device.value + "," + device.type + ")")
    }
  }

  // binds the data to the TextView in each cell
  override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
    holder.tv_name.setText(mData[position].name)
    holder.tv_type.setText(mData[position].type)
    holder.tv_value.setText(mData[position].value)
    Log.i(TAG, "name: " + mData[position].name)
  }

  // stores and recycles views as they are scrolled off screen
  inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var tv_name: TextView
    var tv_value: TextView
    var tv_type: TextView
    override fun onClick(view: View?) {
      if (mClickListener != null) mClickListener!!.onItemClick(view, getAdapterPosition())
    }

    init {
      tv_name = itemView.findViewById(R.id.tv_name)
      tv_value = itemView.findViewById(R.id.tv_value)
      tv_type = itemView.findViewById(R.id.tv_type)
      itemView.setOnClickListener(this)
    }
  }

  // convenience method for getting data at click position
  fun getItem(id: Int): wt_device {
    return mData[id]
  }

  // allows clicks events to be caught
  fun setClickListener(itemClickListener: ItemClickListener?) {
    this.mClickListener = itemClickListener
  }

  // parent activity will implement this method to respond to click events
  interface ItemClickListener {
    fun onItemClick(view: View?, position: Int)
  }

  override fun getItemCount(): Int {
    Log.i(TAG, "size: " + mData.size)
    return mData.size;
  }

}