package com.cat.zhou.removtewebview.remotewebview.progessbar

interface BaseProgressSpec {
    fun show()

    fun hide()

    fun reset()

    fun setProgress(newProgress: Int)
}
