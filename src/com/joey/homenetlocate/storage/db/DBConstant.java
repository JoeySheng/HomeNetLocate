
package com.joey.homenetlocate.storage.db;

/**
 * 文件名称 : DBConstant
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : DBConstant - 数据库常量
 * <p>
 * 创建时间 : 2014-3-24 下午10:13:30
 * <p>
 */
public class DBConstant
{
    // 数据库版本
    public static final int DB_VERSION = 1;
    
    // 数据库名称
    public static final String DB_NAME = "locate.db";
    
    // 以下为景点(attractions)数据库字段名称
    // 景点(attractions)表名
    public static final String ATTRACTIONS_TABLE_NAME = "attractions";
    
    // id 唯一标识符
    public static final String ATTRACTIONS_ID = "_id";
    
    // 名称
    public static final String ATTRACTIONS_NAME = "name";
    
    // 纬度
    public static final String ATTRACTIONS_LATITUDE = "latitude";
    
    // 经度
    public static final String ATTRACTIONS_LONGITUDE = "longitude";
    
    // 时间
    public static final String ATTRACTIONS_TIME = "time";
    
    // 城市
    public static final String ATTRACTIONS_CITY = "city";
}
