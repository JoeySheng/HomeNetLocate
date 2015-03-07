
package com.joey.homenetlocate.storage.xml.saver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlSerializer;

import android.content.ContentValues;
import android.util.Xml;

/**
 * 文件名称 : BaseSaver
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : BaseSaver - 数据保存基类
 * <p>
 * 创建时间 : 2014-3-25 上午12:30:21
 * <p>
 */
public abstract class BaseSaver implements ISaver
{
    /**
     * XML file encoding
     */
    protected static final String XML_ENCODING = "utf-8";
    
    /**
     * Path of XML file.
     */
    protected String mXmlFilePath;
    
    /**
     * Used for serialization of XML info set.
     */
    protected XmlSerializer mXmlSerializer;
    
    @Override
    public void save()
    {
        File xmlFile = new File(mXmlFilePath);
        mXmlSerializer = Xml.newSerializer();
        FileOutputStream fos = null;
        
        try
        {
            if (!xmlFile.isFile())
            {
                xmlFile.createNewFile();
            }
            
            fos = new FileOutputStream(xmlFile);
            
            // indent output
            mXmlSerializer
                    .setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            mXmlSerializer.setOutput(fos, XML_ENCODING);
            mXmlSerializer.startDocument(XML_ENCODING, null);
            
            writeObj();
            
            // 刷新缓冲区
            fos.getFD().sync();
            
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (null != mXmlSerializer)
                {
                    mXmlSerializer.flush();
                    mXmlSerializer = null;
                    
                }
                
                if (null != fos)
                {
                    fos.close();
                }
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Start to write a special object initialized in subclass, and the subclass
     * implements this method correspondingly.
     */
    protected abstract void writeObj();
    
    /**
     * 写 text 值
     */
    protected void writeText(String tag, String text, String namespace)
            throws IllegalArgumentException, IllegalStateException, IOException
    {
        if (null == text) { return; }
        mXmlSerializer.startTag(namespace, tag);
        mXmlSerializer.text(text);
        mXmlSerializer.endTag(namespace, tag);
    }
    
    /**
     * Write XML element's attributes like name="value".
     * 
     * @param values
     */
    protected void writeAttrs(ContentValues values)
    {
        Iterator<Entry<String, Object>> iterator = values.valueSet().iterator();
        
        Entry<String, Object> next;
        while (iterator.hasNext())
        {
            next = iterator.next();
            
            try
            {
                final Object value = next.getValue();
                
                mXmlSerializer.attribute(null, next.getKey(),
                        null == value ? "" : String.valueOf(value));
            }
            catch (IllegalArgumentException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalStateException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
