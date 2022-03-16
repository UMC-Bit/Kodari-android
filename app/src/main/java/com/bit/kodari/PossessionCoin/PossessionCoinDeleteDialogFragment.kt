package com.bit.kodari.PossessionCoin

import android.R
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.databinding.FragmentPossessionCoinDeleteDialogBinding


class PossessionCoinDeleteDialogFragment : Fragment(){
    lateinit var binding: FragmentPossessionCoinDeleteDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPossessionCoinDeleteDialogBinding.inflate(inflater, container, false)
//        Spannable span = binding.possessionCoinDeleteDialogAskTV.getText().setSpan(ForegroundColorSpan(Color.RED), 0, 5, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)

        //삭제 빨갛게 만들기
        val builder = SpannableStringBuilder(binding.possessionCoinDeleteDialogAskTV.text)
        builder.setSpan(ForegroundColorSpan(Color.RED) , 7,9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.possessionCoinDeleteDialogAskTV.setText(builder)

        return binding.root
    }
}