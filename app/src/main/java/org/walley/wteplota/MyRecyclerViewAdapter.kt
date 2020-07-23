package org.walley.wteplota;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerViewAdapter internal constructor(context: Context?, data: Array<String>) : RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder?>() {
  private val mData: Array<String> = data
  private val mInflater: LayoutInflater = LayoutInflater.from(context)
  private var mClickListener: ItemClickListener? = null;

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view: View = mInflater.inflate(R.layout.item_recyclerview, parent, false)
    return ViewHolder(view)
  }

  // binds the data to the TextView in each cell
  override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
    holder.myTextView.setText(mData[position])
  }

  // stores and recycles views as they are scrolled off screen
  inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var myTextView: TextView
    override fun onClick(view: View?) {
      if (mClickListener != null) mClickListener!!.onItemClick(view, getAdapterPosition())
    }

    init {
      myTextView = itemView.findViewById(R.id.info_text)
      itemView.setOnClickListener(this)
    }
  }

  // convenience method for getting data at click position
  fun getItem(id: Int): String {
    return mData[id]
  }

  // allows clicks events to be caught
  fun setClickListener(itemClickListener: ItemClickListener?) {
    mClickListener = itemClickListener
  }

  // parent activity will implement this method to respond to click events
  interface ItemClickListener {
    fun onItemClick(view: View?, position: Int)
  }

  override fun getItemCount(): Int {
    return mData.size;
  }

}