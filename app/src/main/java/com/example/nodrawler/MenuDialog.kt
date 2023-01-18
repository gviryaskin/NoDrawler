package com.example.nodrawler

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.gesture.GestureLibraries
import android.gesture.GestureLibrary
import android.gesture.GestureOverlayView
import android.gesture.Prediction
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.View.OnTouchListener
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlin.math.abs


class MenuDialog : DialogFragment() {
    lateinit var recycler: RecyclerView
    lateinit var v:RvView
    lateinit var library: GestureLibrary

    private lateinit var gesture: GestureDetectorCompat
    private val gestureListener = object : SimpleOnGestureListener() {
        override fun onFling(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
            Log.d("TTT", "onFling")
            return true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (context == null)
            throw java.lang.IllegalStateException("")
        v = RvView(requireContext())
        recycler = v.findViewById(R.id.rView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = PlaceholderAdapter()
        v.swipeListener = RvView.OnSwipeListener {
            dismiss()
        }
//        gesture = GestureDetectorCompat(context, gestureListener)
//        library = GestureLibraries.fromRawResource(context, R.raw.gestures)
//        library.load()
//        val gg = v.findViewById<GestureOverlayView>(R.id.gestures)
//        gg.addOnGesturePerformedListener { overlay, gesture ->
//            val predictions: ArrayList<Prediction> = library.recognize(gesture)
//            Log.d("TTT", "$overlay --- $gesture -- $predictions")
//            if (predictions.size > 0 && predictions[0].score > 1.0) {
//                val action = predictions[0].name
//                if ("prev" == action) {
//                    Toast.makeText(context, "Adding a contact", Toast.LENGTH_SHORT).show()
//                    dismiss()
//                } else if ("action_delete" == action) {
//                    Toast.makeText(context, "Removing a contact", Toast.LENGTH_SHORT).show()
//                } else if ("action_refresh" == action) {
//                    Toast.makeText(context, "Reloading contacts", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

        val myDialog = object : AlertDialog(context, R.style.DialogMenu) {
            override fun onTouchEvent(event: MotionEvent): Boolean {
                if (v.onTouchEvent(event))
                    return true
                return super.onTouchEvent(event)
            }
        }.apply {
            setView(v)
            window?.attributes?.windowAnimations = R.style.DialogMenu
        }
        return myDialog
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
            return 100
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