package com.nordokod.scio.kt.ui.lockedapps

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nordokod.scio.R
import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.constants.enums.SendoScreen
import com.nordokod.scio.kt.model.entity.AppPackage
import com.nordokod.scio.kt.utils.AnalyticsHelper
import com.nordokod.scio.kt.utils.getEnumErrorMessage
import kotlinx.android.synthetic.main.fragment_locked_apps_view.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class LockedAppsView : Fragment(){
    private val navController : NavController by lazy { findNavController()}
    private val viewModel by viewModel<LockedAppsViewModel>()
    private var appsList = listOf<AppPackage>()
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
        observeLiveData()
        return inflater.inflate(R.layout.fragment_locked_apps_view, container, false)
    }

    override fun onStart() {
        super.onStart()
        AnalyticsHelper.recordScreenView(SendoScreen.LOCK_APPS_SCREEN,this::class.simpleName.toString())
        AppBlocked_RV_listApps.layoutManager = LinearLayoutManager(context)
        initListeners()
        countDownTimer.start()
        viewModel.getAllApps()
    }

    private fun initListeners() {
        AppBlocked_BTN_Cancel.setOnClickListener {
            navController.navigateUp()
        }
        AppBlocked_BTN_Save.setOnClickListener {
            val adapterApps = (AppBlocked_RV_listApps.adapter as AppsRVAdapter).apps
            appsList.map { app ->
                app.locked = adapterApps.find { app2 -> app2.packagePath == app.packagePath }?.locked ?: app.locked
            }
            countDownTimer.start()
            viewModel.saveLockedApps(
                    appsList.filter { app -> app.locked }
            )
        }
        Apps_ET_Search.doAfterTextChanged {
            val adapterApps = (AppBlocked_RV_listApps.adapter as AppsRVAdapter).apps
            appsList.map { app ->
                app.locked = adapterApps.find { app2 -> app2.packagePath == app.packagePath }?.locked ?: app.locked
            }

            val filteredList = appsList.filter { app -> app.name.toUpperCase(Locale.ROOT).contains(it.toString().toUpperCase(Locale.ROOT)) }
            refreshApps(filteredList)
        }
    }

    private fun observeLiveData(){
        val context = this.context
        if(context != null){
            viewModel.appsList.observe(
                    viewLifecycleOwner,
                    Observer {
                        dismissLoading()
                        appsList = it
                        refreshApps(appsList)
                    }
            )
            viewModel.successMessage.observe(
                    viewLifecycleOwner,
                    Observer {
                        dismissLoading()
                        it.showMessage(context)
                    }
            )
            viewModel.error.observe(
                    viewLifecycleOwner,
                    Observer {
                        dismissLoading()
                        it.getEnumErrorMessage()
                                .showMessage(context)
                    }
            )
        }

    }

    private fun refreshApps(appsList : List<AppPackage>){
        val context = this.context
        if(context != null)
            AppBlocked_RV_listApps.adapter = AppsRVAdapter(appsList,context)
    }

    private fun dismissLoading() {
        if (isDisplaying){
            navController.navigateUp()
            countDownTimer.cancel()
            isDisplaying = false
        } else countDownTimer.cancel()
    }


}