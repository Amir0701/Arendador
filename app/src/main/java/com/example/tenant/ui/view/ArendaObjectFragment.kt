package com.example.tenant.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tenant.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ArendaObjectFragment : Fragment() {
    private lateinit var addContractButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_arenda_object, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addContractButton = view.findViewById(R.id.addContractButton)

        addContractButton.setOnClickListener {
            val intent = Intent(activity, NewContractActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ArendaObjectFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}