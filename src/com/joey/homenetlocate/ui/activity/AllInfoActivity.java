
package com.joey.homenetlocate.ui.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.joey.homenetlocate.R;
import com.joey.homenetlocate.model.Attraction;
import com.joey.homenetlocate.storage.db.LocateDBHelper;
import com.joey.homenetlocate.ui.adapter.AttractionAdapter;

/**
 * 文件名称 : AllInfoActivity
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : AllInfoActivity - 所有景点
 * <p>
 * 创建时间 : 2014-3-25 上午12:36:06
 * <p>
 */
public class AllInfoActivity extends BaseActivity
{
    private static final int GET_DATA_SUCCESS = 0;
    
    private ListView mListView;
    
    private AttractionAdapter mAdapter;
    
    private ArrayList<Attraction> mList = new ArrayList<Attraction>();
    
    private LocateDBHelper mLocateDBHelper;
    
    private Handler mHandler = new Handler()
    {
        
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what)
            {
                case GET_DATA_SUCCESS:
                {
                    mAdapter.notifyDataSetChanged();
                    break;
                }
                
                default:
                    break;
            }
        }
        
    };
    
    @Override
    public void initView()
    {
        setContentView(R.layout.all_info);
        mListView = (ListView) this.findViewById(R.id.listview);
    }
    
    @Override
    public void initData()
    {
        mLocateDBHelper = LocateDBHelper.getInstance(this);
        mAdapter = new AttractionAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        getData();
    }
    
    private void getData()
    {
        new Thread()
        {
            
            @Override
            public void run()
            {
                ArrayList<Attraction> list = mLocateDBHelper.queryAllAttractions();
                if (null != list && list.size() > 0)
                {
                    mList.clear();
                    mList.addAll(list);
                    mHandler.sendEmptyMessage(GET_DATA_SUCCESS);
                }
            }
        }.start();
    }
    
    @Override
    public void setListener()
    {
        mListView.setOnItemLongClickListener(mItemClickListener);
    }
    
    private OnItemLongClickListener mItemClickListener = new OnItemLongClickListener()
    {
        
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
        {
            longItemClick(arg2);
            return false;
        }
        
    };
    
    private void longItemClick(int position)
    {
        final Attraction attraction = mList.get(position);
        if (null == attraction) { return; }
        new AlertDialog.Builder(this)
                .setItems(R.array.info_long_click, new DialogInterface.OnClickListener()
                {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which)
                        {
                            case 0:
                            {
                                // 删除
                                if (mLocateDBHelper.delete(attraction) > 0)
                                {
                                    mList.remove(attraction);
                                    mAdapter.notifyDataSetChanged();
                                }
                                break;
                            }
                            
                            default:
                                break;
                        }
                    }
                    
                }).create().show();
    }
    
}
