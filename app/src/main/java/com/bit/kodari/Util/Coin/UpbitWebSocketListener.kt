package com.bit.kodari.Util.Coin

import android.os.Handler
import android.util.Log
import okhttp3.*
import okio.ByteString
import org.json.JSONObject

/*
    업비트 시세 받아오는 소켓 리스너
 */
class UpbitWebSocketListener(coinSymbolSet: HashSet<String>) : WebSocketListener(){
    private lateinit var coinView: CoinView

    fun setCoinView(coinView: CoinView) {
        this.coinView = coinView
    }

    lateinit var webSocket: WebSocket
    private val coinPriceMap = HashMap<String, Double>()
    private val coinSymbol = coinSymbolSet
    private val symbols = getCodes()

    override fun onOpen(webSocket: WebSocket, response: Response) {
        val text = "[{\"ticket\":\"kodari\"},{\"type\":\"ticker\",\"codes\":[${symbols}]}]"
        webSocket.send(text)
        this.webSocket = webSocket
        //webSocket.close(NORMAL_CLOSURE_STATUS, null) //없을 경우 끊임없이 서버와 통신함
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        val message = bytes.utf8()
        var symbol = JSONObject(message).getString("code")
        symbol = symbol.replace("KRW-","") // KRW- 제거
        val price = JSONObject(message).getDouble("trade_price")
        coinPriceMap.put(symbol, price)
        Log.d("Upbit_Socket", "Receiving bytes : ${bytes.utf8()}")
        // TODO HomeFragment livedata 처리
        coinView.upbitPriceSuccess(coinPriceMap)

    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("Upbit_Socket","Closing : $code / $reason")
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        webSocket.cancel()
    }
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("Upbit_Socket","Error : " + t.message)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
    // Web socket 시작
    fun start(){
        val request: Request = Request.Builder()
            .url("wss://api.upbit.com/websocket/v1")
            .build()
        var client: OkHttpClient = OkHttpClient()
        client.newWebSocket(request, this)
        client.dispatcher().executorService().shutdown()
    }
    // symbol -> codes 변환
    private fun getCodes(): String{
        val sb = StringBuilder()
        coinSymbol.forEach {
            it -> sb.append("\"")
            sb.append("KRW-")
            sb.append(it.toString())
            sb.append("\",")
        }
        sb.deleteCharAt(sb.length-1) // "," 제거
        return sb.toString()
    }

}