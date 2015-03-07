
package com.joey.homenetlocate.ui.activity;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.joey.homenetlocate.R;
import com.joey.homenetlocate.storage.db.LocateDBHelper;
import com.joey.homenetlocate.storage.xml.parser.DBParseHelper;
import com.joey.homenetlocate.storage.xml.saver.DBBackupHelper;
import com.joey.homenetlocate.utils.DialogUtil;
import com.joey.homenetlocate.utils.FileManager;
import com.joey.homenetlocate.utils.PromptUtils;

/**
 * 文件名称 : HomeActivity
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : HomeActivity - 主界面
 * <p>
 * 创建时间 : 2014-3-25 上午12:34:53
 * <p>
 */
public class HomeActivity extends BaseActivity
{
    
    private static final int BACKUP_SUCCESS = 0;
    
    private static final int RESTORE_SUCCESS = BACKUP_SUCCESS + 1;
    
    private static final int RESTORE_FAIL = RESTORE_SUCCESS + 1;
    
    private String[] mRecommendRoute;
    
    private Button mGetInfoBtn;
    
    private Button mAllInfoBtn;
    
    private Button mMapInfoBtn;
    
    private Button mRecommendBtn;
    
    private Button mBackupBtn;
    
    private Button mRestoreBtn;
    
    private Button mRestoreBtn1;
    
    private LocateDBHelper mLocateDBHelper;
    
    private Dialog mProgressDialog = null;
    
    private Handler mHandler = new Handler()
    {
        
        @Override
        public void handleMessage(Message msg)
        {
            final int what = msg.what;
            switch (what)
            {
                case BACKUP_SUCCESS:
                {
                    PromptUtils.getInstance().displayToastId(HomeActivity.this, true,
                            R.string.backup_success);
                    break;
                }
                case RESTORE_SUCCESS:
                {
                    PromptUtils.getInstance().displayToastId(HomeActivity.this, true,
                            R.string.parse_success);
                    break;
                }
                case RESTORE_FAIL:
                {
                    PromptUtils.getInstance().displayToastId(HomeActivity.this, true,
                            R.string.parse_fail);
                    break;
                }
                
                default:
                    break;
            }
            
            if (null != mProgressDialog)
            {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }
    };
    
    @Override
    public void initView()
    {
        setContentView(R.layout.home);
        mGetInfoBtn = (Button) this.findViewById(R.id.get_info);
        mAllInfoBtn = (Button) this.findViewById(R.id.all_info);
        mMapInfoBtn = (Button) this.findViewById(R.id.map_info);
        mRecommendBtn = (Button) this.findViewById(R.id.recommend_route);
        mBackupBtn = (Button) this.findViewById(R.id.backup_info);
        mRestoreBtn = (Button) this.findViewById(R.id.restore_info);
        mRestoreBtn1 = (Button) this.findViewById(R.id.restore_info1);
    }
    
    @Override
    public void initData()
    {
    }
    
    @Override
    public void setListener()
    {
        mGetInfoBtn.setOnClickListener(mClickListener);
        mAllInfoBtn.setOnClickListener(mClickListener);
        mMapInfoBtn.setOnClickListener(mClickListener);
        mRecommendBtn.setOnClickListener(mClickListener);
        mBackupBtn.setOnClickListener(mClickListener);
        mRestoreBtn.setOnClickListener(mClickListener);
        mRestoreBtn1.setOnClickListener(mClickListener);
    }
    
    private OnClickListener mClickListener = new OnClickListener()
    {
        
        @Override
        public void onClick(View v)
        {
            int id = v.getId();
            switch (id)
            {
                case R.id.get_info:
                {
                    startActivity(new Intent(HomeActivity.this, GetInfoActivity.class));
                    break;
                }
                case R.id.all_info:
                {
                    startActivity(new Intent(HomeActivity.this, AllInfoActivity.class));
                    break;
                }
                case R.id.map_info:
                {
                    startActivity(new Intent(HomeActivity.this, MapAttractionActivity.class));
                    break;
                }
                
                case R.id.recommend_route:
                {
                    initPopWindow();
                    break;
                }
                case R.id.backup_info:
                {
                    if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                    {
                        PromptUtils.getInstance().displayToastId(HomeActivity.this, true,
                                R.string.SD_error);
                        return;
                    }
                    // 将数据库中的内容备份为xml文件保存在SD卡中
                    backupInfo();
                    break;
                }
                case R.id.restore_info:
                {
                    // assets下面的信息XML解析保存到数据库中
                    restoreInfo(false);
                    break;
                }
                case R.id.restore_info1:
                {
                    // SD卡的信息XML解析保存到数据库中
                    restoreInfo(true);
                    break;
                }
                
                default:
                    break;
            }
        }
    };
    
    private void initPopWindow()
    {
        mRecommendRoute = this.getResources().getStringArray(R.array.recommend_routes);
        
        View contentView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.popwindow_recommend_route, null);
        contentView.setBackgroundColor(Color.GRAY);
        
        final PopupWindow popupWindow = new PopupWindow(findViewById(R.id.recommend_route),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(contentView);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        
        ListView listView = (ListView) contentView.findViewById(R.id.recommend_route_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mRecommendRoute);
        listView.setAdapter(adapter);
        final int num = mRecommendRoute.length;
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                if (arg2 == num - 1)
                {
                    popupWindow.dismiss();
                }
                else
                {
                    popupWindow.dismiss();
                    Intent intent = new Intent(HomeActivity.this, MapAttractionActivity.class);
                    intent.putExtra("route", mRecommendRoute[arg2]);
                    intent.putExtra("position", arg2);
                    startActivity(intent);
                }
                
            }
        });
        
        popupWindow.setFocusable(true);
        // popupWindow.showAsDropDown(mRecommendBtn);
        popupWindow.update();
        popupWindow.showAtLocation(mRecommendBtn, Gravity.CENTER_VERTICAL, 0, 0);
    }
    
    /**
     * 备份信息
     */
    private void backupInfo()
    {
        showDialog(R.string.backup_progress, true);
        new Thread()
        {
            
            @Override
            public void run()
            {
                mLocateDBHelper = LocateDBHelper.getInstance(HomeActivity.this);
                ArrayList<ContentValues> values = mLocateDBHelper.queryAllAttractionValues();
                if (null != values && values.size() > 0)
                {
                    ContentResolver resolver = getContentResolver();
                    for (ContentValues value : values)
                    {
                        DBBackupHelper.backup(value, resolver);
                    }
                }
                
                mHandler.sendEmptyMessage(BACKUP_SUCCESS);
            }
            
        }.start();
    }
    
    /**
     * 保存信息
     */
    private void restoreInfo(final boolean isSD)
    {
        showDialog(R.string.parse_progress, true);
        new Thread()
        {
            
            @Override
            public void run()
            {
                // 首先删除数据库中的所有信息
                mLocateDBHelper = LocateDBHelper.getInstance(HomeActivity.this);
                mLocateDBHelper.deleteAllAttractions();
                
                try
                {
                    if (isSD)
                    {
                        
                    }
                    else
                    {
                        String[] paths = getAssets().list(FileManager.RESTORE_DIR);
                        
                        for (String path : paths)
                        {
                            InputStream inputStream = getAssets().open(
                                    FileManager.RESTORE_DIR + File.separator + path);
                            // 解析XML文件信息插入到数据库中
                            ContentValues values = DBParseHelper.parse(inputStream);
                            if (null != values)
                            {
                                mLocateDBHelper.insertAttraction(values);
                            }
                            inputStream.close();
                        }
                        mHandler.sendEmptyMessage(RESTORE_SUCCESS);
                    }
                    
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(RESTORE_FAIL);
                }
                
            }
            
        }.start();
    }
    
    private void showDialog(int msgId, boolean isCancelable)
    {
        mProgressDialog = DialogUtil.showProgessDialog(HomeActivity.this, msgId);
        mProgressDialog.show();
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
    
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            final DialogUtil.DialogParams params = new DialogUtil.DialogParams(
                    getString(R.string.exit), getString(R.string.ok), getString(R.string.cancel));
            params.setCancelListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    params.dismiss();
                }
            });
            params.setConfirmListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    HomeActivity.this.finish();
                }
            });
            DialogUtil.showTwoButtonDialog(HomeActivity.this, params);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    };
    
}
