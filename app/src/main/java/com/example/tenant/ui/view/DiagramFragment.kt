package com.example.tenant.ui.view

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.tenant.R
import com.example.tenant.ui.viewmodel.MainActivityViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate


class DiagramFragment : Fragment() {
    private lateinit var pieChart: PieChart
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var yearSpinner: Spinner
    private lateinit var categorySpinner: Spinner
    private lateinit var exploitationPieChart: PieChart

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
        exploitationPieChart = view.findViewById(R.id.exploitationPieChart)
        yearSpinner = view.findViewById(R.id.yearSpinner)
        categorySpinner = view.findViewById(R.id.categorySpinner)

        (activity as MainActivity).supportActionBar?.title = "Диаграммы"

        observeYears()
        observePieData()
        observeCategory()
        observeExploitationPieData()

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

                    mainActivityViewModel.getHistoryPayByYearAndCategory(list[pos], -1)
                    mainActivityViewModel.getExploitationsByYearAndCategory(list[pos], -1)
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

        yearSpinner.getItemAtPosition(yearSpinner.selectedItemPosition)?.let {
            val year = (it as String).toInt()
            mainActivityViewModel.getHistoryPayByYearAndCategory(year, id)
            mainActivityViewModel.getExploitationsByYearAndCategory(year, id)
        }
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

        val pieDataSet = PieDataSet(list, "")
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
        pieData.setValueFormatter(PercentFormatter(pieChart))
        pieData.isHighlightEnabled = true
        pieChart.data = pieData
        pieChart.setEntryLabelColor(Color.RED)
        pieChart.setCenterTextColor(Color.WHITE)
        pieChart.setDrawEntryLabels(false)
        pieChart.legend.textColor = Color.WHITE
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val pieEntry = (e as PieEntry)
                Log.i("value select", pieEntry.value.toString())
                val dialog = AlertDialog.Builder(context)
                    .setMessage("${pieEntry.value} рублей")
                    .setTitle(pieEntry.label)
                    .setPositiveButton("OK") { p0, p1 ->
                        p0?.cancel()
                    }
                    .create()

                dialog.show()
            }

            override fun onNothingSelected() {
            }
        })
    }

    private fun createExploitationDiagram(list: List<PieEntry>){
        exploitationPieChart.setUsePercentValues(true)
        exploitationPieChart.description.isEnabled = true
        exploitationPieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        exploitationPieChart.dragDecelerationFrictionCoef = 0.99f
        exploitationPieChart.isDrawHoleEnabled = true
        exploitationPieChart.setHoleColor(Color.BLACK)
        exploitationPieChart.transparentCircleRadius = 45f

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
        description.text = "Общая сумма расходов на эксплуатацию ${sum.toLong()}"
        description.textSize = 12f
        exploitationPieChart.description = description
        description.textColor = Color.WHITE

        exploitationPieChart.animateY(1000, Easing.EaseInOutCubic)

        val pieDataSet = PieDataSet(list, "")
        pieDataSet.sliceSpace = 3f
        pieDataSet.selectionShift = 5f
        ColorTemplate.JOYFUL_COLORS?.let {
            pieDataSet.colors = it.toList()
        }

        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(12f)
        pieData.setValueTextColor(Color.BLACK)
        pieData.setValueFormatter(PercentFormatter(exploitationPieChart))
        exploitationPieChart.data = pieData
        exploitationPieChart.setEntryLabelColor(Color.RED)
        exploitationPieChart.setCenterTextColor(Color.WHITE)
        exploitationPieChart.setDrawEntryLabels(false)
        exploitationPieChart.legend.textColor = Color.WHITE
        exploitationPieChart.legend.isWordWrapEnabled = true
        exploitationPieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        exploitationPieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val pieEntry = (e as PieEntry)
                Log.i("value select", pieEntry.value.toString())
                val dialog = AlertDialog.Builder(context)
                    .setMessage("${pieEntry.value} рублей")
                    .setTitle(pieEntry.label)
                    .setPositiveButton("OK") { p0, p1 ->
                        p0?.cancel()
                    }
                    .create()

                dialog.show()
            }

            override fun onNothingSelected() {
            }
        })
    }

    private fun observePieData(){
        mainActivityViewModel.pieData.observe(viewLifecycleOwner, Observer {
            it?.let {list ->
                createDiagram(list)
            }
        })
    }

    private fun observeExploitationPieData(){
        mainActivityViewModel.exploitationsPieData.observe(viewLifecycleOwner, Observer {
            it?.let { list->
                createExploitationDiagram(list)
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