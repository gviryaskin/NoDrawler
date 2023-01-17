package com.example.nodrawler

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.View.OnTouchListener
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlin.math.abs


class MenuDialog : DialogFragment() {
    lateinit var recycler: RecyclerView
    lateinit var v:View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        v = LayoutInflater.from(requireContext()).inflate(R.layout.dlg_menu,null,false)

        recycler=v.findViewById<RecyclerView>(R.id.rView)
        recycler.layoutManager=LinearLayoutManager(requireContext())
        recycler.adapter=PlaceholderAdapter()

        return AlertDialog.Builder(requireContext(),R.style.DialogMenu).setView(v).create()
    }

    class PlaceholderVH(itemView: View) : ViewHolder(itemView){
        val text:TextView=itemView.findViewById(R.id.tView)
    }

    class PlaceholderAdapter: RecyclerView.Adapter<PlaceholderVH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceholderVH {
            return PlaceholderVH(LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false))
        }

        override fun onBindViewHolder(holder: PlaceholderVH, position: Int) {
            holder.text.text="Item $position"
        }

        override fun getItemCount(): Int {
            return 10
        }
    }

    override fun onResume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics =
                requireActivity().windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            val width = windowMetrics.bounds.width() - insets.left -
                    insets.right
            val height = windowMetrics.bounds.height() - insets.top -
                    insets.bottom
            val window = dialog!!.window
            if (window != null) {
                window.setLayout((width * 0.60).toInt(), (height  * 1).toInt())
                window.setGravity(Gravity.END or Gravity.TOP)
                window.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            }

            val params = v.layoutParams
            params.width=(width * 0.60).toInt()
            params.height=(height * 1).toInt()
            v.layoutParams=params


            super.onResume()
        } else {

            val window = dialog!!.window
            val size = Point()

            val display = window!!.windowManager.defaultDisplay
            display.getSize(size)

            window.setLayout((size.x * 0.60).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT)
            window.setGravity(Gravity.END or Gravity.TOP)
            window.setBackgroundDrawable(ColorDrawable(Color.WHITE))

            val params = v.layoutParams
            params.width=(size.x * 0.60).toInt()
            params.height=(size.y* 1).toInt()
            v.layoutParams=params
            
            super.onResume()
        }
    }


}

const val SWIPE_DISTANCE_THRESHOLD = 100
const val SWIPE_VELOCITY_THRESHOLD = 100

open class OnSwipeTouchListener(context: Context?) : OnTouchListener {
    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }

    open fun onSwipeLeft() {}
    open fun onSwipeRight() {}
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event!!)
    }

    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val distanceX = e2.x - e1.x
            val distanceY = e2.y - e1.y
            if (abs(distanceX) > abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0) onSwipeRight() else onSwipeLeft()
                return true
            }
            return false
        }

    }
}