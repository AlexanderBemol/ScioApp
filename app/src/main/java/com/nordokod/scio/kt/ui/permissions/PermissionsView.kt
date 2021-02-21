package com.nordokod.scio.kt.ui.permissions

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nordokod.scio.R
import kotlinx.android.synthetic.main.fragment_permissions_view.*
import org.koin.android.viewmodel.ext.android.viewModel

class PermissionsView : Fragment() {
    private val navController : NavController by lazy { findNavController()}
    private val viewModel by viewModel<PermissionsViewModel>()
    private val args: PermissionsViewArgs by navArgs()

    override fun onStart() {
        super.onStart()
        initComponents()
        initListeners()
        observeLiveData()
        onPermissionsCheck()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_permissions_view, container, false)
    }

    override fun onResume() {
        super.onResume()
        onPermissionsCheck()
    }

    private fun initComponents(){
        viewModel.haveAutoStart()
    }

    private fun initListeners() {
        BTN_Overlay_permission.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                startActivity(intent)
            }
        }
        BTN_Usage_access_permission.setOnClickListener {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        }
        BTN_Autostart_permission.setOnClickListener {
            val intent = Intent()
            val manufacturer = Build.MANUFACTURER
            when {
                "xiaomi".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")
                }
                "oppo".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")
                }
                "vivo".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")
                }
                "Letv".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")
                }
                "Honor".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")
                }
            }

            val list = activity?.packageManager?.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (list!!.size > 0) {
                startActivity(intent)
            }
        }
        BTN_Permission_skip.setOnClickListener {
            if(args.firstConfiguration) navController.navigate(R.id.action_permissionsView_to_mainView)
            else activity?.onBackPressed()
        }
    }

    private fun onPermissionsCheck() {
        viewModel.checkPermissions()
    }

    @SuppressLint("RestrictedApi")
    private fun observeLiveData() {
        val context = this.context
        if (context != null){
            viewModel.haveAutoStart.observe(
                    this,
                    Observer {
                        if(it) BTN_Autostart_permission.visibility = View.VISIBLE
                        else BTN_Autostart_permission.visibility = View.INVISIBLE
                    }
            )
            viewModel.isSystemAlertWindow.observe(
                    this,
                    Observer {
                        if(it) BTN_Overlay_permission.supportBackgroundTintList = ContextCompat.getColorStateList(context,R.color.correctColor)
                        else BTN_Overlay_permission.supportBackgroundTintList = ContextCompat.getColorStateList(context,R.color.errorColor)
                    }
            )
            viewModel.isUsageStats.observe(
                    this,
                    Observer {
                        if(it) BTN_Usage_access_permission.supportBackgroundTintList = ContextCompat.getColorStateList(context,R.color.correctColor)
                        else BTN_Usage_access_permission.supportBackgroundTintList = ContextCompat.getColorStateList(context,R.color.errorColor)
                    }
            )
        }
    }


}