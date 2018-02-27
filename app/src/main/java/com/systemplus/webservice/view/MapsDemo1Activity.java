package com.systemplus.webservice.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.systemplus.webservice.R;
import com.systemplus.webservice.api.ApiClient;
import com.systemplus.webservice.api.ApiInterface;
import com.systemplus.webservice.model.PolyLineResponse;
import com.systemplus.webservice.model.direction.ResponseDirection;
import com.systemplus.webservice.util.AppConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsDemo1Activity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "PlaceDemoMapsActivity";
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng MyLocation,center;
    Button btn_search,btnDirection;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private Location mcurrentLocation;
    private static final String API_KEY="AIzaSyCEFc3SSlylNmio41sIy8Ihlw-gEUiEIgc";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_demo1);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btn_search=findViewById(R.id.btnSearch);
        btnDirection=findViewById(R.id.btnDirection);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    PlaceAutocomplete.IntentBuilder placeAutocompleteBuilder = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY);
                    if (mcurrentLocation != null) {
                        center = new LatLng(mcurrentLocation.getLatitude(), mcurrentLocation.getLongitude());
                        LatLngBounds latLngBounds = toBounds(center, 10000);
                        placeAutocompleteBuilder.setBoundsBias(latLngBounds);
                    }
                    mMap.clear();
                    Intent intent =
                            placeAutocompleteBuilder
                                    .build(MapsDemo1Activity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }

            }
        });
        btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);

                String url = AppConstants.URL_GOOGLE_DIRECTION_API + MyLocation.latitude + "," + MyLocation.longitude + "&destination=" + center.latitude + "," + center.longitude + "&mode=car&alternatives=true&key=" + AppConstants.GOOGLE_DIRECTION_API_KEY;
                String url1=AppConstants.URL_GOOGLE_DISTANCE_API + MyLocation.toString() +"&destinations="+ center.toString()+"&key="+AppConstants.GOOGLE_DISTANCE_API_KEY;
                Call<PolyLineResponse> polyLineResponseCall = apiService.getPolyLine(url);
                final Call<ResponseDirection> distanceCall=apiService.getDistance(url1);
                distanceCall.enqueue(new Callback<ResponseDirection>() {
                    @Override
                    public void onResponse(Call<ResponseDirection> call, Response<ResponseDirection> response) {
                        String responceq=response.body().getRows().get(0).getElements().get(0).getDistance().getText();
                        Toast.makeText(MapsDemo1Activity.this,responceq,Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onFailure(Call<ResponseDirection> call, Throwable t) {

                    }
                });

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
        });
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

        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(MapsDemo1Activity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Add a marker in Sydney and move the camera
                                    mMap.clear();
                                    MyLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(MyLocation).title("Marker at my location"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(MyLocation));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);
                                }
                                else
                                    Toast.makeText(MapsDemo1Activity.this, "Location not found", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                showSettingDialog();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                Toast.makeText(MapsDemo1Activity.this, "Permission not set",Toast.LENGTH_LONG);
            }
        }).check();
        // Add a marker in Sydney and move the camera



    }
    private void showSettingDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MapsDemo1Activity.this);
        builder.setTitle("Need a permissions");
        builder.setMessage("This appNeed a Access for location to make a operations");
        builder.setPositiveButton("Goto Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                openSetting();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });
        builder.show();
    }

    private void openSetting() {
        Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri=Uri.fromParts("package",getPackageName(),null);
        intent.setData(uri);
        startActivityForResult(intent,101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());

                LatLng myLocation = place.getLatLng();
                mMap.addMarker(new MarkerOptions().position(myLocation).title(place.getAddress().toString())).showInfoWindow();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    public static LatLngBounds toBounds(LatLng center, double radiusInMeters) {
        double distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }
}
