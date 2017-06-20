

package com.example.yangj.en_0617;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import net.daum.mf.map.api.MapLayout;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import java.util.ArrayList;

public class MapViewActivity extends AppCompatActivity implements MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener {

    private static final int MENU_MAP_TYPE = Menu.FIRST + 1;
    private static final int MENU_MAP_MOVE = Menu.FIRST + 2;

    private static final String LOG_TAG = "MapViewActivity";

    private MapView mMapView;
    private ArrayList mMapPointList;


    //추가 시작
    ListView listView;
    EditText editText;
    Button sendButton;
    String userName;
    ArrayAdapter adapter;
    String email;
    List<EdataReadWrite> mComments;

    //private RecyclerView mRecyclerView; 데이터 받을 때 필요
   // private RecyclerView.Adapter mAdapter; 데이터 받을 때 필요
   // private RecyclerView.LayoutManager mLayoutManager; 데이터 받을 때 필요

    //String[] myDataset={"우리는","지금","전기프","열코딩중"};
    FirebaseDatabase database;
    //추가 끝
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        //추가 시작
        database = FirebaseDatabase.getInstance();//instance를 받아야돼
        //데이터 송수신

        user = FirebaseAuth.getInstance().getCurrentUser();//현재 user정보를 가지고 옴
        if (user != null) {
            // Name, email address, and profile photo Url

            email = user.getEmail();//사용자 이메일을 받아오는 함수

        }

       // mLayoutManager = new LinearLayoutManager(this);
       // mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mComments=new ArrayList<>();
       // mAdapter = new MyAdapter(mComments);
     //   mRecyclerView.setAdapter(mAdapter);


        //final String readMe=email;
        DatabaseReference myRef = database.getReference(user.getUid());//읽어올 트리 헤드 이름(값이 미리 존재해야 읽을 수 있음)



        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                EdataReadWrite comment = dataSnapshot.getValue(EdataReadWrite.class);


                // [START_EXCLUDE]
                // Update RecyclerView
                //mCommentIds.add(dataSnapshot.getKey());
             //   mComments.add(comment);//list를 만들어줌
              //  mAdapter.notifyItemInserted(mComments.size() - 1);//아이템이 들어가면 반영한다
                // [END_EXCLUDE]

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //추가 끝


        MapLayout mapLayout = new MapLayout(this);
        mMapView = mapLayout.getMapView();


        mMapView.setDaumMapApiKey(MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY);
        mMapView.setOpenAPIKeyAuthenticationResultListener(this);
        mMapView.setMapViewEventListener(this);
        mMapView.setMapType(MapView.MapType.Standard);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapLayout);

        /* MapPoint 저장 배열 */
        mMapPointList = new ArrayList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_MAP_TYPE, Menu.NONE, "MapType");
        menu.add(0, MENU_MAP_MOVE, Menu.NONE, "Move");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int itemId = item.getItemId();

        switch (itemId) {
            case MENU_MAP_TYPE: {

                //String hdMapTile = mMapView.isHDMapTileEnabled()? "HD Map Tile Off" : "HD Map Tile On";

                String hdMapTile;

                if (mMapView.getMapTileMode() == MapView.MapTileMode.HD2X) {
                    hdMapTile = "Set to Standard Mode";
                } else if (mMapView.getMapTileMode() == MapView.MapTileMode.HD) {
                    hdMapTile = "Set to HD 2X Mode";
                } else {
                    hdMapTile = "Set to HD Mode";
                }

                String[] mapTypeMenuItems = { "Standard", "Satellite", "Hybrid", hdMapTile, "Clear Map Tile Cache"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("MapType");
                dialog.setItems(mapTypeMenuItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        controlMapTile(which);
                    }
                });
                dialog.show();


                return true;
            }

            case MENU_MAP_MOVE: {
                String rotateMapMenu = mMapView.getMapRotationAngle() == 0.0f? "Rotate Map 60" : "Unrotate Map";
                String[] mapMoveMenuItems = { "Move to", "Zoom to", "Move and Zoom to", "Zoom In", "Zoom Out", rotateMapMenu};

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Move");
                dialog.setItems(mapMoveMenuItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        controlMapMove(which);
                    }

                });
                dialog.show();

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void controlMapMove(int which) {
        switch (which) {
            case 0: // Move to
            {
                mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);
            }
            break;
            case 1: // Zoom to
            {
                mMapView.setZoomLevel(7, true);
            }
            break;
            case 2: // Move and Zoom to
            {
                mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(33.41, 126.52), 9, true);
            }
            break;
            case 3: // Zoom In
            {
                mMapView.zoomIn(true);
            }
            break;
            case 4: // Zoom Out
            {
                mMapView.zoomOut(true);
            }
            break;
            case 5: // Rotate Map 60, Unrotate Map
            {
                if (mMapView.getMapRotationAngle() == 0.0f) {
                    mMapView.setMapRotationAngle(60.0f, true);
                } else {
                    mMapView.setMapRotationAngle(0.0f, true);
                }
            }
            break;
        }
    }

    private void controlMapTile(int which) {
        switch (which) {
            case 0: // Standard
            {
                mMapView.setMapType(MapView.MapType.Standard);
            }
            break;
            case 1: // Satellite
            {
                mMapView.setMapType(MapView.MapType.Satellite);
            }
            break;
            case 2: // Hybrid
            {
                mMapView.setMapType(MapView.MapType.Hybrid);
            }
            break;
            case 3: // HD Map Tile On/Off
            {
                if (mMapView.getMapTileMode() == MapView.MapTileMode.HD2X) {
                    //Set to Standard Mode
                    mMapView.setMapTileMode(MapView.MapTileMode.Standard);
                } else if (mMapView.getMapTileMode() == MapView.MapTileMode.HD) {
                    //Set to HD 2X Mode
                    mMapView.setMapTileMode(MapView.MapTileMode.HD2X);
                } else {
                    //Set to HD Mode
                    mMapView.setMapTileMode(MapView.MapTileMode.HD);
                }
            }
            break;
            case 4: // Clear Map Tile Cache
            {
                MapView.clearMapTilePersistentCache();
            }
            break;
        }
    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int resultCode, String resultMessage) {
        Log.i(LOG_TAG,	String.format("Open API Key Authentication Result : code=%d, message=%s", resultCode, resultMessage));
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        Log.i(LOG_TAG, "MapView had loaded. Now, MapView APIs could be called safely");
        //mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(37.537229,127.005515), 2, true);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapCenterPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapCenterPoint.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onMapViewCenterPointMoved (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("DaumMapLibrarySample");
        alertDialog.setMessage(String.format("Double-Tap on (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, final MapPoint mapPoint) {

        final MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("좌표 추가");
        alertDialog.setMessage(String.format("Long-Press on (%f,%f) 추가하시겠습니까?", mapPointGeo.latitude, mapPointGeo.longitude));
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //등록 버튼을 누를 경우

                MapPolyline existingPolyline = mMapView.findPolylineByTag(2000);
                if (existingPolyline != null) {
                    mMapView.removePolyline(existingPolyline);
                }

                mMapPointList.add(MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude));
                String str= ""+mapPointGeo.latitude;
                String str2= ""+mapPointGeo.longitude;

                //추가 시작
                //데이터가 입력되었을 때, 데이터베이스에 전송

                // FirebaseDatabase database = FirebaseDatabase.getInstance();//instance를 받아야돼

                // DatabaseReference myRef = database.getReference("dot array");//트리 헤드 이름

                //헤드의 child를 데이터를 수신한 시간으로 설정함
                //head dotarray에 날짜별로 데이터가 올라가짐
                Calendar c =Calendar.getInstance();
                SimpleDateFormat df=new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                String formattedDate=df.format((c.getTime()));

                DatabaseReference myRef = database.getReference(user.getUid()).child(formattedDate);//트리 헤드 이름



                //해쉬테이블 이용합니당
                Hashtable<String, String> dataDot
                        = new Hashtable<String, String>();
                dataDot.put("latitude", str);//key, value(내용)//key값이름 바꾸려면 저기도 반드시 같이 수정해줘야됨//경도 저장되는지 확인
                dataDot.put("longitude", str2);//일단 위도값먼저 저장되는지 확인

               // dataDot.put("구분", "보호자");//데이터베이스 저장소에는 보호자만 되어있음
                //사용자는 저장된 데이터만 읽어서 가져옴


                myRef.setValue(dataDot);
                //Toast.makeText(EChatActivity.this,email+","+stText,Toast.LENGTH_SHORT).show();

            //추가 끝


                Toast.makeText(MapViewActivity.this,str+","+str2,Toast.LENGTH_SHORT).show();//확인용

                MapPOIItem poiItemPick = new MapPOIItem();
                poiItemPick.setItemName("Pick");
                poiItemPick.setTag(10001);
                poiItemPick.setMapPoint(MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude));
                poiItemPick.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                poiItemPick.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
                poiItemPick.setShowCalloutBalloonOnTouch(false);
                poiItemPick.setCustomImageResourceId(R.drawable.location_map_pin_pink);
                mMapView.addPOIItem(poiItemPick);

                MapPolyline polyline = new MapPolyline(mMapPointList.size());
                polyline.setTag(2000);
                polyline.setLineColor(Color.argb(128, 255, 93, 117));
                for (int i=0;i<mMapPointList.size();i++){
                    polyline.addPoint((MapPoint) mMapPointList.get(i));
                }
                mMapView.addPolyline(polyline);
            }
        });


        alertDialog.setNegativeButton("CANCLE", null);
        alertDialog.show();
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onMapViewSingleTapped (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onMapViewDragStarted (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onMapViewDragEnded (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onMapViewMoveFinished (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int zoomLevel) {
        Log.i(LOG_TAG, String.format("MapView onMapViewZoomLevelChanged (%d)", zoomLevel));
    }

}
