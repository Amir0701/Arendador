package com.example.tenant.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.tenant.R
import com.example.tenant.data.model.Contract
import com.example.tenant.data.model.ContractStatus
import com.example.tenant.data.model.ContractWithTenant
import com.example.tenant.data.model.PayTime
import com.example.tenant.ui.model.ChosenActivityViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class ArendaObjectFragment : Fragment() {
    private lateinit var addContractButton: FloatingActionButton
    private lateinit var nameTextView: TextView
    private lateinit var phoneNumberTextView: TextView
    private lateinit var dateOfConclusionTextView: TextView
    private lateinit var dateOfEndConclusionTextView: TextView
    private lateinit var sumTextView: TextView
    private lateinit var payTimeTextView: TextView
    private lateinit var zalogTextView: TextView
    private lateinit var viewModel: ChosenActivityViewModel

    private var contractWithTenant: ContractWithTenant? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_arenda_object, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addContractButton = view.findViewById(R.id.addContractButton)
        initViews(view)

        addContractButton.setOnClickListener {
            val intent = Intent(activity, NewContractActivity::class.java)
            (activity as ChosenObjectActivity).objectAndCategory?.let {
                intent.putExtra("object", it)
                intent.putExtra("con_with", contractWithTenant)
            }
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel = (activity as ChosenObjectActivity).chosenActivityViewModel
        observeContractWithTenant()
        (activity as ChosenObjectActivity).objectAndCategory?.id?.let {
            viewModel.getContractWithTenantByObjectId(
                it
            )
        }

        setContractStatusChangeListener()
    }

    private fun initViews(view: View){
        nameTextView = view.findViewById(R.id.nameInputTextView)
        phoneNumberTextView = view.findViewById(R.id.phoneInputTypeInputTextView)
        dateOfConclusionTextView = view.findViewById(R.id.dateOfConclusionInputTextView)
        dateOfEndConclusionTextView = view.findViewById(R.id.dateOfEndConclusionInputTextView)
        sumTextView = view.findViewById(R.id.sumInputTextView)
        payTimeTextView = view.findViewById(R.id.payTimeInputTextView)
        zalogTextView = view.findViewById(R.id.zalogInputTextView)
    }

    private fun observeContractWithTenant(){
        viewModel.contractWithTenant.observe(viewLifecycleOwner, Observer {con->
            con?.let {list ->
                if(list.isNotEmpty()){
                    list[list.size - 1].let {
                        if(it.status == ContractStatus.ACTIVE){
                            contractWithTenant = it
                            nameTextView.text = "${it.firstName} ${it.lastName}"
                            phoneNumberTextView.text = it.phoneNumber
                            dateOfConclusionTextView.text = reformat(it.dateOfContract)
                            dateOfEndConclusionTextView.text = reformat(it.dateOfEnd)
                            sumTextView.text = it.sum.toString()
                            payTimeTextView.text = payTimeReformat(it.timeOfPay)
                            zalogTextView.text = (it.zalog ?: "нет").toString()
                            addContractButton.setImageResource(R.drawable.ic_edit)
                        }
                    }
                }
            }
        })
    }


    private fun reformat(date: Date): String{
        val format = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        return simpleDateFormat.format(date)
    }

    private fun payTimeReformat(payTime: PayTime): String{
        return when(payTime){
            PayTime.DAY -> "Каждый день"
            PayTime.MONTH -> "Раз в месяц"
            PayTime.HALF_MONTH -> "Два раза в месяц"
        }
    }

    private fun setContractStatusChangeListener(){
        (activity as ChosenObjectActivity).setOnContractStatusChange(object : ChosenObjectActivity.OnContractStatusChange{
            override fun onStatusChange() {
                contractWithTenant?.let {
                    val contract = Contract(
                        it.id,
                        it.objectId,
                        it.tenantId,
                        it.sum,
                        it.dateOfContract,
                        it.dateOfEnd,
                        it.timeOfPay,
                        it.zalog,
                        ContractStatus.NO_ACTIVE
                    )

                    viewModel.upsertContract(contract)
                }
            }
        })
    }
}