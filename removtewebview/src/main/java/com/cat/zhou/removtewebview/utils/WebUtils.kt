package com.cat.zhou.removtewebview.utils

import android.content.Context

object WebUtils {

    fun dipToPx(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }


    fun isNotNull(list: List<*>?): Boolean {
        return if (list != null && list.size > 0) {
            true
        } else false
    }

    fun isNotNull(set: Set<*>?): Boolean {
        return if (set != null && set.size > 0) {
            true
        } else false
    }

    fun isNotNull(map: Map<*, *>?): Boolean {
        return if (map != null && map.size > 0) {
            true
        } else false
    }

}
