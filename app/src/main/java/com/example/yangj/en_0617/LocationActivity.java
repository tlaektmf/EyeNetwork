package com.example.yangj.en_0617;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

import static java.lang.Math.sqrt;

public class LocationActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {

    private static final String LOG_TAG = "LocationActivity";
    private final int MENU_LOCATION = Menu.FIRST;
    private final int MENU_REVERSE_GEO = Menu.FIRST + 1;

    private MapView mMapView;
    private MapReverseGeoCoder mReverseGeoCoder = null;
    private boolean isUsingCustomLocationMarker = false;

    /* ProximityAlert */
    private LocationManager mLocationManager;
    private CoffeeIntentReceiver mIntentReceiver;

    ArrayList mPendingIntentList;
    private ArrayList mMapPointList;

    String intentKey = "coffeeProximity";

    double get_latitude = 0;
    double get_longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.setDaumMapApiKey(MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY);
        mMapView.setCurrentLocationEventListener(this);

        /* ProximityAlert */
        Log.d("slog", "onCreate()");

        // 위치 관리자 객체 참조
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mPendingIntentList = new ArrayList();

        /* MapPoint 저장 배열 */
        mMapPointList = new ArrayList();

        mMapPointList.add(MapPoint.mapPointWithGeoCoord(37.543682, 127.077555));
        mMapPointList.add(MapPoint.mapPointWithGeoCoord(37.543736, 127.076801));
        mMapPointList.add(MapPoint.mapPointWithGeoCoord(37.545369,127.076477));
        mMapPointList.add(MapPoint.mapPointWithGeoCoord(37.545035,127.075318));
        mMapPointList.add(MapPoint.mapPointWithGeoCoord(37.545422,127.074127));
        mMapPointList.add(MapPoint.mapPointWithGeoCoord(37.546215,127.074337));

        for (int i = 0; i < mMapPointList.size(); i++) {
            MapPoint temp = (MapPoint) mMapPointList.get(i);
            register(i, temp.getMapPointGeoCoord().latitude, temp.getMapPointGeoCoord().longitude, 10, -1);
        }

        addCircles();

        mIntentReceiver = new CoffeeIntentReceiver(intentKey);
        registerReceiver(mIntentReceiver, mIntentReceiver.getFilter());

        Toast.makeText(getApplicationContext(), mMapPointList.size() + "개 지점에 대한 근접 리스너 등록", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mMapView.setShowCurrentLocationMarker(false);
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_LOCATION, Menu.NONE, "Location");
        menu.add(0, MENU_REVERSE_GEO, Menu.NONE, "Reverse Geo-Coding");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int itemId = item.getItemId();

        switch (itemId) {
            case MENU_LOCATION: {
                String[] mapMoveMenuItems = {
                        "User Location On",
                        "User Location On, MapMoving Off",
                        "User Location+Heading On",
                        "User Location+Heading On, MapMoving Off",
                        "Off",
                        (isUsingCustomLocationMarker ? "Default" : "Custom") + " Location Marker",
                        "Show Location Marker",
                        "Hide Location Marker"
                };
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Location");
                dialog.setItems(mapMoveMenuItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // User Location On
                            {
                                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                            }
                            break;
                            case 1: // User Location On, MapMoving Off
                            {
                                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
                            }
                            break;
                            case 2: // User Location+Heading On
                            {
                                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                            }
                            break;
                            case 3: // User Location+Heading On, MapMoving Off
                            {
                                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeadingWithoutMapMoving);
                            }
                            break;
                            case 4: // Off
                            {
                                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                                mMapView.setShowCurrentLocationMarker(false);
                            }
                            break;
                            case 5: // Default/Custom Location Marker
                            {
                                if (isUsingCustomLocationMarker) {
                                    mMapView.setCurrentLocationRadius(0);
                                    mMapView.setDefaultCurrentLocationMarker();
                                } else {
                                    mMapView.setCurrentLocationRadius(100); // meter
                                    mMapView.setCurrentLocationRadiusFillColor(Color.argb(77, 255, 255, 0));
                                    mMapView.setCurrentLocationRadiusStrokeColor(Color.argb(77, 255, 165, 0));

                                    MapPOIItem.ImageOffset trackingImageAnchorPointOffset = new MapPOIItem.ImageOffset(16, 16); // 좌하단(0,0) 기준 앵커포인트 오프셋
                                    MapPOIItem.ImageOffset directionImageAnchorPointOffset = new MapPOIItem.ImageOffset(65, 65);
                                    MapPOIItem.ImageOffset offImageAnchorPointOffset = new MapPOIItem.ImageOffset(15, 15);
                                    mMapView.setCustomCurrentLocationMarkerTrackingImage(R.drawable.custom_map_present_tracking, trackingImageAnchorPointOffset);
                                    mMapView.setCustomCurrentLocationMarkerDirectionImage(R.drawable.custom_map_present_direction, directionImageAnchorPointOffset);
                                    mMapView.setCustomCurrentLocationMarkerImage(R.drawable.custom_map_present, offImageAnchorPointOffset);
                                }
                                isUsingCustomLocationMarker = !isUsingCustomLocationMarker;
                            }
                            break;
                            case 6: // Show Location Marker
                            {
                                mMapView.setShowCurrentLocationMarker(true);
                            }
                            break;
                            case 7: // Hide Location Marker
                            {
                                if (mMapView.isShowingCurrentLocationMarker()) {
                                    mMapView.setShowCurrentLocationMarker(false);
                                }
                            }
                            break;
                        }
                    }

                });
                dialog.show();
                return true;
            }

            case MENU_REVERSE_GEO: {
                mReverseGeoCoder = new MapReverseGeoCoder(MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY, mMapView.getMapCenterPoint(), LocationActivity.this, LocationActivity.this);
                mReverseGeoCoder.startFindingAddress();

                return true;
            }
        }

        return onOptionsItemSelected(item);

    }

    private void addCircles() {
        for (int i = 0; i < mMapPointList.size(); i++) {
            MapPoint point = (MapPoint) mMapPointList.get(i);
            MapCircle temp = new MapCircle(MapPoint.mapPointWithGeoCoord(point.getMapPointGeoCoord().latitude, point.getMapPointGeoCoord().longitude),
                    10,
                    Color.argb(128, 255, 0, 0),
                    Color.argb(128, 0, 255, 0));
            temp.setTag(i);
            mMapView.addCircle(temp);
        }
    }


    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        mapReverseGeoCoder.toString();
        onFinishReverseGeoCoding(s);
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {

    }

    private void onFinishReverseGeoCoding(String result) {
        Toast.makeText(LocationActivity.this, "현재 위치(추가!) : " + result, Toast.LENGTH_SHORT).show();
    }

    /* ProximityAlert */
    private void register(int id, double latitude, double longitude, float radius, long expiration) {

        Intent proximityIntent = new Intent(intentKey);
        proximityIntent.putExtra("id", id);
        proximityIntent.putExtra("latitude", latitude);
        proximityIntent.putExtra("longitude", longitude);
        PendingIntent intent = PendingIntent.getBroadcast(this, id, proximityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.addProximityAlert(latitude, longitude, radius, expiration, intent);
        //parameter값들을 이용해서 넘김(등록)
        //intent는 팬딩인텐트, 인텐트가 시스템에 넘겨졌을 때 바로 실행되는게 아니라
        //어떤 조건이 만족했을 떄 인텐트를 넘겨

        mPendingIntentList.add(intent);
    }

    public void onStop() {
        super.onStop();

        unregister();
    }

    private void unregister() {
        if (mPendingIntentList != null) {
            for (int i = 0; i < mPendingIntentList.size(); i++) {
                PendingIntent curIntent = (PendingIntent) mPendingIntentList.get(i);
                mLocationManager.removeProximityAlert(curIntent);
                mPendingIntentList.remove(i);
            }
        }

        if (mIntentReceiver != null) {
            unregisterReceiver(mIntentReceiver);
            mIntentReceiver = null;
        }
    }

    private class CoffeeIntentReceiver extends BroadcastReceiver {

        private String mExpectedAction;
        private Intent mLastReceivedIntent;

        public CoffeeIntentReceiver(String expectedAction) {
            mExpectedAction = expectedAction;
            mLastReceivedIntent = null;
        }

        public IntentFilter getFilter() {
            IntentFilter filter = new IntentFilter(mExpectedAction);
            return filter;
        }

        /**
         * 받았을 때 호출되는 메소드
         *
         * @param context
         * @param intent
         */
        public void onReceive(Context context, Intent intent) {
            int id = intent.getIntExtra("id", 0);

            if (intent != null) {
                mLastReceivedIntent = intent;

                double latitude = intent.getDoubleExtra("latitude", 0.0D);
                double longitude = intent.getDoubleExtra("longitude", 0.0D);


                Toast.makeText(context, "근접한 커피숍 : " + id + ", " + latitude + ", " + longitude, 200).show();
            }


            boolean isEntering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);

            if(isEntering){
                Toast.makeText(context, "목표 지점에 접근중..", Toast.LENGTH_LONG).show();
                guide(id);
            }

            else

                Toast.makeText(context, "목표 지점에서 벗어납니다.", Toast.LENGTH_LONG).show();

        }

        public Intent getLastReceivedIntent() {
            return mLastReceivedIntent;
        }

        public void clearReceivedIntents() {
            mLastReceivedIntent = null;
        }
    }

    public void guide(int id){
        if(id!=0 && id!=mMapPointList.size() - 1){
            MapPoint temp1 = (MapPoint) mMapPointList.get(id - 1);
            double x1 = temp1.getMapPointGeoCoord().latitude;
            double y1 = temp1.getMapPointGeoCoord().longitude;

            MapPoint temp2 = (MapPoint) mMapPointList.get(id);
            double x2 = temp2.getMapPointGeoCoord().latitude;
            double y2 = temp2.getMapPointGeoCoord().longitude;

            MapPoint temp3 = (MapPoint) mMapPointList.get(id + 1);
            double x3 = temp3.getMapPointGeoCoord().latitude;
            double y3 = temp3.getMapPointGeoCoord().longitude;

            double ax = x1 - x2;
            double ay = y1 - y2;

            double bx = x2 - x3;
            double by = y2 - y3;

            double k = sqrt(ax*ax + ay*ay)*sqrt(bx*bx + by*by);
            double m = ax*bx + ay*by;

            double d1 = sqrt( (x2-x1)*(x2-x1)+(y2-y1)*(y2-y1) );
            double d2 = sqrt( (x3-x2)*(x3-x2)+(y3-y2)*(y3-y2) );

            double cos = m/k;
            double sin = sqrt(1-cos*cos);

            double x4 = ( x2*(d1+d2)-d2*x1 )/d1;
            double y4 = ( y2*(d1+d2)-d2*y1 )/d1;

            double new_x = cos*(x4-x2)+sin*(y4-y2)+x2;
            double new_y = -sin*(x4-x2)+cos*(y4-y2)+y2;

            double new_x2 = cos*(x4-x2)+-sin*(y4-y2)+x2;
            double new_y2 = sin*(x4-x2)+cos*(y4-y2)+y2;

            double vecAx = (x2-new_x);
            double vecAy = (y2-new_y);

            double realx=(x2-x3);
            double realy=(y2-y3);

            double vecBx=(x2-new_x2);
            double vecBy=(y2-new_y2);

            double k1 = sqrt((vecAx)*vecAx + (vecAy)*vecAy)*sqrt((realx)*realx + (realy)*realy);
            double m1 = vecAx*realx + vecAy*realy;
            double cos1 = m1/k1;
            double sin1 = sqrt(1-cos1*cos1);

            double k2 = sqrt((vecBx)*vecBx + (vecBy)*vecBy)*sqrt((realx)*realx + (realy)*realy);
            double m2 = vecBx*realx + vecBy*realy;
            double cos2 = m2/k2;
            double sin2=sqrt(1-cos2*cos2);

            int time = 0;

            if(cos1<cos2){
                if(Math.cos(Math.toDegrees(0.0)) <= cos && cos < Math.cos(Math.toDegrees(15.0))){
                    //12시 방향
                    time = 12;
                }
                else if(Math.cos(Math.toDegrees(15.0)) <= cos && cos < Math.cos(Math.toDegrees(45.0))){
                    //1시 방향
                    time = 1;
                }
                else if(Math.cos(Math.toDegrees(45.0)) <= cos && cos < Math.cos(Math.toDegrees(75.0))){
                    //2시 방향
                    time = 2;
                }
                else if(Math.cos(Math.toDegrees(75.0)) <= cos && cos < Math.cos(Math.toDegrees(105.0))){
                    //3시 방향
                    time = 3;
                }
                else if(Math.cos(Math.toDegrees(105.0)) <= cos && cos < Math.cos(Math.toDegrees(135.0))){
                    //4시 방향
                    time = 4;
                }
                else if(Math.cos(Math.toDegrees(135.0)) <= cos && cos < Math.cos(Math.toDegrees(165.0))){
                    //5시 방향
                    time = 5;
                }
                else if(Math.cos(Math.toDegrees(165.0)) <= cos && cos < Math.cos(Math.toDegrees(180.0))){
                    //6시 방향
                    time = 6;
                }
            }
            else{
                if(Math.cos(Math.toDegrees(180.0)) <= cos && cos < Math.cos(Math.toDegrees(195.0))){
                    //6시 방향
                    time = 6;
                }
                else if(Math.cos(Math.toDegrees(195.0)) <= cos && cos < Math.cos(Math.toDegrees(225.0))){
                    //7시 방향
                    time = 7;
                }
                else if(Math.cos(Math.toDegrees(225.0)) <= cos && cos < Math.cos(Math.toDegrees(255.0))){
                    //8시 방향
                    time = 8;
                }
                else if(Math.cos(Math.toDegrees(255.0)) <= cos && cos < Math.cos(Math.toDegrees(285.0))){
                    //9시 방향
                    time = 9;
                }
                else if(Math.cos(Math.toDegrees(285.0)) <= cos && cos < Math.cos(Math.toDegrees(315.0))){
                    //10시 방향
                    time = 10;
                }
                else if(Math.cos(Math.toDegrees(315.0)) <= cos && cos < Math.cos(Math.toDegrees(345.0))){
                    //11시 방향
                    time = 11;
                }
                else if(Math.cos(Math.toDegrees(345.0)) <= cos && cos < Math.cos(Math.toDegrees(360.0))) {
                    //12 방향
                    time = 12;
                }
            }

            Toast.makeText(getApplicationContext(), time + "시 방향으로 이동", Toast.LENGTH_LONG).show();
        }
        else if(id == 0){
            Toast.makeText(getApplicationContext(), "출발", Toast.LENGTH_LONG).show();
        }
        else if(id == mMapPointList.size() - 1){
            Toast.makeText(getApplicationContext(), "도착", Toast.LENGTH_LONG).show();
        }
    }
}
