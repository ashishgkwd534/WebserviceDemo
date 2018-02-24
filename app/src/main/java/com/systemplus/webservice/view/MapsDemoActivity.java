package com.systemplus.webservice.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.PolyUtil;
import com.systemplus.webservice.R;
import com.systemplus.webservice.api.ApiClient;
import com.systemplus.webservice.api.ApiInterface;
import com.systemplus.webservice.model.PolyLineResponse;
import com.systemplus.webservice.util.AppConstants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsDemoActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //private GoogleMap mMap;


    private LatLng DRIVER_LOCATION;

    private static final LatLng MID_LOCATION = new LatLng(18.544752, 73.905965);

    private static final LatLng CURBEE_LOCATION = new LatLng(18.543440, 73.905482);

    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_demo);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(MapsDemoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsDemoActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(MapsDemoActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Add a marker in Sydney and move the camera
                            DRIVER_LOCATION = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(DRIVER_LOCATION).title("Marker in Pune"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(DRIVER_LOCATION));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);
                        }
                    }
                });

        // Add a marker in Sydney and move the camera




        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {


                CircleOptions circleOptions = new CircleOptions();
                circleOptions.center(new LatLng(18.545052, 73.906048));
                circleOptions.radius(100);
                circleOptions.strokeColor(Color.BLUE);
                circleOptions.strokeWidth(5);
                circleOptions.fillColor(Color.RED);

                PolygonOptions polygonOptions = new PolygonOptions();
                polygonOptions.add(new LatLng(18.545975, 73.904735));
                polygonOptions.add(new LatLng(18.546987, 73.904891));
                polygonOptions.add(new LatLng(18.546130, 73.905301));
                polygonOptions.add(new LatLng(18.546514, 73.905293));
                polygonOptions.strokeColor(Color.BLUE);
                polygonOptions.strokeWidth(5);
                polygonOptions.add(new LatLng(18.545975, 73.904735));
                polygonOptions.fillColor(Color.RED);

                mMap.addPolygon(polygonOptions);


                mMap.addCircle(circleOptions);

                double dist1= CalculationByDistance(DRIVER_LOCATION,CURBEE_LOCATION);

                mMap.addMarker(new MarkerOptions().position(DRIVER_LOCATION).snippet(""));

                mMap.addMarker(new MarkerOptions().position(CURBEE_LOCATION).title("Distanace="+dist1));

                PolylineOptions polylineOptions = new PolylineOptions().width(10).color(Color.CYAN);
                List<LatLng> points = new ArrayList<>();
                points.add(CURBEE_LOCATION);
                points.add(MID_LOCATION);
                points.add(DRIVER_LOCATION);

                polylineOptions.addAll(points);

                Polyline polyline = mMap.addPolyline(polylineOptions);

                polyline.setEndCap(new RoundCap());
                polyline.setStartCap(new RoundCap());
                polyline.setPattern(PATTERN_POLYGON_ALPHA);

                callPolyLine();
            }
        });

    }

    private void callPolyLine() {
        final ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String url = AppConstants.URL_GOOGLE_DIRECTION_API + DRIVER_LOCATION.latitude + "," + DRIVER_LOCATION.longitude + "&destination=" + CURBEE_LOCATION.latitude + "," + CURBEE_LOCATION.longitude + "&mode=car&alternatives=true&key=" + AppConstants.GOOGLE_DIRECTION_API_KEY;
        Call<PolyLineResponse> polyLineResponseCall = apiService.getPolyLine(url);

        polyLineResponseCall.enqueue(new Callback<PolyLineResponse>() {
            @Override
            public void onResponse(Call<PolyLineResponse> call, Response<PolyLineResponse> response) {
                PolyLineResponse polyLineResponse = response.body();

                String polyLineString = polyLineResponse.getRoutes().get(0).getOverviewPolyline().getPoints();

                List<LatLng> latLngs = PolyUtil.decode(polyLineString);

                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.addAll(latLngs);

                mMap.addPolyline(polylineOptions);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (LatLng latLng : latLngs) {
                    builder.include(latLng);
                }
                LatLngBounds bounds = builder.build();

                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);

                mMap.animateCamera(cu);
            }

            @Override
            public void onFailure(Call<PolyLineResponse> call, Throwable t) {

            }
        });



    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }
}