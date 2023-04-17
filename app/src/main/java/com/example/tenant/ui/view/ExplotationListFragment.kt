package com.example.tenant.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tenant.R
import com.example.tenant.data.model.Exploitation
import com.example.tenant.ui.model.ChosenActivityViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ExplotationListFragment : Fragment() {
    private lateinit var addExploitationButton: FloatingActionButton
    private lateinit var viewModel: ChosenActivityViewModel
    private lateinit var exploitationsRecyclerView: RecyclerView
    private lateinit var exploitationAdapter: ExploitationListAdapter
    private var id: Int? = null

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
        viewModel = (activity as ChosenObjectActivity).chosenActivityViewModel
        exploitationsRecyclerView = view.findViewById(R.id.exploitationRecyclerView)
        setUpRecyclerView()
        observeExploitations()
        id = (activity as ChosenObjectActivity).objectAndCategory?.id

        addExploitationButton.setOnClickListener {
            val intent = Intent(activity, NewExploitationActivity::class.java)
            intent.putExtra("obj_id", id)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        id?.let {
            viewModel.getAllExploitationByObjectId(it)
        }
    }

    private fun setUpRecyclerView(){
        exploitationAdapter = ExploitationListAdapter()
        exploitationsRecyclerView.adapter = exploitationAdapter
        exploitationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        exploitationAdapter.setOnItemClickListener(object : ExploitationListAdapter.OnItemClickListener{
            override fun onItemClick(exploitation: Exploitation) {
                val intent = Intent(activity, NewExploitationActivity::class.java)
                id?.let {
                    intent.putExtra("obj_id", id)
                }
                val bundle = Bundle()
                bundle.putSerializable("expo", exploitation)
                intent.putExtra("expo_bundle", bundle)
                startActivity(intent)
            }
        })
    }

    private fun observeExploitations(){
        viewModel.exploitations.observe(viewLifecycleOwner, Observer {
            it?.let {list->
                exploitationAdapter.exploitationList.submitList(list)
            }
        })
    }
}