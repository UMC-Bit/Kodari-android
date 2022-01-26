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
import androidx.viewpager2.widget.ViewPager2
import com.bit.kodari.Main.Adapter.HomeVPAdapter
import com.bit.kodari.PossessionCoin.PossessionCoinManagementFragment
import com.bit.kodari.R

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var homeVPAdapter: HomeVPAdapter
    var portfolioList = ArrayList<Fragment>()

    //프래그먼트는 다시 돌아오면 onCreateView로 실행
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setChartDummy()
        setListener()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initSetting()
    }

    fun initSetting() {
        setViewpager()
    }


    //초기 클릭 리스너 등 모든 리스너를 정의하는 함수
    fun setListener() {
        //소득 클릭시 변경
        binding.homeIncomeTv.setOnClickListener {
            binding.homeIncomeTv.background = AppCompatResources.getDrawable(
                context as MainActivity,
                R.drawable.btn_outline_yellow
            )
            binding.homeYieldTv.background = AppCompatResources.getDrawable(
                context as MainActivity,
                R.drawable.btn_outline_white
            )
        }
        //수익률 클릭시 변경
        binding.homeYieldTv.setOnClickListener {
            binding.homeYieldTv.background = AppCompatResources.getDrawable(
                context as MainActivity,
                R.drawable.btn_outline_yellow
            )
            binding.homeIncomeTv.background = AppCompatResources.getDrawable(
                context as MainActivity,
                R.drawable.btn_outline_white
            )
        }

        //일 클릭시 변경
        binding.homeDayTv.setOnClickListener {
            binding.homeDayTv.background = AppCompatResources.getDrawable(
                context as MainActivity,
                R.drawable.btn_outline_yellow
            )
            binding.homeWeekTv.background = AppCompatResources.getDrawable(
                context as MainActivity,
                R.drawable.btn_outline_white
            )
            binding.homeMonthTv.background = AppCompatResources.getDrawable(
                context as MainActivity,
                R.drawable.btn_outline_white
            )
        }

        binding.homeWeekTv.setOnClickListener {
            binding.homeDayTv.background = AppCompatResources.getDrawable(
                context as MainActivity,
                R.drawable.btn_outline_white
            )
            binding.homeWeekTv.background = AppCompatResources.getDrawable(
                context as MainActivity,
                R.drawable.btn_outline_yellow
            )
            binding.homeMonthTv.background = AppCompatResources.getDrawable(
                context as MainActivity,
                R.drawable.btn_outline_white
            )
        }

        binding.homeMonthTv.setOnClickListener {
            binding.homeDayTv.background = AppCompatResources.getDrawable(
                context as MainActivity,
                R.drawable.btn_outline_white
            )
            binding.homeWeekTv.background = AppCompatResources.getDrawable(
                context as MainActivity,
                R.drawable.btn_outline_white
            )
            binding.homeMonthTv.background = AppCompatResources.getDrawable(
                context as MainActivity,
                R.drawable.btn_outline_yellow
            )
        }

        binding.homeNextBtnIb.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, RepresentativeCoinMainFragment())
                .commitAllowingStateLoss()
        }

        binding.homeMyNextBtnIb.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinManagementFragment())
                .commitNowAllowingStateLoss()
        }

        binding.homeVpPreviewBtn.setOnClickListener {
            val current = binding.homeViewpagerVp.currentItem           //현재 뷰페이저 위치 받아오기
            binding.homeViewpagerVp.setCurrentItem(current-1,false) //위치 -1 페이지로 이동 -> 이동후 onPage~ 리스너로 인디케이터 변화
        }

        binding.homeVpNextBtn.setOnClickListener {
            val current = binding.homeViewpagerVp.currentItem
            binding.homeViewpagerVp.setCurrentItem(current+1,false)
        }

    }

    fun setViewpager() {
        portfolioList.add(MyPortfolioFragment())
        portfolioList.add(MyPortfolioFragment())
        portfolioList.add(MyPortfolioFragment())
        homeVPAdapter = HomeVPAdapter(this, portfolioList)
        //homeVPAdapter.addFragment(MyPortfolioFragment())
        binding.homeViewpagerVp.adapter = homeVPAdapter
        binding.homeViewpagerVp.offscreenPageLimit = 3

        //homeVPAdapter.addFragment(MyPortfolioFragment())          //왜 처음 셋팅떄는 되는데 그 뒤론 안될까 ?

        binding.myRecordIndicators.setViewPager(binding.homeViewpagerVp)
        binding.myRecordIndicators.createIndicators(homeVPAdapter.itemCount, 0)

        binding.homeViewpagerVp.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        binding.homeVpPreviewBtn.visibility = View.GONE
                    }
                    portfolioList.size - 1 -> {
                        binding.homeVpNextBtn.visibility = View.GONE
                    }
                    else -> {
                        binding.homeVpNextBtn.visibility =View.VISIBLE
                        binding.homeVpPreviewBtn.visibility = View.VISIBLE
                    }
                }
                binding.myRecordIndicators.animatePageSelected(position)
            }
        })
    }


    //차트에 더미데이터 셋팅하고 차트 보여주는 함수
    fun setChartDummy() {
        binding.homeChartLc.getDescription().setEnabled(false);
        // enable touch gestures
        binding.homeChartLc.setTouchEnabled(true);

        // enable scaling and dragging
        binding.homeChartLc.setDragEnabled(true);
        binding.homeChartLc.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        binding.homeChartLc.setPinchZoom(false);

        binding.homeChartLc.setBackgroundColor(Color.rgb(89, 199, 250));

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
    fun setChartDummyData(): LineData {
        val values: ArrayList<Entry> = ArrayList()

        for (i in 0 until 12) {
            val tempVal = (Math.random() * 100).toFloat() + 3
            values.add(Entry(i.toFloat(), tempVal))
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