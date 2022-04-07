package com.bit.kodari.Main

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewpager2.widget.ViewPager2
import com.MyApplicationClass
import com.MyApplicationClass.Companion.marketName
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
import com.bit.kodari.Util.Coin.Bithumb.BithumbWebSocketListener
import com.bit.kodari.Util.Coin.USD.UsdService
import com.bit.kodari.Util.Coin.USD.UsdView
import com.bit.kodari.Util.Coin.Upbit.UpbitWebSocketListener
import com.bit.kodari.Util.Upbit.CoinService
import com.bit.kodari.databinding.FragmentHomeBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import kotlin.properties.Delegates


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate), PortfolioView,
    CoinView, HomeView, UsdView {
    companion object {
        var usdtPrice = 1180
        private const val NORMAL_STATUS_CLOSE = 1000
    }

    private var coinPriceMap = HashMap<String, Double>(); // key:symbol, value:price
    private var userCoinList = ArrayList<PossesionCoinResult>() // 유저 코인 리스트
    private var representCoinList = ArrayList<RepresentCoinResult>() // 대표 코인 리스트
    private lateinit var homeVPAdapter: HomeVPAdapter
    private var homeRCRVAdapter = HomeRCRVAdapter(representCoinList)
    private var homePCRVAdapter = HomePCRVAdapter(userCoinList)
    private lateinit var viewModel: CoinViewModel
    private lateinit var viewModelFactory: CoinViewModelFactory
    private var viewPagerPosition = MyApplicationClass.pageIdx;
    private var checkView = true
    private var portfolioList = ArrayList<Fragment>()
    private var portIdxList = ArrayList<Int>()
    lateinit var accounName: String
    private var marketIdx by Delegates.notNull<Int>()       //marketIdx 선언
    private var market: HashMap<String, Int> = hashMapOf("업비트" to 1, "빗썸" to 2)
    private var canClick:Boolean = false

    private var coinSymbolSet = HashSet<String>()    // 수익률 리스트    // 유저 코인, 대표 코인 심볼 저장

    private var upbitWebSocket: UpbitWebSocketListener? = null    // 업비트, 바이낸스, 빗썸 웹 소켓
    private var binanceWebSocket: BinanceWebSocketListener? = null
    private var bithumbWebSocket: BithumbWebSocketListener? = null
    private lateinit var portFolioView: PortfolioView
    private lateinit var coinService: CoinService


    fun setPortFolioView(portFolioView: PortfolioView) {
        this.portFolioView = portFolioView
    }


    //BaseFragment에서 onStart 에서 실행시켜줌
    override fun initAfterBinding() {
        //Get FireBase device Token
//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.d("device token", task.exception.toString())
//            }
//            val token = task.result
//            Log.d("device token", token)
//        }

        showLoadingDialog(requireContext())
        // 사용자의 포트폴리오 리스트 가져오기, 바이낸스, 업비트 시세 받아옴
        val portFolioService = PortfolioService()
        portFolioService.setPortfolioView(this)
        portFolioService.getPortfolioList(getUserIdx())
        // 환율 받아오기
        val usdService = UsdService()
        usdService.setUsdView(this)
        usdService.getUsdExchangeRate()
        // 라이브 데이터 뷰 모델 세팅
        viewModelFactory = CoinViewModelFactory(userCoinList, representCoinList)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CoinViewModel::class.java)
        // 업비트, 바이낸스 서비스 세팅
        coinService = CoinService()
        coinService.setViewModel(viewModel)
//        setChartDummy()          포폴 조회 or 버튼 누를떄마다 차트 생성하게해야함.

        setListener()
        Log.d(
            "info",
            "jwt : ${getJwt()} , email : ${getEmail()} , pw : ${getPw()} , userIdx: ${getUserIdx()} "
        )
        viewModel.representCoinData.observe(this, androidx.lifecycle.Observer {
            if (homeRCRVAdapter != null) {
                var position = viewModel.getRepresentCoinPosition()
                homeRCRVAdapter.setData(representCoinList, position)
            } else {
                setRepresentRV()
            }
        })
        viewModel.userCoinData.observe(this, androidx.lifecycle.Observer {
            var position = viewModel.getUserCoinPosition()
            if (homePCRVAdapter != null && userCoinList.size > position) {
                // 소유 코인 시세 받아오기, 수익률 구하기
                val marketPrice = userCoinList[position].marketPrice
                if (marketPrice == 0.0)
                    return@Observer
                val priceAvg = userCoinList[position].priceAvg
                val amount = userCoinList[position].amount
                userCoinList[position].profit = getProfit(marketPrice, priceAvg, amount)
                userCoinList[position].profitRate = getProfitRate(marketPrice, priceAvg, amount)
                homePCRVAdapter.setData(userCoinList, position)
            } else {
                setRepresentPV()
            }
        })
    }

    override fun onDestroyView() {
        checkView = false
        Log.d("onDestroyView Home", "실행")
        requireActivity().window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        requireActivity().window!!.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.white)
        super.onDestroyView()       //부모의 onDestryView 호출
    }

    fun setRepresentRV() {
        if (checkView && binding != null) {
            val animator = binding.homeRepresentCoinRv.itemAnimator // 애니메이션 제거
            if (animator is SimpleItemAnimator) { //아이템 애니메이커 기본 하위클래스
                animator.supportsChangeAnimations =
                    false  //애니메이션 값 false (리사이클러뷰가 화면을 다시 갱신 했을때 뷰들의 깜빡임 방지)
            }
            homeRCRVAdapter = HomeRCRVAdapter(representCoinList)
            binding.homeRepresentCoinRv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.homeRepresentCoinRv.adapter = homeRCRVAdapter
        }
    }

    fun setRepresentPV() {
        if (checkView && binding != null) {
            val animator = binding.homeMyCoinRv.itemAnimator // 애니메이션 제거
            if (animator is SimpleItemAnimator) { //아이템 애니메이커 기본 하위클래스
                animator.supportsChangeAnimations =
                    false  //애니메이션 값 false (리사이클러뷰가 화면을 다시 갱신 했을때 뷰들의 깜빡임 방지)
            }
            homePCRVAdapter = HomePCRVAdapter(userCoinList)
            homePCRVAdapter.setMyItemClickListener(object :
                HomePCRVAdapter.MyItemClickListener {
                override fun onClickItem(item: PossesionCoinResult) {
                    val dialog = DialogMemoAndTwitter(item.coinIdx, item.twitter)
                    dialog.show(
                        requireActivity().supportFragmentManager,
                        "DialogMemoAndTwitter"
                    )
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
            if(MyApplicationClass.myAccountIdx != 0){
                callGetProfit()
            }

        }
        //수익률 클릭시 변경
        binding.homeYieldOffTv.setOnClickListener {
            binding.homeIncomeOffTv.visibility = View.VISIBLE
            binding.homeIncomeOnTv.visibility = View.GONE
            binding.homeYieldOnTv.visibility = View.VISIBLE
            binding.homeYieldOffTv.visibility = View.GONE
            if(MyApplicationClass.myAccountIdx != 0) {
                callGetProfit()
            }
        }

        //일 클릭시 변경
        binding.homeDayOffTv.setOnClickListener {
            binding.homeDayOffTv.visibility = View.GONE
            binding.homeDayOnTv.visibility = View.VISIBLE
            binding.homeWeekOffTv.visibility = View.VISIBLE
            binding.homeWeekOnTv.visibility = View.GONE
            binding.homeMonthOffTv.visibility = View.VISIBLE
            binding.homeMonthOnTv.visibility = View.GONE
            if(MyApplicationClass.myAccountIdx != 0) {
                callGetProfit()
            }
        }

        binding.homeWeekOffTv.setOnClickListener {
            binding.homeDayOffTv.visibility = View.VISIBLE
            binding.homeDayOnTv.visibility = View.GONE
            binding.homeWeekOffTv.visibility = View.GONE
            binding.homeWeekOnTv.visibility = View.VISIBLE
            binding.homeMonthOffTv.visibility = View.VISIBLE
            binding.homeMonthOnTv.visibility = View.GONE
            if (MyApplicationClass.myAccountIdx != 0) {
                callGetProfit()
            }
        }

        binding.homeMonthOffTv.setOnClickListener {
            binding.homeDayOffTv.visibility = View.VISIBLE
            binding.homeDayOnTv.visibility = View.GONE
            binding.homeWeekOffTv.visibility = View.VISIBLE
            binding.homeWeekOnTv.visibility = View.GONE
            binding.homeMonthOffTv.visibility = View.GONE
            binding.homeMonthOnTv.visibility = View.VISIBLE
            if(MyApplicationClass.myAccountIdx != 0) {
                callGetProfit()
            }
        }
        //대표코인 클릭했을 때
        binding.homeNextBtnIb.setOnClickListener {
            if(portfolioList.size == 1 || canClick){
                showToast("포트폴리오를 추가해주세요.")
            } else{
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_container_fl,
                        RepresentativeCoinManagementFragment(marketIdx)
                    )
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }
        }

        //소유코인 클릭했을때
        binding.homeMyNextBtnIb.setOnClickListener {
            if(portfolioList.size == 1 || canClick){
               showToast("포트폴리오를 추가해주세요.")
            } else{
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_container_fl,
                        PossessionCoinManagementFragment(accounName, marketIdx)
                    )
                    .commitNowAllowingStateLoss()
            }

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
    }

    //뷰 페이저 셋팅 -> 리스트에 더미데이터 넣어놓은 상태
    //API 호출 이후 실행
    fun setViewpager() {
        if (checkView) {
            homeVPAdapter = HomeVPAdapter(this, portfolioList)
            binding.homeViewpagerVp.adapter = homeVPAdapter
            binding.homeViewpagerVp.offscreenPageLimit = 3

            binding.myRecordIndicators.setViewPager(binding.homeViewpagerVp)
            binding.myRecordIndicators.createIndicators(homeVPAdapter.itemCount, 0)

            //뷰페이저 화살표 설정 리스너.
            binding.homeViewpagerVp.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {        //page변경됐을떄
                    super.onPageSelected(position)
                    viewPagerPosition = position
                    when (position) {
                        0 -> {      //시작
                            binding.homeVpPreviewBtn.visibility = View.GONE
                            binding.homeVpNextBtn.visibility = View.VISIBLE
                            if (portIdxList.size != 0) {
                                callPortfolioInfo(portIdxList[position])
                            }
                            MyApplicationClass.pageIdx =
                                position               //홈으로 돌아왔을떄 보던 포토폴리오 유지
                            canClick = false
                        }
                        portfolioList.size - 1 -> {     //마지막
                            binding.homeVpNextBtn.visibility = View.GONE
                            binding.homeVpPreviewBtn.visibility = View.VISIBLE
                            userCoinList.clear()
                            representCoinList.clear()
                            setRepresentRV()
                            setRepresentPV()
                            setChartDummy(profitList = ArrayList())
                            MyApplicationClass.pageIdx = 0               //홈으로 돌아왔을떄 보던 포토폴리오 유지 단 마지막 포폴 생성일 때는 0번으로
                            canClick = true
                        }
                        else -> {
                            binding.homeVpNextBtn.visibility = View.VISIBLE
                            binding.homeVpPreviewBtn.visibility = View.VISIBLE
                            callPortfolioInfo(portIdxList[position].toInt())
                            MyApplicationClass.pageIdx =
                                position               //홈으로 돌아왔을떄 보던 포토폴리오 유지
                            canClick = false
                        }
                    }
                    binding.myRecordIndicators.animatePageSelected(position)

                }
            })
            binding.homeViewpagerVp.currentItem =
                MyApplicationClass.pageIdx        //현재 포폴로 이동 -> 전부 불러온 뒤
            Log.d("pageIDx", "${MyApplicationClass.pageIdx}")
            //portfolioList.clear()       //다시 조회할때 채우기 위해 기존꺼 삭제
        }
    }


    //차트에 더미데이터 셋팅하고 차트 보여주는 함수
    fun setChartDummy(profitList: ArrayList<GetProfitResult>) {
        if (checkView) {
            if (profitList.size == 0) {
                binding.homeChartLc.clear()
                binding.homeChartLc.setBackgroundColor(Color.rgb(255, 255, 255))
                return
            }

            val temp: ArrayList<String> = ArrayList()
            for (cur in profitList) {
                val date = cur.createAt.replace("-", "/")
                temp.add(date)
            }
            Log.d("setChart", "${profitList.size}")
            Log.d("setChart", "${temp}")


            binding.homeChartLc.data = setChartDummyData(profitList)        //데이터추가
            binding.homeChartLc.xAxis.setDrawGridLines(false)
            binding.homeChartLc.axisLeft.setDrawGridLines(false)
            binding.homeChartLc.xAxis.axisLineColor =
                resources.getColor(R.color.chartTextColor)//top line
            binding.homeChartLc.xAxis.textColor = resources.getColor(R.color.chartTextColor)
            binding.homeChartLc.xAxis.position = XAxis.XAxisPosition.BOTTOM
            binding.homeChartLc.xAxis.granularity =
                1f                      //간격 설정하니까 해결 됨 -> why?
            binding.homeChartLc.axisLeft.axisLineColor =
                resources.getColor(R.color.chartTextColor)//left line
            binding.homeChartLc.axisLeft.textColor = resources.getColor(R.color.chartTextColor)
            binding.homeChartLc.axisRight.isEnabled = false
            binding.homeChartLc.setDrawBorders(false)
            binding.homeChartLc.setDrawGridBackground(false)
            binding.homeChartLc.description = null
            binding.homeChartLc.isAutoScaleMinMaxEnabled = false
            binding.homeChartLc.setBackgroundDrawable(resources.getDrawable(R.drawable.chart_background))
            binding.homeChartLc.legend.isEnabled = false
            binding.homeChartLc.xAxis.valueFormatter = IndexAxisValueFormatter(temp)
            binding.homeChartLc.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            binding.homeChartLc.getRenderer().getPaintRender()
                .setShadowLayer(3f, 5f, 3f, Color.GRAY);

            binding.homeChartLc.invalidate()
        }
    }

    //차트에 더미 데이터 셋팅팅
    //7일치만 먼저 불러와보자 .
    @SuppressLint("UseCompatLoadingForDrawables")
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

        } else if (binding.homeYieldOnTv.visibility == View.VISIBLE) {  //수익률
            for (cur in 0 until profitList.size) { //until이 마지막 전까지
                //배열에 하나씩 꺼내보기
                val temp = profitList[cur]
                val tempVal = temp.profitRate.toFloat()          //
                Log.d("수익률", "${tempVal}")
                values.add(
                    Entry(
                        cur.toFloat(),
                        tempVal
                    )
                )         //X축 , Y축 값 등록 -> 기본적으로 float이라 int로 변환한후 가져와얗마.
            }
        }

        val set1 = LineDataSet(values, "")
        //수정
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.setDrawFilled(true)
        set1.setDrawHighlightIndicators(true)
        set1.fillDrawable = resources.getDrawable(R.drawable.chart_fill) //차트 밑에 색상
        set1.lineWidth = 1.95f
        set1.circleRadius = 5f
        set1.color = Color.parseColor("#EFEFFF")
        set1.setDrawCircleHole(false)
        set1.setDrawCircles(false)
        set1.highLightColor = Color.WHITE
        set1.setDrawValues(false)

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

    // 포트폴리오 API 호출 성공(계좌, 유저코인 리스트, 대표코인 리스트, 수익률 리스트 받아옴) , 단일 포폴 조회
    override fun portfolioSuccess(response: PortfolioResponse) {
        if(checkView){
            dismissLoadingDialog()
            when (response.code) {
                1000 -> {
                    // 계좌
                    getAccountResult(response)
                    // 코인 시세 받아오기
                    getCoinPrice(response)
                    // 수익률 리스트
                    val profitList = response.result.profitResultList
                    //AccountIdx, PortIdx, marketName 싱글톤에 셋팅
                    MyApplicationClass.myAccountIdx = response.result.accountIdx
                    MyApplicationClass.myPortIdx = response.result.portIdx
                    MyApplicationClass.marketName = response.result.marketName
                    when (marketName) {
                        "빗썸" -> binding.homeMarketTv.setText("빗썸")
                        "업비트" -> binding.homeMarketTv.setText("업비트")
                    }
                    marketIdx = market.get(response.result.marketName)!!     //마켓 인덱스 셋팅
                    accounName = response.result.accountName                //계좌이름
                    Log.d(
                        "인덱스 정보",
                        "account : ${MyApplicationClass.myAccountIdx} , Port : ${MyApplicationClass.myPortIdx}"
                    )
                    Log.d(
                        "Callidx",
                        "포트 : ${MyApplicationClass.myPortIdx}  , 계좌 : ${MyApplicationClass.myAccountIdx} , 마켓 정보 :${response.result.marketName} , 마켓 인덱스 : ${marketIdx}"
                    )
                    //계좌 인덱스 셋팅 됐을때 차트 호출
                    callGetProfit()
                }
                else -> {
                    showToast(response.message)
                }
            }
        }
    }

    // 업비트 시세 조회 API 호출 성공
    override fun marketPriceSuccess(marketCoinPriceMap: HashMap<String, Double>) {
        if (requireActivity() != null && checkView) {
            var currentSum = 0.0
            var sumBuyCoin = 0.0
            var userCoinPosition = 0
            var representCoinPosition = 0
            var userCoinCheck = false;
            var representCoinCheck = false;
            requireActivity().runOnUiThread() {
                // 소유 코인 시세 받아오기, 수익률 구하기
                for (i in userCoinList.indices) {
                    val symbol = userCoinList[i].symbol
                    if (marketCoinPriceMap.containsKey(symbol)) {
                        val marketPrice = marketCoinPriceMap.get(symbol)!!
                        coinPriceMap.put(symbol, marketPrice)
                        val change = marketCoinPriceMap.get(symbol + "change")
                        val amount = userCoinList[i].amount
                        val priceAvg = userCoinList[i].priceAvg
                        userCoinList[i].marketPrice = marketPrice
                        if (change != null) {
                            userCoinList[i].change = change
                        }
                        userCoinList[i].profit = getProfit(marketPrice, priceAvg, amount)
                        userCoinList[i].profitRate =
                            getProfitRate(marketPrice, priceAvg, amount)
                        userCoinPosition = i;
                        userCoinCheck = true
                    }
                }
                // 소유 코인 소득 구하기
                for (i in userCoinList.indices) {
                    val amount = userCoinList[i].amount
                    val priceAvg = userCoinList[i].priceAvg
                    val marketPrice = userCoinList[i].marketPrice
                    sumBuyCoin += amount * priceAvg
                    currentSum += marketPrice * amount
//                        userCoinPosition = i;
//                        userCoinCheck = true
                }
                // 대표 코인 시세 받아오기
                for (i in representCoinList.indices) {
                    val symbol = representCoinList[i].symbol
                    if (marketCoinPriceMap.containsKey(symbol)) {
                        val change = marketCoinPriceMap.get(symbol + "change")
                        representCoinList[i].marketPrice = marketCoinPriceMap.get(symbol)!!
                        if (change != null) {
                            representCoinList[i].change = change
                        }
                        representCoinPosition = i;
                        representCoinCheck = true
                    }
                }
                if (viewPagerPosition < portfolioList.size && currentSum != 0.0
                    && portfolioList[viewPagerPosition] is MyPortfolioFragment
                ) {
                    val myPortfolioFragment: MyPortfolioFragment =
                        portfolioList[viewPagerPosition] as MyPortfolioFragment
                    myPortfolioFragment.getAccountProfit(
                        currentSum,
                        sumBuyCoin
                    )    //업비트 시세 받아오면 함수실행해서 총자산갱신
                }
                //시세 호출하면 ViewModel 내부의 LiveData Update 이 후 , observer 패턴으로
                if (userCoinCheck) {
                    viewModel.getUpdateUserCoin(userCoinList, userCoinPosition)
                }
                if (representCoinCheck) {
                    viewModel.getUpdateRepresentCoin(representCoinList, representCoinPosition)
                }
                // 계좌 수익률 보내주기
            }
        }
    }


    // 바이낸스 시세 조회 API 호출 성공
    override fun binancePriceSuccess(binanceCoinPriceMap: HashMap<String, Double>) {
        var position = 0
        if (requireActivity() != null && checkView) {
            requireActivity().runOnUiThread() {
                // 대표 코인
                for (i in representCoinList.indices) {
                    val symbol = representCoinList[i].symbol
                    if (binanceCoinPriceMap.containsKey(symbol)) {
                        val upbitPrice = representCoinList[i].marketPrice
                        val binancePrice = binanceCoinPriceMap.get(symbol)!! * usdtPrice!!
                        var kimchi = ((upbitPrice - binancePrice) / upbitPrice) * 100
                        representCoinList[i].binancePrice = binancePrice
                        representCoinList[i].kimchi = kimchi
                        position = i;
                    }
                }
                viewModel.getUpdateRepresentCoin(representCoinList, position)
            }
        }
    }

    override fun coinPriceFailure(message: String) {

    }


    // 시세 받아오기
    fun getCoinPrice(response: PortfolioResponse) {
        val marketName = response.result.marketName
        val userCoinNameList = ArrayList<String>()
        val representCoinNameList = ArrayList<String>()
        // 리스트 초기화
        userCoinList.clear()
        representCoinList.clear()
        coinSymbolSet.clear()
        // 소켓 닫기
        upbitWebSocket?.close()
        bithumbWebSocket?.webSocket?.cancel()
        // 계좌
        getAccountResult(response)
        // 유저 코인 리스트
        this.userCoinList = response.result.userCoinList as ArrayList<PossesionCoinResult>
        // 대표 코인 리스트
        this.representCoinList =
            response.result.representCoinList as ArrayList<RepresentCoinResult>
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

        when (marketName) {
            "업비트" -> {
                upbitWebSocket = UpbitWebSocketListener(coinSymbolSet)
                upbitWebSocket?.setCoinView(this)
                upbitWebSocket?.start() // 업비트 웹 소켓 실행
                coinService.getUpbitCurrentPrice(
                    userCoinList,
                    representCoinList
                ) // 업비트 코인 시세 받아오기
            }
            "빗썸" -> {
                bithumbWebSocket = BithumbWebSocketListener(coinSymbolSet)
                bithumbWebSocket?.setCoinView(this)
                bithumbWebSocket?.start() // 빗썸 웹 소켓 실행
                coinService.getBithumbCurrentPrice(
                    userCoinList,
                    representCoinList
                ) // 빗썸 코인 시세 받아오기
            }
        }
        binanceWebSocket = BinanceWebSocketListener(coinSymbolSet)
        binanceWebSocket?.setCoinView(this)
        binanceWebSocket?.start()
        setRepresentRV()
        setRepresentPV()
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
        Log.d("주별 데이터 ", "${response}")
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
        Log.d("월별 데이터 ", "${response}")
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
        return AccountResult(
            accountIdx,
            accountName,
            property,
            totalProperty,
            userIdx,
            marketName
        )
    }

    override fun onPause() {
        super.onPause()
        checkView = false
        portfolioList.clear()   //초기화
    }

    override fun onResume() {
        super.onResume()
        checkView = true
        //상태바 색상 변경하기
        Log.d("onCreateView Home", "실행")
        requireActivity().window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        requireActivity().window!!.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.main_color)
    }

    override fun onDestroy() {
        super.onDestroy()
        checkView = false
        upbitWebSocket?.webSocket?.cancel() // 웹 소켓 닫기
        binanceWebSocket?.webSocket?.cancel()
        bithumbWebSocket?.webSocket?.cancel()
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

    override fun usdExchangeSuccess(exchangeRate: Double) {
        Log.d("환율 조회성공:", "usdtPrice: ${exchangeRate}")
        usdtPrice = exchangeRate.toInt()
    }
}