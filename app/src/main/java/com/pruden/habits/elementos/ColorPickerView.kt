package com.pruden.habits.elementos
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

class ColorPickerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    private val hsv = FloatArray(3)
    private var selectedColor: Int = Color.WHITE
    private var listener: ((Int) -> Unit)? = null

    private var indicatorX: Float = 0f
    private var indicatorY: Float = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val radius = min(width, height) / 2f
        val cx = width / 2f
        val cy = height / 2f

        // Dibuja el degradado radial
        val shader = SweepGradient(cx, cy, createHueColors(), null)
        paint.shader = shader
        canvas.drawCircle(cx, cy, radius, paint)

        // Dibuja el indicador
        canvas.drawCircle(indicatorX, indicatorY, 20f, indicatorPaint)
    }

    private fun createHueColors(): IntArray {
        val colors = IntArray(361)
        for (i in 0..360) {
            hsv[0] = i.toFloat()
            hsv[1] = 1f
            hsv[2] = 1f
            colors[i] = Color.HSVToColor(hsv)
        }
        return colors
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val cx = width / 2f
        val cy = height / 2f
        val dx = event.x - cx
        val dy = event.y - cy
        val distance = sqrt(dx * dx + dy * dy)
        val radius = min(width, height) / 2f

        if (distance <= radius) {
            val angle = (Math.toDegrees(Math.atan2(dy.toDouble(), dx.toDouble())) + 360) % 360

            hsv[0] = angle.toFloat()
            hsv[1] = distance / radius
            hsv[2] = 1f
            selectedColor = Color.HSVToColor(hsv)

            // Actualiza las coordenadas del indicador
            indicatorX = cx + distance * cos(Math.toRadians(angle)).toFloat()
            indicatorY = cy + distance * sin(Math.toRadians(angle)).toFloat()

            invalidate() // Redibuja la vista
            listener?.invoke(selectedColor)
        }
        return true
    }

    fun setOnColorSelectedListener(listener: (Int) -> Unit) {
        this.listener = listener
    }
}
