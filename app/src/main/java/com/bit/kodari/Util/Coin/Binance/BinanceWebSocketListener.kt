package com.bit.kodari.Util.Coin.Binance

import android.util.Log
import com.bit.kodari.Util.Coin.CoinView
import com.bit.kodari.Util.Coin.Upbit.UsdtService
import okhttp3.*
import org.json.JSONObject

class BinanceWebSocketListener(coinSymbolSet: HashSet<String>) : WebSocketListener(), CoinView {
    var usdtPrice: Int = 1200 // usdt 가격
    private lateinit var coinView: CoinView
    fun setCoinView(coinView: CoinView) {
        this.coinView = coinView
        // Usdt 환율 받아옴
        val usdtService = UsdtService()
        usdtService.setCoinView(this)
        usdtService.getFirsUsdtPrice()  //얘는 뭐지
    }

    var webSocket: WebSocket? = null
    private val coinPriceMap = HashMap<String, Double>()
    private val coinSymbol = coinSymbolSet
    private val symbols = getCodes()

    override fun onOpen(webSocket: WebSocket?, response: Response) {
        this.webSocket = webSocket
        //webSocket.close(NORMAL_CLOSURE_STATUS, null) //없을 경우 끊임없이 서버와 통신함
    }


    override fun onMessage(webSocket: WebSocket, message: String) {
        var symbol = JSONObject(message).getString("s")
        symbol = symbol.replace("USDT","") // KRW- 제거
        val price = JSONObject(message).getDouble("c")
        coinPriceMap.put(symbol, price)
        coinPriceMap.put("usdt", usdtPrice.toDouble())
        Log.d("Binance_Socket", "Receiving bytes : ${message}")
        // TODO HomeFragment livedata 처리
        coinView.binancePriceSuccess(coinPriceMap)

    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("Binance_Socket","Closing : $code / $reason")
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        webSocket.cancel()
    }
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("Binance_Socket","Error : " + t.message)
        webSocket.cancel()
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
    // Web socket 시작
    fun start(){
        val request: Request = Request.Builder()
            .url("wss://stream.binance.com:9443/ws${symbols}")
            .build()
        var client: OkHttpClient = OkHttpClient()
        client.newWebSocket(request, this)
        client.dispatcher().executorService().shutdown()
    }
    // symbol -> codes 변환
    private fun getCodes(): String{
        val sb = StringBuilder()
        coinSymbol.forEach {
                it -> sb.append("/")
            sb.append(it.toString())
            sb.append("usdt@ticker")
        }
        return sb.toString().lowercase()
    }

    override fun upbitPriceSuccess(upbitCoinPriceMap: HashMap<String, Double>) {
        TODO("Not yet implemented")
    }

    override fun binancePriceSuccess(upbitCoinPriceMap: HashMap<String, Double>) {
        TODO("Not yet implemented")
    }

    override fun coinPriceFailure(message: String) {
        TODO("Not yet implemented")
    }

}