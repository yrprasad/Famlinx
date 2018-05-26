package com.nglinx.pulse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nglinx.pulse.R;
import com.nglinx.pulse.adapter.PlaceAutocompleteAdapter;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.FenceModel;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.circle.MapAreaManager;
import com.nglinx.pulse.utils.circle.MapAreaMeasure;
import com.nglinx.pulse.utils.circle.MapAreaWrapper;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class FenceActivity extends AbstractActivity implements AdapterView.OnItemSelectedListener, GoogleMap.OnCameraIdleListener, GoogleApiClient.OnConnectionFailedListener {

    //Fence related
    private Spinner spinner_fence;
    List<FenceModel> myFences = null;
    ArrayAdapter<FenceModel> fenceAdapter;
    FenceModel selectedFence;

    boolean isOptionsOpen = false;

    //Fence option related
    ImageView ms_park_fence;
    ImageView ms_school_fence;
    ImageView ms_home_fence;
    TextView tv_park_fence;
    TextView tv_school_fence;
    TextView tv_home_fence;

    ImageView ms_fence_options;
    ImageView fence_bg_bar;
    Animation pulse_options_open, pulse_options_close;

    Button msEditFence, msOkCreateFence;
    TextView tv_fence_action;

    //Location related
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    private double latitude, longitude;
    private LatLng present_latLng;
    private Address present_address;
    GoogleMap googleMap;
    Geocoder geocoder;
    protected GoogleApiClient mGoogleApiClient;
    private AutoCompleteTextView mAutocompleteView;
    ImageView ms_location_pointer;
    private PlaceAutocompleteAdapter mAdapter;

    FenceOptionClickListener fenceOptionClickListener;

    private MapAreaManager circleManager;

    //Add Fence Dialog Attributes
    Dialog dlg_af;
    EditText af_fence, af_radius;
    TextView af_latitude, af_longitude, af_address;
    Button addFence, editFence, deleteFence, cancel;

    AddFenceClickListener addFenceListener;
    EditFenceClickListener editFenceListener;
    DeleteFenceClickListener deleteFenceListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_fence);

        initializeParent();

        initializeIcons();

        initializeFenceItems();

        initializeLocationItems();
    }


    protected void initializeIcons() {
        spinner_fence = (Spinner) findViewById(R.id.spinner_fence);
        pulse_options_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pulse_options_open);
        pulse_options_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pulse_options_close);

        ms_park_fence = (ImageView) findViewById(R.id.ms_park_fence);
        ms_school_fence = (ImageView) findViewById(R.id.ms_school_fence);
        ms_home_fence = (ImageView) findViewById(R.id.ms_home_fence);

        tv_park_fence = (TextView) findViewById(R.id.tv_park_fence);
        tv_school_fence = (TextView) findViewById(R.id.tv_school_fence);
        tv_home_fence = (TextView) findViewById(R.id.tv_home_fence);

        ms_fence_options = (ImageView) findViewById(R.id.ms_fence_options);
        fence_bg_bar = (ImageView) findViewById(R.id.fence_bg_bar);

        fenceOptionClickListener = new FenceOptionClickListener();
        ms_fence_options.setOnClickListener(fenceOptionClickListener);

        msOkCreateFence = (Button)
                findViewById(R.id.ms_ok_create_fence);

        msEditFence = (Button)
                findViewById(R.id.ms_edit_fence);

        tv_fence_action = (TextView)
                findViewById(R.id.tv_fence_action);

        addFenceListener = new AddFenceClickListener();
        editFenceListener = new EditFenceClickListener();
        deleteFenceListener = new DeleteFenceClickListener();
    }

    private void initializeFenceItems() {

        myFences = new ArrayList<FenceModel>();
        spinner_fence.setOnItemSelectedListener(this);

        fenceAdapter = new ArrayAdapter<FenceModel>(this, android.R.layout.simple_spinner_item, myFences);
        fenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_fence.setAdapter(fenceAdapter);

        getFenceList();
    }

    private void initializeLocationItems() {
        geocoder = new Geocoder(this, Locale.getDefault());

        present_latLng = new LatLng(latitude, longitude);
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fm.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(present_latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.setIndoorEnabled(false);

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    return;

                googleMap.setMyLocationEnabled(false);
                googleMap.setOnCameraIdleListener(FenceActivity.this);
                if (ActivityCompat.checkSelfPermission(FenceActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FenceActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    return;
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.ms_location_search_bar);

        ms_location_pointer = (ImageView)
                findViewById(R.id.ms_location_pointer);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, ApplicationConstants.BOUNDS_GREATER_SYDNEY,
                null);
        mAutocompleteView.setAdapter(mAdapter);
    }


    public void getFenceList() {
        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(FenceActivity.this);

        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.listFence(ds.getUserModel().getId(), new RetroResponse<FenceModel>() {
            @Override
            public void onSuccess() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                myFences.clear();
                myFences.add(getEmptyFence());
                myFences.addAll(models);
                Collections.sort((List<FenceModel>) myFences);
                fenceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                if ((errorMsg == null) || (errorMsg.trim().isEmpty())) {
                    errorMsg = "Failed to get the fence list. Server Error";
                }
                DialogUtils.diaplayErrorDialog(FenceActivity.this, errorMsg);
            }
        });
    }

    private FenceModel getEmptyFence() {
        FenceModel emptyFence = new FenceModel();
        emptyFence.setName("");
        emptyFence.setId("0");
        return emptyFence;
    }

    private void showMyLocation() {
        googleMap.clear();
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ds.getMylatLng()));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setIndoorEnabled(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        selectedFence = (FenceModel) spinner_fence.getSelectedItem();

        if (selectedFence.getId().equals("0")) {
            showMyLocation();
        } else {

            closeFenceOptions();
            msEditFence.setVisibility(View.VISIBLE);
            tv_fence_action.setText("Edit Fence");
            msOkCreateFence.setVisibility(View.GONE);

            double lat = Double.parseDouble(selectedFence.getLatitude());
            double lon = Double.parseDouble(selectedFence.getLongitude());
            LatLng latLng = new LatLng(lat, lon);
            googleMap.clear();
            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(lat, lon))
                    .radius(selectedFence.getRadius())
                    .fillColor(0x40ff0000)
                    .strokeColor(Color.TRANSPARENT)
                    .strokeWidth(2);

            Circle circle = googleMap.addCircle(circleOptions);

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setIndoorEnabled(false);
            ms_location_pointer.setVisibility(View.INVISIBLE);

            MarkerOptions markerOptions;
            markerOptions = new MarkerOptions().position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.myloc_icon));
            Marker marker = googleMap.addMarker(markerOptions);
            marker.showInfoWindow();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Nothing to implement
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i("Fence", "Autocomplete item selected: " + primaryText);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);

            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Log.i("Fence", "Called getPlaceById to get Place details for " + placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {

        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e("Fence", "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }

            googleMap.clear();
            // Get the Place object from the buffer.
            final Place place = places.get(0);
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(ds.getZoom()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);

//            MarkerOptions markerOptions;
//            markerOptions = new MarkerOptions().position(place.getLatLng());
//            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.myloc_icon));
//            Marker marker = googleMap.addMarker(markerOptions);
//            marker.showInfoWindow();

            places.release();
        }
    };

    @Override
    public void onCameraIdle() {
        present_address = null;
        present_latLng = null;

        present_latLng = googleMap.getCameraPosition().target;

        try {
            List<Address> present_addresses = geocoder.getFromLocation(present_latLng.latitude, present_latLng.longitude, 1);

            if (present_addresses.size() == 0) {
                DialogUtils.diaplayErrorDialog(FenceActivity.this, "Failed to get Address");
                return;
            }

            if (present_addresses.size() > 1) {
                DialogUtils.diaplayErrorDialog(FenceActivity.this, "Failed to get Address");
                return;
            }

            present_address = present_addresses.get(0);

            String displayAddress = present_address.getSubLocality() + "," + present_address.getLocality() + "," + present_address.getCountryName();

            ms_location_pointer.setVisibility(View.VISIBLE);

        } catch (IOException ex) {
            System.out.println("Failed to get the address by latitude and longitude");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Fence", "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    public void onMyLocationClick(View v) {
        showMyLocation();
    }


    private void openFenceOptions() {
        ms_park_fence.startAnimation(pulse_options_open);
        tv_park_fence.startAnimation(pulse_options_open);
        ms_park_fence.setClickable(true);

        ms_school_fence.startAnimation(pulse_options_open);
        tv_school_fence.startAnimation(pulse_options_open);
        ms_school_fence.setClickable(true);

        ms_home_fence.startAnimation(pulse_options_open);
        tv_home_fence.startAnimation(pulse_options_open);
        ms_home_fence.setClickable(true);

        ms_park_fence.setVisibility(View.VISIBLE);
        ms_school_fence.setVisibility(View.VISIBLE);
        ms_home_fence.setVisibility(View.VISIBLE);
        fence_bg_bar.setVisibility(View.VISIBLE);
        isOptionsOpen = true;
    }

    private void closeFenceOptions() {
        ms_park_fence.startAnimation(pulse_options_close);
        tv_park_fence.startAnimation(pulse_options_close);
        ms_park_fence.setClickable(false);

        ms_school_fence.startAnimation(pulse_options_close);
        tv_school_fence.startAnimation(pulse_options_close);
        ms_school_fence.setClickable(false);

        ms_home_fence.startAnimation(pulse_options_close);
        tv_home_fence.startAnimation(pulse_options_close);
        ms_home_fence.setClickable(false);

        ms_park_fence.setVisibility(View.INVISIBLE);
        ms_school_fence.setVisibility(View.INVISIBLE);
        ms_home_fence.setVisibility(View.INVISIBLE);
        fence_bg_bar.setVisibility(View.INVISIBLE);
        isOptionsOpen = false;
    }

    class FenceOptionClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            if (isOptionsOpen) {
                closeFenceOptions();
            } else {
                openFenceOptions();
            }
        }
    }


    public void onAddFenceFloatingBtnClick(View v) {

        if ((null != circleManager) && (circleManager.getCircles().size() > 0)) {
            circleManager.getCircles().get(0).deleteCircle();
        }
        googleMap.clear();
        closeFenceOptions();
        spinner_fence.setSelection(0);
        msEditFence.setVisibility(View.GONE);
        msOkCreateFence.setVisibility(View.VISIBLE);
        tv_fence_action.setText("Create Fence");

        circleManager = new MapAreaManager(googleMap,
                4, Color.RED, Color.HSVToColor(70, new float[]{1, 1, 200}), //styling
                R.drawable.ic_move, R.drawable.expand, R.drawable.ic_cancel, //custom drawables for move and resize icons
                0.5f, 0.5f, 0.5f, 0.5f, //sets anchor point of move / resize drawable in the middle

                new MapAreaMeasure(100, MapAreaMeasure.Unit.pixels), //circles will start with 100 pixels (independent of zoom level)

                new MapAreaManager.CircleManagerListener() { //listener for all circle events

                    @Override
                    public void onResizeCircleEnd(MapAreaWrapper draggableCircle) {
                        Toast.makeText(FenceActivity.this, "" + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCreateCircle(MapAreaWrapper draggableCircle) {
                        Toast.makeText(FenceActivity.this, "" + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDeleteCircle(MapAreaWrapper draggableCircle) {
                        Toast.makeText(FenceActivity.this, "" + draggableCircle, Toast.LENGTH_SHORT).show();
                        msEditFence.setVisibility(View.VISIBLE);
                        msOkCreateFence.setVisibility(View.GONE);
                    }

                    @Override
                    public void onMoveCircleEnd(MapAreaWrapper draggableCircle) {
                        Toast.makeText(FenceActivity.this, "" + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onMoveCircleStart(MapAreaWrapper draggableCircle) {
                        Toast.makeText(FenceActivity.this, "" + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResizeCircleStart(MapAreaWrapper draggableCircle) {
                        Toast.makeText(FenceActivity.this, "" + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onMinRadius(MapAreaWrapper draggableCircle) {
                        Toast.makeText(FenceActivity.this, "" + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onMaxRadius(MapAreaWrapper draggableCircle) {
                        Toast.makeText(FenceActivity.this, "" + draggableCircle, Toast.LENGTH_LONG).show();
                    }
                });

        circleManager.createFence(present_latLng);
        msEditFence.setVisibility(View.GONE);
        msOkCreateFence.setVisibility(View.VISIBLE);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(present_latLng, ds.getZoom()));
    }


    public void on_ms_add_fence_click(View v) {

        dlg_af = new Dialog(FenceActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        dlg_af.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg_af.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg_af.setCancelable(true);

        // Include dialog.xml file
        dlg_af.setContentView(R.layout.activity_create_fence_popup);
        dlg_af.show();

        //Add Fence Dialog related attributes
        af_fence = (EditText) dlg_af.findViewById(R.id.et_fence_name);
        af_radius = (EditText) dlg_af.findViewById(R.id.et_radius);
        af_latitude = (TextView) dlg_af.findViewById(R.id.et_latitude);
        af_longitude = (TextView) dlg_af.findViewById(R.id.et_longitude);
        af_address = (TextView) dlg_af.findViewById(R.id.et_address);

        addFence = (Button) dlg_af.findViewById(R.id.ep_btn_activate);
        cancel = (Button) dlg_af.findViewById(R.id.btn_cancel);

        af_radius.setText(String.valueOf((int) circleManager.getCircles().get(0).getRadius()));
        af_latitude.setText(String.valueOf(present_latLng.latitude));
        af_longitude.setText(String.valueOf(present_latLng.longitude));
        af_address.setText(present_address.getSubLocality() + "," + present_address.getLocality() + "," + present_address.getCountryName());
        addFence.setOnClickListener(addFenceListener);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg_af.dismiss();
            }
        });
    }


    class AddFenceClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            if ((af_fence.getText() == null) || (af_fence.getText().toString().equalsIgnoreCase(""))) {
                Toast.makeText(getApplicationContext(), "Fence name is missing", Toast.LENGTH_LONG).show();
                return;
            }

            if ((af_radius.getText() == null) || (af_radius.getText().toString().equalsIgnoreCase(""))) {
                Toast.makeText(getApplicationContext(), "Radius is missing", Toast.LENGTH_LONG).show();
                return;
            }

            final FenceModel fenceModel = new FenceModel();
            fenceModel.setName(af_fence.getText().toString());
            fenceModel.setLatitude(af_latitude.getText().toString());
            fenceModel.setLongitude(af_longitude.getText().toString());
            fenceModel.setRadius(Integer.parseInt(af_radius.getText().toString()));
            fenceModel.setAddress(af_address.getText().toString());

            new AlertDialog.Builder(FenceActivity.this)
                    .setTitle("Add Fence")
                    .setMessage("Do you want to add fence " + af_fence.getText())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(final DialogInterface dialog, int whichButton) {
                            final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(FenceActivity.this);
                            ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                            apiEndpointInterface.createFence(ds.getUserModel().getId(), fenceModel, new RetroResponse<FenceModel>() {
                                @Override
                                public void onSuccess() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    getFenceList();
                                    if (dlg_af != null)
                                        dlg_af.dismiss();
                                    DialogUtils.diaplaySuccessDialog(FenceActivity.this, "Fence " + af_fence.getText() + " created successfully");
                                }

                                @Override
                                public void onFailure() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    if ((errorMsg == null) || (errorMsg.trim().isEmpty())) {
                                        errorMsg = "Server Error";
                                    }
                                    DialogUtils.diaplayDialog(FenceActivity.this, "Failure to create a fence", errorMsg);
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    class EditFenceClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if ((af_fence.getText() == null) || (af_fence.getText().toString().equalsIgnoreCase(""))) {
                Toast.makeText(getApplicationContext(), "Fence name is missing", Toast.LENGTH_LONG).show();
                return;
            }

            if ((af_radius.getText() == null) || (af_radius.getText().toString().equalsIgnoreCase(""))) {
                Toast.makeText(getApplicationContext(), "Radius is missing", Toast.LENGTH_LONG).show();
                return;
            }

            selectedFence.setName(af_fence.getText().toString());
            selectedFence.setRadius(Integer.parseInt(af_radius.getText().toString()));

            new AlertDialog.Builder(FenceActivity.this)
                    .setTitle("Edit Fence")
                    .setMessage("Do you want to edit fence " + af_fence.getText())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(final DialogInterface dialog, int whichButton) {

                            final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(FenceActivity.this);

                            ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                            apiEndpointInterface.updateFence(ds.getUserModel().getId(), selectedFence.getId(), selectedFence, new RetroResponse<FenceModel>() {
                                @Override
                                public void onSuccess() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    getFenceList();
                                    if (dlg_af != null)
                                        dlg_af.dismiss();
                                    DialogUtils.diaplaySuccessDialog(FenceActivity.this, "Fence edited successfully");
                                }

                                @Override
                                public void onFailure() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    DialogUtils.diaplayDialog(FenceActivity.this, "Failed to edit fence", errorMsg);
                                }
                            });


                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }


    class DeleteFenceClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            new AlertDialog.Builder(FenceActivity.this)
                    .setTitle("Delete Fence")
                    .setMessage("Do you want to delete fence " + af_fence.getText())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(final DialogInterface dialog, int whichButton) {
                            final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(FenceActivity.this);

                            ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                            apiEndpointInterface.deleteFence(ds.getUserModel().getId(), selectedFence.getId(), new RetroResponse<FenceModel>() {
                                @Override
                                public void onSuccess() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    getFenceList();
                                    if (dlg_af != null)
                                        dlg_af.dismiss();
                                    DialogUtils.diaplaySuccessDialog(FenceActivity.this, "Fence deleted successfully");
                                }

                                @Override
                                public void onFailure() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    DialogUtils.diaplayDialog(FenceActivity.this, "Failure to delete fence", errorMsg);
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }


    public void onFenceEdit(View v) {
        dlg_af = new Dialog(FenceActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        dlg_af.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg_af.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dlg_af.setContentView(R.layout.activity_edit_fence_popup);
        dlg_af.show();
        selectedFence = (FenceModel) spinner_fence.getSelectedItem();

        af_fence = (EditText) dlg_af.findViewById(R.id.et_fence_name);
        af_fence.setText(selectedFence.getName());

        af_radius = (EditText) dlg_af.findViewById(R.id.et_radius);
        af_radius.setText(selectedFence.getRadius().toString());

        af_latitude = (TextView) dlg_af.findViewById(R.id.et_latitude);
        af_latitude.setText(selectedFence.getLatitude());

        af_longitude = (TextView) dlg_af.findViewById(R.id.et_longitude);
        af_longitude.setText(selectedFence.getLongitude());

        af_address = (TextView) dlg_af.findViewById(R.id.et_address);
        af_address.setText(selectedFence.getAddress());

        editFence = (Button) dlg_af.findViewById(R.id.md_btn_attach);
        deleteFence = (Button) dlg_af.findViewById(R.id.md_btn_detach);
        cancel = (Button) dlg_af.findViewById(R.id.btn_cancel_fence);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg_af.dismiss();
            }
        });

        editFence.setOnClickListener(editFenceListener);
        deleteFence.setOnClickListener(deleteFenceListener);
    }
}