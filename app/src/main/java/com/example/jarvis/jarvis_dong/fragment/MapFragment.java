package com.example.jarvis.jarvis_dong.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.jarvis.jarvis_dong.R;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.mapapi.BMapManager.init;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements View.OnClickListener{
    private static final int BAIDU_READ_PHONE_STATE =100;

    private MapView mMapView=null;
    private BaiduMap mbaidumap=null;
    private PoiSearch poiSearch=null;
    private Button poibtn=null;
    private Button posbtn=null;
    private Button satellite=null;
    private Button heatbtn=null;
    private Button trafficbtn=null;
    private EditText city;
    private EditText key;
    private List<LatLng> pts=new ArrayList<LatLng>();   //用来画线的

    private LocationClient mlocationclient=null;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view =inflater.inflate(R.layout.fragment_map, container, false);
        mMapView=(MapView)view.findViewById(R.id.bmapView);
        mbaidumap=mMapView.getMap();
        mbaidumap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        BDLocationListener listener=new MyLocationListener();
        mlocationclient=new LocationClient(getActivity().getApplicationContext());//声明locationclient类
        mlocationclient.registerLocationListener(listener);//注册位置监听器

        if (Build.VERSION.SDK_INT>=23){
            showContacts();
        }else{
            init();//init为定位方法
        }

        poiSearch=PoiSearch.newInstance();
        OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                List<PoiInfo>poiInfos=poiResult.getAllPoi();
                for (PoiInfo p:poiInfos)
                {
                    System.out.println(p.address+"---"+p.city+"---"+p.name+"---"+p.location);
                    BitmapDescriptor bd= BitmapDescriptorFactory.fromResource(R.mipmap.location);
                    OverlayOptions options=new MarkerOptions().position(p.location).icon(bd);
                    mbaidumap.addOverlay(options);
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        };
        poiSearch.setOnGetPoiSearchResultListener(poiListener);

        city=(EditText)view.findViewById(R.id.city);
        key=(EditText)view.findViewById(R.id.key);
        poibtn=(Button)view.findViewById(R.id.poibtn);
        posbtn=(Button)view.findViewById(R.id.posbtn);
        satellite=(Button)view.findViewById(R.id.satellite);
        heatbtn=(Button)view.findViewById(R.id.heatbtn);
        trafficbtn=(Button)view.findViewById(R.id.trafficbtn);

        poibtn.setOnClickListener(this);
        posbtn.setOnClickListener(this);
        satellite.setOnClickListener(this);
        heatbtn.setOnClickListener(this);
        trafficbtn.setOnClickListener(this);
        return view;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            // //请求我的位置信息
            case R.id.posbtn: initLocation();
                mlocationclient.start();//开启定位
                mlocationclient.requestLocation();//发送请求
                break;
            //发起一个检索请求，这是一个异步的过程
            case R.id.poibtn:  if(city.getText().toString().equals("")||key.getText().equals(""))
            {
                Toast.makeText(getActivity().getApplicationContext(),"你的城市或者关键词为空",Toast.LENGTH_SHORT).show();break;
            }
            else {
                poiSearch.searchInCity((new PoiCitySearchOption())
                        .city(city.getText().toString())
                        .keyword(key.getText().toString())
                        .pageNum(10));
                break;
            }
            case R.id.satellite:if(mbaidumap.getMapType()==BaiduMap.MAP_TYPE_NORMAL) {
                mbaidumap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            }
            default:mbaidumap.setMapType(BaiduMap.MAP_TYPE_NORMAL);break;
            case R.id.heatbtn:if (mbaidumap.isBaiduHeatMapEnabled()){
                mbaidumap.setBaiduHeatMapEnabled(false);
                break;
            }
            else {
                mbaidumap.setBaiduHeatMapEnabled(true);
                break;
            }
            case R.id.trafficbtn:if (mbaidumap.isTrafficEnabled()) {
                mbaidumap.setTrafficEnabled(false);
                break;
            }
            else {
                mbaidumap.setTrafficEnabled(true);
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        poiSearch.destroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    //位置监听器，BDLocationListener为结果监听接口，异步获取定位结果，实现方式如下：
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {  //接收位置信息的回调方法

            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型

            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息

            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度

            if (location.getLocType() == BDLocation.TypeGpsLocation){

                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息

            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }

            //  Log.i("BaiduLocationApiDem", sb.toString());
            System.out.println("baidulocaition"+sb.toString());

            BitmapDescriptor bitmapDescriptor=BitmapDescriptorFactory.fromResource(R.mipmap.location);
            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
            OverlayOptions options=new MarkerOptions().position(latLng).icon(bitmapDescriptor);
            mbaidumap.addOverlay(options);

            pts.add(latLng);
            OverlayOptions line=new PolylineOptions().color(R.color.colorPrimary).points(pts);
            mbaidumap.addOverlay(line);

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    //请求我的位置信息，我通过选择按键触发
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        //返回坐标类型包括：
        //1. gcj02：国测局坐标；
        // 2. bd09：百度墨卡托坐标；
        //3. bd09ll：百度经纬度坐标；

        int span=1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setNeedDeviceDirect(true);
        //返回的位置结果包含手机机头的方向

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mlocationclient.setLocOption(option);
    }


    //蛋疼，做到一半手机坏了，换了我用的真机调试小米是android 6.0   要动态获取权限
    public void showContacts(){
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity().getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        }else{
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    init();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getActivity().getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

    }


}
