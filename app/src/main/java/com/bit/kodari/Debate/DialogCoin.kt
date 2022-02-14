package com.bit.kodari.Debate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Debate.Adapter.DebateCoinRVAdapter
import com.bit.kodari.Debate.PostData.DebateCoinResponse
import com.bit.kodari.Debate.PostData.DebateCoinResult
import com.bit.kodari.Debate.Retrofit.DebateCoinView
import com.bit.kodari.Debate.Service.DebateService
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.R
import com.bit.kodari.databinding.DialogCoinBinding

//코인 클릭시 해당 코인 이름을 bundle에 담아서 넘겨줘야함.
class DialogCoin : DialogFragment(), DebateCoinView {

    private var _binding : DialogCoinBinding? = null
    private val binding get() = _binding!!
    private var coinList = ArrayList<DebateCoinResult>()
    private var filteredList  = ArrayList<DebateCoinResult>()
    private lateinit var debateCoinRVAdapter: DebateCoinRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogCoinBinding.inflate(inflater,container,false)
        setListener()
        binding.dialogCloseIv.setOnClickListener {
            dialog!!.dismiss()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getCoins()
    }


    fun setRecyclerView(){
        debateCoinRVAdapter = DebateCoinRVAdapter(coinList)
        //아이템 클릭 리스너를 현재 뷰에서 처리
        //Adapter에 있는 position값과 같이 HomeFragment로 넘어와서 자동 셋팅
        debateCoinRVAdapter.setMyItemClickListener(object :DebateCoinRVAdapter.MyItemClickListener{
            override fun onItemClick(item: DebateCoinResult) {      //이 아이템 클릭시 작동하게해야함
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, DebateCoinPostFragment().apply {
                        arguments = Bundle().apply {
                            putString("coinName", item.coinName)
                            putInt("coinIdx" , item.coinIdx)
                        }
                    })
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
                //Toast.makeText(requireContext(),"${item.coinName}" , Toast.LENGTH_SHORT).show()
                dismiss()

            }
        })
        binding.dialogListRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.dialogListRv.adapter = debateCoinRVAdapter

    }

    fun getCoins(){
        val debateService = DebateService()
        debateService.setDebateCoinView(this)
        debateService.getCoinsAll()
    }

    override fun getCoinsAllSuccess(response: DebateCoinResponse) {
        coinList = response.result as ArrayList<DebateCoinResult>
        Log.d("getCoinsAllSuccess" ,"${coinList.size}")
        setRecyclerView()
    }

    override fun getCoinsAllFailure(message: String) {
        Log.d("DebateCoin", "코인 목록 불러오기 실패 ,${message}")
    }

    fun setListener(){
        binding.dialogInputEt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                    Log.d("setlistener", "실행")
                    var searchText =  binding.dialogInputEt.text.toString()
                    searchFilter(searchText)
            }
        })
    }

    fun searchFilter(searchText: String) {
        filteredList.clear()
        for (i in 0 until coinList.size) {
            if (coinList[i].coinName.toLowerCase()
                    .contains(searchText.toLowerCase())
            ) {
                filteredList.add(coinList[i])
            }
        }
        debateCoinRVAdapter.filterList(filteredList)
    }

    //다이얼로그 크기는 onResume에서 window이용해서 선언
    override fun onResume() {
        super.onResume()
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
    }


    //메모리 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}