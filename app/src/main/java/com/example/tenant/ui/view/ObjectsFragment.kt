package com.example.tenant.ui.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tenant.R
import com.example.tenant.data.model.Category
import com.example.tenant.data.model.Obbject
import com.example.tenant.data.model.ObjectAndCategory
import com.example.tenant.data.model.ObjectStatus
import com.example.tenant.ui.viewmodel.MainActivityViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class ObjectsFragment : Fragment() {
    private lateinit var objectsRecyclerView: RecyclerView
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var mapSwitch: SwitchCompat
    private lateinit var addNewObjectButton: ExtendedFloatingActionButton

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
        addNewObjectButton = view.findViewById(R.id.add_new_object)
        objectsRecyclerView = view.findViewById(R.id.objectsRecycler)

        addNewObjectButton.setOnClickListener {
            findNavController().navigate(R.id.action_objectsFragment_to_newObjectFragment2)
        }

        (activity as MainActivity).supportActionBar?.title = "Объекты недвижимости"
        mapSwitch = view.findViewById(R.id.mapSwitch)
        mapSwitch.setOnCheckedChangeListener { compoundButton, b ->
            val intent = Intent((activity as MainActivity), MapActivity::class.java)
            startActivity(intent)
        }

        addNewObjectButton.text = "Добавить объект"
    }

    override fun onStart() {
        super.onStart()
        mainActivityViewModel = (activity as MainActivity).mainActivityViewModel
        observeObj()
        observeObjects()
        observeObjectsWithCategory()
        observeDeleted()
        observeCategories()
        val objec1 = Obbject(1, "Квартира", 1, ObjectStatus.IN_TENANT, 32.0, "")
        //mainActivityViewModel.addObject(objec1)
        //mainActivityViewModel.getObject(1)
        //mainActivityViewModel.getAllObjects()
        mainActivityViewModel.getObjectsWithCategory()
        mainActivityViewModel.getAllCategories()
        initRecycler()
        setDeleteClickListener()
        mapSwitch.isChecked = false
        addNewObjectButton.text = "Добавить объект"
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

        objectsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

//                if(dy > 0)
//                    addNewObjectButton.text = null
//                else
//                    addNewObjectButton.text = "Добавить объект"
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
                if(list.isNotEmpty())
                    Log.i("imgg", list.get(list.size - 1).image.toString())
            }
        })
    }

    private fun observeDeleted(){
        mainActivityViewModel.deletedObjectCount.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainActivityViewModel.getObjectsWithCategory()
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

    private fun observeCategories(){
        mainActivityViewModel.categoryLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it.isEmpty()){
                    mainActivityViewModel.addCategory(Category(0, "Квартира"))
                    mainActivityViewModel.addCategory(Category(0, "Дача"))
                    mainActivityViewModel.addCategory(Category(0, "Комната"))
                    mainActivityViewModel.addCategory(Category(0, "Гараж"))
                    Thread.sleep(500L)
                    mainActivityViewModel.getAllCategories()
                }
            }
        })
    }
}