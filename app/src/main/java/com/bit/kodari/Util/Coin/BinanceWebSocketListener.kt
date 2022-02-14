package com.bit.kodari.Util.Coin

import android.util.Log
import com.bit.kodari.Main.HomeFragment
import okhttp3.*
import okio.ByteString
import org.json.JSONObject
import java.text.DecimalFormat

class BinanceWebSocketListener(coinSymbolSet: HashSet<String>) : WebSocketListener() {
    private lateinit var coinView: CoinView
    fun setCoinView(coinView: CoinView) {
        this.coinView = coinView
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

}