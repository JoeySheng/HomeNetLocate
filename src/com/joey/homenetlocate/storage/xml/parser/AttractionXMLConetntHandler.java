
package com.joey.homenetlocate.storage.xml.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.ContentValues;

import com.joey.homenetlocate.storage.db.DBConstant;

/**
 * 文件名称 : AttractionXMLConetntHandler
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : AttractionXMLConetntHandler - 景点xml解析
 * <p>
 * 创建时间 : 2014-3-24 下午10:16:11
 * <p>
 */
public class AttractionXMLConetntHandler extends DefaultHandler
{
    /**
     * <?xml version='1.0' encoding='uft-8'?> <attractions time="1370569091417"
     * longitude="118.783891" latitude="32.089089" name="长江科技园  city="南京"
     */
    
    private static final String ATTRACTION = "attractions";
    
    private static final String NAME = "name";
    
    private static final String TIME = "time";
    
    private static final String LONGITUDE = "longitude";
    
    private static final String LATITUDE = "latitude";
    
    private static final String CITY = "city";
    
    private ContentValues values;
    
    private String localName;
    
    @Override
    public void startDocument() throws SAXException
    {
        values = new ContentValues();
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
    {
        this.localName = localName;
        if (localName.equals(ATTRACTION))
        {
            values.put(DBConstant.ATTRACTIONS_NAME, attributes.getValue(NAME));
            values.put(DBConstant.ATTRACTIONS_TIME, attributes.getValue(TIME));
            values.put(DBConstant.ATTRACTIONS_LONGITUDE, attributes.getValue(LONGITUDE));
            values.put(DBConstant.ATTRACTIONS_LATITUDE, attributes.getValue(LATITUDE));
            values.put(DBConstant.ATTRACTIONS_CITY, attributes.getValue(CITY));
        }
    }
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        // TODO Auto-generated method stub
        super.characters(ch, start, length);
    }
    
    @Override
    public void endDocument() throws SAXException
    {
        // TODO Auto-generated method stub
        super.endDocument();
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        // TODO Auto-generated method stub
        super.endElement(uri, localName, qName);
    }
    
    public ContentValues getAttraction()
    {
        return values;
    }
    
}
