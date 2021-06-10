package com.nordokod.scio.kt.ui.guidemenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.nordokod.scio.R
import com.nordokod.scio.kt.constants.enums.GuideCategory
import kotlinx.android.synthetic.main.dialog_guide_menu_view.*

class GuideMenuView : DialogFragment() {
    private val args: GuideMenuViewArgs by navArgs()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_guide_menu_view , container)
    }
    override fun onStart() {
        super.onStart()
        val dialog = this.dialog
        if(dialog != null){
            val params = WindowManager.LayoutParams()
            params.copyFrom(dialog.window?.attributes)
            params.width = (resources.displayMetrics.widthPixels * 0.85).toInt()
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = params

            GM_IV_Icon.setImageResource(args.guideIcon)
            GM_TV_Topic.text = args.guideTopic

        }

    }
}