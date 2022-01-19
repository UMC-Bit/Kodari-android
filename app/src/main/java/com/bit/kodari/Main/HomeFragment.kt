package com.bit.kodari.Main

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

import androidx.appcompat.content.res.AppCompatResources
import com.bit.kodari.R

class HomeFragment : Fragment() {

    lateinit var binding:FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater , container , false)
        setChartDummy()
        setListener()
        return binding.root
    }
    //처음 화면이 켜져있을 때 셋팅되어야하는 것들 -> 데이터 불러오기 or 일/주/월, 등 선택
    fun initSetting(){

    }
    //초기 클릭 리스너 등 모든 리스너를 정의하는 함수
    fun setListener(){
        //소득 클릭시 변경
        binding.homeIncomeTv.setOnClickListener {
            binding.homeIncomeTv.background = AppCompatResources.getDrawable(context as MainActivity,
                R.drawable.btn_outline_yellow
            )
            binding.homeYieldTv.background = AppCompatResources.getDrawable(context as MainActivity,
                R.drawable.btn_outline_white
            )
        }
        //수익률 클릭시 변경
        binding.homeYieldTv.setOnClickListener {
            binding.homeYieldTv.background = AppCompatResources.getDrawable(context as MainActivity,
                R.drawable.btn_outline_yellow
            )
            binding.homeIncomeTv.background = AppCompatResources.getDrawable(context as MainActivity,
                R.drawable.btn_outline_white
            )
        }

        //일 클릭시 변경
        binding.homeDayTv.setOnClickListener {
            binding.homeDayTv.background = AppCompatResources.getDrawable(context as MainActivity,
                R.drawable.btn_outline_yellow
            )
            binding.homeWeekTv.background = AppCompatResources.getDrawable(context as MainActivity,
                R.drawable.btn_outline_white
            )
            binding.homeMonthTv.background = AppCompatResources.getDrawable(context as MainActivity,
                R.drawable.btn_outline_white
            )
        }

        binding.homeWeekTv.setOnClickListener {
            binding.homeDayTv.background = AppCompatResources.getDrawable(context as MainActivity,
                R.drawable.btn_outline_white
            )
            binding.homeWeekTv.background = AppCompatResources.getDrawable(context as MainActivity,
                R.drawable.btn_outline_yellow
            )
            binding.homeMonthTv.background = AppCompatResources.getDrawable(context as MainActivity,
                R.drawable.btn_outline_white
            )
        }

        binding.homeMonthTv.setOnClickListener {
            binding.homeDayTv.background = AppCompatResources.getDrawable(context as MainActivity,
                R.drawable.btn_outline_white
            )
            binding.homeWeekTv.background = AppCompatResources.getDrawable(context as MainActivity,
                R.drawable.btn_outline_white
            )
            binding.homeMonthTv.background = AppCompatResources.getDrawable(context as MainActivity,
                R.drawable.btn_outline_yellow
            )
        }
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