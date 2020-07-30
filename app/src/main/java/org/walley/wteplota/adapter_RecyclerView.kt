package org.walley.wteplota;

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class adapter_RecyclerView : RecyclerView.Adapter<adapter_RecyclerView.ViewHolder?> {

  internal constructor(context: Context?, data: java.util.ArrayList<wt_device>) : super() {
    this.mData = data
    this.mInflater = LayoutInflater.from(context)
    this.context = context!!
    dump_data()
  }

  var TAG = "WT-RV"
  val context: Context
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

    val positive: Int
    positive = if (is_dark_theme()) {
      ContextCompat.getColor(context, R.color.light_green)
    } else {
      ContextCompat.getColor(context, R.color.dark_green)
    }

    var temp_temp = 0
    temp_temp = try {
      holder.tv_value.toString().replace("\"", "").toInt()
    } catch (e: Exception) {
      1
    }

    if (temp_temp < -120) {
      holder.tv_value.setTextColor(ContextCompat.getColor(context, R.color.red))
    } else if (-120 < temp_temp && temp_temp < 0) {
      holder.tv_value.setTextColor(ContextCompat.getColor(context, R.color.blue))
    } else {
      holder.tv_value.setTextColor(positive)
    }

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

  fun is_dark_theme(): Boolean {
    when (context?.getResources()?.getConfiguration()?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
      Configuration.UI_MODE_NIGHT_YES -> return true
      Configuration.UI_MODE_NIGHT_NO -> return false
    }
    return true
  }

}