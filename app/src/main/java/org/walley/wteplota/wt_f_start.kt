package org.walley.wteplota

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_start.view.*

class wt_f_start : Fragment(), adapter_RecyclerView.ItemClickListener {

  private lateinit var adapter: adapter_RecyclerView
  private val data: Array<String> = arrayOf("1", "2", "3", "wr4r", "a", "::")
  private val numberOfColumns: Int = 3

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

    val root = inflater.inflate(R.layout.fragment_start, container, false)

    val recyclerView: RecyclerView

    root.rv_start.layoutManager = GridLayoutManager(context, numberOfColumns)

    val adapter = adapter_RecyclerView(context = context, data = data)
    adapter.setClickListener(this)

    root.rv_start.adapter = adapter

    return root
  }

  override fun onItemClick(view: View?, position: Int) {
    //TODO("Not yet implemented")
  }
}
