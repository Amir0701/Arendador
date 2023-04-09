package com.example.tenant.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tenant.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ExplotationListFragment : Fragment() {
    private lateinit var addExploitationButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explotation_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addExploitationButton = view.findViewById(R.id.addExploitationButton)
        addExploitationButton.setOnClickListener {
            val intent = Intent(activity, NewExploitationActivity::class.java)
            startActivity(intent)
        }
    }
}