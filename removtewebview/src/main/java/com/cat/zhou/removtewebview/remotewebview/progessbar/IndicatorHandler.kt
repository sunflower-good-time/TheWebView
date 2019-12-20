package com.cat.zhou.removtewebview.remotewebview.progessbar

class IndicatorHandler {

    internal var baseProgressSpec: BaseProgressSpec? = null

    fun progress(newProgress: Int) {
        if (newProgress == 0) {
            reset()
        } else if (newProgress > 0 && newProgress <= 10) {
            showProgressBar()
        } else if (newProgress > 10 && newProgress < 95) {
            setProgressBar(newProgress)
        } else {
            setProgressBar(newProgress)
            finish()
        }

    }

    fun offerIndicator(): BaseProgressSpec? {
        return this.baseProgressSpec
    }

    fun reset() {

        if (baseProgressSpec != null) {
            baseProgressSpec!!.reset()
        }
    }

    fun finish() {
        if (baseProgressSpec != null) {
            baseProgressSpec!!.hide()
        }
    }

    fun setProgressBar(n: Int) {
        if (baseProgressSpec != null) {
            baseProgressSpec!!.setProgress(n)
        }
    }

    fun showProgressBar() {
        if (baseProgressSpec != null) {
            baseProgressSpec!!.show()
        }
    }

    fun inJectProgressView(baseProgressSpec: BaseProgressSpec): IndicatorHandler {
        this.baseProgressSpec = baseProgressSpec
        return this
    }

    companion object {

        val instance = IndicatorHandler()
    }
}
