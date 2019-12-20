package com.cat.zhou.removtewebview.mainproess

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

/**
 * @author: zjf
 * @data:2019/12/18
 */
class MainProHandeRemoteService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        val pid = android.os.Process.myPid()
        Log.d(
            "onBind", String.format(
                "MainProHandleRemoteService: %s",
                "当前进程ID为：$pid----客户端与服务端连接成功，服务端返回BinderPool.BinderPoolImpl 对象"
            )
        )
        val mBinderPool = RemoteWebBinderPool.BinderPoolImpl(this)
        return mBinderPool
    }
}