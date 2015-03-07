
package com.joey.homenetlocate.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 文件名称 : CheckNetworkUtils
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : CheckNetworkUtils - 网络处理工具类
 * <p>
 * 创建时间 : 2014-3-25 上午12:31:12
 * <p>
 */
public class CheckNetworkUtils
{
    private static CheckNetworkUtils instance;
    
    /**
     * 单实例对象
     */
    public static synchronized CheckNetworkUtils getInstance()
    {
        if (null == instance)
        {
            instance = new CheckNetworkUtils();
        }
        return instance;
    }
    
    public boolean isNetworkAvailable(Activity mActivity)
    {
        Context context = mActivity.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null)
        {
            return false;
        }
        else
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
            {
                for (int i = 0; i < info.length; i++)
                {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) { return true; }
                }
            }
        }
        return false;
    }
}
