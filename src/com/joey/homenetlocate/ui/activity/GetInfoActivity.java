
package com.joey.homenetlocate.ui.activity;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.joey.homenetlocate.R;
import com.joey.homenetlocate.application.LocateApplication;
import com.joey.homenetlocate.fusion.FusionCode;
import com.joey.homenetlocate.fusion.FusionField;
import com.joey.homenetlocate.storage.db.DBConstant;
import com.joey.homenetlocate.storage.db.LocateDBHelper;
import com.joey.homenetlocate.utils.CheckNetworkUtils;
import com.joey.homenetlocate.utils.PromptUtils;

/**
 * 文件名称 : GetInfoActivity
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : GetInfoActivity - 景点采集
 * <p>
 * 创建时间 : 2014-3-25 上午12:36:26
 * <p>
 */
public class GetInfoActivity extends BaseActivity
{
    
    private String mName, mLongitude, mLatitude;
    
    private EditText mNameEt, mLongitudeEt, mLatitudeEt;
    
    private Button mGetInfoBtn, mSaveInfoBtn;
    
    private static MapView mMapView = null;
    
    private MapController mMapController = null;
    
    private MKMapViewListener mMapListener = null;
    
    private LocationClient mLocClient;
    
    public MyLocationListener mMyListener = new MyLocationListener();
    
    private MyLocationOverlay mMyLocationOverlay = null;
    
    private LocationData locData = null;
    
    private LocateDBHelper mLocateDBHelper = null;
    
    private Handler mHandler = new Handler()
    {
        
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what)
            {
                case FusionCode.MAP_RELOC:
                {
                    mLongitudeEt.setText("" + FusionField.longitude);
                    mLatitudeEt.setText("" + FusionField.latitude);
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
        LocateApplication app = (LocateApplication) this.getApplication();
        if (app.mBMapManager == null)
        {
            app.mBMapManager = new BMapManager(this);
            app.mBMapManager.init(FusionCode.BMAPKEY, new LocateApplication.MyGeneralListener());
        }
        setContentView(R.layout.get_info);
        mMapView = (MapView) this.findViewById(R.id.bmapView);
        mMapController = mMapView.getController();
        
        initMapView();
        
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(mMyListener);
        
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        
        mMapView.setBuiltInZoomControls(true);
        mMapListener = new MKMapViewListener()
        {
            
            @Override
            public void onMapMoveFinish()
            {
                // TODO Auto-generated method stub
            }
            
            @Override
            public void onClickMapPoi(MapPoi mapPoiInfo)
            {
                // TODO Auto-generated method stub
            }
            
            @Override
            public void onGetCurrentMap(Bitmap b)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onMapAnimationFinish()
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onMapLoadFinish()
            {
                // TODO Auto-generated method stub
                
            }
        };
        mMapView.regMapViewListener(LocateApplication.getInstance().mBMapManager, mMapListener);
        mMyLocationOverlay = new MyLocationOverlay(mMapView);
        locData = new LocationData();
        mMyLocationOverlay.setData(locData);
        mMapView.getOverlays().add(mMyLocationOverlay);
        mMyLocationOverlay.enableCompass();
        mMapView.refresh();
        mNameEt = (EditText) this.findViewById(R.id.name);
        mLongitudeEt = (EditText) this.findViewById(R.id.longitude);
        mLatitudeEt = (EditText) this.findViewById(R.id.latitude);
        mGetInfoBtn = (Button) this.findViewById(R.id.get_info);
        mSaveInfoBtn = (Button) this.findViewById(R.id.save_info);
    }
    
    private void initMapView()
    {
        mMapView.setLongClickable(true);
    }
    
    @Override
    protected void onPause()
    {
        mMapView.onPause();
        super.onPause();
    }
    
    @Override
    protected void onResume()
    {
        mMapView.onResume();
        super.onResume();
    }
    
    @Override
    protected void onDestroy()
    {
        if (null != mLocClient)
        {
            mLocClient.stop();
        }
        mMapView.destroy();
        
        LocateApplication app = (LocateApplication) this.getApplication();
        if (app.mBMapManager != null)
        {
            app.mBMapManager.destroy();
            app.mBMapManager = null;
        }
        super.onDestroy();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        mMapView.onRestoreInstanceState(savedInstanceState);
    }
    
    @Override
    public void initData()
    {
        if (!CheckNetworkUtils.getInstance().isNetworkAvailable(this))
        {
            PromptUtils.getInstance().displayToastId(this, true, R.string.net_error);
        }
        
        mLocateDBHelper = LocateDBHelper.getInstance(GetInfoActivity.this);
    }
    
    @Override
    public void setListener()
    {
        mGetInfoBtn.setOnClickListener(mClickListener);
        mSaveInfoBtn.setOnClickListener(mClickListener);
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
                    // 获取经纬度
                    locate();
                    break;
                }
                case R.id.save_info:
                {
                    // 保存信息
                    if (verifyInfo())
                    {
                        saveData();
                    }
                    break;
                }
                
                default:
                    break;
            }
        }
    };
    
    private void locate()
    {
        mLocClient.requestLocation();
    }
    
    /**
     * 保存信息
     */
    private void saveData()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstant.ATTRACTIONS_NAME, mName);
        contentValues.put(DBConstant.ATTRACTIONS_LONGITUDE, Double.valueOf(mLongitude));
        contentValues.put(DBConstant.ATTRACTIONS_LATITUDE, Double.valueOf(mLatitude));
        contentValues.put(DBConstant.ATTRACTIONS_TIME, System.currentTimeMillis());
        contentValues.put(DBConstant.ATTRACTIONS_CITY, FusionField.city);
        
        if (mLocateDBHelper.insertAttraction(contentValues) > 0)
        {
            PromptUtils.getInstance().displayToastId(GetInfoActivity.this, true,
                    R.string.save_success);
            mNameEt.setText("");
            mLongitudeEt.setText("");
            mLatitudeEt.setText("");
            mName = "";
            mLongitude = "";
            mLatitude = "";
        }
        else
        {
            PromptUtils.getInstance()
                    .displayToastId(GetInfoActivity.this, true, R.string.save_fail);
        }
        
    }
    
    private boolean verifyInfo()
    {
        mName = mNameEt.getText().toString().trim();
        mLongitude = mLongitudeEt.getText().toString().trim();
        mLatitude = mLatitudeEt.getText().toString().trim();
        if (TextUtils.isEmpty(mName))
        {
            PromptUtils.getInstance().displayToastId(GetInfoActivity.this, true,
                    R.string.name_empty_hint);
            return false;
        }
        if (TextUtils.isEmpty(mLongitude))
        {
            PromptUtils.getInstance().displayToastId(GetInfoActivity.this, true,
                    R.string.longitude_empty_hint);
            return false;
        }
        if (TextUtils.isEmpty(mLatitude))
        {
            PromptUtils.getInstance().displayToastId(GetInfoActivity.this, true,
                    R.string.latitude_empty_hint);
            return false;
        }
        return true;
    }
    
    /**
     * 监听函数，有新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class MyLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location)
        {
            if (location == null) { return; }
            
            FusionField.longitude = location.getLongitude();
            FusionField.latitude = location.getLatitude();
            FusionField.city = location.getCity();
            Log.d("city========>", location.getLongitude() + "/" + location.getLatitude() + "/"
                    + FusionField.city);
            
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            locData.accuracy = location.getRadius();
            locData.direction = location.getDerect();
            mMyLocationOverlay.setData(locData);
            mMapView.refresh();
            mMapController.animateTo(new GeoPoint((int) (locData.latitude * 1e6),
                    (int) (locData.longitude * 1e6)), mHandler.obtainMessage(1));
        }
        
        public void onReceivePoi(BDLocation poiLocation)
        {
            if (poiLocation == null) { return; }
        }
    }
    
}
