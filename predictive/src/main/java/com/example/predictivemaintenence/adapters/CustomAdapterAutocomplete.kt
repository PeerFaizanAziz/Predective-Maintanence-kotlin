package com.example.predictivemaintenence.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.example.predictivemaintenence.modelClass.PredictiveMonitorClass
import com.example.predictivemaintenence.R

class CustomAdapterAutocomplete(
    private val c: Context,
    @LayoutRes private val layoutResource: Int,
    private val items: ArrayList<PredictiveMonitorClass>
) :
    ArrayAdapter<PredictiveMonitorClass>(c, layoutResource, items) {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): PredictiveMonitorClass = items[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView ?: LayoutInflater.from(c).inflate(layoutResource, parent, false)

        val tvName: TextView = view.findViewById(R.id.tvMovieName)
        val p: PredictiveMonitorClass = items[position]

        tvName.text = p.className

        return view
    }
}