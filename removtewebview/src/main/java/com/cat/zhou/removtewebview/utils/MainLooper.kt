package com.cat.zhou.removtewebview.utils

import android.os.Handler
import android.os.Looper

class MainLooper protected constructor(looper: Looper) : Handler(looper) {
    companion object {

        val instance = MainLooper(Looper.getMainLooper())

        fun runOnUiThread(runnable: Runnable) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                runnable.run()
            } else {
                instance.post(runnable)
            }
        }
    }
}