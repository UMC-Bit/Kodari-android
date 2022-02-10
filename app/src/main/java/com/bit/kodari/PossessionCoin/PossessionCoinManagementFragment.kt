package com.bit.kodari.PossessionCoin

//import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinManagementRVAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinManagementAdapter
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinMgtInsquireView
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtInsquireResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtInsquireResult
import com.bit.kodari.PossessionCoin.Service.PsnCoinService
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentPossessionCoinManagementBinding

class PossessionCoinManagementFragment : Fragment(), PsnCoinMgtInsquireView {
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
            deleteDialog()
        }

        return binding.root
        }


    fun memoDialog()
    {
        binding.tempDialogBT.setOnClickListener {
            val memoDialogView=LayoutInflater.from(context as MainActivity).inflate(R.layout.dialog_memo_and_twitter, null)
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
                .replace(R.id.main_container_fl, PossessionCoinSearchFragment()).addToBackStack(null).commitAllowingStateLoss()
        }

        binding.possessionCoinManagementModifyOffButtonIB.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinModifyFragment()).addToBackStack(null).commitAllowingStateLoss()
        }

        binding.possessionCoinManagementBeforeButtonBT.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, HomeFragment()).addToBackStack(null).commitAllowingStateLoss()
        }
    }

    fun setRecyclerView(){
        possessionCoinManagementAdapter = PossessionCoinManagementAdapter(coinList)
        //아이템 클릭 리스너를 현재 뷰에서 처리
        //Adapter에 있는 position값과 같이 HomeFragment로 넘어와서 자동 셋팅
        possessionCoinManagementAdapter.setMyItemClickListener(object :PossessionCoinManagementAdapter.MyItemClickListener{

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
        Log.d("psnSuccesscoinSize", "${coinList.size}")         //리스트가 없다 ..?

        setRecyclerView()
    }

    override fun psnCoinInsquireFailure(message: String) {
        Log.d("InsquireFailure", "코인 목록 불러오기 실패, ${message}")
    }
}