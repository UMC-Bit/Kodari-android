package com.bit.kodari.PossessionCoin

//import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinManagementRVAdapter
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
import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinManagementAdapter
import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinData
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentPossessionCoinManagementBinding

class PossessionCoinManagementFragment : Fragment() {
    lateinit var binding: FragmentPossessionCoinManagementBinding

    val PossessionCoinList = arrayListOf(
        PossessionCoinData(R.drawable.btc, "비트코인", "btc", "10000", "+1000", "10000", R.drawable.select_off_button),
        PossessionCoinData(R.drawable.btc, "비트코인", "btc", "10000", "+1000", "10000", R.drawable.select_off_button),
        PossessionCoinData(R.drawable.btc, "비트코인", "btc", "10000", "+1000", "10000", R.drawable.select_off_button),
        PossessionCoinData(R.drawable.btc, "비트코인", "btc", "10000", "+1000", "10000", R.drawable.select_off_button),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPossessionCoinManagementBinding.inflate(inflater, container, false)

        moveLayout()
        memoDialog()

        binding.possessionCoinManagementRV.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.possessionCoinManagementRV.adapter=PossessionCoinManagementAdapter(PossessionCoinList)

        binding.possessionCoinManagementDeleteButtonIB.setOnClickListener {
            deleteDialog()
        }

        return binding.root
        }


    fun memoDialog()
    {
        binding.tempDialogBT.setOnClickListener {
            val memoDialogView=LayoutInflater.from(context as MainActivity).inflate(R.layout.fragment_memo_and_twitter, null)
            val memoDialogBuilder= AlertDialog.Builder(context as MainActivity)
                .setView(memoDialogView)

            memoDialogBuilder.show()
        }
    }

                fun deleteDialog()
                {
                    val deleteDialogView=LayoutInflater.from(context as MainActivity).inflate(R.layout.fragment_possession_coin_delete_dialog, null)
                    val deleteDialogBuilder=AlertDialog.Builder(context as MainActivity)
                        .setView(deleteDialogView)

                    val deleteAlertDialog = deleteDialogBuilder.show()

                    val deleteConfirmButton=deleteDialogView.findViewById<TextView>(R.id.possession_coin_delete_dialog_delete_confirm_TV)
                    val cancelButton=deleteDialogView.findViewById<TextView>(R.id.possession_coin_delete_dialog_cancel_TV)

                    // 여기에 어댑터와 연결해서 삭제 기능 불러오기
                    deleteConfirmButton.setOnClickListener {
                        deleteAlertDialog.dismiss()
                    }

                    cancelButton.setOnClickListener {
                        deleteAlertDialog.dismiss()
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