package org.walley.wteplota;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class adapter_sms(
  private val context: Context?, private val mList: List<String>
                 ) : RecyclerView.Adapter<adapter_sms.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(context).inflate(R.layout.item_sms_cardview, parent, false)
    val recyclerview = parent.findViewById<RecyclerView>(R.id.rv_sms)
    recyclerview.layoutManager = LinearLayoutManager(context)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val ItemsViewModel = mList[position]
    holder.imageView.setImageResource(R.drawable.ic_cat_24)
    holder.textView.text = mList[position]
  }

  override fun getItemCount(): Int {
    return mList.size
  }


  // stores and recycles views as they are scrolled off screen
  inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
                                                                View.OnClickListener {

    val imageView: ImageView = itemView.findViewById(R.id.imageview)
    val textView: TextView = itemView.findViewById(R.id.textView)

    override fun onClick(view: View?) {
//      if (mClickListener != null) mClickListener!!.onItemClick(view, getAdapterPosition())
    }

    init {
      itemView.setOnClickListener(this)
    }
  }

}
