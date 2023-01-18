package com.example.nodrawler

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.navigation.NavigationView


@Suppress("Deprecation", "ClickableViewAccessibility")
class MenuDialog: DialogFragment() {
    lateinit var recycler: RecyclerView
    lateinit var v: View
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        v = LayoutInflater.from(context).inflate(R.layout.draw_l, null, false)
        recycler = v.findViewById(R.id.v_rv)
        recycler.adapter = PlaceholderAdapter()
        drawerLayout = v.findViewById(R.id.drawer)
        navView = v.findViewById(R.id.v_nav_end)
        drawerLayout.addDrawerListener(object : SimpleDrawerListener() {
            override fun onDrawerClosed(drawerView: View) {
                dismiss()
            }
        })

        return AlertDialog.Builder(requireContext(), R.style.DialogMenu).create().apply {
            setView(v)
            window?.attributes?.windowAnimations = R.style.DialogMenu
        }
    }

    class PlaceholderVH(itemView: View) : ViewHolder(itemView){
        val text:TextView=itemView.findViewById(R.id.tView)
    }

    class PlaceholderAdapter: RecyclerView.Adapter<PlaceholderVH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceholderVH {
            return PlaceholderVH(LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false))
        }

        override fun onBindViewHolder(holder: PlaceholderVH, position: Int) {
            holder.text.text = "Item $position"
        }

        override fun getItemCount(): Int {
            return 100
        }
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics =
                requireActivity().windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            val width = windowMetrics.bounds.width() - insets.left -
                    insets.right
            val height = windowMetrics.bounds.height() - insets.top -
                    insets.bottom
            dialog?.window?.let { window ->
                window.setLayout((width * dialogWidthPercent).toInt(), (height  * 1))
                window.setGravity(Gravity.END or Gravity.TOP)
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            val params = v.layoutParams
            params.width = (width * dialogWidthPercent).toInt()
            params.height = (height * 1)
            v.layoutParams = params
            drawerLayout.openDrawer(GravityCompat.END)
        } else {
            val size = Point()
            dialog?.window?.let { window ->
                val display = window.windowManager.defaultDisplay
                display.getSize(size)

                window.setLayout((size.x * dialogWidthPercent).toInt(),
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                window.setGravity(Gravity.END or Gravity.TOP)
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            val params = v.layoutParams
            params.width = (size.x * dialogWidthPercent).toInt()
            params.height = (size.y * 1)
            v.layoutParams = params
        }
    }

    companion object {
        /**
         * Константа указывающая широту диалога в процентах
         */
        const val dialogWidthPercent = 0.3
    }
}
