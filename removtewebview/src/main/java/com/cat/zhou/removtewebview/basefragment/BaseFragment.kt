package com.cat.zhou.removtewebview.basefragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    protected var mContext: Context? = null

    fun setTitle(titleId: Int) {
        activity!!.setTitle(titleId)
    }

    fun setTitle(title: CharSequence) {
        activity!!.title = title
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mContext == null) {
            mContext = context
        }
    }

    override fun getContext(): Context? {
        return if (super.getContext() == null) mContext else super.getContext()
    }

}
