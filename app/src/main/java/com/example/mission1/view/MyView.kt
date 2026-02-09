package com.example.mission1.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.mission1.R

// 커스텀 뷰 클래스
// 커스텀 뷰 클래스를 만들었다면, => 이 뷰를 이용해 activity 화면을 구성해야한다.
// 그렇다면, 액티비티 개발자가 뷰를 어떻게 생성해서 이용할 수 있을까?
// 어떻게 이용하냐면, 코드에서 직접 생성하거나, layout xml 에 동록해서 생성한다.
// 그렇다면 그때 호출되는 생성자는 어떤걸까? => 상황에 따라 호출되는 생성자가 다르다.
// view가 가지고 있는 생성자는 3개가 있다. 이용 범위를 높일려면 3개의 생성자를 모두 선언하는 것이 좋다.

// 주 생성자 1개 : 코드에서 직접 사용할 수 있다. (attrs 속성은 넣을게 없다는 의미)
// 보조 생성자 2개 : 주 생성자만으로 코드에서 attrs같은 값을 가져올 수 없기 때문에 보조생성자를 사용한다. 보조생성자를 주생성자를 호출하기 때문에 가능하다.
class MyView(
    context: Context,
    attrs: AttributeSet? = null,   // layout xml 에 등록되어 사용된다면, xml에 선언된 속성 값 획득할 수 있다.
    defStyleAttr: Int = 0          // layout xml 에 등록되어 있는 style 정보가 있다면, 그 정보를 획득할 수 있다.
) : View(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)

    var score: Int = 0
        set(value) { // activity에서 score를 전달할 목적으로 호출한다. 그래서 setter를 사용한다.
            field = value
            // set이 호출되었다. 새로운 시험점수가 등록된 것이다.
            // 현재 80점으로 도넛 표시 그림이 그려졌는데, 갑자기 90점이 대입되었다. => 즉, 그림이 다시 그려져야한다.
            invalidate() // 그림을 다시 그려줘! 이렇게 되면 onDraw() 함수가 다시 호출된다.
            // 시험 점수를 변경하고 invalidate()를 호출해서 변경한 값으로 다시 그려지게 한다.
        }

    private var color: Int = 0

    init {
        // 속성(attrs) 값이 지정이 되어 있다면
        attrs?.let {
            // 원하는 속성 값을 획득한다. attrs.xml에서 만든 customColor
            val a = context.obtainStyledAttributes(it, R.styleable.AAA)
            color = a.getColor(R.styleable.AAA_customColor, Color.YELLOW)
            a.recycle() // 재 사용 풀에 등록.. 동일 뷰가 한 앱에서 여러분 사용 가능하다. 재사용을 위해.
        }
    }

    // 모든 뷰는 화면에 각자의 내용이 출력되어야 한다.
    // 화면 출력 내용을 그리기 위해서 자동 호출된다.
    // 최초 한번 호출되고, 이후 invalidate() 시마다 반복 호출된다.
    // Canvas - drawing api를 제공한다. (사각형, 문자열, 버튼 등등을 그리는 api)
    override fun onDraw(canvas: Canvas) {
        // 화면 지정된 색성으로 지운다..
        // 지우고 다시 그려야한다. 안지우면 이전에 그린게 남아있기 때문이다.
        canvas.drawColor(Color.alpha(Color.CYAN))

        // 그리기 위한 사이즈를 알아낸다.
        // 그림을 그리기 위한 사이즈를 dimens.xml 에서 획득한다.
        val size = resources.getDimensionPixelSize(R.dimen.myview_size)
        val myStrokeWidth = resources.getDimensionPixelOffset(R.dimen.myview_stroke_width)

        // 그리기 위한 사각형 정보
        val rect =
            RectF(
                myStrokeWidth.toFloat(),
                myStrokeWidth.toFloat(),
                (size - myStrokeWidth).toFloat(),
                (size - myStrokeWidth).toFloat()
            )

        // 그리기 옵션 - color, text라면 font
        val paint = Paint().apply {
            color = Color.GRAY
            style = Paint.Style.STROKE  // 선만 그린다는 의미
            isAntiAlias = true          // 전산 그래픽의 공통 용어. 원 같은 곡선을 그릴때 울퉁불퉁이 아닌 매끄럽게 칠해주는 작업
            strokeWidth = myStrokeWidth.toFloat()
        }

        // 기본 색으로 360도 그린다.
        canvas.drawArc(rect, 0f, 360f, false, paint)

        // 시험 점수에 해당되는 각도를 계산한다.
        val endAngle = (360 * score) / 100f

        // 시험 점수에 해당되는 호를 덧칠하듯이 색상을 바꿔서 그린다.
        paint.color = if (color == 0) Color.RED else color
        canvas.drawArc(rect, -90f, endAngle, false, paint)
    }
}