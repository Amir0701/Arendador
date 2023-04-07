package com.example.tenant.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.tenant.R
import com.example.tenant.data.model.Obbject
import com.example.tenant.data.model.ObjectStatus
import com.example.tenant.ui.model.MainActivityViewModel
import com.google.android.material.textfield.TextInputLayout

class NewObjectFragment : Fragment() {
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var categoryList: AutoCompleteTextView
    private lateinit var addObjectButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var squareEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var categoryInputLayout: TextInputLayout

    private val categoryNames = mutableListOf<String>()

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
        addObjectButton = view.findViewById(R.id.addObjectButton)
        nameEditText = view.findViewById(R.id.objectNameEditText)
        squareEditText = view.findViewById(R.id.squareEditText)
        addressEditText = view.findViewById(R.id.addressEditText)
        nameInputLayout = view.findViewById(R.id.nameInputLayout)
        categoryInputLayout = view.findViewById(R.id.categoriesInputLayout)

        addObjectButton.setOnClickListener {
            getData()?.let {
                mainActivityViewModel.addObject(it)
                findNavController().navigate(R.id.action_newObjectFragment2_to_objectsFragment)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mainActivityViewModel = (activity as MainActivity).mainActivityViewModel
        observeCategories()
        mainActivityViewModel.getAllCategories()
    }

    private fun observeCategories(){
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

    private fun getData(): Obbject?{
        val name:String? = if(nameEditText.text.toString().isNotEmpty()){
            nameInputLayout.helperText = ""
            nameEditText.text.toString()
        }else{
            nameInputLayout.helperText = requireContext().resources.getString(R.string.fillIn)
            null
        }

        val idCat:Int? = if(categoryList.text.toString() != "Вид объекта"){
            categoryInputLayout.helperText = ""
            var id = 0
            mainActivityViewModel.categoryLiveData.value?.let { list->
                list.forEach{category->
                    if(category.name == categoryList.text.toString()){
                        id = category.id
                    }
                }
            }
            id
        }
        else{
            categoryInputLayout.helperText = requireContext().resources.getString(R.string.choose_category)
            null
        }

        val address:String? = if(nameEditText.text != null){
            nameEditText.text.toString()
        }
        else{
            null
        }

        val square: Double? = if(squareEditText.text.toString().isNotEmpty()){
            squareEditText.text.toString().toDouble()
        }
        else{
            null
        }

        if(name != null && idCat != null){
            return Obbject(0, name, idCat, ObjectStatus.FREE, square, address)
        }
        return null
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