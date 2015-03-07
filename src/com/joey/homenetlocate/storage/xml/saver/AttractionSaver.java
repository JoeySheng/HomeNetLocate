
package com.joey.homenetlocate.storage.xml.saver;

import java.io.IOException;

import android.content.ContentValues;

import com.joey.homenetlocate.storage.db.DBConstant;

/**
 * 文件名称 : AttractionSaver
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : AttractionSaver - 景点保存
 * <p>
 * 创建时间 : 2014-3-25 上午12:30:42
 * <p>
 */
public class AttractionSaver extends BaseSaver
{
    /**
     * <?xml version='1.0' encoding='uft-8'?> <attractions time="1370569091417"
     * longitude="118.783891" latitude="32.089089" name="长江科技园 city="南京"
     */
    
    private ContentValues mAttractionValues;
    
    public AttractionSaver(String xmlPath, ContentValues attraContentValues)
    {
        mXmlFilePath = xmlPath;
        this.mAttractionValues = attraContentValues;
    }
    
    @Override
    protected void writeObj()
    {
        try
        {
            mXmlSerializer.startTag(null, DBConstant.ATTRACTIONS_TABLE_NAME);
            
            /*
             * Note: remove _id value which is auto generated by DB, we don't
             * need it when parse the XML.
             */
            mAttractionValues.remove(DBConstant.ATTRACTIONS_ID);
            writeAttrs(mAttractionValues);
            
            mXmlSerializer.endTag(null, DBConstant.ATTRACTIONS_TABLE_NAME);
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
