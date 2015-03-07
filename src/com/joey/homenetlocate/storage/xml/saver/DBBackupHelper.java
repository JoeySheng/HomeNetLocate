
package com.joey.homenetlocate.storage.xml.saver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.util.Log;

import com.joey.homenetlocate.storage.db.DBConstant;
import com.joey.homenetlocate.utils.DateUtil;
import com.joey.homenetlocate.utils.FileManager;

/**
 * 文件名称 : DBBackupHelper
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : DBBackupHelper - 备份数据
 * <p>
 * 创建时间 : 2014-3-25 上午12:30:03
 * <p>
 */
public class DBBackupHelper
{
    /**
     * 备份
     * 
     * @param values
     * @param contentResolver
     */
    public static void backup(final ContentValues values, final ContentResolver contentResolver)
    {
        if (null == values) { return; }
        
        backupAttraction(values, contentResolver);
    }
    
    private static void backupAttraction(ContentValues attractionValues,
            ContentResolver contentResolver)
    {
        Integer attractionId = attractionValues.getAsInteger(DBConstant.ATTRACTIONS_ID);
        Long createTime = attractionValues.getAsLong(DBConstant.ATTRACTIONS_TIME);
        if (null == attractionId) { return; }
        
        String xmlPath = FileManager.genBackupXml(DateUtil.currentDate(createTime));
        if (null == xmlPath) { return; }
        Log.e("xusheng", "xmlPath=" + xmlPath);
        
        new AttractionSaver(xmlPath, attractionValues).save();
    }
}
