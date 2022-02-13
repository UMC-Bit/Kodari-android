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
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Main.Adapter.RptCoinManagementAdapter
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinManagementAdapter
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtInsquireResult
import com.bit.kodari.R
import com.bit.kodari.RepresentativeCoin.Retrofit.RptCoinMgtInsquireView
import com.bit.kodari.RepresentativeCoin.RetrofitData.DeleteRptCoinResponse
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinMgtInsquireResponse
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinMgtInsquireResult
import com.bit.kodari.RepresentativeCoin.Service.RptCoinService
import com.bit.kodari.databinding.FragmentRepresentativeCoinManagementBinding
import com.bumptech.glide.Glide
//삭제 누르면 끝나고 작업 다시 재조회 -> deletList 초기화
class RepresentativeCoinManagementFragment : BaseFragment<FragmentRepresentativeCoinManagementBinding>(FragmentRepresentativeCoinManagementBinding::inflate), RptCoinMgtInsquireView {

    private lateinit var rptCoinManagementAdapter: RptCoinManagementAdapter
    private var coinList = ArrayList<RptCoinMgtInsquireResult>()
    private var deleteRcoinList = HashSet<Int>()      //대표코인 삭제할 리스트들
    companion object{
        private var cnt = 0
    }
    override fun initAfterBinding() {
        deleteDialog()
        setListener()
        getRptCoins()
    }
    fun setListener(){
        binding.representativeCoinManagementAddTV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, RepresentativeCoinSearchFragment()).addToBackStack(null).commitAllowingStateLoss()
        }

        binding.representativeCoinManagementBeforeButtonBT.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, HomeFragment()).commitAllowingStateLoss()
        }


    }

    fun setRecyclerView(){
        rptCoinManagementAdapter= RptCoinManagementAdapter(coinList)
        //아이템 클릭 리스너를 현재 뷰에서 처리
        //Adapter에 있는 position값과 같이 HomeFragment로 넘어와서 자동 셋팅
        rptCoinManagementAdapter.setMyItemClickListener(object :
            RptCoinManagementAdapter.MyItemClickListener{
            override fun onItemCheck(item: RptCoinMgtInsquireResult) {  //선택했을때 -> off 눌렀을때
                item.isChecked = true                                   //true로 변경
                if(!deleteRcoinList.contains(item.representIdx)){
                    deleteRcoinList.add(item.representIdx)
                    Log.d("deleteRCoin", "삭제 리스트 추가 : ${item.representIdx}")
                }
            }

            override fun onItemUnCheck(item: RptCoinMgtInsquireResult) {    //선택 해제했을때 -> on 눌렀을 때
                item.isChecked = false
                if(deleteRcoinList.contains(item.representIdx)){
                    deleteRcoinList.remove(item.representIdx)
                    Log.d("deleteRCoin", "삭제 리스트 제거 : ${item.representIdx}")
                }
            }
        })

        binding.representativeCoinManagementRV.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.representativeCoinManagementRV.adapter= rptCoinManagementAdapter
    }

    //대표 코인 조회
    fun getRptCoins(){
        val rptCoinService = RptCoinService()
        rptCoinService.setRptCoinMgtInsquireView(this)
        showLoadingDialog(requireContext())
        rptCoinService.getRptCoinMgtInsquire()
    }

    fun deleteRcoin(){
        val rptCoinService = RptCoinService()
        rptCoinService.setRptCoinMgtInsquireView(this)
        showLoadingDialog(requireContext())
        rptCoinService.deleteRptCoin(deleteRcoinList)
    }

    fun deleteDialog()
    {
        binding.representativeCoinManagementDeleteButtonIB.setOnClickListener {
            val deleteDialogView=LayoutInflater.from(context as MainActivity).inflate(R.layout.fragment_representative_coin_delete_dialog, null)
            val deleteDialogBuilder= AlertDialog.Builder(context as MainActivity)
                .setView(deleteDialogView)

            val deleteAlertDialog = deleteDialogBuilder.show()

            val deleteConfirmButton=deleteDialogView.findViewById<TextView>(R.id.representative_coin_delete_dialog_delete_confirm_TV)
            val cancelButton=deleteDialogView.findViewById<TextView>(R.id.representative_coin_delete_dialog_cancel_TV)

            deleteConfirmButton.setOnClickListener {
                deleteRcoin()       //삭제눌렀을때 삭제
                deleteAlertDialog.dismiss()
            }

            cancelButton.setOnClickListener {
                deleteAlertDialog.dismiss()
            }
        }
    }


    override fun rptCoinInsquireSuccess(response: RptCoinMgtInsquireResponse) {
        dismissLoadingDialog()
        Log.d("InsquireSuccess" , "${response}")
        coinList = response.result
        //널값 넘어오면서 오류
        //Log.d("psnSuccesscoinSize", "${coinList.size}")

        setRecyclerView()
    }

    override fun rptCoinInsquireFailure(message: String) {
        dismissLoadingDialog()
        Log.d("InsquireFailure", "코인 목록 불러오기 실패, ${message}")
    }

    //삭제 성공
    override fun deleteRptCoinSuccess(response: DeleteRptCoinResponse) {
        cnt++
        Log.d("deleteRCoin", "삭제 리스트 추가 : ${deleteRcoinList.size}")
        if(cnt == deleteRcoinList.size){
            dismissLoadingDialog()
            getRptCoins()           //삭제 완료됐으면 재호출
            deleteRcoinList.clear() //삭제 리스트 초기화
            Log.d("deleteRCoin", "삭제 리스트 추가 : ${deleteRcoinList.size}")
        }

    }
    //삭제 실패
    override fun deleteRptCoinFailure(message: String) {
        dismissLoadingDialog()
        showToast(message)
    }
}