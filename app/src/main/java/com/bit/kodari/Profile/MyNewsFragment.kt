package com.bit.kodari.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.bit.kodari.R


class MyNewsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var web : WebView = view.findViewById(R.id.webapp)

        web.settings.javaScriptEnabled = true

        //스크롤바 없애기
        web.isHorizontalScrollBarEnabled = false
        web.isVerticalScrollBarEnabled = false

        val source = "<a class=\"twitter-timeline\" href=\"https://twitter.com/wavesprotocol?ref_src=twsrc%5Etfw\">Tweets by AxieInfinity</a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>"

        web.loadData(source, "text/html", "UTF-8")

    }

}