package com.nordokod.scio.kt.ui.newguide

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.color.MaterialColors
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.nordokod.scio.R
import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.constants.enums.GuideCategory
import com.nordokod.scio.kt.utils.*
import kotlinx.android.synthetic.main.fragment_new_guide_view.*
import kotlinx.android.synthetic.main.list_categories.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class NewGuideView : BottomSheetDialogFragment() {
    private val calendar by lazy { Calendar.getInstance() }
    private var selectedHour: Int = 0
    private var selectedMinute: Int = 0
    private var selectedCategory: GuideCategory? = null
    private val newGuideViewModel by viewModel<NewGuideViewModel>()
    private val navController : NavController by lazy { findNavController()}
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
        initListeners()
        initComponents()
    }

    private fun initComponents() {
        calendar.time = Date()
        onDateSelected(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis)
        onTimeSelected(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
    }

    private fun initListeners() {
        CL_Exacts.setOnClickListener { view: View? -> onClickCategoryListener(view) }
        CL_Socials.setOnClickListener { view: View? -> onClickCategoryListener(view) }
        CL_Sports.setOnClickListener { view: View? -> onClickCategoryListener(view) }
        CL_Art.setOnClickListener { view: View? -> onClickCategoryListener(view) }
        CL_Tech.setOnClickListener { view: View? -> onClickCategoryListener(view) }
        CL_Entertainment.setOnClickListener { view: View? -> onClickCategoryListener(view) }
        CL_Others.setOnClickListener { view: View? -> onClickCategoryListener(view) }

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
        FNewGuide_BTN_Cancel.setOnClickListener { this.dismiss() }
        FNewGuide_BTN_Create.setOnClickListener {
            countDownTimer.start()
            createGuide()
        }
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
                    }
            )
        }
    }

    private fun createGuide() {
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                selectedHour,
                selectedMinute
        )
        val selectedDate = Date(selectedCalendar.timeInMillis)
        newGuideViewModel.createGuide(
                selectedCategory,
                FNewGuide_ET_Topic.text.toString(),
                selectedDate
        )
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