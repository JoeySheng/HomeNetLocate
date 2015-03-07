
package com.joey.homenetlocate.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.MKTransitRoutePlan;
import com.joey.homenetlocate.R;

/**
 * 文件名称 : RouteListAdapter
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : RouteListAdapter - 路线列表适配器
 * <p>
 * 创建时间 : 2014-3-25 上午12:34:29
 * <p>
 */
public class RouteListAdapter extends BaseAdapter
{
    private List<MKTransitRoutePlan> routesList;
    
    private Context mContext;
    
    public RouteListAdapter(List<MKTransitRoutePlan> routesList, Context mContext)
    {
        super();
        this.routesList = routesList;
        this.mContext = mContext;
    }
    
    @Override
    public int getCount()
    {
        return routesList.size();
        
    }
    
    @Override
    public Object getItem(int position)
    {
        return routesList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder;
        if (convertView == null)
        {
            holder = new Holder();
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.route_plan_list_item, null);
            holder.num = (TextView) convertView.findViewById(R.id.plan_num);
            holder.cont = (TextView) convertView.findViewById(R.id.plan_cont);
            holder.distance = (TextView) convertView.findViewById(R.id.plan_distance);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }
        
        holder.num.setText("方案" + (position + 1) + ":");
        holder.cont.setText(routesList.get(position).getContent());
        holder.distance.setText("距离：" + routesList.get(position).getDistance() + " 米");
        return convertView;
    }
    
    public class Holder
    {
        TextView num;
        
        TextView cont;
        
        TextView distance;
    }
}
