package com.example.tenant.ui.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tenant.R
import com.example.tenant.data.model.Obbject
import com.example.tenant.data.model.ObjectStatus
import com.example.tenant.ui.viewmodel.MainActivityViewModel
import com.example.tenant.util.ImageUtil
import com.example.tenant.util.RealPathUtil
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.IOException


class NewObjectFragment : Fragment() {
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var categoryList: AutoCompleteTextView
    private lateinit var addObjectButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var squareEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var categoryInputLayout: TextInputLayout
    private lateinit var addImageButton: ImageView
    private var imagePath: String? = null

    private val args: NewObjectFragmentArgs by navArgs()

    private val categoryNames = mutableListOf<String>()

    private val CHOOSE_IMAGE = 101

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
        addImageButton = view.findViewById(R.id.addImageButton)

        addObjectButton.setOnClickListener {
            getData()?.let {
                mainActivityViewModel.addObject(it)
                findNavController().navigate(R.id.action_newObjectFragment2_to_objectsFragment)
            }
        }

        addImageButton.setOnClickListener {
            chooseImage()
        }

        setObject()
    }

    override fun onStart() {
        super.onStart()
        mainActivityViewModel = (activity as MainActivity).mainActivityViewModel
        observeCategories()
        mainActivityViewModel.getAllCategories()
    }

    private fun setObject(){
        args.editObject?.let {obj->
            nameEditText.setText(obj.name)
            obj.square?.let {
                squareEditText.setText(it.toString())
            }

            obj.address?.let {
                addressEditText.setText(it)
            }

            obj.image?.let {
                Glide.with(requireContext())
                    .load(getImage(it, requireContext()))
                    .into(addImageButton)
            }

            addObjectButton.text = "Изменить"
        }
    }

    private fun observeCategories(){
        mainActivityViewModel.categoryLiveData.observe(viewLifecycleOwner, Observer {
            var pos = 0
            var i = 0
            if(categoryList.adapter == null){
                it?.let {list->
                    list.forEach {cat->
                        categoryNames.add(cat.name)
                        if(cat.name == args.editObject?.name)
                            pos = i
                        i++
                    }
                    val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categoryNames)
                    categoryList.setAdapter(adapter)
                    args.editObject?.let { obj->
                        categoryList.setSelection(pos)
                    }
                }
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

        val idCat:Int? = if(categoryList.text.toString().isNotEmpty()){
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

        val address:String? = if(addressEditText.text != null){
            addressEditText.text.toString()
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
            args.editObject?.let {
                if(imagePath != null)
                    return Obbject(it.id, name, idCat, it.objectStatus, square, address, imagePath)
                else
                    return Obbject(it.id, name, idCat, it.objectStatus, square, address, it.image)
            }
            return Obbject(0, name, idCat, ObjectStatus.FREE, square, address, imagePath)
        }
        return null
    }

    private fun chooseImage(){
        if (ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(Intent.createChooser(intent, "SELECT PHOTO"), CHOOSE_IMAGE)
        } else {
            ActivityCompat.requestPermissions(
                (activity as MainActivity),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK){
            if(data != null){
                val uri = data.data
                val mipmap = uri?.let {
                    loadImageFromUri(uri)
                }
                val file = File(RealPathUtil.getRealPath(requireContext(), uri))

                uri?.let {
                    val flag = ImageUtil.saveBitmapToFile(mipmap, file.name, requireContext())
                    Log.i("bit", flag.toString())
                    if(flag)
                        imagePath = file.name
                }

                if(mipmap != null){
                    addImageButton.setImageBitmap(mipmap)
                }
            }
        }
    }

    private fun loadImageFromUri(uri: Uri): Bitmap? {
        try {
            // Получаем AssetFileDescriptor для выбранного изображения
            val assetFileDescriptor: AssetFileDescriptor =
                context?.contentResolver?.openAssetFileDescriptor(uri, "r")!!
            if (assetFileDescriptor != null) {
                // Получаем FileDescriptor из AssetFileDescriptor
                val fileDescriptor: FileDescriptor = assetFileDescriptor.fileDescriptor

                // Декодируем изображение с помощью BitmapFactory
                // и создаем Bitmap из FileDescriptor
                Log.i("bitmap", "ok")
                return BitmapFactory.decodeFileDescriptor(fileDescriptor)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getImage(name: String, context: Context): ByteArray? {
        var fileInputStream: FileInputStream? = null
        try {
            fileInputStream = context.openFileInput(name)
            val b = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                fileInputStream?.readAllBytes()
            } else {
                fileInputStream?.readBytes()
            }
            Log.i("imggg", b.toString())
            //val file = File("img")

            //val fileOutputStream = FileOutputStream(file)
            //fileOutputStream.write(b)
            return b

        }catch (ex: IOException){
            ex.printStackTrace()
        }
        finally {
            fileInputStream?.close()
        }

        return null
    }
}