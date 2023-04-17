package com.example.tenant.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tenant.R
import com.example.tenant.data.model.Obbject
import com.example.tenant.data.model.ObjectAndCategory
import com.example.tenant.data.model.ObjectStatus
import com.example.tenant.data.model.ObjectWithContracts
import com.google.android.material.button.MaterialButton

class ObjectsAdapter: RecyclerView.Adapter<ObjectsAdapter.ViewHolder>() {

    private val differ = object: DiffUtil.ItemCallback<ObjectAndCategory>(){
        override fun areItemsTheSame(
            oldItem: ObjectAndCategory,
            newItem: ObjectAndCategory
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ObjectAndCategory,
            newItem: ObjectAndCategory
        ): Boolean {
            return oldItem == newItem
        }

    }

    val objectsList = AsyncListDiffer(this, differ)

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameTextView: TextView = itemView.findViewById(R.id.cardObjectName)
        val categoryTextView: TextView = itemView.findViewById(R.id.cardObjectCategory)
        val statusTextView: TextView = itemView.findViewById(R.id.cardObjectStatus)
        val editButton: MaterialButton = itemView.findViewById(R.id.editObjectButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.objects_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentObject = objectsList.currentList[position]

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

        holder.editButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("edit_object", currentObject)
            it.findNavController().navigate(R.id.action_objectsFragment_to_newObjectFragment2, bundle)
        }
    }

    override fun getItemCount(): Int {
        return objectsList.currentList.size
    }

    interface ObjectItemClickListener{
        fun onItemClick(objectAndCategory: ObjectAndCategory)
    }

    private var objectItemClickListener: ObjectItemClickListener? = null

    fun setObjectItemClickListener(objectItemClickListener: ObjectItemClickListener){
        this.objectItemClickListener = objectItemClickListener
    }
}