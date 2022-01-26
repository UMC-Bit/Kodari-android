package com.bit.kodari.PossessionCoin

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.Adapter.coinData
import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinManagementAdapter
//import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinManagementRVAdapter
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentPossessionCoinManagementBinding

class PossessionCoinManagementFragment : Fragment() {
    lateinit var binding: FragmentPossessionCoinManagementBinding

    val coinList = arrayListOf(
        coinData(R.drawable.btc, "비트코인", "btc", "10000", "+1000", "10000", R.drawable.select_off_button),
        coinData(R.drawable.btc, "비트코인", "btc", "10000", "+1000", "10000", R.drawable.select_off_button),
        coinData(R.drawable.btc, "비트코인", "btc", "10000", "+1000", "10000", R.drawable.select_off_button),
        coinData(R.drawable.btc, "비트코인", "btc", "10000", "+1000", "10000", R.drawable.select_off_button),
    )



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPossessionCoinManagementBinding.inflate(inflater, container, false)

        moveLayout()
        memoDialog()
        deleteDialog()

        binding.possessionCoinManagementRV.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.possessionCoinManagementRV.adapter=PossessionCoinManagementAdapter(coinList)


            return binding.root
        }

    fun memoDialog()
    {
        binding.tempDialogBT.setOnClickListener {
            val memoDialogView=LayoutInflater.from(context as MainActivity).inflate(R.layout.fragment_possession_coin_memo_dialog, null)
            val memoDialogBuilder= AlertDialog.Builder(context as MainActivity)
                .setView(memoDialogView)

            memoDialogBuilder.show()

            val memoOffButton=memoDialogView.findViewById<TextView>(R.id.possession_coin_dialog_memo_off_TV)
            val memoOnButton=memoDialogView.findViewById<TextView>(R.id.possession_coin_dialog_memo_on_TV)
            val twitterOffButton=memoDialogView.findViewById<TextView>(R.id.possession_coin_dialog_twitter_off_TV)
            val twitterOnButton=memoDialogView.findViewById<TextView>(R.id.possession_coin_dialog_twitter_on_TV)

            memoOffButton.setOnClickListener {
                memoOnButton.visibility=View.VISIBLE
                twitterOnButton.visibility=View.GONE
            }
            twitterOffButton.setOnClickListener {
                twitterOnButton.visibility=View.VISIBLE
                memoOnButton.visibility=View.GONE
                memoOffButton.visibility=View.VISIBLE
            }
        }
    }

    fun deleteDialog()
    {
        binding.possessionCoinManagementDeleteButtonIB.setOnClickListener {
            val deleteDialogView=LayoutInflater.from(context as MainActivity).inflate(R.layout.fragment_possession_coin_delete_dialog, null)
            val deleteDialogBuilder=AlertDialog.Builder(context as MainActivity)
                .setView(deleteDialogView)

            val deleteAlertDialog = deleteDialogBuilder.show()

            val deleteConfirmButton=deleteDialogView.findViewById<TextView>(R.id.possession_coin_delete_dialog_delete_confirm_TV)
            val cancelButton=deleteDialogView.findViewById<TextView>(R.id.possession_coin_delete_dialog_cancel_TV)

            deleteConfirmButton.setOnClickListener {
                deleteAlertDialog.dismiss()
            }

            cancelButton.setOnClickListener {
                deleteAlertDialog.dismiss()
            }
        }
    }

    fun moveLayout()
    {
        binding.possessionCoinManagementAddTV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinSearchFragment()).commitAllowingStateLoss()
        }

        binding.possessionCoinManagementModifyOffButtonIB.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinModifyFragment()).commitAllowingStateLoss()
        }

        binding.possessionCoinManagementBeforeButtonBT.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, HomeFragment()).commitAllowingStateLoss()
        }
    }
}