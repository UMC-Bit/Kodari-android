package com.bit.kodari.RepresentativeCoin

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Main.Adapter.RptCoinManagementAdapter
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinManagementAdapter
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtInsquireResult
import com.bit.kodari.R
import com.bit.kodari.RepresentativeCoin.Retrofit.RptCoinMgtInsquireView
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinMgtInsquireResponse
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinMgtInsquireResult
import com.bit.kodari.RepresentativeCoin.Service.RptCoinService
import com.bit.kodari.databinding.FragmentRepresentativeCoinManagementBinding
import com.bumptech.glide.Glide

class RepresentativeCoinManagementFragment : Fragment(), RptCoinMgtInsquireView {

    lateinit var binding: FragmentRepresentativeCoinManagementBinding
    private lateinit var rptCoinManagementAdapter: RptCoinManagementAdapter
    private var coinList = ArrayList<RptCoinMgtInsquireResult>()

    override fun onStart() {
        super.onStart()
        getRptCoins()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRepresentativeCoinManagementBinding.inflate(inflater , container , false)

        deleteDialog()

        binding.representativeCoinManagementAddTV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, RepresentativeCoinSearchFragment()).addToBackStack(null).commitAllowingStateLoss()
        }

        binding.representativeCoinManagementBeforeButtonBT.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, HomeFragment()).addToBackStack(null).commitAllowingStateLoss()
        }

        return binding.root
    }

    fun setRecyclerView(){
        rptCoinManagementAdapter= RptCoinManagementAdapter(coinList)
        //아이템 클릭 리스너를 현재 뷰에서 처리
        //Adapter에 있는 position값과 같이 HomeFragment로 넘어와서 자동 셋팅
        rptCoinManagementAdapter.setMyItemClickListener(object :
            RptCoinManagementAdapter.MyItemClickListener{

            override fun onItemClick(item: RptCoinMgtInsquireResult) {
                TODO("Not yet implemented")
            }
        })

        binding.representativeCoinManagementRV.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.representativeCoinManagementRV.adapter= rptCoinManagementAdapter
    }


    fun getRptCoins(){
        val rptCoinService = RptCoinService()
        rptCoinService.setRptCoinMgtInsquireView(this)
        rptCoinService.getRptCoinMgtInsquire()
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



    override fun rptCoinInsquireSuccess(response: RptCoinMgtInsquireResponse) {
        Log.d("InsquireSuccess" , "${response}")
        coinList = response.result
        Log.d("psnSuccesscoinSize", "${coinList.size}")

        setRecyclerView()
    }

    override fun rptCoinInsquireFailure(message: String) {
        Log.d("InsquireFailure", "코인 목록 불러오기 실패, ${message}")
    }
}