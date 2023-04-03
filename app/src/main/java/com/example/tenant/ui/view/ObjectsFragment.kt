package com.example.tenant.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tenant.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ObjectsFragment : Fragment() {

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

        addNewObjectButton.setOnClickListener {
            findNavController().navigate(R.id.action_objectsFragment_to_arendatorFragment)
        }
    }
}