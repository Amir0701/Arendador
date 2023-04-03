package com.example.tenant.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tenant.R
import com.example.tenant.data.model.Obbject
import com.example.tenant.data.model.ObjectStatus
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ObjectsFragment : Fragment() {
    private lateinit var objectsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_objects, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addNewObjectButton: FloatingActionButton = view.findViewById(R.id.add_new_object)
        objectsRecyclerView = view.findViewById(R.id.objectsRecycler)
        initRecycler()

        addNewObjectButton.setOnClickListener {
            findNavController().navigate(R.id.action_objectsFragment_to_arendatorFragment)
        }
    }

    private fun initRecycler(){
        val objec1 = Obbject(1, "Квартира", 1, ObjectStatus.IN_TENANT, 32.0, "")
        val objec2 = Obbject(2, "Дача", 2, ObjectStatus.FREE, 44.0, "")
        val list = listOf(objec1, objec2)
        val adapter = ObjectsAdapter()
        adapter.objectsList = list
        objectsRecyclerView.adapter = adapter
        objectsRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter.setObjectItemClickListener(object: ObjectsAdapter.ObjectItemClickListener{
            override fun onItemClick(obbject: Obbject) {
                val intent = Intent(activity, ChosenObjectActivity::class.java)
                startActivity(intent)
            }
        })
    }
}