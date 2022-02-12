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
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.MyApplicationClass
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Debate.DialogCoin
import com.bit.kodari.Login.LoginActivity
import com.bit.kodari.Main.Adapter.HomePCRVAdapter
import com.bit.kodari.Main.Adapter.HomeRCRVAdapter
import com.bit.kodari.Main.Adapter.HomeVPAdapter
import com.bit.kodari.Main.Data.*
import com.bit.kodari.Main.RetrofitInterface.HomeView
import com.bit.kodari.Main.Service.HomeService
import com.bit.kodari.Portfolio.Retrofit.PortfolioInterface
import com.bit.kodari.Portfolio.Retrofit.PortfolioView
import com.bit.kodari.Portfolio.Service.PortfolioService
import com.bit.kodari.PossessionCoin.DialogMemoAndTwitter
import com.bit.kodari.PossessionCoin.PossessionCoinManagementFragment
import com.bit.kodari.R
import com.bit.kodari.RepresentativeCoin.RepresentativeCoinManagementFragment
import com.bit.kodari.Util.*
import com.bit.kodari.Util.Binance.BinanceService
import com.bit.kodari.Util.Upbit.UpbitService
import com.bit.kodari.Util.getEmail
import com.bit.kodari.Util.getJwt
import com.bit.kodari.Util.getPw
import com.bit.kodari.Util.getUserIdx
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.newFixedThreadPoolContext
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.thread

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate), PortfolioView , HomeView {

    lateinit var homeVPAdapter: HomeVPAdapter
    lateinit var homeRCRVAdapter: HomeRCRVAdapter
    lateinit var homePCRVAdapter: HomePCRVAdapter
    var portfolioList = ArrayList<Fragment>()
    var portIdxList = ArrayList<Int>()
    lateinit var accounName: String
    //var profitList = LinkedList<GetProfitResult>()

    // 업비트, 바이낸스 코인 가격 리스트
    lateinit var upbitUserCoinPriceList: List<Int>
    lateinit var binanceUserCoinPriceList: HashMap<String, Any>
    lateinit var upbitRepresentCoinPriceList: List<Int>
    lateinit var binanceRepresentCoinPriceList: HashMap<String, Any>

    // 유저 코인 리스트
//    lateinit var userCoinList: List<PossesionCoinResult>
    lateinit var userCoinList: ArrayList<PossesionCoinResult>
    // 대표 코인 심볼 리스트
    lateinit var representCoinList: List<RepresentCoinResult>
    // 수익률 리스트
    // val profitList = response.result.profitResultList

    //BaseFragment에서 onStart에서 실행시켜줌
    override fun initAfterBinding() {

        // 사용자의 포트폴리오 리스트 가져오기, 바이낸스, 업비트 시세 받아옴
        val portFolioService = PortfolioService()
        portFolioService.setPortfolioView(this)
        showLoadingDialog(requireContext())
        portFolioService.getPortfolioList(getUserIdx())

//        setChartDummy()          포폴 조회 or 버튼 누를떄마다 차트 생성하게해야함.
        setListener()

        //setRepresentPV()            //테스트 위해 추가

        Log.d(
            "info",
            "jwt : ${getJwt()} , email : ${getEmail()} , pw : ${getPw()} , userIdx: ${getUserIdx()}"
        )

        //차트 데이터 가져오기 테스트

    }


    //초기 클릭 리스너 등 모든 리스너를 정의하는 함수
    fun setListener() {
        //소득 클릭시 변경
        binding.homeIncomeOffTv.setOnClickListener {
            binding.homeIncomeOffTv.visibility = View.GONE
            binding.homeIncomeOnTv.visibility = View.VISIBLE
            binding.homeYieldOnTv.visibility = View.GONE
            binding.homeYieldOffTv.visibility = View.VISIBLE
            callGetProfit()
        }
        //수익률 클릭시 변경
        binding.homeYieldOffTv.setOnClickListener {
            binding.homeIncomeOffTv.visibility = View.VISIBLE
            binding.homeIncomeOnTv.visibility = View.GONE
            binding.homeYieldOnTv.visibility = View.VISIBLE
            binding.homeYieldOffTv.visibility = View.GONE
            callGetProfit()
        }

        //일 클릭시 변경
        binding.homeDayOffTv.setOnClickListener {
            binding.homeDayOffTv.visibility = View.GONE
            binding.homeDayOnTv.visibility = View.VISIBLE
            binding.homeWeekOffTv.visibility = View.VISIBLE
            binding.homeWeekOnTv.visibility = View.GONE
            binding.homeMonthOffTv.visibility = View.VISIBLE
            binding.homeMonthOnTv.visibility = View.GONE
            callGetProfit()
        }

        binding.homeWeekOffTv.setOnClickListener {
            binding.homeDayOffTv.visibility = View.VISIBLE
            binding.homeDayOnTv.visibility = View.GONE
            binding.homeWeekOffTv.visibility = View.GONE
            binding.homeWeekOnTv.visibility = View.VISIBLE
            binding.homeMonthOffTv.visibility = View.VISIBLE
            binding.homeMonthOnTv.visibility = View.GONE
            callGetProfit()
        }

        binding.homeMonthOffTv.setOnClickListener {
            binding.homeDayOffTv.visibility = View.VISIBLE
            binding.homeDayOnTv.visibility = View.GONE
            binding.homeWeekOffTv.visibility = View.VISIBLE
            binding.homeWeekOnTv.visibility = View.GONE
            binding.homeMonthOffTv.visibility = View.GONE
            binding.homeMonthOnTv.visibility = View.VISIBLE
            callGetProfit()
        }

        binding.homeNextBtnIb.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, RepresentativeCoinManagementFragment())
                .addToBackStack(null).commitAllowingStateLoss()

        }

        binding.homeMyNextBtnIb.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinManagementFragment(accounName))
                .commitNowAllowingStateLoss()
        }

        //화살표 관련 리스너
        binding.homeVpPreviewBtn.setOnClickListener {
            val current = binding.homeViewpagerVp.currentItem           //현재 뷰페이저 위치 받아오기
            binding.homeViewpagerVp.setCurrentItem(
                current - 1,
                false
            ) //위치 -1 페이지로 이동 -> 이동후 onPage~ 리스너로 인디케이터 변화
        }

        binding.homeVpNextBtn.setOnClickListener {
            val current = binding.homeViewpagerVp.currentItem
            binding.homeViewpagerVp.setCurrentItem(current + 1, false)
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
        Log.d("setViewpager" , "뷰페이저 크ㅡ기 : ${portfolioList.size}")
        homeVPAdapter = HomeVPAdapter(this, portfolioList)
        //homeVPAdapter.addFragment(MyPortfolioFragment())
        binding.homeViewpagerVp.adapter = homeVPAdapter
        binding.homeViewpagerVp.offscreenPageLimit = 3

        //homeVPAdapter.addFragment(MyPortfolioFragment())          //왜 처음 셋팅떄는 되는데 그 뒤론 안될까 ?

        binding.myRecordIndicators.setViewPager(binding.homeViewpagerVp)
        binding.myRecordIndicators.createIndicators(homeVPAdapter.itemCount, 0)

        binding.homeViewpagerVp.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {        //page변경됐을떄
                super.onPageSelected(position)
                when (position) {
                    0 -> {      //시작
                        binding.homeVpPreviewBtn.visibility = View.GONE
                        callPortfolioInfo(portIdxList[position])
                        Log.d("callIdx" ,portfolioList.size.toString())
                    }
                    portfolioList.size - 1 -> {     //마지막
                        binding.homeVpNextBtn.visibility = View.GONE
                    }
                    else -> {
                        binding.homeVpNextBtn.visibility =View.VISIBLE
                        binding.homeVpPreviewBtn.visibility = View.VISIBLE
                        callPortfolioInfo(portIdxList[position].toInt())
                    }
                }
                binding.myRecordIndicators.animatePageSelected(position)

            }
        })
    }

    fun setRepresentRV() {
        homeRCRVAdapter = HomeRCRVAdapter(representCoinList)
        binding.homeRepresentCoinRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.homeRepresentCoinRv.adapter = homeRCRVAdapter

    }

    fun setRepresentPV() {
        //userCoinList = ArrayList()
        //userCoinList.add(PossesionCoinResult(10,10.0,10.0,"","test",10.0,"active",10,"BTC"))
        homePCRVAdapter = HomePCRVAdapter(userCoinList)
        homePCRVAdapter.setMyItemClickListener(object : HomePCRVAdapter.MyItemClickListener{
            override fun onClickItem(item: PossesionCoinResult) {
                val dialog = DialogMemoAndTwitter(item.coinIdx)
                dialog.show(requireActivity().supportFragmentManager , "DialogMemoAndTwitter")
            }
        })
        binding.homeMyCoinRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.homeMyCoinRv.adapter = homePCRVAdapter

    }


    //차트에 더미데이터 셋팅하고 차트 보여주는 함수
    fun setChartDummy(profitList: ArrayList<GetProfitResult>) {

//        binding.homeChartLc.apply {
//            setDrawGridBackground(false);
//            setDrawBorders(false);
//            getLegend().setEnabled(false);
//            setAutoScaleMinMaxEnabled(true);
//            setTouchEnabled(true);
//            setDragEnabled(true);
//            setScaleEnabled(true);
//            setPinchZoom(true);
//            setDoubleTapToZoomEnabled(false);
//            setBackgroundColor(Color.BLUE);         //수정함
//            getAxisRight().isEnabled = false;
//            getDescription().isEnabled = false;
//
//        }


        binding.homeChartLc.getDescription().setEnabled(false);
        // enable touch gestures
        binding.homeChartLc.setTouchEnabled(false);

        // enable scaling and dragging
        binding.homeChartLc.setDragEnabled(false);
        binding.homeChartLc.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        binding.homeChartLc.setPinchZoom(false);

        binding.homeChartLc.setBackgroundColor(Color.rgb(89, 199, 250))

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        binding.homeChartLc.setViewPortOffsets(0f, 0f, 0f, 0f);

//        binding.homeChartLc.axisLeft.apply {
//            setLabelCount(4, true)
//            setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
//            setTextColor(Color.BLACK)
//            setGridColor(Color.argb(102, 255, 255, 255))
//            setAxisLineColor(Color.TRANSPARENT)
//        }
        binding.homeChartLc.legend.isEnabled = false            //범례 없애기
        binding.homeChartLc.data = setChartDummyData(profitList)

        //Y축 셋팅
        binding.homeChartLc.axisLeft.isEnabled = true;
        binding.homeChartLc.axisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)        //차트 어떻게 셋팅하지 ?
        binding.homeChartLc.axisLeft.spaceTop = 40f;
        binding.homeChartLc.axisLeft.spaceBottom = 40f;
        binding.homeChartLc.axisRight.isEnabled = false;

            //X축 셋팅
//        binding.homeChartLc.xAxis.apply {
//            valueFormatter = object :ValueFormatter(){
//                override fun getFormattedValue(value: Float): String {          //-10 이들어옴 Why?
//                    Log.d("listtest" , "${value}")
//                    //Log.d("list", profitList[value.toInt()].createAt)
//                    return profitList[value.toInt()].createAt
//                }
//            }
//            setDrawLimitLinesBehindData(true)
//            setPosition(XAxis.XAxisPosition.BOTTOM)
//            setTextColor(Color.WHITE)
//            disableGridDashedLine()
//            setDrawGridLines(false)
//            setGridColor(Color.argb(204, 255, 255, 255))
//            setAxisLineColor(Color.TRANSPARENT)
//            setLabelCount(4)
//            setAvoidFirstLastClipping(true)
//            setSpaceMin(10f)
//        }
        //X축 String으로 셋팅
//        binding.homeChartLc.xAxis.valueFormatter = object :ValueFormatter(){
//            override fun getFormattedValue(value: Float): String {
//                Log.d("listtest", "${value} , ${value.toInt()}" )
//                if(value >=0) return profitList[value.toInt()].createAt
//            }
//        }

        val temp :ArrayList<String> = ArrayList()
        for(cur in profitList){
            temp.add(cur.createAt)
        }

        binding.homeChartLc.xAxis.valueFormatter = IndexAxisValueFormatter(temp)

        //X축 셋팅.
        binding.homeChartLc.xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        binding.homeChartLc.xAxis.setLabelCount(7,true)
        //binding.homeChartLc.xAxis.setDrawLabels(true)
        binding.homeChartLc.xAxis.textColor = Color.BLACK
        binding.homeChartLc.xAxis.axisLineColor = Color.BLACK
        binding.homeChartLc.xAxis.isEnabled = true
        binding.homeChartLc.xAxis.textSize = 7f



        binding.homeChartLc.invalidate()
    }

    //차트에 더미 데이터 셋팅팅
    //7일치만 먼저 불러와보자 .
    fun setChartDummyData(profitList:ArrayList<GetProfitResult>): LineData {
        val values: ArrayList<Entry> = ArrayList()

        //소득이 체크되어있을떄
        if(binding.homeIncomeOnTv.visibility == View.VISIBLE){
            for (cur in 0 until profitList.size ) { //until이 마지막 전까지
                //배열에 하나씩 꺼내보기
                val temp = profitList[cur]
                val tempVal = temp.earning.toFloat()          //소득
                values.add(Entry(cur.toFloat(), tempVal))         //X축 , Y축 값 등록 -> 기본적으로 float이라 int로 변환한후 가져와얗마.
                Log.d("List" ,"${cur.toFloat()} , ${tempVal}")
            }

        } else if(binding.homeYieldOnTv.visibility == View.VISIBLE){
            for (cur in 0 until profitList.size ) { //until이 마지막 전까지
                //배열에 하나씩 꺼내보기
                val temp = profitList[cur]
                val tempVal = temp.profitRate.toFloat()          //
                values.add(Entry(cur.toFloat(), tempVal))         //X축 , Y축 값 등록 -> 기본적으로 float이라 int로 변환한후 가져와얗마.
            }
        }

        val set1 = LineDataSet(values, "")
        set1.setColor(Color.WHITE)
        set1.setCircleColor(Color.BLACK)
        set1.setLineWidth(3f)
        set1.setDrawCircles(false)
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER)

        set1.setValueTextSize(9f)
        set1.setDrawValues(false)
        set1.setDrawFilled(true)
        set1.setFormLineWidth(1f)
        set1.setFormSize(15f)
//
//        val set1 = LineDataSet(values, "DataSet 1")
//        set1.setFillAlpha(110);
//        set1.setFillColor(Color.RED);
//        set1.lineWidth = 1.75f
//        set1.circleRadius = 5f
//        set1.circleHoleRadius = 2.5f
//        set1.color = Color.WHITE
//        set1.setCircleColor(Color.WHITE)
//        set1.highLightColor = Color.WHITE
//        set1.setDrawValues(false)

        return LineData(set1)
    }

    //포토폴리오 IDX 조회 성공
    override fun getPortIdxSuccess(resp: PortIdxResponse) {
        Log.d("getPortIdx" , "성공")
//        portIdxList.clear()               데이터 자동 추가가 왜됌?
//        portfolioList.clear()
        for(idx in resp.result){
            portfolioList.add(MyPortfolioFragment(idx.portIdx))         //포폴 추가. 이 후 Service내부에서 단일 포폴 조회
            portIdxList.add(idx.portIdx)                                //포토폴리오 인덱스 저장
        }
        Log.d("portIdx" , "사이즈 : ${portIdxList.size} , ${portIdxList}")
        setViewpager()      //여기에 실행 안하면 이게 너무 늦게 실행되서 안됨 , 포폴 목록 조회해서 뷰페이저에 셋팅.
    }
    //포토폴리오 IDX 조회 실패
    override fun getPortIdxFailure(message: String) {
        Log.d("getPortIdx" , "실패")
    }

    // 포트폴리오 API 호출 성공(계좌, 유저코인 리스트, 대표코인 리스트, 수익률 리스트 받아옴)
    override fun portfolioSuccess(response: PortfolioResponse) {
        dismissLoadingDialog()
        when (response.code) {
            1000 -> {
                val userCoinNameList = ArrayList<String>()
                val representCoinNameList = ArrayList<String>()
                // 계좌
                getAccountResult(response)
                // 유저 코인 리스트
                this.userCoinList = response.result.userCoinList as ArrayList<PossesionCoinResult>  //ArrayList로 바꾸면서 추가
                // 대표 코인 리스트
                this.representCoinList = response.result.representCoinList
                // 수익률 리스트
                val profitList = response.result.profitResultList
                // 소유코인 이름 저장
                for (i in 0 until userCoinList.size) {
                    userCoinNameList.add(userCoinList[i].symbol)
                }

                // 대표코인 이름 저장
                for (i in 0 until representCoinList.size) {
                    representCoinNameList.add(representCoinList[i].symbol + "USDT")
                }
                // 소유코인 업비트 시세 받아오기
                this.upbitUserCoinPriceList = UpbitService.getCurrentPrice(userCoinNameList)
                // 소유코인 바이낸스 시세 받아오기
                this.binanceUserCoinPriceList = BinanceService.getCurrentPrice(userCoinNameList)
                // 대표코인 업비트 시세 받아오기
                this.upbitRepresentCoinPriceList =
                    UpbitService.getCurrentPrice(representCoinNameList)
                //AccountIdx 와 PortIdx 싱글톤에 셋팅

                MyApplicationClass.myAccountIdx = response.result.accountIdx
                MyApplicationClass.myPortIdx = response.result.portIdx
                accounName = response.result.accountName                //계좌이름
                Log.d("인덱스 정보" , "account : ${MyApplicationClass.myAccountIdx} , Port : ${MyApplicationClass.myPortIdx}")
                Log.d("Callidx" , "포트 : ${MyApplicationClass.myPortIdx}  , 계좌 : ${MyApplicationClass.myAccountIdx}")
                // 대표코인, 소유코인 뷰 바인딩
                setRepresentRV()
                setRepresentPV()


                //계좌 인덱스 셋팅 됐을때 차트 호출
                callGetProfit()
            }
            else -> {
                showToast(response.message)
            }
        }
    }

    // 포트폴리오 API 호출 실패
    override fun portfolioFailure(message: String) {
        showToast("포트폴리오 불러오기 실패")
    }

    //일별 데이터 성공
    override fun getDayProfitSuccess(response: GetProfitResponse) {
        val profitList : ArrayList<GetProfitResult> = ArrayList<GetProfitResult>()
        for(cur in response.result){
            cur.createAt = cur.createAt.substring(5,10)         //월-일만 저장
            profitList.add(cur)             //정보들 저장
        }
        setChartDummy(profitList)
    }

    //일별 데이터 실패
    override fun getDayProfitFrailure(message: String) {
        showToast(message)
    }

    //주별 데이터 성공
    override fun getWeekProfitSuccess(response: GetProfitResponse) {
        val profitList : ArrayList<GetProfitResult> = ArrayList<GetProfitResult>()
        for(cur in response.result){
            cur.createAt = cur.createAt.substring(5,10)         //월-일만 저장
            profitList.add(cur)             //정보들 저장
        }
        setChartDummy(profitList)
    }
    //주별 데이터 실패
    override fun getWeekProfitFailure(message: String) {
        showToast(message)
    }

    //월별 데이터 성공
    override fun getMonthProfitSuccess(response: GetProfitResponse) {
        val profitList : ArrayList<GetProfitResult> = ArrayList()
        for(cur in response.result){
            cur.createAt = cur.createAt.substring(5,10)         //월-일만 저장
            profitList.add(cur)             //정보들 저장
        }
        setChartDummy(profitList)
    }

    //월별 데이터 실패
    override fun getMonthProfitFailure(message: String) {
        showToast(message)
    }

    fun getAccountResult(response: PortfolioResponse): AccountResult {
        val accountIdx = response.result.accountIdx
        val accountName = response.result.accountName
        val property = response.result.property
        val totalProperty = response.result.totalProperty
        val userIdx = response.result.userIdx
        val marketName = response.result.marketName
        return AccountResult(accountIdx, accountName, property, totalProperty, userIdx, marketName)
    }
    // 주기적으로 시세를 가져오고 뷰 바인딩 해주는 스레드
    fun coinPriceThread(){
        Thread(Runnable {

        })
    }

    fun callPortfolioInfo(portIdx:Int){
        val portfolioService = PortfolioService()
        portfolioService.setPortfolioView(this)
        portfolioService.getPortfolioInfo(portIdx)
    }

    fun callGetProfit(){
        showToast("호출")
        if(binding.homeDayOnTv.visibility == View.VISIBLE){
            val homeService = HomeService()
            homeService.setHomeView(this)
            homeService.getDayProfit(MyApplicationClass.myAccountIdx)
        } else if(binding.homeWeekOnTv.visibility == View.VISIBLE){
            val homeService = HomeService()
            homeService.setHomeView(this)
            homeService.getWeekProfit(MyApplicationClass.myAccountIdx)
        } else if(binding.homeMonthOnTv.visibility == View.VISIBLE){
            val homeService = HomeService()
            homeService.setHomeView(this)
            homeService.getMonthProfit(MyApplicationClass.myAccountIdx)
        }

    }

}