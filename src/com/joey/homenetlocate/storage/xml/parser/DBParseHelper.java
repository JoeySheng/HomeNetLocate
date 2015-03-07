
package com.joey.homenetlocate.storage.xml.parser;

import java.io.InputStream;

import android.content.ContentValues;
import android.util.Xml;
import android.util.Xml.Encoding;

/**
 * 文件名称 : DBParseHelper
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : DBParseHelper - 数据库解析类
 * <p>
 * 创建时间 : 2014-3-25 上午12:29:18
 * <p>
 */
public class DBParseHelper
{
    public static ContentValues parse(InputStream inputStream) throws Exception
    {
        if (null == inputStream) { return null; }
        AttractionXMLConetntHandler attractionXMLConetntHandler = new AttractionXMLConetntHandler();
        Xml.parse(inputStream, Encoding.UTF_8, attractionXMLConetntHandler);
        return attractionXMLConetntHandler.getAttraction();
    }
}
