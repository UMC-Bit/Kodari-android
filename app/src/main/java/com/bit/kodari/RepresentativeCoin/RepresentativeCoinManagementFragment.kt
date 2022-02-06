package com.bit.kodari.RepresentativeCoin

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Main.Adapter.ManagementCoinData
import com.bit.kodari.Main.Adapter.RptCoinManagementAdapter
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentRepresentativeCoinManagementBinding

class RepresentativeCoinManagementFragment : Fragment() {

    lateinit var binding: FragmentRepresentativeCoinManagementBinding

    val ManagementCoinList = arrayListOf(
        ManagementCoinData(R.drawable.btc, "비트코인", "btc", "56,000,000", "56,000,000", "4.6%", R.drawable.select_off_button),
        ManagementCoinData(R.drawable.btc, "비트코인", "btc", "56,000,000", "56,000,000", "4.6%", R.drawable.select_off_button),
        ManagementCoinData(R.drawable.btc, "비트코인", "btc", "56,000,000", "56,000,000", "4.6%", R.drawable.select_off_button)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRepresentativeCoinManagementBinding.inflate(inflater , container , false)

        deleteDialog()

        binding.representativeCoinManagementRV.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.representativeCoinManagementRV.adapter= RptCoinManagementAdapter(ManagementCoinList)

        binding.representativeCoinManagementAddTV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, RepresentativeCoinSearchFragment()).commitAllowingStateLoss()
        }

        binding.representativeCoinManagementBeforeButtonBT.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, HomeFragment()).commitAllowingStateLoss()
        }

        return binding.root
    }

    fun deleteDialog()
    {
        binding.tempDeleteDialogBT.setOnClickListener {
            val deleteDialogView=LayoutInflater.from(context as MainActivity).inflate(R.layout.fragment_representative_coin_delete_dialog, null)
            val deleteDialogBuilder= AlertDialog.Builder(context as MainActivity)
                .setView(deleteDialogView)

            val deleteAlertDialog = deleteDialogBuilder.show()

            val deleteConfirmButton=deleteDialogView.findViewById<TextView>(R.id.representative_coin_delete_dialog_delete_confirm_TV)
            val cancelButton=deleteDialogView.findViewById<TextView>(R.id.representative_coin_delete_dialog_cancel_TV)

            deleteConfirmButton.setOnClickListener {
                deleteAlertDialog.dismiss()
            }

            cancelButton.setOnClickListener {
                deleteAlertDialog.dismiss()
            }
        }
    }
}