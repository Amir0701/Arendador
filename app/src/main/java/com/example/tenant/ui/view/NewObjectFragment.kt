package com.example.tenant.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.Observer
import com.example.tenant.R
import com.example.tenant.ui.model.MainActivityViewModel

class NewObjectFragment : Fragment() {
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var categoryList: AutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_object, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryList = view.findViewById(R.id.categoryList)
    }

    override fun onStart() {
        super.onStart()
        mainActivityViewModel = (activity as MainActivity).mainActivityViewModel
        observeCategories()
        mainActivityViewModel.getAllCategories()
    }

    private fun observeCategories(){
        val categoryNames = mutableListOf<String>()
        mainActivityViewModel.categoryLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {list->
                Log.i("categ", list.size.toString())
                list.forEach {cat->
                    categoryNames.add(cat.name)
                }
                val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categoryNames)
                categoryList.setAdapter(adapter)
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewObjectFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}