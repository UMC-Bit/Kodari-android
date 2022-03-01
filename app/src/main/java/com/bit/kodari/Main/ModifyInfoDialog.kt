package com.bit.kodari.Main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bit.kodari.Main.Account.Data.*
import com.bit.kodari.Main.Account.Retrofit.ModifyDialogView
import com.bit.kodari.Main.Account.Servcie.AccountService
import com.bit.kodari.R
import com.bit.kodari.Util.formatPrice
import com.bit.kodari.databinding.DialogModifyInfoBinding
import kotlin.properties.Delegates


//커스텀 다이얼로그 만들기
class ModifyInfoDialog(val accountIdx: Int , val portIdx:Int) : DialogFragment() ,ModifyDialogView {

    private var _binding : DialogModifyInfoBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogModifyInfoBinding.inflate(inflater, container, false)
        setInit()
        setListener()
        return binding.root
    }

    fun setInit(){
        //argument로 넘어온 값 셋팅
        if(requireArguments().containsKey("accountName") && requireArguments().containsKey("myAsset")){
            binding.infoDialogInputAccountEt.setText(requireArguments().getString("accountName"))
            binding.infoDialogInputCashEt.setText(requireArguments().getString("myAsset"))
            Log.d("myasset", "${requireArguments().getString("myAsset")}")
        }
        Log.d("accountIdx " ,accountIdx.toString())
    }

    fun setListener() {

        binding.infoDialogExitBtn.setOnClickListener {
            dismiss()           //끝났을떄 다시 조회해야함 -> How?
        }

        binding.infoDialogModifyNameBtn.setOnClickListener {
            val accountName = binding.infoDialogInputAccountEt.text.toString()
            callModifyName(accountName)
        }

        binding.infoDialogModifyCashBtn.setOnClickListener {
            //val money = binding.infoDialogInputCashEt.text.toString().substring(0,binding.infoDialogInputCashEt.text.toString().length-1)
            val money = binding.infoDialogInputCashEt.text.toString().replace(",","")
            Log.d("money","${money}")
            callModifyProperty(money)
        }

        binding.infoDialogDeleteBtn.setOnClickListener {
            //포토폴리오 삭제 API 호출해야함.
            callDeletePort(portIdx)
       }

    }

    //이름 변경 성공했을 떄
    override fun modifyAccountNameSuccess(response: ModifyAccountNameResponse) {
        Toast.makeText(requireContext() ,"${response.result}" , Toast.LENGTH_SHORT).show()
    }
    //이름 변경 실패했을 때
    override fun modifyAccountNameFailure(message: String) {
        Toast.makeText(requireContext() ,message , Toast.LENGTH_SHORT).show()
    }

    //자산 변경 성공했을 떄
    override fun modifyPropertySuccess(response: ModifyPropertyResponse) {
        Toast.makeText(requireContext() ,"${response.result}" , Toast.LENGTH_SHORT).show()
    }

    //자산 변경 실패했을 때
    override fun modifyPropertyFailure(message: String) {
        Toast.makeText(requireContext() ,message , Toast.LENGTH_SHORT).show()
    }
    //삭제 버튼 눌렀을때 포토폴리오 삭제 성공
    override fun deletePortSuccess(response: DeletePortResponse) {
        Toast.makeText(requireContext() ,"${response.result}" , Toast.LENGTH_SHORT).show()
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_container_fl,HomeFragment())
            .commit()
        dismiss()
    }
    //삭제 버튼 눌렀을때 포토폴리오 삭제 실패
    override fun deletePortFailure(message: String) {
        Toast.makeText(requireContext() ,message , Toast.LENGTH_SHORT).show()
    }

    fun callModifyName(accountName:String){
        val modifyAccountNameRequest = ModifyAccountNameRequest(accountName)
        val accountService = AccountService()
        accountService.setModifyDialogView(this)
        accountService.modifyAccountName(accountIdx , modifyAccountNameRequest)

    }

    fun callModifyProperty(property : String){
        val modifyPropertyRequest = ModifyPropertyRequest(property)
        val accountService = AccountService()
        accountService.setModifyDialogView(this)
        accountService.modifyProperty(accountIdx , modifyPropertyRequest)
    }

    fun callDeletePort(portIdx: Int){
        val accountService = AccountService()
        accountService.setModifyDialogView(this)
        accountService.deletePort(portIdx)
    }
    //다이얼로그 크기는 onResume에서 window이용해서 선언
    override fun onResume() {
        super.onResume()
        dialog!!.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
    }


    //메모리 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}