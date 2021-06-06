package com.nordokod.scio.kt.ui.lockedapps

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.nordokod.scio.R
import com.nordokod.scio.kt.model.entity.AppPackage

class AppViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        private val context : Context
) : RecyclerView.ViewHolder(inflater.inflate(R.layout.app_card,parent,false)) {
    var appIcon : AppCompatImageView? = null
    var appName : AppCompatTextView? = null
    var appLocked : SwitchCompat? = null
    init {
        appIcon = itemView.findViewById(R.id.QCard_IV_Icon)
        appName = itemView.findViewById(R.id.TV_Name)
        appLocked = itemView.findViewById(R.id.Switch_State)
    }
    fun bind(app : AppPackage){
        appIcon?.setImageBitmap(app.icon)
        appName?.text = app.name
        appLocked?.isChecked = app.locked
    }
}