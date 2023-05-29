package com.example.tenant.ui.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.tenant.R
import com.example.tenant.data.model.ContractWithTenant
import com.example.tenant.data.model.Tenant
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class ArendatorFragment : Fragment() {
    private lateinit var dateEditText: EditText
    private lateinit var addUserButton: Button
    private val calendar = Calendar.getInstance()
    private lateinit var nameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var passportEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var passportInputLayout: TextInputLayout
    private lateinit var lastInputLayout: TextInputLayout
    private lateinit var phoneNumberInputLayout: TextInputLayout
    private var contractWithTenant: ContractWithTenant? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_arendator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setDateOnClickListener()
        addUserButton.setOnClickListener {
            getData()?.let {ten->
                val bundle = Bundle()
                bundle.putSerializable("ten", ten)
                findNavController().navigate(R.id.action_arendatorFragment_to_newContractFragment, bundle)
            }
        }

        (activity as NewContractActivity).supportActionBar?.title = "Данные арендатора"
        setTenantData()
    }

    private fun initViews(view: View){
        dateEditText = view.findViewById(R.id.dateEditText)
        addUserButton = view.findViewById(R.id.addUserButton)
        nameEditText = view.findViewById(R.id.nameEditText)
        lastNameEditText = view.findViewById(R.id.lastNameEditText)
        passportEditText = view.findViewById(R.id.passportNumberEditText)
        phoneNumberEditText = view.findViewById(R.id.phoneNumberEditText)
        nameInputLayout = view.findViewById(R.id.nameEditTextLayout)
        passportInputLayout = view.findViewById(R.id.passportNumberEditTextLayout)
        lastInputLayout = view.findViewById(R.id.lastNameEditTextLayout)
        phoneNumberInputLayout = view.findViewById(R.id.phoneNumberEditTextLayout)
    }

    private fun setTenantData(){
        contractWithTenant = (activity as NewContractActivity).contractWithTenant

        contractWithTenant?.let {
            it.dateOfBirth?.let { date->
                calendar.time = date
                updateLabel()
            }

            nameEditText.setText(it.firstName)
            lastNameEditText.setText(it.lastName)
            passportEditText.setText(it.passportNumber)
            phoneNumberEditText.setText(it.phoneNumber)
        }
    }

    private fun setDateOnClickListener(){
        val datePickerDialogListener = DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
            calendar.set(Calendar.YEAR, i)
            calendar.set(Calendar.MONTH, i2)
            calendar.set(Calendar.DAY_OF_MONTH, i3)
            updateLabel()
        }

        dateEditText.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                datePickerDialogListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateLabel(){
        val format = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        dateEditText.setText(simpleDateFormat.format(calendar.time))
    }

    private fun getData(): Tenant?{
        val name = if(nameEditText.text.toString().trim().isNotEmpty()){
            nameInputLayout.helperText = ""
            nameEditText.text.toString()
        } else {
            nameInputLayout.helperText = requireContext().resources.getString(R.string.fillIn)
            ""
        }

        val lastName = if(lastNameEditText.text.toString().trim().isNotEmpty()){
            lastInputLayout.helperText = ""
            lastNameEditText.text.toString()
        }
        else{
            lastInputLayout.helperText = requireContext().resources.getString(R.string.fillIn)
            ""
        }

        val date: Date? = if(dateEditText.text.toString().isNotEmpty()){
            calendar.time
        }
        else {
            null
        }


        val passport = if(passportEditText.text.toString().trim().isNotEmpty()){
            passportInputLayout.helperText = ""
            passportEditText.text.toString()
        }
        else{
            passportInputLayout.helperText = requireContext().resources.getString(R.string.fillIn)
            ""
        }

        val phoneNumber = if(phoneNumberEditText.text.toString().trim().isNotEmpty()){
            phoneNumberInputLayout.helperText = ""
            phoneNumberEditText.text.toString()
        }
        else{
            phoneNumberInputLayout.helperText = resources.getString(R.string.fillIn)
            ""
        }

        if(phoneNumber.isNotEmpty() && passport.isNotEmpty() && lastName.isNotEmpty() && name.isNotEmpty()){
            val id = contractWithTenant?.id ?: 0
            return Tenant(id, name, lastName, date, passport, phoneNumber)
        }

        return null
    }
}