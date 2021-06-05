package com.nordokod.scio.kt.constants.enums

import com.nordokod.scio.R
import java.sql.Types

enum class GuideCategory(val code:Int) {
    EXACT_SCIENCES(1),
    SOCIAL_SCIENCES(2),
    SPORTS(3),
    ART(4),
    TECH(5),
    ENTERTAINMENT(6),
    OTHERS(7);

    companion object {
        private val enumValues = values()
        fun fromInt(code: Int) : GuideCategory? {
            return if(code in 1..7)
                enumValues.firstOrNull {it.code == code}
            else OTHERS
        }
    }

    fun toListItemID() : Int {
        return when (this){
            ART -> R.id.IV_Art
            ENTERTAINMENT -> R.id.IV_Entertainment
            EXACT_SCIENCES -> R.id.IV_Exacts
            SOCIAL_SCIENCES -> R.id.IV_Socials
            SPORTS -> R.id.IV_Sports
            TECH -> R.id.IV_Tech
            OTHERS -> R.id.IV_Others
        }
    }

    fun toIconID() : Int{
        return when (this){
            ART -> R.drawable.ic_palette
            ENTERTAINMENT -> R.drawable.ic_theatre
            EXACT_SCIENCES -> R.drawable.ic_flask
            SOCIAL_SCIENCES -> R.drawable.ic_globe
            SPORTS -> R.drawable.ic_ball
            TECH -> R.drawable.ic_code
            OTHERS -> R.drawable.ic_scio_face
        }
    }

    fun toStringResource(): Int {
        return when (code){
            1 -> R.string.category_exact_sciences
            2 -> R.string.category_social_sciences
            3 -> R.string.category_sports
            4 -> R.string.category_art
            5 -> R.string.category_tech
            6 -> R.string.category_entertainment
            else -> R.string.category_others
        }
    }
}