
package com.joey.homenetlocate.utils;

import android.content.Context;

/**
 * 文件名称 : ResourceUtil
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : ResourceUtil - 资源处理类
 * <p>
 * 创建时间 : 2014-3-25 上午12:32:35
 * <p>
 */
public class ResourceUtil
{
    
    public static int getScreenHeight(Context context)
    {
        if (context == null) { return 0; }
        return context.getResources().getDisplayMetrics().heightPixels;
    }
    
    public static int getScreenWidth(Context context)
    {
        if (context == null) { return 0; }
        return context.getResources().getDisplayMetrics().widthPixels;
    }
    
    public static float getDesity(Context context)
    {
        if (context == null) { return 0; }
        return context.getResources().getDisplayMetrics().density;
    }
    
}
