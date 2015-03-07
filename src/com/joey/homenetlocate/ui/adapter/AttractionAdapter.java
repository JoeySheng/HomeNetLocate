
package com.joey.homenetlocate.ui.adapter;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joey.homenetlocate.R;
import com.joey.homenetlocate.model.Attraction;
import com.joey.homenetlocate.utils.DateUtil;

/**
 * 文件名称 : AttractionAdapter
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : AttractionAdapter - 景点列表适配器
 * <p>
 * 创建时间 : 2014-3-25 上午12:33:30
 * <p>
 */
public class AttractionAdapter extends BaseAdapter
{
    private Context mContext;
    
    private LayoutInflater mInflater;
    
    private ArrayList<Attraction> mAttractions;
    
    public AttractionAdapter(Context context, ArrayList<Attraction> attractions)
    {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mAttractions = attractions;
    }
    
    @Override
    public int getCount()
    {
        if (null != mAttractions) { return mAttractions.size(); }
        return 0;
    }
    
    @Override
    public Object getItem(int arg0)
    {
        if (null != mAttractions) { return mAttractions.get(arg0); }
        return null;
    }
    
    @Override
    public long getItemId(int arg0)
    {
        return arg0;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.attraction_list_item, null);
            
            holder = new ViewHolder();
            holder.mNameTv = (TextView) convertView.findViewById(R.id.name);
            holder.mLongitudeTv = (TextView) convertView.findViewById(R.id.longitude);
            holder.mLatitudeTv = (TextView) convertView.findViewById(R.id.latitude);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.time);
            
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        
        final Attraction attraction = mAttractions.get(position);
        if (null != attraction)
        {
            holder.mNameTv.setText(mContext.getResources().getString(R.string.name_result,
                    String.valueOf(attraction.getName())));
            holder.mLongitudeTv.setText(mContext.getResources().getString(
                    R.string.longitude_result, String.valueOf(attraction.getLongitude())));
            holder.mLatitudeTv.setText(mContext.getResources().getString(R.string.latitude_result,
                    String.valueOf(attraction.getLatitude())));
            holder.mTimeTv.setText(mContext.getResources().getString(R.string.time_result,
                    DateUtil.formatTimeInMillis(attraction.getTime(), Calendar.getInstance())));
        }
        return convertView;
    }
    
    class ViewHolder
    {
        TextView mNameTv;
        
        TextView mLongitudeTv;
        
        TextView mLatitudeTv;
        
        TextView mTimeTv;
    }
    
}
