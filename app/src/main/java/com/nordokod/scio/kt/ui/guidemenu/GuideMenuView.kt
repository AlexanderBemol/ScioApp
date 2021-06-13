package com.nordokod.scio.kt.ui.guidemenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nordokod.scio.R
import com.nordokod.scio.kt.ui.newguide.NewGuideViewDirections
import kotlinx.android.synthetic.main.dialog_guide_menu_view.*

class GuideMenuView : BottomSheetDialogFragment() {
    private val args: GuideMenuViewArgs by navArgs()
    private val navController : NavController by lazy { findNavController()}
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_guide_menu_view , container)
    }
    override fun onStart() {
        super.onStart()
        GM_IV_Icon.setImageResource(args.guideIcon)
        GM_TV_Topic.text = args.guideTopic

        item_edit.setOnClickListener {
            navController.navigateUp()
            navController.navigate(NewGuideViewDirections.actionGlobalNewGuideView(args.guideId))
        }

    }
}