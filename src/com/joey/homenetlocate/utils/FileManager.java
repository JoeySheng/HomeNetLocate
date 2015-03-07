
package com.joey.homenetlocate.utils;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

/**
 * 文件管理 文件名称 : FileManager
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : FileManager - 在这里增加文件描述
 * <p>
 * 创建时间 : 2014-3-25 上午12:31:59
 * <p>
 */
public class FileManager
{
    /**
     * 根目录
     */
    public static final String SD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    
    /**
     * 应用保存目录
     */
    public static final String USER_DIR = SD_DIR + "/locate";
    
    public static final String BACKUP_DIR = "/backup";
    
    public static final String XML_SUFFIX = ".xml";
    
    /**
     * 数据保存在assets下面的目录
     */
    public static final String RESTORE_DIR = "locate/backup";
    
    public static String genBackupXml(String dir)
    {
        if (null == dir) { return null; }
        
        File file = new File(USER_DIR + BACKUP_DIR);
        if (!file.exists())
        {
            boolean mkdirsSuccess = file.mkdirs();
            if (!mkdirsSuccess) { return null; }
        }
        
        File xmlFile = new File(USER_DIR + BACKUP_DIR + File.separator + dir + XML_SUFFIX);
        
        try
        {
            if (xmlFile.exists() && xmlFile.isFile())
            {
                xmlFile.delete();
            }
            
            xmlFile.createNewFile();
            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return xmlFile.getAbsolutePath();
    }
}
