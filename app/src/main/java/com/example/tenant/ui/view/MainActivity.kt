package com.example.tenant.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.tenant.App
import com.example.tenant.R
import com.example.tenant.data.repository.CategoryRepository
import com.example.tenant.data.repository.ObjectRepository
import com.example.tenant.ui.model.MainActivityViewModel
import com.example.tenant.ui.model.MainActivityViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity: AppCompatActivity(){
    lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Objects"
        val factory = MainActivityViewModelFactory(application, ObjectRepository((application as App).db.getDao()), CategoryRepository((application as App).db.getDao()))
        mainActivityViewModel = ViewModelProvider(this, factory)[MainActivityViewModel::class.java]
    }
}