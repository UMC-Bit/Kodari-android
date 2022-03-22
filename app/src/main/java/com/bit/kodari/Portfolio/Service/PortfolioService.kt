package com.bit.kodari.Portfolio.Service

import android.util.Log
import com.MyApplicationClass
import com.bit.kodari.Main.Data.PortIdxResponse
import com.bit.kodari.Main.Data.PortfolioResponse
import com.bit.kodari.Main.Service.HomeService
import com.bit.kodari.Portfolio.Data.*
import com.bit.kodari.Portfolio.Retrofit.PortManagementView
import com.bit.kodari.Portfolio.Retrofit.SearchCoinView
import com.bit.kodari.Portfolio.Retrofit.PortfolioInterface
import com.bit.kodari.Portfolio.Retrofit.PortfolioView
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinRetrofitInterface
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddInfo
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddTradeInfo
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddTradeResponse
import com.bit.kodari.Util.getJwt
import com.bit.kodari.Util.getRetorfit
import com.bit.kodari.Util.getUserIdx
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PortfolioService {

    private lateinit var portfolioView: PortfolioView
    private lateinit var searchCoinView: SearchCoinView
    private lateinit var portManagementView: PortManagementView

    fun setPortfolioView(portfolioView: PortfolioView){
        this.portfolioView = portfolioView
    }

    fun setSearchCoinView(searchCoinView: SearchCoinView){
        this.searchCoinView = searchCoinView
    }

    fun setPortManagementView(portManagementView: PortManagementView){
        this.portManagementView = portManagementView
    }

    // 유저 인덱스로 포트폴리오 List 조회
    fun getPortfolioList(userIdx: Int){
        val portService = getRetorfit().create(PortfolioInterface::class.java)
        portService.getAllPortfolio(userIdx, getJwt()).enqueue(object: Callback<PortIdxResponse> {
            override fun onResponse(
                call: Call<PortIdxResponse>,
                response: Response<PortIdxResponse>
            ) {
                Log.d("portIdx", "포트폴리오 리스트 불러오기 성공!")
                if(response.body()!!.result.size >= 0){
                    if(response.body()!!.result.size == 0){
                        portfolioView.getPortIdxSuccess(response.body()!!)
                    } else{
                        val portIdx = response.body()!!.result!![0].portIdx         //첫번째만 ? ? 이거 왜 이렇게해놨지 ?
                        portfolioView.getPortIdxSuccess(response.body()!!)
                        getPortfolioInfo(portIdx)                                   //이런식이면 안되지 않을까 ?
                    }

                }
            }
            override fun onFailure(call: Call<PortIdxResponse>, t: Throwable) {
                Log.d("portIdx", "불러오기 실패")

                portfolioView.getPortIdxFailure("${t}")
            }
        })
    }

    // 포트폴리오 인덱스로 포트폴리오 내용 조회
    fun getPortfolioInfo(portIdx: Int){
        val portService = getRetorfit().create(PortfolioInterface::class.java)
        portService.getPortfolioByPortIdx(portIdx, getJwt()).enqueue(object: Callback<PortfolioResponse>{
            override fun onResponse(
                call: Call<PortfolioResponse>,
                response: Response<PortfolioResponse>
            ) {
                Log.d("portFolio", response.body().toString())
                portfolioView.portfolioSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<PortfolioResponse>, t: Throwable) {
                Log.d("portIdx", "불러오기 실패")
                Log.d("portIdxTTTTTT" , t.toString())
            }

        })
    }

    // 코인 목록 불러오기
    fun getCoinsAll(){
        val portfolioService = getRetorfit().create(PortfolioInterface::class.java)

        portfolioService.getSearchCoinAll().enqueue(object : Callback<SearchCoinResponse> {
            override fun onResponse(
                call: Call<SearchCoinResponse>,
                response: Response<SearchCoinResponse>
            ) {
                searchCoinView.getSearchCoinAllSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<SearchCoinResponse>, t: Throwable) {
                searchCoinView.getSearchCoinAllFailure("t")
            }
        })
    }

    //마켓별 코인 조회
    fun getMarketCoin(marketIdx : Int){
        val portfolioService = getRetorfit().create(PortfolioInterface::class.java)
        portfolioService.getSearchMarketCoin(marketIdx).enqueue(object : Callback<SearchCoinResponse>{
            override fun onResponse(
                call: Call<SearchCoinResponse>,
                response: Response<SearchCoinResponse>
            ) {
                searchCoinView.getSearchCoinAllSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<SearchCoinResponse>, t: Throwable) {
                searchCoinView.getSearchCoinAllFailure("t")
            }
        })
    }


    //계좌생성 API , 소유코인으로 추가할 코인 리스트도 넘겨줘야 한다.
    fun postAccount(postAccountRequest: PostAccountRequest , addCoinList : ArrayList<PsnCoinAddTradeInfo>){
        val portfolioService = getRetorfit().create(PortfolioInterface::class.java)
        portfolioService.postAccount(getJwt()!!, postAccountRequest).enqueue(object : Callback<PostAccountResponse>{
            override fun onResponse(
                call: Call<PostAccountResponse>,
                response: Response<PostAccountResponse>
            ) {
                val resp = response.body()!!
                Log.d("postAccount " , "통신 성공")
                when(resp.code){
                  1000 -> { //계좌 등록 성공하면 포토폴리오 만들기.
                      Log.d("postAccount " , "계좌 등록 성공 , ${resp.result.accountIdx} , ${resp.result.accountName}")
                      val postPortRequest = PostPortFolioRequest(resp.result.accountIdx , getUserIdx())
                      postPort(postPortRequest , addCoinList)           //이어서 포토폴리오 생성 API 호출
                  }
                  else -> {
                      Log.d("postAccount " , "통신 실패 : ${resp.message}")
                      //실패했을떄 처리 모두 해줘야함
                      portManagementView.makePortFailure("${resp.message}")

                  }
                }
            }

            override fun onFailure(call: Call<PostAccountResponse>, t: Throwable) {
                Log.d("postAccount " , "통신 실패  $t")
                portManagementView.makePortFailure("${t}")
            }
        })

    }

    fun postPort(postPortRequest : PostPortFolioRequest , addCoinList : ArrayList<PsnCoinAddTradeInfo>){
        val portfolioService = getRetorfit().create(PortfolioInterface::class.java)
        portfolioService.postPortFolio(getJwt()!! , postPortRequest).enqueue(object : Callback<PostPortFolioResponse>{
            override fun onResponse(
                call: Call<PostPortFolioResponse>,
                response: Response<PostPortFolioResponse>
            ) {
                Log.d("postPort" , "통신은 성공")
                val resp = response.body()!!
                when(resp.code){
                  1000 -> {
                      Log.d("postPort" , "포폴 생성 성공 : ${resp.result.portIdx} , ${resp.result.accountIdx}")
                      //여기서 이제 소유코인 등록 API 호출 해야함.
                      for(cur in addCoinList){
                          //cur.accountIdx = resp.result.accountIdx   //계좌 인덱스 셋팅
                          cur.portIdx = resp.result.portIdx  //계좌 번호 셋팅
                          //                          //API 호출해야함 .
                          getPsnCoinAddPf(cur)
                      }

                  }
                  else -> {
                      Log.d("postPort" , "생성 실패 : ${resp.message}")
                      portManagementView.makePortFailure("${resp.message}")
                  }
                }
            }

            override fun onFailure(call: Call<PostPortFolioResponse>, t: Throwable) {
                Log.d("postPort" , "통신 실패")
                portManagementView.makePortFailure("${t}")
            }
        })

    }

    //거래내역 추가..

    //소유코인 추가 API -> 거래 생성으로 진행.
    fun getPsnCoinAddPf(psnCoinAddTradeInfo: PsnCoinAddTradeInfo){
        val psnCoinService = getRetorfit().create(PsnCoinRetrofitInterface::class.java)

        psnCoinService.getPsnCoinAddTradePortfolio(getJwt()!!, psnCoinAddTradeInfo).enqueue(object : Callback<PsnCoinAddTradeResponse> {
            override fun onResponse( // 통신 성공
                call: Call<PsnCoinAddTradeResponse>,
                response: Response<PsnCoinAddTradeResponse>
            ) {
                val resp = response.body()!!
                when(resp.code){
                  1000-> {
                      Log.d("getPsncoinAdd", "소유 코인 추가 성공")

                      portManagementView.makePortSuccess(resp)
                  }
                  else -> {
                      Log.d("getPsncoinAdd", "소유 코인 추가 실패 : ${resp.message}")
                      portManagementView.makePortFailure("${resp.message}")
                  }
                }
            }

            override fun onFailure(call: Call<PsnCoinAddTradeResponse>, t: Throwable) { // 통신 실패
                Log.d("getPsncoinAdd", "소유 코인 추가 실패  : ${t}")
                //Log.d("getPsncoinAdd" , "${PsnCoinAddResponse}")
                portManagementView.makePortFailure("${t}")
            }
        })
    }

}
