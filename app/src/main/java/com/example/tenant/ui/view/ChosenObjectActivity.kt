package com.example.tenant.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.example.tenant.R
import com.example.tenant.data.model.ObjectAndCategory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ChosenObjectActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    var objectAndCategory: ObjectAndCategory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chosen_object)
        val bundle = intent.getBundleExtra("intentObject")
        objectAndCategory = bundle?.let {
            it.getSerializable("object") as ObjectAndCategory?
        }
        Log.i("obj", objectAndCategory?.name.toString())
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        initTabLayout()
        actionBar?.title = "Chosen Object"
    }

    private fun initTabLayout(){
        viewPager.adapter = Adapter(this)
        TabLayoutMediator(tabLayout, viewPager){tab, pos ->
            val title = when(pos){
                0 -> "Аренда"
                else -> "Экспулатация"
            }
            tab.text = title
        }.attach()
    }
}