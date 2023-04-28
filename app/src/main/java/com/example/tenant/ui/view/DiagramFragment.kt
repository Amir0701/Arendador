package com.example.tenant.ui.view

import android.R.attr.data
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
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
        createDiagram()
        observeYears()

        mainActivityViewModel.getDistinctYears()
    }

    private fun observeYears(){
        mainActivityViewModel.years.observe(viewLifecycleOwner, Observer {
            it?.let {list->
                if(list.isNotEmpty()){
                    val adapter: ArrayAdapter<Int> =
                        ArrayAdapter<Int>(requireContext(), android.R.layout.simple_spinner_item, list)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    yearSpinner.adapter = adapter

                    var i = 0
                    var pos = -1
                    var max = 0
                    list.forEach {year->
                        if(year > max) {
                            pos = i
                            max = year
                        }
                        i++
                    }

                    yearSpinner.setSelection(pos)
                }
            }
        })
    }

    private fun createDiagram(){
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = true
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart.dragDecelerationFrictionCoef = 0.99f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.BLACK)
        pieChart.transparentCircleRadius = 45f

        val values = mutableListOf<PieEntry>()
        values.add(PieEntry(200000f, "Двухкомнатная квартира на Пирогова"))
        values.add(PieEntry(250000f, "Дача"))
        values.add(PieEntry(500000f, "Трехкомнатная квартира на Пушкина"))
        values.add(PieEntry(302000f, "Трехкомнатная квартира на Лещина"))
        val description = Description()
        description.text = "Общая сумма доходов 950000"
        description.textSize = 12f
        pieChart.description = description
        description.textColor = Color.WHITE

        pieChart.animateY(1000, Easing.EaseInOutCubic)

        val pieDataSet = PieDataSet(values, "Объекты")
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
}