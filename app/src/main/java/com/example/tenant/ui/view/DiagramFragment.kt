package com.example.tenant.ui.view

import android.R.attr.data
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.tenant.R
import com.example.tenant.ui.model.MainActivityViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate


class DiagramFragment : Fragment() {
    private lateinit var pieChart: PieChart
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var yearSpinner: Spinner
    private lateinit var categorySpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diagram, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivityViewModel = (activity as MainActivity).mainActivityViewModel
        pieChart = view.findViewById(R.id.piechart)
        yearSpinner = view.findViewById(R.id.yearSpinner)
        categorySpinner = view.findViewById(R.id.categorySpinner)
        observeYears()
        observePieData()
        observeCategory()
        setYearSpinnerClickListener()
        setCategorySpinnerClickListener()
        mainActivityViewModel.getDistinctYears()
        mainActivityViewModel.getAllCategories()
    }

    private fun observeYears(){
        mainActivityViewModel.years.observe(viewLifecycleOwner, Observer {
            it?.let {list->
                if(list.isNotEmpty()){
                    val listString = mutableListOf<String>()
                    for (el in list)
                        listString.add(el.toString())

                    val adapter: ArrayAdapter<String> =
                        ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, listString)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    yearSpinner.adapter = adapter

                    var pos = -1
                    var max = 0
                    for((i, year) in list.withIndex()){
                        if(year > max) {
                            pos = i
                            max = year
                        }
                    }

                    yearSpinner.setSelection(pos)

                    mainActivityViewModel.getHistoryPayByYear(list[pos], -1)
                }
            }
        })
    }

    private fun setYearSpinnerClickListener(){
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                onSpinnerItemSelected()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun setCategorySpinnerClickListener(){
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                onSpinnerItemSelected()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun onSpinnerItemSelected(){
        val position = categorySpinner.selectedItemPosition
        var id = -1
        if(mainActivityViewModel.categoryLiveData.value?.size != position){
            id = position + 1
        }
        val year = (yearSpinner.getItemAtPosition(yearSpinner.selectedItemPosition) as String).toInt()
        mainActivityViewModel.getHistoryPayByYear(year, id)
    }

    private fun createDiagram(list: List<PieEntry>){
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = true
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart.dragDecelerationFrictionCoef = 0.99f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.BLACK)
        pieChart.transparentCircleRadius = 45f

//        val values = mutableListOf<PieEntry>()
//        values.add(PieEntry(200000f, "Двухкомнатная квартира на Пирогова"))
//        values.add(PieEntry(250000f, "Дача"))
//        values.add(PieEntry(500000f, "Трехкомнатная квартира на Пушкина"))
//        values.add(PieEntry(302000f, "Трехкомнатная квартира на Лещина"))

        var sum = 0f
        list.forEach { pieEntry ->
            sum += pieEntry.value
        }
        val description = Description()
        description.text = "Общая сумма доходов ${sum.toLong()}"
        description.textSize = 12f
        pieChart.description = description
        description.textColor = Color.WHITE

        pieChart.animateY(1000, Easing.EaseInOutCubic)

        val pieDataSet = PieDataSet(list, "Объекты")
        pieDataSet.sliceSpace = 3f
        pieDataSet.selectionShift = 5f
        //pieDataSet.colors = ColorTemplate.JOYFUL_COLORS
        //pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS)
        //pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS, context)
        ColorTemplate.JOYFUL_COLORS?.let {
            pieDataSet.colors = it.toList()
        }

        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(12f)
        pieData.setValueTextColor(Color.BLACK)
        //pieData.setValueFormatter()
        pieChart.data = pieData
        pieChart.setEntryLabelColor(Color.RED)
        pieChart.setCenterTextColor(Color.WHITE)
        pieChart.setDrawEntryLabels(false)
    }

    private fun observePieData(){
        mainActivityViewModel.pieData.observe(viewLifecycleOwner, Observer {
            it?.let {list ->
                createDiagram(list)
            }
        })
    }

    private fun observeCategory(){
        mainActivityViewModel.categoryLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                val listString = mutableListOf<String>()
                for (elem in it)
                    listString.add(elem.name)
                listString.add("Все")
                val adapter: ArrayAdapter<String> =
                    ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, listString)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                categorySpinner.adapter = adapter

                categorySpinner.setSelection(listString.size - 1)
            }
        })
    }
}