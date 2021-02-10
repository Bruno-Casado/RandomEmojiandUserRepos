package com.brunocasado.randomemojianduserrepos.core

import android.content.Context
import android.util.DisplayMetrics

class AndroidUtils {
    fun calculateNumberOfColumns(context: Context, itemSize: Int): Int {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / itemSize).toInt()
    }
}