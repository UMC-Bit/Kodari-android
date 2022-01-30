package com.bit.kodari.Debate

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.bit.kodari.R
import com.bit.kodari.databinding.DialogDeleteBinding

class DeleteDialog(val context:Context): View.OnClickListener {
    private val dialog = Dialog(context)

    fun showDiaglog(){
        dialog.setContentView(R.layout.dialog_delete)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT)
        //dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }


    override fun onClick(view: View?) {
        when(view?.id){
            R.id.delete_dialog_delete_btn-> {
                //게시글 삭제 API 호출 ->
                Toast.makeText(context,"게시글 삭제",Toast.LENGTH_SHORT).show()
            }
            //취소 버튼 누를시 취소
            R.id.delete_dialog_cancel_btn -> {dialog.dismiss()}
        }
    }
}