package com.cat.zhou.removtewebview.mainproess;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.cat.zhou.removtewebview.IBinderPool;

import java.util.concurrent.CountDownLatch;

/**
 *
 * 用于remoteweb process 向 main process 获取binder
 */

public class RemoteWebBinderPool {

    public static final int BINDER_WEB_AIDL = 1;

    private Context mContext;
    private IBinderPool mBinderPool;
    private static volatile RemoteWebBinderPool sInstance;
    private CountDownLatch mConnectBinderPoolCountDownLatch;

    private RemoteWebBinderPool(Context context) {
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    /**
     * 单例
     * @param context
     * @return
     */
    public static RemoteWebBinderPool getInstance(Context context) {
        if (sInstance == null) {
            synchronized (RemoteWebBinderPool.class) {
                if (sInstance == null) {
                    sInstance = new RemoteWebBinderPool(context);
                }
            }
        }
        return sInstance;
    }

    @SuppressLint("LongLogTag")
    private synchronized void connectBinderPoolService() {
        Log.i("connectBinderPoolService","ok");
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext, MainProHandeRemoteService.class);
        mContext.bindService(service, mBinderPoolConnection, Context.BIND_AUTO_CREATE);
        try {
            mConnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询IBinder
     * @param binderCode
     * @return
     */
    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        try {
            if (mBinderPool != null) {
                binder = mBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }


    /**
     * MainProHandRemoteService ，的控制器，通过控制器创建查询Binder的Aidl
     */
    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {   // 5

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mConnectBinderPoolCountDownLatch.countDown();
        }
    };

    /**
     * 如果连接失败从新连接
     */
    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {    // 6
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
            mBinderPool = null;
            connectBinderPoolService();
        }
    };

    public static class BinderPoolImpl extends IBinderPool.Stub {

        private Context context;

        public BinderPoolImpl(Context context) {
            this.context = context;
        }

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                case BINDER_WEB_AIDL: {
                    binder = new MainProAidlInterface(context);
                    break;
                }
                default:
                    break;
            }
            return binder;
        }
    }

}
