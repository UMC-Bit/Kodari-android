package com.bit.kodari.Main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.databinding.FragmentHomeBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Login.LoginActivity
import com.bit.kodari.Main.Adapter.HomePCRVAdapter
import com.bit.kodari.Main.Adapter.HomeRCRVAdapter
import com.bit.kodari.Main.Adapter.HomeVPAdapter
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.Main.Data.RepresentCoinResult
import com.bit.kodari.PossessionCoin.PossessionCoinManagementFragment
import com.bit.kodari.R
import com.bit.kodari.Util.*

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    lateinit var homeVPAdapter: HomeVPAdapter
    lateinit var homeRCRVAdapter: HomeRCRVAdapter
    lateinit var homePCRVAdapter: HomePCRVAdapter
    var portfolioList = ArrayList<Fragment>()
    var representList = ArrayList<RepresentCoinResult>()
    var possessionList = ArrayList<PossesionCoinResult>()

    //BaseFragment에서 onStart에서 실행시켜줌
    override fun initAfterBinding() {
        setChartDummy()
        setListener()
        setViewpager()
        setRepresentRV()
        setRepresentPV()
        Log.d("info" , "jwt : ${getJwt()} , email : ${getEmail()} , pw : ${getPw()} , userIdx: ${getUserIdx()}")
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
                .replace(R.id.main_container_fl , RepresentativeCoinManagementFragment())
                .addToBackStack(null).commitAllowingStateLoss()

        }

        binding.homeMyNextBtnIb.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinManagementFragment())
                .commitNowAllowingStateLoss()
        }

        //화살표 관련 리스너
        binding.homeVpPreviewBtn.setOnClickListener {
            val current = binding.homeViewpagerVp.currentItem           //현재 뷰페이저 위치 받아오기
            binding.homeViewpagerVp.setCurrentItem(current-1,false) //위치 -1 페이지로 이동 -> 이동후 onPage~ 리스너로 인디케이터 변화
        }

        binding.homeVpNextBtn.setOnClickListener {
            val current = binding.homeViewpagerVp.currentItem
            binding.homeViewpagerVp.setCurrentItem(current+1,false)
        }

        //임시 로그아웃 버튼
        binding.logout.setOnClickListener {
            saveLoginInfo(null,null,null,0)     //0이면 유저 없는거
            saveAutoLogin(false)
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

    }
    //뷰 페이저 셋팅 -> 리스트에 더미데이터 넣어놓은 상태
    //API 호출 이후 실행
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

    fun setRepresentRV(){
        representList.add(RepresentCoinResult(1,"null","test1,",2,3,"active","AES"))
        representList.add(RepresentCoinResult(1,"null","test2,",2,3,"active","ATE"))
        representList.add(RepresentCoinResult(1,"null","test3,",2,3,"active","TES"))
        homeRCRVAdapter = HomeRCRVAdapter(representList)
        binding.homeRepresentCoinRv.layoutManager =  LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL,false)
        binding.homeRepresentCoinRv.adapter = homeRCRVAdapter

    }

    fun setRepresentPV(){
        possessionList.add(PossesionCoinResult(1,"test1",5000,"active",3))
        possessionList.add(PossesionCoinResult(1,"test2",10000000,"active",3))
        possessionList.add(PossesionCoinResult(1,"test3",42,"active",2))
        homePCRVAdapter = HomePCRVAdapter(possessionList)
        binding.homeMyCoinRv.layoutManager =  LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL,false)
        binding.homeMyCoinRv.adapter = homePCRVAdapter

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