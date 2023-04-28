package com.example.tenant.ui.view

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.tenant.App
import com.example.tenant.R
import com.example.tenant.data.model.ObjectAndCategory
import com.example.tenant.data.model.ObjectStatus
import com.example.tenant.data.repository.ContractRepository
import com.example.tenant.data.repository.ExploitationRepository
import com.example.tenant.data.repository.HistoryPayRepository
import com.example.tenant.data.repository.ObjectRepository
import com.example.tenant.ioc.component.ChosenActivityComponent
import com.example.tenant.ui.model.ChosenActivityViewModel
import com.example.tenant.ui.model.ChosenActivityViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class ChosenObjectActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    lateinit var chosenActivityViewModel: ChosenActivityViewModel

    var objectAndCategory: ObjectAndCategory? = null

    lateinit var chosenActivityComponent: ChosenActivityComponent
    @Inject
    lateinit var exploitationRepository: ExploitationRepository

    @Inject
    lateinit var contractRepository: ContractRepository

    @Inject
    lateinit var objectRepository: ObjectRepository

    @Inject
    lateinit var historyPayRepository: HistoryPayRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chosen_object)
        chosenActivityComponent = (application as App).appComponent.getChosenActivityComponent()
        chosenActivityComponent.inject(this)
        val factory = ChosenActivityViewModelFactory(
            (application as App),
            exploitationRepository,
            contractRepository,
            objectRepository,
            historyPayRepository
        )

        chosenActivityViewModel = ViewModelProvider(this, factory)[ChosenActivityViewModel::class.java]
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.contract_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.closeContract -> {
                createDialog()
            }
            R.id.payContract -> {
                onPayContractListener?.onPayContract()
            }
        }
        return true
    }

    private fun createDialog(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Закрыть договор")
        alertDialog.setMessage("Вы хотите закрыть договор?")
        alertDialog.setPositiveButton("Да"){ dialogInterface: DialogInterface, i: Int ->
            onContractStatusChange?.onStatusChange()
            objectAndCategory?.let{
                chosenActivityViewModel.updateStatusObject(it.id, ObjectStatus.FREE)
            }
        }
        alertDialog.setNegativeButton("Нет"){ dialogInterface: DialogInterface, i: Int ->
            dialogInterface.cancel()
        }
        alertDialog.create().show()
    }

    interface OnContractStatusChange{
        fun onStatusChange()
    }

    private var onContractStatusChange: OnContractStatusChange? = null

    fun setOnContractStatusChange(onContractStatusChange: OnContractStatusChange){
        this.onContractStatusChange = onContractStatusChange
    }


    interface OnPayContractListener{
        fun onPayContract()
    }

    private var onPayContractListener: OnPayContractListener? = null

    fun setOnPayContractListener(onPayContractListener: OnPayContractListener){
        this.onPayContractListener = onPayContractListener
    }
}