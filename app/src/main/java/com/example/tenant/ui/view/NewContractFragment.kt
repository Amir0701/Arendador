package com.example.tenant.ui.view

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.example.tenant.R
import com.example.tenant.data.model.*
import com.example.tenant.ui.model.NewContractActivityViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*


class NewContractFragment : Fragment() {
    private val args: NewContractFragmentArgs by navArgs()

    private lateinit var dateOfConclusion: EditText;
    private lateinit var dateOfEnd: EditText
    private val startCalendar = Calendar.getInstance()
    private val endCalendar = Calendar.getInstance()
    private lateinit var nextButton: Button
    private lateinit var sumEditText: EditText
    private lateinit var dateOfConclusionInputLayout: TextInputLayout
    private lateinit var endOfEndConclusionInputLayout: TextInputLayout
    private lateinit var sumInputLayout: TextInputLayout
    private lateinit var zalogEditTextInputLayout: TextInputLayout
    private lateinit var zalogEditText: TextInputEditText
    private lateinit var timeToPayLayout: TextInputLayout
    private lateinit var timeToPay: AutoCompleteTextView

    private lateinit var viewModel: NewContractActivityViewModel

    private lateinit var obbject: Obbject
    private var contractWithTenant: ContractWithTenant? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_contract, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewContractActivity).newContractActivityViewModel

        initView(view)
        setUpTimeToPayList()
        setDateOfConclusionListener()
        setDateOfEndListener()

        observeObject()
        viewModel.getObjectById((activity as NewContractActivity).objectAndCategory?.id!!)
        observeContract()

        nextButton.setOnClickListener {
            getData()?.let {
                observeTenant(it)
                viewModel.addTenant(args.ten)
            }
        }

        setContractData()
    }

    private fun initView(view: View){
        dateOfConclusion = view.findViewById(R.id.dateOfConclusionOfAnAgreement)
        dateOfEnd = view.findViewById(R.id.dateOfEndConclusion)
        nextButton = view.findViewById(R.id.nextButton)
        sumEditText = view.findViewById(R.id.sum)
        dateOfConclusionInputLayout = view.findViewById(R.id.dateOfConclusionOfAnAgreementLayout)
        endOfEndConclusionInputLayout = view.findViewById(R.id.dateOfEndConclusionLayout)
        sumInputLayout = view.findViewById(R.id.sumLayout)
        zalogEditTextInputLayout = view.findViewById(R.id.zalogInputLayout)
        zalogEditText = view.findViewById(R.id.zalogEditText)
        timeToPayLayout = view.findViewById(R.id.timeToPayInputLayout)
        timeToPay = view.findViewById(R.id.timeToPayList)
    }

    private fun setContractData(){
        contractWithTenant = (activity as NewContractActivity).contractWithTenant

        contractWithTenant?.let {
            startCalendar.time = it.dateOfContract
            updateDateOfConclusion()
            endCalendar.time = it.dateOfEnd
            updateDateOfEnd()
            sumEditText.setText(it.sum.toString())
            it.zalog?.let{z->
                zalogEditText.setText(z.toString())
            }
            nextButton.text = "Изменить"
        }

    }

    private fun setUpTimeToPayList(){
        R.array.pay_time_array
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.pay_time_array, R.layout.dropdown_item)
        timeToPay.setAdapter(adapter)
    }

    private fun setDateOfConclusionListener(){
        val datePickerDialogListener = DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
            startCalendar.set(Calendar.YEAR, i)
            startCalendar.set(Calendar.MONTH, i2)
            startCalendar.set(Calendar.DAY_OF_MONTH, i3)
            updateDateOfConclusion()
        }

        dateOfConclusion.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                datePickerDialogListener,
                startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH),
                startCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateOfConclusion(){
        val format = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        dateOfConclusion.setText(simpleDateFormat.format(startCalendar.time))
    }

    private fun setDateOfEndListener(){
        val datePickerDialogListener = DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
            endCalendar.set(Calendar.YEAR, i)
            endCalendar.set(Calendar.MONTH, i2)
            endCalendar.set(Calendar.DAY_OF_MONTH, i3)
            updateDateOfEnd()
        }

        dateOfEnd.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                datePickerDialogListener,
                endCalendar.get(Calendar.YEAR),
                endCalendar.get(Calendar.MONTH),
                endCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateOfEnd(){
        val format = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        dateOfEnd.setText(simpleDateFormat.format(endCalendar.time))
    }


    private fun getData(): Contract?{
        val startDate:Date? = if(dateOfConclusion.text.toString().isNotEmpty()){
            dateOfConclusionInputLayout.helperText = ""
            startCalendar.time
        }
        else{
            dateOfConclusionInputLayout.helperText = requireContext().resources.getString(R.string.date_fill_in)
            null
        }

        val endDate: Date? = if(dateOfEnd.text.toString().isNotEmpty()){
            endOfEndConclusionInputLayout.helperText = ""
            endCalendar.time
        }
        else{
            endOfEndConclusionInputLayout.helperText = requireContext().resources.getString(R.string.date_fill_in)
            null
        }

        val sum: Int? = if(sumEditText.text.toString().isNotEmpty()){
            sumInputLayout.helperText = ""
            sumEditText.text.toString().toInt()
        }
        else{
            sumInputLayout.helperText = requireContext().resources.getString(R.string.fillIn)
            null
        }

        val zalog: Int? = if(zalogEditText.text.toString().isNotEmpty()){
            zalogEditText.text.toString().toInt()
        }
        else{
            null
        }

        val payTime: PayTime? = if(timeToPay.text.toString().isNotEmpty()){
            val pay = when(timeToPay.text.toString()){
                "Каждый день" -> PayTime.DAY
                "Раз в месяц" -> PayTime.MONTH
                else -> PayTime.HALF_MONTH
            }
            pay

        }else{
            timeToPayLayout.helperText = "Выберите время оплаты"
            null
        }

        if(startDate != null && endDate != null && sum != null && payTime != null){
            val id = contractWithTenant?.id ?: 0
            return Contract(id, 0, 0, sum, startDate, endDate, payTime, zalog, ContractStatus.ACTIVE)
        }

        return null
    }

    private fun observeTenant(contract: Contract){
        viewModel.tenantIdLiveDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            it?.let {id->
                contract.tenantId = id.toInt()
                contract.objectId = (activity as NewContractActivity).objectAndCategory?.id!!
                viewModel.addContract(contract)
            }
        })
    }

    private fun observeContract(){
        viewModel.contractIdLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {id->
                obbject.objectStatus = ObjectStatus.IN_TENANT
                viewModel.updateObject(obbject)
                (activity as NewContractActivity).finish()
            }
        })
    }

    private fun observeObject(){
        viewModel.objectLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {obj->
                obbject = obj
            }
        })
    }
}