package com.example.mission1.callback

// 앱 전역에서 alertdialog 띄울일이 많을 것 같다는 가정으로인해서
// 인터페이스로 만들었음
// util 함수를 만들어서, dialog를 함수 호출해서 띄우게 하고 싶다는 의도
// dialog의 버튼 이벤트 처리는 dialog 띄우는 곳에서 해야 한다.
// util에서 버튼 이벤트 처리를 위한 callback을 받아들여 이벤트가 발생하면 callback을 호출만 하게 구현하고 싶다!
interface DialogCallback {
    fun onPositiveCallBack()
    fun onNegativeCallBack()
}