package com.example.tenant.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tenant.R
import com.example.tenant.data.model.Obbject
import com.example.tenant.data.model.ObjectAndCategory
import com.example.tenant.data.model.ObjectStatus
import com.example.tenant.data.model.ObjectWithContracts

class ObjectsAdapter: RecyclerView.Adapter<ObjectsAdapter.ViewHolder>() {
    var objectsList: List<ObjectAndCategory> = ArrayList()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameTextView: TextView = itemView.findViewById(R.id.cardObjectName)
        val categoryTextView: TextView = itemView.findViewById(R.id.cardObjectCategory)
        val statusTextView: TextView = itemView.findViewById(R.id.cardObjectStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.objects_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentObject = objectsList[position]

        holder.nameTextView.text = currentObject.name
        holder.categoryTextView.text = currentObject.categoryName
        when (currentObject.objectStatus) {
            ObjectStatus.FREE -> {
                holder.statusTextView.text = "Свободный"
                holder.statusTextView.setTextColor(holder.itemView.resources.getColor(R.color.green))
            }
            ObjectStatus.IN_TENANT -> {
                holder.statusTextView.text = "В аренде"
                holder.statusTextView.setTextColor(holder.itemView.resources.getColor(R.color.red))
            }
            else -> {
                holder.statusTextView.text = "В ремонте"
                holder.statusTextView.setTextColor(holder.itemView.resources.getColor(R.color.blue))
            }
        }

        holder.itemView.setOnClickListener {
            objectItemClickListener?.onItemClick(currentObject)
        }
    }

    override fun getItemCount(): Int {
        return objectsList.size
    }

    interface ObjectItemClickListener{
        fun onItemClick(objectAndCategory: ObjectAndCategory)
    }

    private var objectItemClickListener: ObjectItemClickListener? = null

    fun setObjectItemClickListener(objectItemClickListener: ObjectItemClickListener){
        this.objectItemClickListener = objectItemClickListener
    }
}