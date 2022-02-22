package com.bit.kodari.Main

import android.graphics.Color
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bit.kodari.databinding.FragmentHomeBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.MyApplicationClass
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Main.Adapter.HomePCRVAdapter
import com.bit.kodari.Main.Adapter.HomeRCRVAdapter
import com.bit.kodari.Main.Adapter.HomeVPAdapter
import com.bit.kodari.Main.Data.*
import com.bit.kodari.Main.RetrofitInterface.HomeView
import com.bit.kodari.Main.Service.HomeService
import com.bit.kodari.Portfolio.Retrofit.PortfolioView
import com.bit.kodari.Portfolio.Service.PortfolioService
import com.bit.kodari.PossessionCoin.DialogMemoAndTwitter
import com.bit.kodari.PossessionCoin.PossessionCoinManagementFragment
import com.bit.kodari.R
import com.bit.kodari.RepresentativeCoin.RepresentativeCoinManagementFragment
import com.bit.kodari.Util.*
import com.bit.kodari.Util.Coin.*
import com.bit.kodari.Util.Coin.Binance.BinanceWebSocketListener
import com.bit.kodari.Util.Coin.Upbit.UpbitWebSocketListener
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate), PortfolioView,
    CoinView, HomeView {
    private lateinit var homeVPAdapter: HomeVPAdapter
    private lateinit var homeRCRVAdapter: HomeRCRVAdapter
    private lateinit var homePCRVAdapter: HomePCRVAdapter
    private lateinit var viewModel: CoinViewModel
    private lateinit var viewModelFactory: CoinViewModelFactory
    private var viewPagerPosition = 0;
    private var checkView = true
    var portfolioList = ArrayList<Fragment>()
    var portIdxList = ArrayList<Int>()
    lateinit var accounName: String

    // 유저 코인 리스트
    var userCoinList = ArrayList<PossesionCoinResult>()

    // 대표 코인 리스트
    var representCoinList = ArrayList<RepresentCoinResult>()

    // 수익률 리스트
    // val profitList = response.result.profitResultList
    // 유저 코인, 대표 코인 심볼 저장
    var coinSymbolSet = HashSet<String>()

    // 업비트, 바이낸스 웹 소켓
    var upbitWebSocket: UpbitWebSocketListener? = null
    var binanceWebSocket: BinanceWebSocketListener? = null
    private lateinit var portFolioView: PortfolioView

    fun setPortFolioView(portFolioView: PortfolioView) {
        this.portFolioView = portFolioView
    }

    //BaseFragment에서 onStart에서 실행시켜줌
    override fun initAfterBinding() {
        // 사용자의 포트폴리오 리스트 가져오기, 바이낸스, 업비트 시세 받아옴
        val portFolioService = PortfolioService()
        portFolioService.setPortfolioView(this)
        showLoadingDialog(requireContext())
        portFolioService.getPortfolioList(getUserIdx())

//        setChartDummy()          포폴 조회 or 버튼 누를떄마다 차트 생성하게해야함.
        setListener()

        Log.d(
            "info",
            "jwt : ${getJwt()} , email : ${getEmail()} , pw : ${getPw()} , userIdx: ${getUserIdx()}"
        )

        viewModelFactory = CoinViewModelFactory(userCoinList, representCoinList)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CoinViewModel::class.java)
        viewModel.representCoinData.observe(this, androidx.lifecycle.Observer {
            setRepresentRV()
        })
        viewModel.userCoinData.observe(this, androidx.lifecycle.Observer {
            setRepresentPV()
        })
    }
    override fun onDestroyView() {
        checkView = false
        super.onDestroyView()
    }

    fun setRepresentRV() {
        if (binding != null) {
            homeRCRVAdapter = HomeRCRVAdapter(representCoinList)
            binding.homeRepresentCoinRv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.homeRepresentCoinRv.adapter = homeRCRVAdapter
        }
    }

    fun setRepresentPV() {
        if (binding != null) {
            homePCRVAdapter = HomePCRVAdapter(userCoinList)
            homePCRVAdapter.setMyItemClickListener(object : HomePCRVAdapter.MyItemClickListener {
                override fun onClickItem(item: PossesionCoinResult) {
                    val dialog = DialogMemoAndTwitter(item.coinIdx, item.twitter)
                    dialog.show(requireActivity().supportFragmentManager, "DialogMemoAndTwitter")
                }
            })
            binding.homeMyCoinRv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.homeMyCoinRv.adapter = homePCRVAdapter
        }
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
                .addToBackStack(null)
                .commitAllowingStateLoss()
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

        /*//임시 로그아웃 버튼
        binding.logout.setOnClickListener {
            saveLoginInfo(null, null, null, 0)     //0이면 유저 없는거
            saveAutoLogin(false)
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }*/

    }

    //뷰 페이저 셋팅 -> 리스트에 더미데이터 넣어놓은 상태
    //API 호출 이후 실행
    fun setViewpager() {
        Log.d("setViewpager", "뷰페이저 크ㅡ기 : ${portfolioList.size}")
        homeVPAdapter = HomeVPAdapter(this, portfolioList)
        Log.d("setViewpager", "뷰페이저 크ㅡ기2 : ${portfolioList.size}")      //여기서 2가됨
        //homeVPAdapter.addFragment(MyPortfolioFragment())
        binding.homeViewpagerVp.adapter = homeVPAdapter
        Log.d("setViewpager", "뷰페이저 크ㅡ기3 : ${portfolioList.size}")
        binding.homeViewpagerVp.offscreenPageLimit = 3
        Log.d("setViewpager", "뷰페이저 크ㅡ기4 : ${portfolioList.size}")

        //homeVPAdapter.addFragment(MyPortfolioFragment())          //왜 처음 셋팅떄는 되는데 그 뒤론 안될까 ?

        binding.myRecordIndicators.setViewPager(binding.homeViewpagerVp)
        Log.d("setViewpager", "뷰페이저 크ㅡ기5 : ${portfolioList.size}")
        binding.myRecordIndicators.createIndicators(homeVPAdapter.itemCount, 0)
        Log.d("setViewpager", "뷰페이저 크ㅡ기6 : ${portfolioList.size}")
        binding.homeViewpagerVp.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {        //page변경됐을떄
                super.onPageSelected(position)
                Log.d("setViewpager", "뷰페이저 크ㅡ기7 : ${portfolioList.size}")
                viewPagerPosition = position
                when (position) {
                    0 -> {      //시작
                        binding.homeVpPreviewBtn.visibility = View.GONE
                        if (portIdxList.size != 0) {
                            callPortfolioInfo(portIdxList[position])
                            Log.d("callIdx", portfolioList.size.toString())
                        }
                    }
                    portfolioList.size - 1 -> {     //마지막
                        binding.homeVpNextBtn.visibility = View.GONE
                    }
                    else -> {
                        Log.d("setViewpager", "else position : ${position}")
                        binding.homeVpNextBtn.visibility = View.VISIBLE
                        binding.homeVpPreviewBtn.visibility = View.VISIBLE
                        callPortfolioInfo(portIdxList[position].toInt())
                    }
                }
                binding.myRecordIndicators.animatePageSelected(position)

            }
        })
        //portfolioList.clear()       //다시 조회할때 채우기 위해 기존꺼 삭제
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
        if(checkView == true) {
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

            val temp: ArrayList<String> = ArrayList()
            for (cur in profitList) {
                temp.add(cur.createAt)
            }

            binding.homeChartLc.xAxis.valueFormatter = IndexAxisValueFormatter(temp)

            //X축 셋팅.
            binding.homeChartLc.xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
            binding.homeChartLc.xAxis.setLabelCount(7, true)
            //binding.homeChartLc.xAxis.setDrawLabels(true)
            binding.homeChartLc.xAxis.textColor = Color.BLACK
            binding.homeChartLc.xAxis.axisLineColor = Color.BLACK
            binding.homeChartLc.xAxis.isEnabled = true
            binding.homeChartLc.xAxis.textSize = 7f
            binding.homeChartLc.invalidate()
        }
    }

    //차트에 더미 데이터 셋팅팅
    //7일치만 먼저 불러와보자 .
    fun setChartDummyData(profitList: ArrayList<GetProfitResult>): LineData {
        val values: ArrayList<Entry> = ArrayList()

        //소득이 체크되어있을떄
        if (binding.homeIncomeOnTv.visibility == View.VISIBLE) {
            for (cur in 0 until profitList.size) { //until이 마지막 전까지
                //배열에 하나씩 꺼내보기
                val temp = profitList[cur]
                val tempVal = temp.earning.toFloat()          //소득
                values.add(
                    Entry(
                        cur.toFloat(),
                        tempVal
                    )
                )         //X축 , Y축 값 등록 -> 기본적으로 float이라 int로 변환한후 가져와얗마.
                Log.d("List", "${cur.toFloat()} , ${tempVal}")
            }

        } else if (binding.homeYieldOnTv.visibility == View.VISIBLE) {
            for (cur in 0 until profitList.size) { //until이 마지막 전까지
                //배열에 하나씩 꺼내보기
                val temp = profitList[cur]
                val tempVal = temp.profitRate.toFloat()          //
                values.add(
                    Entry(
                        cur.toFloat(),
                        tempVal
                    )
                )         //X축 , Y축 값 등록 -> 기본적으로 float이라 int로 변환한후 가져와얗마.
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
        dismissLoadingDialog()
//        portIdxLi.st.clear()               데이터 자동 추가가 왜됌?
//        portfolioList.clear()
        for (idx in resp.result) {
            portfolioList.add(
                MyPortfolioFragment(
                    idx.portIdx,
                    this
                )
            )         //포폴 추가. 이 후 Service내부에서 단일 포폴 조회
            portIdxList.add(idx.portIdx)                                //포토폴리오 인덱스 저장
        }
        Log.d("portIdx", "사이즈 : ${portIdxList.size} , ${portIdxList}")
        setViewpager()      //여기에 실행 안하면 이게 너무 늦게 실행되서 안됨 , 포폴 목록 조회해서 뷰페이저에 셋팅.
    }

    //포토폴리오 IDX 조회 실패
    override fun getPortIdxFailure(message: String) {
        dismissLoadingDialog()
    }

    // 포트폴리오 API 호출 성공(계좌, 유저코인 리스트, 대표코인 리스트, 수익률 리스트 받아옴)
    override fun portfolioSuccess(response: PortfolioResponse) {
        dismissLoadingDialog()
        when (response.code) {
            1000 -> {
                // 계좌
                getAccountResult(response)
                // 코인 시세 받아오기
                getCoinPrice(response)
                // 수익률 리스트
                val profitList = response.result.profitResultList
                //AccountIdx 와 PortIdx 싱글톤에 셋팅
                MyApplicationClass.myAccountIdx = response.result.accountIdx
                MyApplicationClass.myPortIdx = response.result.portIdx
                accounName = response.result.accountName                //계좌이름
                Log.d(
                    "인덱스 정보",
                    "account : ${MyApplicationClass.myAccountIdx} , Port : ${MyApplicationClass.myPortIdx}"
                )
                Log.d(
                    "Callidx",
                    "포트 : ${MyApplicationClass.myPortIdx}  , 계좌 : ${MyApplicationClass.myAccountIdx}"
                )
                //계좌 인덱스 셋팅 됐을때 차트 호출
                callGetProfit()
            }
            else -> {
                showToast(response.message)
            }
        }
    }

    // 업비트 시세 조회 API 호출 성공
    override fun upbitPriceSuccess(upbitCoinPriceMap: HashMap<String, Double>) {
        if (requireActivity() != null && checkView) {
            var currentSum = 0.0
            var sumBuyCoin = 0.0
            requireActivity().runOnUiThread() {
                // 소유 코인
                for (i in userCoinList.indices) {
                    val symbol = userCoinList[i].symbol
                    if (upbitCoinPriceMap.containsKey(symbol)) {
                        val upbitPrice = upbitCoinPriceMap.get(symbol)!!
                        val change = upbitCoinPriceMap.get(symbol+"change")
                        val amount = userCoinList[i].amount
                        val priceAvg = userCoinList[i].priceAvg
                        sumBuyCoin += amount * priceAvg
                        userCoinList[i].upbitPrice = upbitPrice
                        if (change != null) {
                            userCoinList[i].change = change
                        }
                        userCoinList[i].profit = getProfit(upbitPrice, priceAvg, amount)
                        userCoinList[i].profitRate = getProfitRate(upbitPrice, priceAvg, amount)
                        currentSum += upbitPrice * amount
                    }
                }
                // 대표 코인
                for (i in representCoinList.indices) {
                    val symbol = representCoinList[i].symbol
                    if (upbitCoinPriceMap.containsKey(symbol)) {
                        val change = upbitCoinPriceMap.get(symbol+"change")
                        representCoinList[i].upbitPrice = upbitCoinPriceMap.get(symbol)!!
                        if (change != null) {
                            representCoinList[i].change = change
                        }
                    }
                }
                if(viewPagerPosition < portfolioList.size && currentSum != 0.0
                    && portfolioList[viewPagerPosition] is MyPortfolioFragment) {
                    val myPortfolioFragment: MyPortfolioFragment =
                        portfolioList[viewPagerPosition] as MyPortfolioFragment
                    myPortfolioFragment.getAccountProfit(currentSum, sumBuyCoin)
                }
                //시세 호출하면 ViewModel 내부의 LiveData Update 이 후 , observer 패턴으로
                viewModel.getUpdateUserCoin(userCoinList)
                viewModel.getUpdateRepresentCoin(representCoinList)
                // 계좌 수익률 보내주기
            }
        }
    }

    // 바이낸스 시세 조회 API 호출 성공
    override fun binancePriceSuccess(binanceCoinPriceMap: HashMap<String, Double>) {
        if (requireActivity() != null && checkView) {
            requireActivity().runOnUiThread() {
                var usdtPrice = 1197
                // 대표 코인
                for (i in representCoinList.indices) {
                    val symbol = representCoinList[i].symbol
                    if (binanceCoinPriceMap.containsKey(symbol)) {
                        val upbitPrice = representCoinList[i].upbitPrice
                        val binancePrice = binanceCoinPriceMap.get(symbol)!! * usdtPrice!!
                        var kimchi = ((upbitPrice - binancePrice) / upbitPrice) * 100
                        representCoinList[i].binancePrice = binancePrice
                        representCoinList[i].kimchi = kimchi
                    }
                }
                viewModel.getUpdateUserCoin(userCoinList)
                viewModel.getUpdateRepresentCoin(representCoinList)
            }
        }
    }

    override fun usdtPriceSuccess(usdtPrice: Int) {
        TODO("Not yet implemented")
    }

    override fun coinPriceFailure(message: String) {
        TODO("Not yet implemented")
    }


    // 시세 받아오기
    fun getCoinPrice(response: PortfolioResponse) {
        val userCoinNameList = ArrayList<String>()
        val representCoinNameList = ArrayList<String>()
        userCoinList.clear()
        representCoinList.clear()
        // 계좌
        getAccountResult(response)
        // 유저 코인 리스트
        this.userCoinList = response.result.userCoinList as ArrayList<PossesionCoinResult>
        // 대표 코인 리스트
        this.representCoinList = response.result.representCoinList as ArrayList<RepresentCoinResult>
        // 수익률 리스트
        val profitList = response.result.profitResultList
        // 소유코인 이름 저장
        for (i in 0 until userCoinList.size) {
            coinSymbolSet.add(userCoinList[i].symbol)
        }
        // 대표코인 이름 저장
        for (i in 0 until representCoinList.size) {
            coinSymbolSet.add(representCoinList[i].symbol)
        }

        // 웹 소켓 연결
        upbitWebSocket = UpbitWebSocketListener(coinSymbolSet)
        upbitWebSocket?.setCoinView(this)
        upbitWebSocket?.start() // 업비트 웹 소켓 실행
        binanceWebSocket = BinanceWebSocketListener(coinSymbolSet)
        binanceWebSocket?.setCoinView(this)
        binanceWebSocket?.start()
    }

    // 뷰 바인딩 해주기
    fun setPortViewBinding() {
        // 대표코인, 소유코인 뷰 바인딩

    }

    // 포트폴리오 API 호출 실패
    override fun portfolioFailure(message: String) {
        showToast("포트폴리오 불러오기 실패")
    }

    //일별 데이터 성공
    override fun getDayProfitSuccess(response: GetProfitResponse) {
        if (response.result[0].profitIdx == 0)
            return
        val profitList: ArrayList<GetProfitResult> = ArrayList<GetProfitResult>()
        for (cur in response.result) {
            cur.createAt = cur.createAt.substring(5, 10)         //월-일만 저장
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
        if (response.result[0].profitIdx == 0)
            return
        val profitList: ArrayList<GetProfitResult> = ArrayList<GetProfitResult>()

        for (cur in response.result) {
            cur.createAt = cur.createAt.substring(5, 10)         //월-일만 저장
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
        if (response.result[0].profitIdx == 0)
            return
        val profitList: ArrayList<GetProfitResult> = ArrayList()
        for (cur in response.result) {
            cur.createAt = cur.createAt.substring(5, 10)         //월-일만 저장
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

    override fun onPause() {
        super.onPause()
        checkView = false
        portfolioList.clear()   //초기화
    }

    override fun onResume() {
        super.onResume()
        checkView = true
    }

    override fun onDestroy() {
        super.onDestroy()
        checkView = false
        upbitWebSocket?.webSocket?.cancel() // 웹 소켓 닫기
        binanceWebSocket?.webSocket?.cancel()
    }

    fun callPortfolioInfo(portIdx: Int) {
        val portfolioService = PortfolioService()
        portfolioService.setPortfolioView(this)
        portfolioService.getPortfolioInfo(portIdx)
    }

    fun callGetProfit() {
        if (!checkView)
            return
        if (binding.homeDayOnTv.visibility == View.VISIBLE) {
            val homeService = HomeService()
            homeService.setHomeView(this)
            homeService.getDayProfit(MyApplicationClass.myAccountIdx)
        } else if (binding.homeWeekOnTv.visibility == View.VISIBLE) {
            val homeService = HomeService()
            homeService.setHomeView(this)
            homeService.getWeekProfit(MyApplicationClass.myAccountIdx)
        } else if (binding.homeMonthOnTv.visibility == View.VISIBLE) {
            val homeService = HomeService()
            homeService.setHomeView(this)
            homeService.getMonthProfit(MyApplicationClass.myAccountIdx)
        }
    }

    fun getProfit(currentPrice: Double, priceAvg: Double, amount: Double): Double {
        return (currentPrice * amount) - (priceAvg * amount)
    }
    fun getProfitRate(currentPrice: Double, priceAvg: Double, amount: Double): Double {
        return ((currentPrice * amount) / (priceAvg * amount)) * 100 - 100
    }
}