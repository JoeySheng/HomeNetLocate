
package com.joey.homenetlocate.storage.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.joey.homenetlocate.model.Attraction;

/**
 * 文件名称 : LocateDBHelper
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : LocateDBHelper - 数据库操作类
 * <p>
 * 创建时间 : 2014-3-24 下午10:14:38
 * <p>
 */
public class LocateDBHelper extends SQLiteOpenHelper
{
    private static final String TAG = "LocateDBHelper";
    
    private static LocateDBHelper mInstance = null;
    
    public static synchronized LocateDBHelper getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new LocateDBHelper(context);
        }
        return mInstance;
    }
    
    private LocateDBHelper(Context context)
    {
        super(context, DBConstant.DB_NAME, null, DBConstant.DB_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.i(TAG, "onCreate");
        StringBuffer buffer = new StringBuffer("create table ");
        buffer.append(DBConstant.ATTRACTIONS_TABLE_NAME);
        buffer.append("(");
        buffer.append(DBConstant.ATTRACTIONS_ID);
        buffer.append(" integer primary key autoincrement, ");
        buffer.append(DBConstant.ATTRACTIONS_NAME);
        buffer.append(" text,");
        buffer.append(DBConstant.ATTRACTIONS_LONGITUDE);
        buffer.append(" double, ");
        buffer.append(DBConstant.ATTRACTIONS_TIME);
        buffer.append(" long, ");
        buffer.append(DBConstant.ATTRACTIONS_CITY);
        buffer.append(" text,");
        buffer.append(DBConstant.ATTRACTIONS_LATITUDE);
        buffer.append(" double)");
        
        String sql = buffer.toString();
        Log.i(TAG, sql);
        db.execSQL(sql);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2)
    {
        // TODO Auto-generated method stub
        Log.i(TAG, "onUpgrade");
    }
    
    public long insertAttraction(ContentValues contentValues)
    {
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(DBConstant.ATTRACTIONS_TABLE_NAME, null, contentValues);
    }
    
    public ArrayList<Attraction> queryAllAttractions()
    {
        ArrayList<Attraction> list = new ArrayList<Attraction>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try
        {
            cursor = db.query(DBConstant.ATTRACTIONS_TABLE_NAME, new String[]
            {
                    DBConstant.ATTRACTIONS_ID, DBConstant.ATTRACTIONS_NAME,
                    DBConstant.ATTRACTIONS_LONGITUDE, DBConstant.ATTRACTIONS_LATITUDE,
                    DBConstant.ATTRACTIONS_TIME, DBConstant.ATTRACTIONS_CITY
            }, null, null, null, null, null);
            
            if (null != cursor)
            {
                while (cursor.moveToNext())
                {
                    Attraction attraction = new Attraction();
                    attraction
                            .setId(cursor.getInt(cursor.getColumnIndex(DBConstant.ATTRACTIONS_ID)));
                    attraction.setName(cursor.getString(cursor
                            .getColumnIndex(DBConstant.ATTRACTIONS_NAME)));
                    attraction.setLongitude(cursor.getDouble(cursor
                            .getColumnIndex(DBConstant.ATTRACTIONS_LONGITUDE)));
                    attraction.setLatitude(cursor.getDouble(cursor
                            .getColumnIndex(DBConstant.ATTRACTIONS_LATITUDE)));
                    attraction.setTime(cursor.getLong(cursor
                            .getColumnIndex(DBConstant.ATTRACTIONS_TIME)));
                    attraction.setCity(cursor.getString(cursor
                            .getColumnIndex(DBConstant.ATTRACTIONS_CITY)));
                    
                    list.add(attraction);
                }
            }
        }
        catch (Exception e)
        {
        }
        finally
        {
            if (null != cursor)
            {
                cursor.close();
            }
        }
        
        return list;
    }
    
    public ArrayList<ContentValues> queryAllAttractionValues()
    {
        ArrayList<ContentValues> list = new ArrayList<ContentValues>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try
        {
            cursor = db.query(DBConstant.ATTRACTIONS_TABLE_NAME, new String[]
            {
                    DBConstant.ATTRACTIONS_ID, DBConstant.ATTRACTIONS_NAME,
                    DBConstant.ATTRACTIONS_LONGITUDE, DBConstant.ATTRACTIONS_LATITUDE,
                    DBConstant.ATTRACTIONS_TIME, DBConstant.ATTRACTIONS_CITY
            }, null, null, null, null, null);
            
            if (null != cursor)
            {
                while (cursor.moveToNext())
                {
                    ContentValues values = new ContentValues();
                    values.put(DBConstant.ATTRACTIONS_ID,
                            cursor.getInt(cursor.getColumnIndex(DBConstant.ATTRACTIONS_ID)));
                    values.put(DBConstant.ATTRACTIONS_NAME,
                            cursor.getString(cursor.getColumnIndex(DBConstant.ATTRACTIONS_NAME)));
                    values.put(DBConstant.ATTRACTIONS_LONGITUDE, cursor.getDouble(cursor
                            .getColumnIndex(DBConstant.ATTRACTIONS_LONGITUDE)));
                    values.put(DBConstant.ATTRACTIONS_LATITUDE, cursor.getDouble(cursor
                            .getColumnIndex(DBConstant.ATTRACTIONS_LATITUDE)));
                    values.put(DBConstant.ATTRACTIONS_TIME,
                            cursor.getLong(cursor.getColumnIndex(DBConstant.ATTRACTIONS_TIME)));
                    values.put(DBConstant.ATTRACTIONS_CITY,
                            cursor.getString(cursor.getColumnIndex(DBConstant.ATTRACTIONS_CITY)));
                    
                    list.add(values);
                }
            }
        }
        catch (Exception e)
        {
        }
        finally
        {
            if (null != cursor)
            {
                cursor.close();
            }
        }
        
        return list;
    }
    
    public int delete(Attraction attraction)
    {
        if (null == attraction) { return -1; }
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(DBConstant.ATTRACTIONS_TABLE_NAME, DBConstant.ATTRACTIONS_ID + "=?",
                new String[]
                {
                    String.valueOf(attraction.getId())
                });
    }
    
    /**
     * 删除所有景点信息
     */
    public void deleteAllAttractions()
    {
        SQLiteDatabase db = getWritableDatabase();
        
        String[] projection =
        {
            DBConstant.ATTRACTIONS_ID
        };
        Cursor cursor = null;
        
        try
        {
            cursor = db.query(DBConstant.ATTRACTIONS_TABLE_NAME, projection, null, null, null,
                    null, null);
            if ((null != cursor) && cursor.moveToFirst())
            {
                do
                {
                    int attractionId = cursor.getInt(cursor
                            .getColumnIndex(DBConstant.ATTRACTIONS_ID));
                    db.delete(DBConstant.ATTRACTIONS_TABLE_NAME, DBConstant.ATTRACTIONS_ID + "=?",
                            new String[]
                            {
                                String.valueOf(attractionId)
                            });
                }
                while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
        }
        finally
        {
            if (null != cursor)
            {
                cursor.close();
            }
        }
    }
    
}
