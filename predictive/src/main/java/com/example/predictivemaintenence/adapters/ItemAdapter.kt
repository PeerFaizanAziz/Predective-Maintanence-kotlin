package com.example.predictivemaintenence.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.predictivemaintenence.modelClass.SearchEquipmentClass
import com.example.predictivemaintenence.R

class ItemAdapter(
    private val exampleList: ArrayList<SearchEquipmentClass>,
    private val listner: onItemClickListener
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {



    inner class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val textItemName: TextView = itemView.findViewById(R.id.name_item)
        val textLocation: TextView = itemView.findViewById(R.id.location_tv)
        val textBuilding: TextView = itemView.findViewById(R.id.building_tv)

        init {
            itemView.setOnClickListener {
                listner.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_item_row, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = exampleList[position]
        holder.textItemName.text = currentItem.name
        holder.textLocation.text = currentItem.locationName
        holder.textBuilding.text = currentItem.buildingName
    }

    override fun getItemCount() = exampleList.size

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }
}