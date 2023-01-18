package com.example.nodrawler

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.GestureDetectorCompat
import kotlin.math.abs

class RvView(context: Context): LinearLayoutCompat(context) {
    private var gesture: GestureDetectorCompat
    private val view = LayoutInflater.from(context).inflate(R.layout.dlg_menu, this, true)
    private val listener = object : SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            Log.d("TTT", "scroll")
            return false
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            Log.d("TTT", "onFling")
            val distanceX = e2.x - e1.x
            val distanceY = e2.y - e1.y
            if (abs(distanceX) > abs(distanceY) && abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0) onSwipeRight() else onSwipeLeft()
                return true
            }
            return false
        }
    }
    var swipeListener: OnSwipeListener? = null
    init {
        gesture = GestureDetectorCompat(context, listener)
    }

    fun onSwipeLeft() {
        Log.d("TTT", "left")
    }

    fun onSwipeRight() {
        Log.d("TTT", "right")
        swipeListener?.onRightSwipe()
    }
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        if (gesture.onTouchEvent(event)) {
            return true
        }
        return super.onInterceptTouchEvent(event)
    }
    fun interface OnSwipeListener {
        fun onRightSwipe()
    }
}