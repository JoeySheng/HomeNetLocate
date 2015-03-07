
package com.joey.homenetlocate.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 文件名称 : PromptUtils
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : PromptUtils - 提示类
 * <p>
 * 创建时间 : 2014-3-25 上午12:32:16
 * <p>
 */
public class PromptUtils
{
    private static PromptUtils instance;
    
    /**
     * 单实例对象
     */
    public static synchronized PromptUtils getInstance()
    {
        if (null == instance)
        {
            instance = new PromptUtils();
        }
        return instance;
    }
    
    /**
     * Toast
     * 
     * @param context
     * @param isLong
     * @param msgId
     */
    public void displayToastId(Context context, boolean isLong, int msgId)
    {
        if (isLong)
        {
            Toast.makeText(context, msgId, Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show();
        }
        
    }
    
    public void displayToastString(Context context, boolean isLong, String msg)
    {
        if (isLong)
        {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
        
    }
}
