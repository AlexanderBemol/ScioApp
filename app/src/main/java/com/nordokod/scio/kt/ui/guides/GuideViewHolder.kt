package com.nordokod.scio.kt.ui.guides

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.nordokod.scio.R
import com.nordokod.scio.kt.constants.enums.GuideCategory
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.ui.guidemenu.GuideMenuViewDirections
import com.nordokod.scio.kt.utils.daysLeft

class GuideViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        private val context : Context
) : RecyclerView.ViewHolder(inflater.inflate(R.layout.guide_card,parent,false)) {
    var categoryIcon : AppCompatImageView? = null
    var topic : AppCompatTextView? = null
    var category : AppCompatTextView? = null
    var days : AppCompatTextView?= null
    var isActive : SwitchCompat?= null
    var guideCard : ConstraintLayout?= null

    init {
        categoryIcon = itemView.findViewById(R.id.QCard_IV_Icon)
        topic = itemView.findViewById(R.id.TV_Topic)
        category = itemView.findViewById(R.id.TV_Category)
        days = itemView.findViewById(R.id.TV_Days)
        isActive = itemView.findViewById(R.id.Switch_ActivateGuide)
        guideCard = itemView.findViewById(R.id.CL_Guide)
    }
    fun bind(guide : Guide, nav : NavController){
        val guideCategory = GuideCategory.fromInt(guide.category)

        categoryIcon?.setImageResource(guideCategory.toIconID())
        topic?.text  = guide.topic
        category?.setText(guideCategory.toStringResource())
        context.resources.getString(R.string.txt_days_left,guide.testDate.daysLeft())
        isActive?.isChecked = false

        guideCard?.setOnClickListener {
            val action = GuideMenuViewDirections.actionGlobalGuideMenuView(guide.id,guide.topic,guideCategory.toIconID())
            nav.navigate(action)
        }

    }
}