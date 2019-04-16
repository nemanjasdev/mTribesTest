package m.tribes.test.app.view.map;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import m.tribes.test.app.R;
import m.tribes.test.app.core.presenter.IPresenter;
import m.tribes.test.app.core.interactor.MapInteractor;
import m.tribes.test.app.core.presenter.MapPresenter;


public class GoogleMapFragment extends Fragment implements IPresenter,
  OnMapReadyCallback,
  ActivityCompat.OnRequestPermissionsResultCallback {
  Unbinder unbinder;

  @BindView(R.id.progressBar)
  ProgressBar spinner;

  @BindView(R.id.searchView)
  SearchView searchView;

  SupportMapFragment mapFragment;

  private LocationCallback locationCallback;
  private MapPresenter presenter;

  private GoogleMap mMap;
  private FusedLocationProviderClient fusedLocationClient;

  private boolean isGPS = false;
  private boolean isStarted = false;
  private boolean isVisible = false;
  private boolean mPermissionDenied = false;

  public GoogleMapFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));

    if (!isGPS) {
      showMessage(Constants.ErrorMessage.GPS.toString());
      return;
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    isStarted = true;
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    isVisible = isVisibleToUser;
    if (isStarted && isVisible) {
      showProgress(spinner);
      setMarkers(mapFragment);
      hideProgress(spinner);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_map, container, false);
    unbinder = ButterKnife.bind(this, rootView);

    presenter = new MapPresenter(this, new MapInteractor());
    mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String title) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String title) {
        presenter.returnCarsByName(title);
        return false;
      }
    });

    return rootView;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    if (requestCode != Constants.LOCATION_PERMISSION_REQUEST_CODE) {
      return;
    }

    if (PermissionUtils.isPermissionGranted(permissions, grantResults,
      Manifest.permission.ACCESS_FINE_LOCATION)) {
      enableMyLocation();
    } else {
      //PermissionUtils.PermissionDeniedDialog.newInstance(true);
      mPermissionDenied = true;
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == Constants.GPS_PERMISSION_REQUEST_CODE) {
        isGPS = true;
      }
    }
  }

  @Override
  public void showProgress(ProgressBar spinner) {
    spinner.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideProgress(ProgressBar spinner) {
    spinner.setVisibility(View.GONE);
  }

  @Override
  public void showMessage(String message) {
    Toasty.warning(getContext(), message, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onMapReady(GoogleMap map) {
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
    presenter.onDestroy();
  }

  //
  //private methods
  //

  private void setMarkers(SupportMapFragment mapFragment) {
    mapFragment.getMapAsync(mMap -> {
      mMap.clear();
      this.mMap = mMap;
      enableMyLocation();
      fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
      presenter.returnPlacemarksMap(getActivity(), mMap, fusedLocationClient);

      mMap.setOnMyLocationButtonClickListener(() -> {
        new GPSUtils(getContext()).turnGPSOn(isGPSEnable -> {
          isGPS = isGPSEnable;
        });

        locationCallback = new LocationCallback() {
          @Override
          public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
              return;
            }
            fusedLocationClient.removeLocationUpdates(locationCallback);
          }
        };
        return false;
      });

      setMapStyle(mMap);
    });
  }

  private void setMapStyle(GoogleMap mMap) {
    mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style));
    mMap.setBuildingsEnabled(true);
    mMap.setIndoorEnabled(true);
    mMap.setTrafficEnabled(true);

    UiSettings mUiSettings = mMap.getUiSettings();
    mUiSettings.setZoomControlsEnabled(true);
    mUiSettings.setCompassEnabled(true);
    mUiSettings.setMyLocationButtonEnabled(true);
    mUiSettings.setScrollGesturesEnabled(true);
    mUiSettings.setZoomGesturesEnabled(true);
    mUiSettings.setTiltGesturesEnabled(true);
    mUiSettings.setRotateGesturesEnabled(true);
  }

  private void enableMyLocation() {
    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
      != PackageManager.PERMISSION_GRANTED) {
      this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
        Constants.LOCATION_PERMISSION_REQUEST_CODE);
    } else if (mMap != null) {
      mMap.setMyLocationEnabled(true);
    }
  }
}
