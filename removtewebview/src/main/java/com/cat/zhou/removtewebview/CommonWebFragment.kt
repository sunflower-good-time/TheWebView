package com.cat.zhou.removtewebview

import android.os.Bundle
import com.cat.zhou.removtewebview.basefragment.BaseWebviewFragment
import com.cat.zhou.removtewebview.utils.WebConstants


class CommonWebFragment : BaseWebviewFragment() {
    override fun layoutRes(): Int {
        return R.layout.fragment_common_webview
    }

    var url: String? = null

    companion object {

        fun newInstance(url: String): CommonWebFragment {
            val fragment = CommonWebFragment()
            val bundle = Bundle()
            bundle.putSerializable("url", url)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getCommandLevel(): Int {
        return WebConstants.LEVEL_BASE
    }
}
