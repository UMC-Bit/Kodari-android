package com.bit.kodari.Util.Coin.Bithumb

import android.util.Log
import com.bit.kodari.Util.Coin.CoinView
import com.bit.kodari.Util.Coin.Upbit.UpbitWebSocketListener
import okhttp3.*
import okio.ByteString
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/*
    빗썸 코인 시세 소켓 리스너
 */
class BithumbWebSocketListener(coinSymbolSet: HashSet<String>) : WebSocketListener() {
    private lateinit var coinView: CoinView

    fun setCoinView(coinView: CoinView) {
        this.coinView = coinView
    }

    var webSocket: WebSocket? = null
    private val coinSymbol = coinSymbolSet
    private val symbols = getCodes()
    private lateinit var text: String
    //처음에 onOpen으로 소켓열음
    override fun onOpen(webSocket: WebSocket, response: Response) {
        // {"type":"transaction", "symbols":["BTC_KRW" , "ETH_KRW"]}
        this.webSocket = webSocket
        text = "{\"type\":\"transaction\",\"symbols\":[${symbols}]}"
        webSocket.send(text)
        //webSocket.close(NORMAL_CLOSURE_STATUS, null) //없을 경우 끊임없이 서버와 통신함
    }

    //onMessage로 응답 받고 HomeFragment로 넘김
    override fun onMessage(webSocket: WebSocket, message: String) {
        this.webSocket = webSocket
        val coinPriceMap = HashMap<String, Double>()
        try {
            val content = JSONObject(message).getJSONObject("content")
            val list = content.getJSONArray("list")
            val response = list.getJSONObject(0)
            var symbol = response.getString("symbol")
            symbol = symbol.replace("_KRW", "") // _KRW 제거
            val price = response.getDouble("contPrice") // 가격 받아오기
            coinPriceMap.put(symbol, price)
            val change = response.getString("buySellGb") // 체결종류(1:매도체결, 2:매수체결)
            var changeNum = 0.0
            if (change.equals("2")) {
                changeNum = 1.0
            } else if (change.equals("1")) {
                changeNum = -1.0
            }
            coinPriceMap.put(symbol + "change", changeNum)
            Log.d("Bithumb_Socket", "Receiving String : ${message}")
            coinView.marketPriceSuccess(coinPriceMap)
        }catch(e: JSONException){
            webSocket.send(text) // 응답 받은 후 다시 데이터 넘겨주기
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("Bithumb_Socket", "Closing : $code / $reason")
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        webSocket.cancel()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("Bithumb_Socket", "Error : " + t.message)
        webSocket.cancel()
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }

    // Web socket 시작
    fun start() {
        val request: Request = Request.Builder()
            .url("wss://pubwss.bithumb.com/pub/ws")
            .build()
        var client: OkHttpClient = OkHttpClient()
        client.newWebSocket(request, this)
        client.dispatcher().executorService().shutdown()

    }
    // symbol -> 빗썸 symbols 변환
    private fun getCodes(): String {

        val sb = StringBuilder()
        coinSymbol.forEach { it ->
            sb.append("\"")
            sb.append(it.toString())
            sb.append("_KRW")
            sb.append("\",")
        }
        if(sb.length > 0)
            sb.deleteCharAt(sb.length - 1) // "," 제거
        return sb.toString()
    }
}