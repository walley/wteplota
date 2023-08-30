package org.walley.wteplota;

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView

class adapter_RecyclerView internal constructor(
  context: Context?, data: java.util.ArrayList<wt_device>
                                               ) :
  RecyclerView.Adapter<adapter_RecyclerView.ViewHolder?>() {

  var TAG = "WT-ARV"
  val context: Context
  private val mData: java.util.ArrayList<wt_device> = data
  private val mInflater: LayoutInflater
  private var mClickListener: ItemClickListener? = null;

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view: View = mInflater.inflate(R.layout.item_recyclerview, parent, false)
    return ViewHolder(view)
  }

  public fun dump_data() {
    Log.i(TAG, "Data dump:")
    for (device in mData) {
      Log.i(TAG, "devices: (" + device.name + "," + device.value + "," + device.type + ")")
    }
  }

  // binds the data to the TextView in each cell
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.tv_name.setText(mData[position].name)
    holder.tv_type.setText(mData[position].type)
    holder.tv_value.setText(mData[position].value)

    val positive: Int
    positive = if (is_dark_theme()) {
      ContextCompat.getColor(context, R.color.light_green)
    } else {
      ContextCompat.getColor(context, R.color.dark_green)
    }

    var temp_temp: Float = 0F
    Log.i(wt_f_devices.TAG, "converting:" + mData[position].value)
    temp_temp = try {
      mData[position].value.replace("\"", "").toFloat()
    } catch (e: Exception) {
      1F
    }

    if (temp_temp <= -120) {
      holder.tv_value.setTextColor(ContextCompat.getColor(context, R.color.red))
    } else if (-120F < temp_temp && temp_temp < 0F) {
      holder.tv_value.setTextColor(ContextCompat.getColor(context, R.color.blue))
      Log.i(wt_f_devices.TAG, "value $temp_temp is negative")
    } else {
      holder.tv_value.setTextColor(positive)
      Log.i(wt_f_devices.TAG, "value $temp_temp is positive")
    }

    val unwrappedDrawable: Drawable?
    unwrappedDrawable = when (mData[position].type) {
      "dvere"      -> AppCompatResources.getDrawable(context, R.drawable.ic_door_open_24)
      "door"       -> AppCompatResources.getDrawable(context, R.drawable.ic_door_open_24)
      "teplomer"   -> AppCompatResources.getDrawable(context, R.drawable.ic_thermometer_empty_24)
      "thermostat" -> AppCompatResources.getDrawable(context, R.drawable.ic_thermometer_empty_24)
      "motor"      -> AppCompatResources.getDrawable(context, R.drawable.ic_fan_24)
      "voda"       -> AppCompatResources.getDrawable(context, R.drawable.ic_faucet_24)
      else         -> AppCompatResources.getDrawable(context, R.drawable.ic_cat_24)
    }

    val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
    if (is_dark_theme()) {
      DrawableCompat.setTint(wrappedDrawable, Color.WHITE)
      Log.i(wt_f_devices.TAG, "Dark Theme")
    } else {
      Log.i(wt_f_devices.TAG, "Shit Theme")
      DrawableCompat.setTint(wrappedDrawable, Color.BLACK)
    }

    holder.is_image.setImageDrawable(wrappedDrawable)
    Log.i(TAG, "name: " + mData[position].name)
  }

  // stores and recycles views as they are scrolled off screen
  inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var tv_name: TextView
    var tv_value: TextView
    var tv_type: TextView
    var is_image: ImageView
    override fun onClick(view: View?) {
      if (mClickListener != null) mClickListener!!.onItemClick(view, getAdapterPosition())
    }

    init {
      tv_name = itemView.findViewById(R.id.is_name)
      tv_value = itemView.findViewById(R.id.tv_value)
      tv_type = itemView.findViewById(R.id.tv_type)
      is_image = itemView.findViewById(R.id.is_image)
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
    when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
      Configuration.UI_MODE_NIGHT_YES -> return true
      Configuration.UI_MODE_NIGHT_NO -> return false
    }
    return true
  }

  init {
    this.mInflater = LayoutInflater.from(context)
    this.context = context!!
    dump_data()
  }

}