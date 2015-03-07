
package com.joey.homenetlocate.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRoutePlan;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.joey.homenetlocate.R;
import com.joey.homenetlocate.application.LocateApplication;
import com.joey.homenetlocate.fusion.FusionCode;
import com.joey.homenetlocate.fusion.FusionField;
import com.joey.homenetlocate.model.Attraction;
import com.joey.homenetlocate.storage.db.LocateDBHelper;
import com.joey.homenetlocate.ui.adapter.RouteListAdapter;
import com.joey.homenetlocate.utils.PromptUtils;
import com.joey.homenetlocate.utils.RouteDialog;

/**
 * 文件名称 : MapAttractionActivity
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : MapAttractionActivity - 地图界面
 * <p>
 * 创建时间 : 2014-3-25 上午12:39:35
 * <p>
 */
public class MapAttractionActivity extends BaseActivity
{
    private List<Attraction> mAttractions;
    
    private LocateDBHelper mLocateDBHelper;
    
    private static MapView mMapView = null;
    
    private MapController mMapController = null;
    
    private MKMapViewListener mMapListener = null;
    
    private LocationClient mLocClient;
    
    private MyLocationListener myListener = new MyLocationListener();
    
    MyLocationOverlay myLocationOverlay = null;
    
    LocationData locData = null;
    
    private Drawable marker = null;
    
    OverlayTest ov = null;
    
    private MKSearch mMKSearch = null;
    
    public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
    
    /**
     * 查询路线的Dialog
     */
    protected RouteDialog dialog;
    
    /**
     * 公交线路
     */
    private TransitOverlay routeOverlay;
    
    /**
     * 查询公交 返回结果
     */
    private MKTransitRouteResult routeResult;
    
    private List<MKTransitRoutePlan> routesList = new ArrayList<MKTransitRoutePlan>();
    
    /**
     * 线路
     */
    private RouteOverlay rOverlay;
    
    /**
     * 查询步行 返回结果
     */
    private MKWalkingRouteResult wallRouteRes;
    
    private ImageView mLocateIv;
    
    private ImageView mMapModeChangeIv;// 改变地图展现模式的按钮图标
    
    private RelativeLayout mTotalAttractionsLayout;
    
    private TextView mTotalNumTv;
    
    private static boolean isWeiXing = false;// 卫星地图展示标识
    
    private static boolean isSimple = true;// 普通地图展示标识
    
    private Button mRouteShowBtn;
    
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
                    // 从数据库中读取所有的景点信息
                    queryAttractions();
                    break;
                }
                case FusionCode.MAP_KEY_SEARCH:
                {
                    // 显示在地图上
                    loadAttractions();
                    break;
                }
                case FusionCode.MAP_KEY_BUS_ROUTE:
                {
                    if (dialog.isShowing())
                    {
                        dialog.dismiss();
                    }
                    routeResult = (MKTransitRouteResult) (msg.obj);
                    
                    if (routesList == null)
                    {
                        routesList = new ArrayList<MKTransitRoutePlan>();
                    }
                    else
                    {
                        routesList.clear();
                    }
                    for (int i = 0; i < routeResult.getNumPlan(); i++)
                    {
                        routesList.add(routeResult.getPlan(i));
                    }
                    
                    chooseRouteDialog(routesList);
                    break;
                }
                case FusionCode.MAP_KEY_WALK_ROUTE:
                {
                    if (dialog.isShowing())
                    {
                        dialog.dismiss();
                    }
                    wallRouteRes = (MKWalkingRouteResult) (msg.obj);
                    rOverlay = new RouteOverlay(MapAttractionActivity.this, mMapView);
                    rOverlay.setData(wallRouteRes.getPlan(0).getRoute(0));
                    mMapView.getOverlays().clear();
                    mMapView.getOverlays().add(rOverlay);
                    mMapView.refresh();
                    mMapView.getController().zoomToSpan(rOverlay.getLatSpanE6(),
                            rOverlay.getLonSpanE6());
                    mMapView.getController().animateTo(wallRouteRes.getStart().pt);
                    break;
                }
                
                default:
                    break;
            }
        }
        
    };
    
    public void chooseRouteDialog(List<MKTransitRoutePlan> routesList)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapAttractionActivity.this);
        LayoutInflater inflater = (LayoutInflater) MapAttractionActivity.this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.route_plan_list, null);
        
        ListView myListView = (ListView) layout.findViewById(R.id.route_plan);
        RouteListAdapter adapter = new RouteListAdapter(routesList, this);
        myListView.setAdapter(adapter);
        
        builder.setTitle(R.string.route_plan);
        builder.setView(layout);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        
        myListView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                routeOverlay = new TransitOverlay(MapAttractionActivity.this, mMapView);
                routeOverlay.setData(routeResult.getPlan(position));
                mMapView.getOverlays().clear();
                mMapView.getOverlays().add(routeOverlay);
                mMapView.refresh();
                mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
                        routeOverlay.getLonSpanE6());
                mMapView.getController().animateTo(routeResult.getStart().pt);
                alertDialog.dismiss();
            }
        });
        
    }
    
    private void queryAttractions()
    {
        mLocateDBHelper = LocateDBHelper.getInstance(MapAttractionActivity.this);
        if (null == mAttractions)
        {
            mAttractions = new ArrayList<Attraction>();
        }
        
        mAttractions.clear();
        mAttractions = mLocateDBHelper.queryAllAttractions();
        mHandler.sendEmptyMessage(FusionCode.MAP_KEY_SEARCH);
    }
    
    private void loadAttractions()
    {
        if (null == marker)
        {
            marker = MapAttractionActivity.this.getResources().getDrawable(R.drawable.bike_marker);
        }
        if (null == ov)
        {
            ov = new OverlayTest(marker, MapAttractionActivity.this, mMapView);
        }
        mMapView.getOverlays().add(ov);
        
        if (null == mGeoList)
        {
            mGeoList = new ArrayList<OverlayItem>();
        }
        
        if (null != mGeoList && mGeoList.size() > 0)
        {
            mGeoList.clear();
        }
        
        if (mAttractions != null && mAttractions.size() > 0)
        {
            mTotalNumTv.setText("" + mAttractions.size());
            
            for (Attraction attraction : mAttractions)
            {
                OverlayItem overlayItem = new OverlayItem(new GeoPoint(
                        (int) (attraction.getLatitude() * 1E6),
                        (int) (attraction.getLongitude() * 1E6)), attraction.getName(),
                        attraction.getName());
                
                Drawable drawable = new BitmapDrawable(convertViewToBitmap(attraction.getName()));
                overlayItem.setMarker(drawable);
                mGeoList.add(overlayItem);
            }
            
            if (mGeoList != null && mGeoList.size() > 0)
            {
                ov.addItem(mGeoList);
            }
            mMapView.refresh();
        }
    }
    
    @Override
    public void initView()
    {
        LocateApplication app = (LocateApplication) this.getApplication();
        if (app.mBMapManager == null)
        {
            app.mBMapManager = new BMapManager(this);
            app.mBMapManager.init(FusionCode.BMAPKEY, new LocateApplication.MyGeneralListener());
        }
        setContentView(R.layout.map_info);
        
        mLocateIv = (ImageView) this.findViewById(R.id.attractions_locate);
        mTotalAttractionsLayout = (RelativeLayout) this.findViewById(R.id.attractions_total_lay);
        mTotalNumTv = (TextView) this.findViewById(R.id.total_num);
        
        mMapModeChangeIv = (ImageView) this.findViewById(R.id.map_mode_change);
        mRouteShowBtn = (Button) this.findViewById(R.id.route_show);
        mRouteShowBtn.setVisibility(View.GONE);
        
        if (this.getIntent().hasExtra("route"))
        {
            mLocateIv.setVisibility(View.GONE);
            mTotalAttractionsLayout.setVisibility(View.GONE);
            int position = this.getIntent().getExtras().getInt("position");
            if (position == 0)
            {
                initRoute(31.226797, 120.596436);
                // 120.596436,31.226797 国际教育园
                showSZIESRoute();
                initMapView();
            }
            else if (position == 1)
            {
                // 120.600799,31.230271 苏州职业大学
                initRoute(31.230271, 120.600799);
                showSZDRoute();
            }
        }
        else
        {
            initMapView();
        }
        // 创建线路dialog
        dialog = new RouteDialog(this, R.style.dialog3);
        dialog.setContentView(R.layout.from_to);
    }
    
    private void initMapView()
    {
        mMapView = (MapView) this.findViewById(R.id.attractions_map);
        mMapController = mMapView.getController();
        mMapView.setLongClickable(true);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        
        mMKSearch = new MKSearch();
        // 注意，MKSearchListener只支持一个，以最后一次设置为准
        mMKSearch.init(LocateApplication.getInstance().mBMapManager, new MySearchListener(mHandler,
                this));
        
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
        myLocationOverlay = new MyLocationOverlay(mMapView);
        locData = new LocationData();
        myLocationOverlay.setData(locData);
        mMapView.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableCompass();
        mMapView.refresh();
    }
    
    private void initRoute(final double cLat, final double cLon)
    {
        mMapView = (MapView) this.findViewById(R.id.attractions_map);
        mMapController = mMapView.getController();
        mMapController.enableClick(true);
        mMapView.getController().setZoom(18);
        /**
         * 将地图移动至指定点
         * 使用百度经纬度坐标，可以通过http://api.map.baidu.com/lbsapi/getpoint/index
         * .html查询地理坐标 如果需要在百度地图上显示使用其他坐标系统的位置，请发邮件至mapapi@baidu.com申请坐标转换接口
         */
        GeoPoint p;
        Intent intent = getIntent();
        if (intent.hasExtra("x") && intent.hasExtra("y"))
        {
            // 当用intent参数时，设置中心点为指定点
            Bundle b = intent.getExtras();
            p = new GeoPoint(b.getInt("y"), b.getInt("x"));
        }
        else
        {
            // 设置中心点为所需的地点
            p = new GeoPoint((int) (cLat * 1E6), (int) (cLon * 1E6));
        }
        
        mMapController.setCenter(p);
        
        /**
         * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
         */
        mMapListener = new MKMapViewListener()
        {
            @Override
            public void onMapMoveFinish()
            {
                /**
                 * 在此处理地图移动完成回调 缩放，平移等操作完成后，此回调被触发
                 */
            }
            
            @Override
            public void onClickMapPoi(MapPoi mapPoiInfo)
            {
                /**
                 * 在此处理底图poi点击事件 显示底图poi名称并移动至该点 设置过：
                 * mMapController.enableClick(true); 时，此回调才能被触发
                 */
                String title = "";
                if (mapPoiInfo != null)
                {
                    title = mapPoiInfo.strText;
                    Toast.makeText(MapAttractionActivity.this, title, Toast.LENGTH_SHORT).show();
                    mMapController.animateTo(mapPoiInfo.geoPt);
                }
            }
            
            @Override
            public void onGetCurrentMap(Bitmap b)
            {
                /**
                 * 当调用过 mMapView.getCurrentMap()后，此回调会被触发 可在此保存截图至存储设备
                 */
            }
            
            @Override
            public void onMapAnimationFinish()
            {
                /**
                 * 地图完成带动画的操作（如: animationTo()）后，此回调被触发
                 */
            }
            
            @Override
            public void onMapLoadFinish()
            {
                // TODO Auto-generated method stub
                
            }
        };
        mMapView.regMapViewListener(LocateApplication.getInstance().mBMapManager, mMapListener);
    }
    
    @Override
    public void initData()
    {
        
    }
    
    @Override
    public void setListener()
    {
        mLocateIv.setOnClickListener(mClickListener);
        mTotalAttractionsLayout.setOnClickListener(mClickListener);
        mMapModeChangeIv.setOnClickListener(mClickListener);
        mRouteShowBtn.setOnClickListener(mClickListener);
    }
    
    private OnClickListener mClickListener = new OnClickListener()
    {
        
        public void onClick(View v)
        {
            int id = v.getId();
            switch (id)
            {
                case R.id.attractions_locate:
                {
                    // 获取经纬度
                    locate();
                    break;
                }
                case R.id.attractions_total_lay:
                {
                    // 进入列表界面
                    startActivity(new Intent(MapAttractionActivity.this, AllInfoActivity.class));
                    break;
                }
                
                case R.id.map_mode_change:
                {
                    if (isWeiXing)
                    {
                        isWeiXing = false;
                        isSimple = true;
                        mMapView.setSatellite(isWeiXing);
                        mMapView.setTraffic(isSimple);
                    }
                    else if (isSimple)
                    {
                        isSimple = false;
                        isWeiXing = true;
                        mMapView.setSatellite(isWeiXing);
                        mMapView.setTraffic(isSimple);
                    }
                    break;
                }
                case R.id.route_show:
                {
                    // 展示自设路线
                    showSZDRoute();
                    break;
                }
                
                default:
                    break;
            }
        }
    };
    
    private void showSZDRoute()
    {
        /**
         * 演示自定义路线使用方法
         * 想知道某个点的百度经纬度坐标请点击：http://api.map.baidu.com/lbsapi/getpoint/index.html
         */
        GeoPoint p1 = new GeoPoint((int) (31.230271 * 1E6), (int) (120.600799 * 1E6));// 118.89356,32.091581
        GeoPoint p2 = new GeoPoint((int) (31.230027 * 1E6), (int) (120.600034 * 1E6));// 118.896048,32.094234
        GeoPoint p3 = new GeoPoint((int) (31.229314 * 1E6), (int) (120.598851 * 1E6));// 118.903702,32.0987
        GeoPoint p4 = new GeoPoint((int) (31.22917 * 1E6), (int) (120.600568 * 1E6));// 118.911356,32.091298
        GeoPoint p5 = new GeoPoint((int) (31.227815 * 1E6), (int) (120.601148 * 1E6));// 118.911356,32.091298
        GeoPoint p6 = new GeoPoint((int) (31.226823 * 1E6), (int) (120.600981 * 1E6));// 118.911356,32.091298
        GeoPoint p7 = new GeoPoint((int) (31.226823 * 1E6), (int) (120.600981 * 1E6));// 118.923321,32.097783
        GeoPoint p8 = new GeoPoint((int) (31.228571 * 1E6), (int) (120.602208 * 1E6));// 118.915991,32.102669
        GeoPoint p9 = new GeoPoint((int) (31.229479 * 1E6), (int) (120.603789 * 1E6));// 118.917212,32.109337
        GeoPoint p10 = new GeoPoint((int) (31.23005 * 1E6), (int) (120.603241 * 1E6));// 118.917212,32.109337
        GeoPoint p11 = new GeoPoint((int) (31.230665 * 1E6), (int) (120.602347 * 1E6));// 118.917212,32.109337
        // 起点坐标
        GeoPoint start = p1;
        // 终点坐标
        GeoPoint stop = p11;
        // 第一站，站点坐标为p3,经过p1,p2
        GeoPoint[] step1 = new GeoPoint[3];
        step1[0] = p1;
        step1[1] = p2;
        step1[2] = p3;
        // 第二站，站点坐标为p5,经过p4
        GeoPoint[] step2 = new GeoPoint[4];
        step2[0] = p4;
        step2[1] = p5;
        step2[2] = p6;
        step2[3] = p7;
        // 第三站，站点坐标为p7,经过p6
        GeoPoint[] step3 = new GeoPoint[2];
        step3[0] = p8;
        step3[1] = p9;
        GeoPoint[] step4 = new GeoPoint[2];
        step4[0] = p10;
        step4[1] = p11;
        // 站点数据保存在一个二维数据中
        GeoPoint[][] routeData = new GeoPoint[4][];
        routeData[0] = step1;
        routeData[1] = step2;
        routeData[2] = step3;
        routeData[3] = step4;
        
        // 用站点数据构建一个MKRoute
        MKRoute route = new MKRoute();
        route.customizeRoute(start, stop, routeData);
        // 将包含站点信息的MKRoute添加到RouteOverlay中
        RouteOverlay routeOverlay = new RouteOverlay(MapAttractionActivity.this, mMapView);
        routeOverlay.setData(route);
        mMapView.getOverlays().clear();
        // 向地图添加构造好的RouteOverlay
        mMapView.getOverlays().add(routeOverlay);
        // 执行刷新使生效
        mMapView.refresh();
    }
    
    private void showSZIESRoute()
    {
        /**
         * 演示自定义路线使用方法
         * 想知道某个点的百度经纬度坐标请点击：http://api.map.baidu.com/lbsapi/getpoint/index.html
         */
        GeoPoint p1 = new GeoPoint((int) (31.226361 * 1E6), (int) (120.60028 * 1E6));// 118.89356,32.091581
        GeoPoint p2 = new GeoPoint((int) (31.226403 * 1E6), (int) (120.599292 * 1E6));// 118.896048,32.094234
        GeoPoint p3 = new GeoPoint((int) (31.226797 * 1E6), (int) (120.596431 * 1E6));// 118.903702,32.0987
        GeoPoint p4 = new GeoPoint((int) (31.227245 * 1E6), (int) (120.593844 * 1E6));// 118.911356,32.091298
        GeoPoint p5 = new GeoPoint((int) (31.226353 * 1E6), (int) (120.593669 * 1E6));// 118.911356,32.091298
        GeoPoint p6 = new GeoPoint((int) (31.226419 * 1E6), (int) (120.593206 * 1E6));// 118.911356,32.091298
        GeoPoint p7 = new GeoPoint((int) (31.22521 * 1E6), (int) (120.592923 * 1E6));// 118.923321,32.097783
        GeoPoint p8 = new GeoPoint((int) (31.224851 * 1E6), (int) (120.592825 * 1E6));// 118.915991,32.102669
        GeoPoint p9 = new GeoPoint((int) (31.224527 * 1E6), (int) (120.594675 * 1E6));// 118.917212,32.109337
        GeoPoint p10 = new GeoPoint((int) (31.225168 * 1E6), (int) (120.595025 * 1E6));// 118.917212,32.109337
        GeoPoint p11 = new GeoPoint((int) (31.226117 * 1E6), (int) (120.595299 * 1E6));// 118.917212,32.109337
        // 起点坐标
        GeoPoint start = p1;
        // 终点坐标
        GeoPoint stop = p11;
        // 第一站，站点坐标为p3,经过p1,p2
        GeoPoint[] step1 = new GeoPoint[3];
        step1[0] = p1;
        step1[1] = p2;
        step1[2] = p3;
        // 第二站，站点坐标为p5,经过p4
        GeoPoint[] step2 = new GeoPoint[4];
        step2[0] = p4;
        step2[1] = p5;
        step2[2] = p6;
        step2[3] = p7;
        // 第三站，站点坐标为p7,经过p6
        GeoPoint[] step3 = new GeoPoint[2];
        step3[0] = p8;
        step3[1] = p9;
        GeoPoint[] step4 = new GeoPoint[2];
        step4[0] = p10;
        step4[1] = p11;
        // 站点数据保存在一个二维数据中
        GeoPoint[][] routeData = new GeoPoint[4][];
        routeData[0] = step1;
        routeData[1] = step2;
        routeData[2] = step3;
        routeData[3] = step4;
        
        // 用站点数据构建一个MKRoute
        MKRoute route = new MKRoute();
        route.customizeRoute(start, stop, routeData);
        // 将包含站点信息的MKRoute添加到RouteOverlay中
        RouteOverlay routeOverlay = new RouteOverlay(MapAttractionActivity.this, mMapView);
        routeOverlay.setData(route);
        mMapView.getOverlays().clear();
        // 向地图添加构造好的RouteOverlay
        mMapView.getOverlays().add(routeOverlay);
        // 执行刷新使生效
        mMapView.refresh();
    }
    
    private void locate()
    {
        mLocClient.requestLocation();
    }
    
    @Override
    protected void onDestroy()
    {
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
    protected void onPause()
    {
        mMapView.onPause();
        LocateApplication app = (LocateApplication) this.getApplication();
        if (app.mBMapManager != null)
        {
            app.mBMapManager.stop();
        }
        super.onPause();
    }
    
    @Override
    protected void onResume()
    {
        mMapView.onResume();
        LocateApplication app = (LocateApplication) this.getApplication();
        if (app.mBMapManager != null)
        {
            app.mBMapManager.start();
        }
        
        if (!this.getIntent().hasExtra("route"))
        {
            locate();
        }
        super.onResume();
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
            
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            locData.accuracy = location.getRadius();
            locData.direction = location.getDerect();
            myLocationOverlay.setData(locData);
            mMapView.refresh();
            mMapController.animateTo(new GeoPoint((int) (locData.latitude * 1e6),
                    (int) (locData.longitude * 1e6)), mHandler.obtainMessage(FusionCode.MAP_RELOC));
        }
        
        public void onReceivePoi(BDLocation poiLocation)
        {
            if (poiLocation == null) { return; }
        }
    }
    
    public class MySearchListener implements MKSearchListener
    {
        /**
         * 搜索后回调
         */
        private Handler searchHandler;
        
        /**
         * mContext
         */
        private Context mContext;
        
        public MySearchListener(Handler searchHandler, Context context)
        {
            super();
            this.searchHandler = searchHandler;
            this.mContext = context;
        }
        
        @Override
        public void onGetAddrResult(MKAddrInfo type, int error)
        {
            
        }
        
        @Override
        public void onGetBusDetailResult(MKBusLineResult arg0, int arg1)
        {
            // 错误号可参考MKEvent中的定义
            if (arg1 == MKEvent.ERROR_RESULT_NOT_FOUND)
            {
                PromptUtils.getInstance().displayToastString(MapAttractionActivity.this, true,
                        "搜索出错啦..");
                return;
            }
            else if (arg1 != 0 || arg0 == null)
            {
                PromptUtils.getInstance().displayToastString(MapAttractionActivity.this, true,
                        "抱歉，未找到结果");
                return;
            }
            
        }
        
        @Override
        public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1)
        {
            // 错误号可参考MKEvent中的定义
            if (arg1 == MKEvent.ERROR_RESULT_NOT_FOUND)
            {
                PromptUtils.getInstance().displayToastString(MapAttractionActivity.this, true,
                        "搜索出错啦..");
                return;
            }
            else if (arg1 != 0 || arg0 == null)
            {
                PromptUtils.getInstance().displayToastString(MapAttractionActivity.this, true,
                        "抱歉，未找到结果");
                return;
            }
            
        }
        
        @Override
        public void onGetPoiDetailSearchResult(int arg0, int arg1)
        {
            
        }
        
        @Override
        public void onGetPoiResult(MKPoiResult res, int type, int error)
        {
            if (error == MKEvent.ERROR_RESULT_NOT_FOUND)
            {
                PromptUtils.getInstance().displayToastString(MapAttractionActivity.this, true,
                        "搜索出错啦..");
                return;
            }
            else if (error != 0 || res == null)
            {
                PromptUtils.getInstance().displayToastString(MapAttractionActivity.this, true,
                        "抱歉，未找到结果");
                return;
            }
        }
        
        @Override
        public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1)
        {
            
        }
        
        @Override
        public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1)
        {
            // 错误号可参考MKEvent中的定义
            if (arg1 == MKEvent.ERROR_RESULT_NOT_FOUND)
            {
                PromptUtils.getInstance().displayToastString(MapAttractionActivity.this, true,
                        "未搜索到公交路线！");
                return;
            }
            else if (arg1 != 0 || arg0 == null)
            {
                PromptUtils.getInstance().displayToastString(MapAttractionActivity.this, true,
                        "抱歉，未找到结果");
                return;
            }
            Message message = new Message();
            message.obj = arg0;
            message.what = FusionCode.MAP_KEY_BUS_ROUTE;
            searchHandler.sendMessage(message);
            
        }
        
        @Override
        public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1)
        {
            // 错误号可参考MKEvent中的定义
            if (arg1 == MKEvent.ERROR_RESULT_NOT_FOUND)
            {
                PromptUtils.getInstance().displayToastString(MapAttractionActivity.this, true,
                        "搜索出错啦..");
                return;
            }
            else if (arg1 != 0 || arg0 == null)
            {
                PromptUtils.getInstance().displayToastString(MapAttractionActivity.this, true,
                        "抱歉，未找到结果");
                return;
            }
            
            Message message = new Message();
            message.obj = arg0;
            message.what = FusionCode.MAP_KEY_WALK_ROUTE;
            searchHandler.sendMessage(message);
        }
        
        @Override
        public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2)
        {
            // TODO Auto-generated method stub
            
        }
        
    }
    
    public Bitmap convertViewToBitmap(String str)
    {
        View view = LayoutInflater.from(this).inflate(R.layout.map_pop_view, null);
        TextView textView = (TextView) view.findViewById(R.id.name_text);
        textView.setText(str);
        textView.setTextSize(dip2px(MapAttractionActivity.this, 10));
        
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        
        return bitmap;
    }
    
    public Bitmap convertTextViewToBitmap(int gResId, String str)
    {
        
        TextView textView = new TextView(this);
        textView.setText(str);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(dip2px(MapAttractionActivity.this, 10));
        textView.setBackgroundResource(gResId);
        textView.setGravity(Gravity.CENTER);
        
        textView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
        textView.buildDrawingCache();
        Bitmap bitmap = textView.getDrawingCache();
        
        return bitmap;
    }
    
    private int dip2px(Context context, float dipValue)
    {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    
    class OverlayTest extends ItemizedOverlay<OverlayItem>
    {
        private Context mContext = null;
        
        PopupOverlay pop = null;
        
        int currentIndex = 0;
        
        public OverlayTest(Drawable marker, Context context, MapView mapView)
        {
            super(marker, mapView);
            this.mContext = context;
            pop = new PopupOverlay(mMapView, new PopupClickListener()
            {
                @Override
                public void onClickedPopup(int index)
                {
                    pop.hidePop();
                    showMyDialog(mGeoList.get(currentIndex), mAttractions.get(currentIndex));
                }
            });
            
        }
        
        protected boolean onTap(int index)
        {
            currentIndex = index;
            if (currentIndex > mGeoList.size() - 1)
            {
                System.out.println("onTap Error: " + index + " 出错了！越界！");
                return false;
            }
            // 这个是直接显示popwindow
            showMyDialog(mGeoList.get(currentIndex), mAttractions.get(currentIndex));
            return true;
        }
        
        public boolean onTap(GeoPoint pt, MapView mapView)
        {
            if (pop != null)
            {
                pop.hidePop();
            }
            super.onTap(pt, mapView);
            return false;
        }
        
    }
    
    private void showMyDialog(final OverlayItem overlayItem, final Attraction attraction)
    {
        if (!dialog.isShowing())
        {
            dialog.show();
        }
        final EditText start = dialog.getStartEdt();
        final EditText end = dialog.getEndEdt();
        Button searchBusBtn = dialog.getSearchBusBtn();
        Button searchwalkButton = dialog.getSearchWalkBtn();
        LinearLayout closeImg = dialog.getCloseImg();
        
        start.setKeyListener(null);
        end.setKeyListener(null);
        end.setText(overlayItem.getTitle());
        
        start.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // do nothing
            }
        });
        
        end.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // do nothing
            }
        });
        
        searchBusBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (TextUtils.isEmpty(end.getText().toString().trim()))
                {
                    PromptUtils.getInstance().displayToastString(MapAttractionActivity.this, true,
                            "请先选择终点！");
                    return;
                }
                
                MKPlanNode stNode = new MKPlanNode();
                MKPlanNode enNode = new MKPlanNode();
                stNode.pt = new GeoPoint((int) (FusionField.latitude * 1E6),
                        (int) (FusionField.longitude * 1E6));
                enNode.pt = overlayItem.getPoint();
                
                if (mMKSearch == null)
                {
                    mMKSearch = new MKSearch();
                    mMKSearch.init(LocateApplication.getInstance().mBMapManager,
                            new MySearchListener(mHandler, MapAttractionActivity.this));
                }
                mMKSearch.transitSearch(attraction.getCity(), stNode, enNode);
            }
        });
        
        searchwalkButton.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (TextUtils.isEmpty(end.getText().toString().trim()))
                {
                    PromptUtils.getInstance().displayToastString(MapAttractionActivity.this, true,
                            "请先选择终点！");
                    return;
                }
                
                MKPlanNode stNode = new MKPlanNode();
                MKPlanNode enNode = new MKPlanNode();
                stNode.pt = new GeoPoint((int) (FusionField.latitude * 1E6),
                        (int) (FusionField.longitude * 1E6));
                enNode.pt = overlayItem.getPoint();
                
                if (mMKSearch == null)
                {
                    mMKSearch = new MKSearch();
                    mMKSearch.init(LocateApplication.getInstance().mBMapManager,
                            new MySearchListener(mHandler, MapAttractionActivity.this));
                }
                mMKSearch.walkingSearch(attraction.getCity(), stNode, attraction.getCity(), enNode);
            }
        });
        
        closeImg.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        
    }
    
}
