
package com.nordokod.scio.kt.ui.newguide

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.color.MaterialColors
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.nordokod.scio.R
import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.constants.enums.GuideCategory
import com.nordokod.scio.kt.constants.enums.SuccessMessage
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.ui.guides.GuidesViewModel
import com.nordokod.scio.kt.utils.*
import kotlinx.android.synthetic.main.fragment_new_guide_view.*
import kotlinx.android.synthetic.main.list_categories.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class NewGuideView : BottomSheetDialogFragment() {
    private val calendar by lazy { Calendar.getInstance() }
    private var selectedHour: Int = 0
    private var selectedMinute: Int = 0
    private var selectedCategory: GuideCategory? = null
    private val newGuideViewModel by viewModel<NewGuideViewModel>()
    private val args : NewGuideViewArgs by navArgs()
    private val navController : NavController by lazy { findNavController()}
    private val guidesViewModel by sharedViewModel<GuidesViewModel>()

    private var isDisplaying = false
    private val countDownTimer = object : CountDownTimer(Generic.BEFORE_LOADING_TIME, Generic.BEFORE_LOADING_TIME) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            isDisplaying = true
            navController.navigate(R.id.action_global_loadingView)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        observeLiveDate()
        return inflater.inflate(R.layout.fragment_new_guide_view, container, false)
    }

    override fun onStart() {
        super.onStart()
        if(args.guideId != 0) {
            countDownTimer.start()
            newGuideViewModel.getGuide(args.guideId)
            FNewGuide_TV_Title.setText(R.string.txt_TV_EditGuide)
            FNewGuide_BTN_Create.setText(R.string.txt_BTN_Save)
        }
        initListeners()
        initComponents()
    }

    private fun initComponents() {
        calendar.time = Date()
        onDateSelected(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis)
        onTimeSelected(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
    }

    private fun initListeners() {
        CL_Exacts.setOnClickListener { l: View? -> onClickCategoryListener(l!!.toGuideCategory()) }
        CL_Socials.setOnClickListener { l: View? -> onClickCategoryListener(l!!.toGuideCategory()) }
        CL_Sports.setOnClickListener { l: View? -> onClickCategoryListener(l!!.toGuideCategory()) }
        CL_Art.setOnClickListener { l: View? -> onClickCategoryListener(l!!.toGuideCategory()) }
        CL_Tech.setOnClickListener { l: View? -> onClickCategoryListener(l!!.toGuideCategory()) }
        CL_Entertainment.setOnClickListener { l: View? -> onClickCategoryListener(l!!.toGuideCategory()) }
        CL_Others.setOnClickListener { l: View? -> onClickCategoryListener(l!!.toGuideCategory()) }
        if (args.guideId == 0){
            FNewGuide_LL_Date.setOnClickListener {
                MaterialDatePicker.Builder
                        .datePicker()
                        .setSelection(calendar.time.time)
                        .build()
                        .apply { addOnPositiveButtonClickListener { selection -> onDateSelected(selection) } }
                        .show(childFragmentManager, "MaterialDatePicker")
            }
            FNewGuide_LL_Time.setOnClickListener {
                val hour = selectedHour
                val minute = selectedMinute

                MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(hour)
                        .setMinute(minute)
                        .build()
                        .apply {
                            addOnPositiveButtonClickListener { onTimeSelected(this.hour, this.minute) }
                        }.show(childFragmentManager, "MaterialTimePicker")
            }
            FNewGuide_BTN_Create.setOnClickListener {
                countDownTimer.start()
                saveGuide()
            }
        }
        FNewGuide_BTN_Cancel.setOnClickListener { this.dismiss() }
    }

    private fun observeLiveDate(){
        val context = this.context
        if(context!=null){
            newGuideViewModel.error.observe(
                    viewLifecycleOwner,
                    androidx.lifecycle.Observer {
                        dismissLoading()
                        it.getEnumErrorMessage().showMessage(context)
                    }
            )
            newGuideViewModel.successMessage.observe(
                    viewLifecycleOwner,
                    androidx.lifecycle.Observer {
                        dismissLoading()
                        it.showMessage(context)
                        this.dismiss()
                        if(it == SuccessMessage.UPDATE_GUIDE) guidesViewModel.getGuides()
                    }
            )
            newGuideViewModel.guide.observe(
                    viewLifecycleOwner,
                    androidx.lifecycle.Observer {
                        dismissLoading()
                        loadGuide(it)
                    }
            )
        }
    }

    private fun loadGuide(guide: Guide){
        FNewGuide_ET_Topic.setText(guide.topic)
        onClickCategoryListener(GuideCategory.fromInt(guide.category))
        FNewGuide_LL_Date.setOnClickListener {
            MaterialDatePicker.Builder
                    .datePicker()
                    .setSelection(guide.testDate.time)
                    .build()
                    .apply { addOnPositiveButtonClickListener { selection -> onDateSelected(selection) } }
                    .show(childFragmentManager, "MaterialDatePicker")
        }
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY)
        selectedMinute = calendar.get(Calendar.MINUTE)
        FNewGuide_LL_Time.setOnClickListener {
            val hour = selectedHour
            val minute = selectedMinute

            MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(hour)
                    .setMinute(minute)
                    .build()
                    .apply {
                        addOnPositiveButtonClickListener { onTimeSelected(this.hour, this.minute) }
                    }.show(childFragmentManager, "MaterialTimePicker")
        }
        FNewGuide_BTN_Create.setOnClickListener {
            countDownTimer.start()
            saveGuide(guide)
        }
    }


    private fun saveGuide(guide : Guide = Guide()) {
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                selectedHour,
                selectedMinute
        )
        val selectedDate = Date(selectedCalendar.timeInMillis)
        guide.testDate = selectedDate
        guide.topic = FNewGuide_ET_Topic.text.toString()
        guide.category = if (selectedCategory == null) 0 else selectedCategory!!.code
        if(guide.id == 0)
            newGuideViewModel.createGuide(
                    selectedCategory,
                    FNewGuide_ET_Topic.text.toString(),
                    selectedDate
            )
        else
            newGuideViewModel.updateGuide(guide)
    }

    private fun onTimeSelected(hour: Int, minute: Int) {
        selectedHour = hour
        selectedMinute = minute
        val hour12 = if (hour < 13) hour else hour - 12
        FNewGuide_TV_Time.text = if (hour < 12) "AM" else "PM"
        FNewGuide_TV_Hour.text = getString(R.string.hour_display, hour12.formatToNDigits(2), minute.formatToNDigits(2))
    }

    private fun onDateSelected(selection: Long) {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = selection
        calendar.set(utc.get(Calendar.YEAR), utc.get(Calendar.MONTH), utc.get(Calendar.DAY_OF_MONTH))
        FNewGuide_TV_Day.text = utc.get(Calendar.DAY_OF_MONTH).formatToNDigits(2)
        FNewGuide_TV_Month.setText(utc.get(Calendar.MONTH).getMonthName())
    }

    private fun onClickCategoryListener(guideCategory: GuideCategory){
        selectedCategory = guideCategory

        val selectedColor = MaterialColors.getColor(this.requireView(),R.attr.iconSelectedColor)
        val defaultColor = MaterialColors.getColor(this.requireView(),R.attr.iconNormalColor)

        requireView().findViewById<AppCompatImageView>(GuideCategory.ART.toListItemID()).setColorFilter(defaultColor)
        requireView().findViewById<AppCompatImageView>(GuideCategory.ENTERTAINMENT.toListItemID()).setColorFilter(defaultColor)
        requireView().findViewById<AppCompatImageView>(GuideCategory.EXACT_SCIENCES.toListItemID()).setColorFilter(defaultColor)
        requireView().findViewById<AppCompatImageView>(GuideCategory.SOCIAL_SCIENCES.toListItemID()).setColorFilter(defaultColor)
        requireView().findViewById<AppCompatImageView>(GuideCategory.SPORTS.toListItemID()).setColorFilter(defaultColor)
        requireView().findViewById<AppCompatImageView>(GuideCategory.TECH.toListItemID()).setColorFilter(defaultColor)
        requireView().findViewById<AppCompatImageView>(GuideCategory.OTHERS.toListItemID()).setColorFilter(defaultColor)

        if(selectedCategory != null){
            val selectedIcon = requireView().findViewById<AppCompatImageView>(selectedCategory!!.toListItemID())
            selectedIcon.setColorFilter(selectedColor)
        }

    }

    private fun dismissLoading(){
        if(isDisplaying){
            navController.popBackStack()
            countDownTimer.cancel()
            isDisplaying = false
        } else countDownTimer.cancel()
    }

}