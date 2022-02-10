package com.bit.kodari.PossessionCoin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentTwitterBinding

class TwitterFragment : Fragment() {
    lateinit var binding: FragmentTwitterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTwitterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var web : WebView = view.findViewById(R.id.webapp)

        web.settings.javaScriptEnabled = true

        //스크롤바 없애기
        web.isHorizontalScrollBarEnabled = false
        web.isVerticalScrollBarEnabled = false

        val source = "<a class=\"twitter-timeline\" href=\"https://twitter.com/Bitcoin?ref_src=twsrc%5Etfw\"></a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>"

        web.loadData(source, "text/html", "UTF-8")

    }
}