package com.bit.kodari.PossessionCoin

//import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinManagementRVAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Debate.DebateMainFragment
import com.bit.kodari.Debate.DebateModifyPostFragment
import com.bit.kodari.Debate.DeleteDialog
import com.bit.kodari.Debate.Service.DebateService
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinManagementAdapter
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinMgtDeleteView
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinMgtInsquireView
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtDeleteResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtInsquireResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtInsquireResult
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinSearchResult
import com.bit.kodari.PossessionCoin.Service.PsnCoinService
import com.bit.kodari.R
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.FragmentPossessionCoinManagementBinding

class PossessionCoinManagementFragment : Fragment(), PsnCoinMgtInsquireView, PsnCoinMgtDeleteView {
    lateinit var binding: FragmentPossessionCoinManagementBinding
    private lateinit var possessionCoinManagementAdapter: PossessionCoinManagementAdapter
    private var coinList = ArrayList<PsnCoinMgtInsquireResult>()

    override fun onStart() {
        super.onStart()
        getPossessionCoins()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPossessionCoinManagementBinding.inflate(inflater, container, false)

        moveLayout()
        memoDialog()

        binding.possessionCoinManagementDeleteButtonIB.setOnClickListener {
            // 선택 버튼 클릭 시에만 삭제 다이얼로그가 띄워짐
            val isClick = PossessionCoinManagementAdapter.isClick
            val position = PossessionCoinManagementAdapter.clickPosition
            if (isClick && position != -1) {
                val deleteDialogView = LayoutInflater.from(context as MainActivity)
                    .inflate(R.layout.fragment_possession_coin_delete_dialog, null)
                val deleteDialogBuilder = AlertDialog.Builder(context as MainActivity)
                    .setView(deleteDialogView)
                val deleteAlertDialog = deleteDialogBuilder.show()
                val position = PossessionCoinManagementAdapter.clickPosition
                deleteDialog(coinList[position].userCoinIdx)
                deleteAlertDialog.dismiss()
            }
        }


        binding.possessionCoinManagementModifyOffButtonIB.setOnClickListener {
            // 선택 버튼 클릭 시에만 수정 fragement로 이동 가능
            val isClick = PossessionCoinManagementAdapter.isClick
            val position = PossessionCoinManagementAdapter.clickPosition
            if (isClick && position != -1) {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, PossessionCoinModifyFragment().apply {
                        arguments = Bundle().apply {
                            putInt("coinIdx", coinList[position].coinIdx)
                            Log.d("checkcoinidx", "${coinList[position]}")
                            putString("coinImage", coinList[position].coinImg)
                            putString("coinName", coinList[position].coinName)
                            putString("coinSymbol", coinList[position].symbol)
                            putString("priceAvg", coinList[position].priceAvg)
                            putString("amount", coinList[position].amount)
                        }
                    }).commitAllowingStateLoss()
            }
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

    fun deleteDialog(userCoinIdx: Int) {
        val deleteDialogView=LayoutInflater.from(context as MainActivity).inflate(R.layout.fragment_possession_coin_delete_dialog, null)
        val deleteDialogBuilder=AlertDialog.Builder(context as MainActivity)
            .setView(deleteDialogView)

        val deleteAlertDialog = deleteDialogBuilder.show()

        val deleteConfirmButton=deleteDialogView.findViewById<TextView>(R.id.possession_coin_delete_dialog_delete_confirm_TV)
        val cancelButton=deleteDialogView.findViewById<TextView>(R.id.possession_coin_delete_dialog_cancel_TV)

        // 여기에 어댑터와 연결해서 삭제 기능 불러오기
        deleteConfirmButton.setOnClickListener {
                // 소유코인 삭제 API 호출 ->
                val psnCoinService = PsnCoinService()
                psnCoinService.setPsnCoinMgtDeleteView(this)
                psnCoinService.deletePsnCoin(userCoinIdx)
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

    fun setRecyclerView(){
        possessionCoinManagementAdapter = PossessionCoinManagementAdapter(coinList)
        //아이템 클릭 리스너를 현재 뷰에서 처리
        //Adapter에 있는 position값과 같이 HomeFragment로 넘어와서 자동 셋팅
        possessionCoinManagementAdapter.setMyItemClickListener(object :
            PossessionCoinManagementAdapter.MyItemClickListener{
            override fun onItemClick(item: PsnCoinMgtInsquireResult) {

            }
        })

        binding.possessionCoinManagementRV.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.possessionCoinManagementRV.adapter=possessionCoinManagementAdapter
    }

    fun getPossessionCoins(){
        val psnCoinService = PsnCoinService()
        psnCoinService.setPsnCoinMgtInsquireView(this)
        psnCoinService.getPsnCoinMgtInsquire()
    }

    override fun psnCoinInsquireSuccess(response: PsnCoinMgtInsquireResponse) {
        Log.d("InsquireSuccess" , "${response}")
        coinList = response.result
        Log.d("psnSuccesscoinSize", "${coinList.size}")

        setRecyclerView()
    }

    override fun psnCoinInsquireFailure(message: String) {
        Log.d("InsquireFailure", "코인 목록 불러오기 실패, ${message}")
    }

    override fun deletePsnCoinSuccess(response: PsnCoinMgtDeleteResponse) {
        when(response.code){
            1000 -> {
                PossessionCoinManagementAdapter.isClick=false
                PossessionCoinManagementAdapter.clickPosition=-1
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl , PossessionCoinManagementFragment()).commitAllowingStateLoss()

            }
            else -> {
                Toast.makeText(context, "${response.message}" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun deletePsnCoinFailure(message: String) {
        Log.d("deltePsnCoinFail" ,"${message}")
    }
}