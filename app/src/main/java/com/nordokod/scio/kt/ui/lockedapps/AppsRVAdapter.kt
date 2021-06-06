package com.nordokod.scio.kt.ui.lockedapps

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.nordokod.scio.kt.model.entity.AppPackage
class AppsRVAdapter(
        val apps : List<AppPackage>,
        private val context: Context
) : RecyclerView.Adapter<AppViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AppViewHolder(inflater, parent, context)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = apps[position]
        initListeners(holder,position)
        holder.bind(app)
    }

    override fun getItemCount(): Int = apps.size

    private fun initListeners(holder : AppViewHolder, position : Int) {
        holder.appLocked?.setOnCheckedChangeListener { compoundButton, b ->
            compoundButton.isChecked = b
            apps[position].locked = b
        }
    }
}