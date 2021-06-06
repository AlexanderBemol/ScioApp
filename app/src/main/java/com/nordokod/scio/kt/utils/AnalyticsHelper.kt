package com.nordokod.scio.kt.utils

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.nordokod.scio.kt.constants.enums.SendoScreen

class AnalyticsHelper {
    companion object {
        fun recordScreenView(screen: SendoScreen, className: String){
            Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW){
                param(FirebaseAnalytics.Param.SCREEN_NAME,screen.name)
                param(FirebaseAnalytics.Param.SCREEN_CLASS,className)
            }
        }
    }

}