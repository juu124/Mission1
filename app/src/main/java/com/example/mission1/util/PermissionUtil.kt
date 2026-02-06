package com.example.mission1.util

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mission1.callback.PermissionCallback

fun checkAllPermission(activity: ComponentActivity, callback: PermissionCallback) {
    // 퍼미션 여러개를 한꺼번에 다룬다.
    // 퍼미션을 문자열로 다룬다.
    val permissionSet = mutableListOf<String>()

    permissionSet.add(Manifest.permission.CALL_PHONE)

    val launcher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()    // 여러 퍼미션을 같이 요청
    ) { result -> // boolean 이 아니라. 여러 퍼미션을 값이다, collection type
        // 모두다 허용된 것인지 판단한다.
        val allGranted = result.values.all { it }
        callback.onPermissionResult(allGranted)
    }
    // 여러 퍼미션 조정 다이얼로그를 띄울려면 퍼미션 문자열 배열로 지정해야한다.
    launcher.launch(permissionSet.toTypedArray())
}