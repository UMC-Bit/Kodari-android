package com.bit.kodari

import android.R.attr
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.databinding.FragmentHomeBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.R.color

import android.R.attr.data

class HomeFragment : Fragment() {

    lateinit var binding:FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater , container , false)
        setChartDummy()
        return binding.root
    }

    //차트에 더미데이터 셋팅하고 차트 보여주는 함수
    fun setChartDummy(){
        binding.homeChartLc.getDescription().setEnabled(false);
        // enable touch gestures
        binding.homeChartLc.setTouchEnabled(true);

        // enable scaling and dragging
        binding.homeChartLc.setDragEnabled(true);
        binding.homeChartLc.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        binding.homeChartLc.setPinchZoom(false);

        binding.homeChartLc.setBackgroundColor(Color.rgb(89,199,250));

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        binding.homeChartLc.setViewPortOffsets(10f, 0f, 10f, 0f);
        binding.homeChartLc.data = setChartDummyData()

        binding.homeChartLc.getAxisLeft().setEnabled(false);
        binding.homeChartLc.getAxisLeft().setSpaceTop(40f);
        binding.homeChartLc.getAxisLeft().setSpaceBottom(40f);
        binding.homeChartLc.getAxisRight().setEnabled(false);

        binding.homeChartLc.getXAxis().setEnabled(false);

    }

    //차트에 더미 데이터 셋팅팅
    fun setChartDummyData() : LineData{
        val values: ArrayList<Entry> = ArrayList()

        for (i in 0 until 12) {
            val tempVal = (Math.random() * 100).toFloat() + 3
            values.add(Entry(i.toFloat(),tempVal))
        }

        val set1 = LineDataSet(values, "DataSet 1")
        set1.setFillAlpha(110);
        set1.setFillColor(Color.RED);
        set1.lineWidth = 1.75f
        set1.circleRadius = 5f
        set1.circleHoleRadius = 2.5f
        set1.color = Color.WHITE
        set1.setCircleColor(Color.WHITE)
        set1.highLightColor = Color.WHITE
        set1.setDrawValues(false)

        return LineData(set1)
    }



}