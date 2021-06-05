package com.nordokod.scio.kt.ui.guides

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.color.MaterialColors
import com.nordokod.scio.R
import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.constants.enums.GuideCategory
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.utils.getEnumErrorMessage
import com.nordokod.scio.kt.utils.toGuideCategory
import kotlinx.android.synthetic.main.fragment_guides_view.*
import kotlinx.android.synthetic.main.list_categories.*
import org.koin.android.viewmodel.ext.android.viewModel

class GuidesView : Fragment() {
    private var selectedCategory = GuideCategory.EXACT_SCIENCES
    private val guidesViewModel by viewModel<GuidesViewModel>()
    private var guidesList = listOf<Guide>()
    private var isDisplaying = false
    private val countDownTimer = object : CountDownTimer(Generic.BEFORE_SHORT_LOADING_TIME, Generic.BEFORE_SHORT_LOADING_TIME) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            isDisplaying = true
            //loading here
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        observeLiveData()
        return inflater.inflate(R.layout.fragment_guides_view, container, false)
    }

    override fun onStart() {
        super.onStart()
        initListeners()
        FGuides_RV_Guides.layoutManager = LinearLayoutManager(context)

        requireView().findViewById<AppCompatImageView>(selectedCategory.toListItemID())
                .setColorFilter(MaterialColors.getColor(this.requireView(),R.attr.iconSelectedColor))
        countDownTimer.start()
        guidesViewModel.getGuides()
    }

    private fun initListeners() {
        CL_Exacts.setOnClickListener { view: View? -> onClickCategoryListener(view) }
        CL_Socials.setOnClickListener { view: View? -> onClickCategoryListener(view) }
        CL_Sports.setOnClickListener { view: View? -> onClickCategoryListener(view) }
        CL_Art.setOnClickListener { view: View? -> onClickCategoryListener(view) }
        CL_Tech.setOnClickListener { view: View? -> onClickCategoryListener(view) }
        CL_Entertainment.setOnClickListener { view: View? -> onClickCategoryListener(view) }
        CL_Others.setOnClickListener { view: View? -> onClickCategoryListener(view) }
        FGuides_Swipe.setOnRefreshListener {
            countDownTimer.start()
            guidesViewModel.getGuides()
        }
    }

    private fun onClickCategoryListener(l : View?){
        selectedCategory = l!!.toGuideCategory()
        val selectedColor = MaterialColors.getColor(this.requireView(),R.attr.iconSelectedColor)
        val defaultColor = MaterialColors.getColor(this.requireView(),R.attr.iconNormalColor)

        requireView().findViewById<AppCompatImageView>(GuideCategory.ART.toListItemID()).setColorFilter(defaultColor)
        requireView().findViewById<AppCompatImageView>(GuideCategory.ENTERTAINMENT.toListItemID()).setColorFilter(defaultColor)
        requireView().findViewById<AppCompatImageView>(GuideCategory.EXACT_SCIENCES.toListItemID()).setColorFilter(defaultColor)
        requireView().findViewById<AppCompatImageView>(GuideCategory.SOCIAL_SCIENCES.toListItemID()).setColorFilter(defaultColor)
        requireView().findViewById<AppCompatImageView>(GuideCategory.SPORTS.toListItemID()).setColorFilter(defaultColor)
        requireView().findViewById<AppCompatImageView>(GuideCategory.TECH.toListItemID()).setColorFilter(defaultColor)
        requireView().findViewById<AppCompatImageView>(GuideCategory.OTHERS.toListItemID()).setColorFilter(defaultColor)

        val selectedIcon = requireView().findViewById<AppCompatImageView>(selectedCategory.toListItemID())
        selectedIcon.setColorFilter(selectedColor)
        refreshGuides()
    }

    private fun observeLiveData(){
        val context = this.context
        if (context != null){
            guidesViewModel.error.observe(
                    viewLifecycleOwner,
                    Observer {
                        dismissDialog()
                        it.getEnumErrorMessage().showMessage(context)
                    }
            )
            guidesViewModel.guides.observe(
                    viewLifecycleOwner,
                    Observer {
                        dismissDialog()
                        guidesList = it
                        refreshGuides()
                    }
            )
        }

    }

    private fun refreshGuides(){
        val myGuides = guidesList.filter { it.category == selectedCategory.code }
        val context = this.context
        if (context != null)
            FGuides_RV_Guides.adapter = GuidesRVAdapter(myGuides, context)

    }

    private fun dismissDialog(){
        if(isDisplaying){
            FGuides_Swipe.isRefreshing = false
            countDownTimer.cancel()
        } else countDownTimer.cancel()
    }

}