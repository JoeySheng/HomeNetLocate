
package com.joey.homenetlocate.application;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.joey.homenetlocate.R;
import com.joey.homenetlocate.fusion.FusionCode;
import com.joey.homenetlocate.utils.PromptUtils;

/**
 * 文件名称 : LocateApplication
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : LocateApplication - 应用
 * <p>
 * 创建时间 : 2014-3-24 下午10:08:10
 * <p>
 */
public class LocateApplication extends Application
{
    private static LocateApplication mInstance = null;
    
    public boolean mIsKeyRight = true;
    
    public BMapManager mBMapManager = null;
    
    private static Context mContext;
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        mInstance = this;
        mContext = LocateApplication.getInstance().getApplicationContext();
        initEngineManager(this);
    }
    
    @Override
    // 建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
    public void onTerminate()
    {
        if (mBMapManager != null)
        {
            mBMapManager.destroy();
            mBMapManager = null;
        }
        super.onTerminate();
    }
    
    public void initEngineManager(Context context)
    {
        if (mBMapManager == null)
        {
            mBMapManager = new BMapManager(context);
        }
        
        if (!mBMapManager.init(FusionCode.BMAPKEY, new MyGeneralListener()))
        {
            PromptUtils.getInstance().displayToastId(mContext, true, R.string.bmap_init_error);
        }
    }
    
    public static LocateApplication getInstance()
    {
        return mInstance;
    }
    
    public static class MyGeneralListener implements MKGeneralListener
    {
        
        @Override
        public void onGetNetworkState(int iError)
        {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT)
            {
                PromptUtils.getInstance().displayToastId(mContext, true, R.string.net_error);
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA)
            {
                PromptUtils.getInstance()
                        .displayToastId(mContext, true, R.string.search_data_error);
            }
        }
        
        @Override
        public void onGetPermissionState(int iError)
        {
            if (iError == MKEvent.ERROR_PERMISSION_DENIED)
            {
                // 授权Key错误：
                PromptUtils.getInstance().displayToastId(mContext, true, R.string.bmap_key_error);
                LocateApplication.getInstance().mIsKeyRight = false;
            }
        }
    }
}
