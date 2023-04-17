package com.example.tenant.ui.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tenant.R
import com.example.tenant.data.model.Obbject
import com.example.tenant.data.model.ObjectAndCategory
import com.example.tenant.data.model.ObjectStatus
import com.example.tenant.ui.model.MainActivityViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ObjectsFragment : Fragment() {
    private lateinit var objectsRecyclerView: RecyclerView
    private lateinit var mainActivityViewModel: MainActivityViewModel

    val adapter = ObjectsAdapter()
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

        addNewObjectButton.setOnClickListener {
            findNavController().navigate(R.id.action_objectsFragment_to_newObjectFragment2)
        }
    }

    override fun onStart() {
        super.onStart()
        mainActivityViewModel = (activity as MainActivity).mainActivityViewModel
        observe()
        observeObj()
        observeObjects()
        observeObjectsWithCategory()
        val objec1 = Obbject(1, "Квартира", 1, ObjectStatus.IN_TENANT, 32.0, "")
        //mainActivityViewModel.addObject(objec1)
        //mainActivityViewModel.getObject(1)
        //mainActivityViewModel.addCategory(Category(0, "Квартира"))
        //mainActivityViewModel.addCategory(Category(0, "Дача"))
        //mainActivityViewModel.addCategory(Category(0, "Комната"))
        //mainActivityViewModel.addCategory(Category(0, "Гараж"))
        //mainActivityViewModel.getAllObjects()
        mainActivityViewModel.getObjectsWithCategory()
        initRecycler()
        setDeleteClickListener()
    }

    private fun initRecycler(){
        val objec1 = Obbject(1, "Квартира", 1, ObjectStatus.IN_TENANT, 32.0, "")
        val objec2 = Obbject(2, "Дача", 2, ObjectStatus.FREE, 44.0, "")
        val list = listOf(objec1, objec2)
        //adapter.objectsList = list
        objectsRecyclerView.adapter = adapter
        objectsRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter.setObjectItemClickListener(object: ObjectsAdapter.ObjectItemClickListener{
            override fun onItemClick(objectAndCategory: ObjectAndCategory) {
                val intent = Intent(activity, ChosenObjectActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("object", objectAndCategory)
                intent.putExtra("intentObject", bundle)
                startActivity(intent)
            }
        })
    }

    private fun observe(){
        mainActivityViewModel.objectIdLiveDate.observe(viewLifecycleOwner, Observer {
            it?.let {id->
                Log.i("objId", id.toString())
            }
        })
    }

    private fun observeObj(){
        mainActivityViewModel.objectLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {obj ->
                Log.i("obj", obj.toString())
            }
        })
    }


    private fun observeObjects(){
        mainActivityViewModel.objectsLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {list->
                //adapter.objectsList = list
            }
        })
    }

    private fun observeObjectsWithCategory(){
        mainActivityViewModel.objectsWithCategory.observe(viewLifecycleOwner, Observer {
            it?.let {list->
                adapter.objectsList.submitList(list)
            }
        })
    }

    private fun setDeleteClickListener(){
        adapter.setDeleteItemClickListener(object : ObjectsAdapter.DeleteItemClickListener{
            override fun onDeleteItem(objectAndCategory: ObjectAndCategory) {
                val alertDialogBuilder = AlertDialog.Builder(context)
                alertDialogBuilder.setTitle("Удаление объекта")
                alertDialogBuilder.setMessage("Вы уверены?")
                alertDialogBuilder.setPositiveButton("Да") { p0, p1 ->
                    mainActivityViewModel.deleteObject(objectAndCategory.id)
                }
                alertDialogBuilder.setNegativeButton("Нет"){ dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.cancel()
                }
                alertDialogBuilder.create().show()
            }
        })
    }
}