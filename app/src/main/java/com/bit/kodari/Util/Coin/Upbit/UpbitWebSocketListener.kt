package com.bit.kodari.Util.Coin.Upbit

import android.util.Log
import com.bit.kodari.Util.Coin.CoinView
import okhttp3.*
import okio.ByteString
import org.json.JSONObject

/*
    업비트 시세 받아오는 소켓 리스너
 */
class UpbitWebSocketListener(coinSymbolSet: HashSet<String>) : WebSocketListener(){
    private lateinit var coinView: CoinView
    private var checkClose = false
    fun setCoinView(coinView: CoinView) {
        this.coinView = coinView
    }

    var webSocket: WebSocket? = null
    private val coinSymbol = coinSymbolSet
    private val symbols = getCodes()

    //처음에 onOpen으로 소켓열음
    override fun onOpen(webSocket: WebSocket, response: Response) {
        val text = "[{\"ticket\":\"kodari\"},{\"type\":\"ticker\",\"codes\":[${symbols}]}]"
        webSocket.send(text)
        this.webSocket = webSocket
        //webSocket.close(NORMAL_CLOSURE_STATUS, null) //없을 경우 끊임없이 서버와 통신함
    }

    //onMessage로 응답 받고 HomeFragment로 넘김
    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        this.webSocket = webSocket
        if(checkClose){
            Log.d("Upbit_Socket","Closing")
            webSocket?.close(NORMAL_CLOSURE_STATUS, null)
            webSocket?.cancel()
        }
        val coinPriceMap = HashMap<String, Double>()
        val message = bytes.utf8()
        var symbol = JSONObject(message).getString("code")
        symbol = symbol.replace("KRW-","") // KRW- 제거
        val price = JSONObject(message).getDouble("trade_price") // 가격 받아오기
        coinPriceMap.put(symbol, price)
        val change = JSONObject(message).getString("ask_bid")
        var changeNum = 0.0
        if(change.equals("ASK")){
            changeNum = -1.0
        }else if(change.equals("BID")){
            changeNum = 1.0
        }
        coinPriceMap.put(symbol+"change",changeNum) // 전일 대비, RISE(상승), EVEN(보합), FALL(하락)
        Log.d("Upbit_Socket", "Receiving bytes : ${bytes.utf8()}")
        coinView.marketPriceSuccess(coinPriceMap)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("Upbit_Socket","Closing : $code / $reason")
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        webSocket.cancel()
    }
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("Upbit_Socket","Error : " + t.message)
        webSocket.cancel()
    }

    companion object {
        const val NORMAL_CLOSURE_STATUS = 1000
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
    fun close(){
        checkClose = true
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