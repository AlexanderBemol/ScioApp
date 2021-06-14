package com.nordokod.scio.kt.ui.guides

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.nordokod.scio.kt.model.entity.Guide

class GuidesRVAdapter (
        private val myGuides : List<Guide>
        ,private val context: Context
        ,private val nav: NavController
)
    : RecyclerView.Adapter<GuideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GuideViewHolder(inflater, parent, context)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val guide: Guide = myGuides[position]
        holder.bind(guide,nav)
    }

    override fun getItemCount(): Int = myGuides.size
}