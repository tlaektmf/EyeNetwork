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

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

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

        int countTargets = 2;
        register(1001, 37.643879, 127.065918, 30, -1);//목표지점(가상으로 찍어줌)

        mIntentReceiver = new CoffeeIntentReceiver(intentKey);
        registerReceiver(mIntentReceiver, mIntentReceiver.getFilter());

        Toast.makeText(getApplicationContext(), countTargets + "개 지점에 대한 근접 리스너 등록", Toast.LENGTH_LONG).show();
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
            if (intent != null) {
                mLastReceivedIntent = intent;

                int id = intent.getIntExtra("id", 0);
                double latitude = intent.getDoubleExtra("latitude", 0.0D);
                double longitude = intent.getDoubleExtra("longitude", 0.0D);


                Toast.makeText(context, "근접한 커피숍 : " + id + ", " + latitude + ", " + longitude, 200).show();
            }


            boolean isEntering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);

            if(isEntering)

                Toast.makeText(context, "목표 지점에 접근중..", Toast.LENGTH_LONG).show();

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
}
