package com.example.mission1.callback

// 퍼미션 조정한다. 앱 전역에서 사용하기 때문에 코드 중복이 심하다
// 그래서 공통 코드로 봐서 Util를 만들겠다는 의미
// 퍼미션 허락/ 실패 상황에서 처리해야 할 업무는 곳곳에서 알아서 이용하게 된다.
interface PermissionCallback {
    fun onPermissionResult(isGranted: Boolean)

}