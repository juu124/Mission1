package com.example.mission1.util

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mission1.callback.DialogCallback
import com.example.mission1.databinding.DialogCustomBinding

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun showCustomDialog(context: Context, defaultResId: Int) {
    // alertDialog는 Builder에 의해서 객체가 생성된다.
    val builder = AlertDialog.Builder(context)

    // layout xml - inflate 시켜야 객체가 생성된다.
    // mainActivity에서의 LayoutInflater는 이미 activity에서 제공하기 때문에 작성형식이 다른 것이다.
    val inflater = context.getSystemService(LayoutInflater::class.java)
    val binding = DialogCustomBinding.inflate(inflater)
    builder.setView(binding.dialogImage)   // AlterDialog 의 본문을 개발자 뷰로...(개인적인 생각) binding.root도 되는 것 같다.
    val imageDialog = builder.create()
    imageDialog.show()

    // 데이터 이미지가 출력될 수 있어서.. dialog 사이즈를 조정해야한다.
    // show() 한 후에 조정해야 한다.
    // 리소스 이미지 획득 하는 로직
    val bitmap = BitmapFactory.decodeResource(context.resources, defaultResId)
    imageDialog.window?.setLayout(bitmap.width, bitmap.height)
}

fun showMessageDialog(
    context: Context,
    message: String,
    positiveText: String?,  // positive 버튼 문자열 원하는대로 넣기
    negativeText: String?,  // negative 버튼 문자열 원하는대로 넣기
    callback: DialogCallback    // 이벤트 처리를 위한 callback을 만들어서 나한테 전달하라.. 나는 callback 호출만 해준다.
) {
    AlertDialog.Builder(context).apply {
        setIcon(android.R.drawable.ic_dialog_info)
        setTitle("Message")
        setMessage(message)

        positiveText?.let {
            setPositiveButton(it) { _, _ ->
                callback.onPositiveCallBack()
            }
        }
        negativeText?.let {
            setNegativeButton(it) { _, _ ->
                callback.onNegativeCallBack()
            }
        }
    }.show()
}