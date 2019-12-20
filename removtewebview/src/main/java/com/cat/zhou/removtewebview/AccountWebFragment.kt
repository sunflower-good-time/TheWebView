package com.cat.zhou.removtewebview

import android.os.Bundle
import android.text.TextUtils
import android.webkit.CookieManager

import com.cat.zhou.removtewebview.basefragment.BaseWebviewFragment
import com.cat.zhou.removtewebview.utils.WebConstants

import java.util.HashMap

class AccountWebFragment : BaseWebviewFragment() {
    override fun layoutRes(): Int {
        return R.layout.fragment_common_webview
    }


    override fun getCommandLevel(): Int {
        return WebConstants.LEVEL_ACCOUNT
    }

    companion object {

        fun newInstance(
            keyUrl: String,
            headers: HashMap<String, String>,
            isSyncToCookie: Boolean
        ): AccountWebFragment {
            val fragment = AccountWebFragment()
            fragment.arguments = getBundle(keyUrl, headers)
            if (isSyncToCookie && headers != null) {
                syncCookie(keyUrl, headers)
            }
            return fragment
        }

        fun getBundle(url: String, headers: HashMap<String, String>): Bundle {
            val bundle = Bundle()
            bundle.putString(WebConstants.INTENT_TAG_URL, url)
            bundle.putSerializable(BaseWebviewFragment.ACCOUNT_INFO_HEADERS, headers)
            return bundle
        }

        /**
         * 将cookie同步到WebView
         *
         * @param url    WebView要加载的url
         * @return true 同步cookie成功，false同步cookie失败
         * @Author JPH
         */
        fun syncCookie(url: String, map: Map<String, String>): Boolean {
            val cookieManager = CookieManager.getInstance()
            for (key in map.keys) {
                cookieManager.setCookie(url, key + "=" + map[key])
            }
            val newCookie = cookieManager.getCookie(url)
            return if (TextUtils.isEmpty(newCookie)) false else true
        }
    }
}