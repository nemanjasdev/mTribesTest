package m.tribes.test.app.core.interactor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import net.sharewire.googlemapsclustering.ClusterManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;
import m.tribes.test.app.core.entity.Placemark;
import m.tribes.test.app.core.entity.Placemarks;
import m.tribes.test.app.core.entity.api.Client;
import m.tribes.test.app.core.entity.api.Service;
import m.tribes.test.app.view.map.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapInteractor {
  Context context;
  ClusterManager<Placemark> mClusterManager;
  public Placemarks placemarks = null;
  private List<Placemark> markerArray, emptyList = new ArrayList<>();

  public void findMapItems(Context context, FusedLocationProviderClient fusedLocationClient, GoogleMap map) {
    loadMapData(context, fusedLocationClient, map);
  }

  public void loadMapData(Context context, FusedLocationProviderClient fusedLocationClient, GoogleMap map) {
    this.context = context;

    Call<Placemarks> call;
    try {
      Service request = Client.retrofit.create(Service.class);
      call = request.getFlowers();
      call.enqueue(new Callback<Placemarks>() {
        @Override
        public void onResponse(Call<Placemarks> call, Response<Placemarks> response) {
          placemarks = response.body();
          setMapMarkers(map, fusedLocationClient);
        }

        @Override
        public void onFailure(Call<Placemarks> call, Throwable t) {
          Log.e(Constants.ErrorMessage.FAILURE.toString(), t.getMessage());
        }
      });
    } catch (
      Exception e) {
      Log.e(Constants.ErrorMessage.ERROR.toString(), e.getMessage());
    }
  }

  public void displayCarsByName(String title) {
    mClusterManager.setItems(emptyList);
    List<Placemark> searchArray = new ArrayList<>();

    for (Placemark pl : markerArray) {
      if (pl.getName().toLowerCase().contains(title.toLowerCase())) {
        searchArray.add(pl);
      }
    }
    mClusterManager.setItems(searchArray);
  }

  //
  //private methods
  //

  @SuppressLint("MissingPermission")
  private void setMapMarkers(GoogleMap map, FusedLocationProviderClient fusedLocationClient) {
    mapDraw(map);
    map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
      @Override
      public View getInfoWindow(Marker marker) {
        return null;
      }

      @Override
      public View getInfoContents(Marker marker) {
        return null;
      }
    });

    map.setOnMarkerClickListener(marker -> {
      if (marker.getSnippet() == null) {
        return false;
      }

      float coordinates[] = new float[]{(float) marker.getPosition().longitude, (float) marker.getPosition().latitude, 0.0f};
      showMarkers(false,
        new Placemark(marker.getSnippet(),
          coordinates, "", "",
          0, "", marker.getTitle(), ""));

      marker.setVisible(true);
      if (!marker.isInfoWindowShown()) {
        marker.showInfoWindow();
      }

      fusedLocationClient.getLastLocation().addOnSuccessListener((Activity) context, location -> {
        if (location != null) {
          showDistance(marker, location);
        }
      });

      return false;
    });

    map.setOnInfoWindowClickListener(marker -> {
      marker.hideInfoWindow();
      float coordinates[] = new float[]{(float) marker.getPosition().latitude, (float) marker.getPosition().longitude, 0.0f};
      showMarkers(true, new Placemark(marker.getSnippet(),
        coordinates, "", "",
        0, "", marker.getTitle(), ""));
    });
  }

  private void showDistance(Marker marker, Location location) {
    Location markerLocation = new Location("marker");
    markerLocation.setLatitude(marker.getPosition().latitude);
    markerLocation.setLongitude(marker.getPosition().longitude);
    float distance = location.distanceTo(markerLocation) / 1000;

    NumberFormat nf = NumberFormat.getNumberInstance();
    nf.setMaximumFractionDigits(0);
    String roundedDistance = nf.format(distance);
    showDistanceFromUser(roundedDistance);
  }

  private void showDistanceFromUser(String roundedDistance) {
    Toasty.info(context, Constants.CarState.DISTANCE.toString() + roundedDistance, Toast.LENGTH_SHORT).show();
  }

  private void showMarkers(boolean isVisible, Placemark placemark) {

    if (!isVisible) {
      //remove other pins
      mClusterManager.setItems(emptyList);
      //set clicked pin
      mClusterManager.setItems(Arrays.asList(placemark));
    } else {
      mClusterManager.setItems(markerArray);
    }
  }

  private void mapDraw(GoogleMap map) {
    LatLngBounds.Builder bounds = new LatLngBounds.Builder();
    markerArray = new ArrayList<>();
    for (Placemark placemark : placemarks.item) {
      mClusterManager = new ClusterManager<>(context, map);
      map.setOnCameraIdleListener(mClusterManager);
      markerArray.add(placemark);
      bounds.include(new LatLng(placemark.getLatitude(), placemark.getLongitude()));
    }
    //set camera by LatLng of markers
    map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 70));
    mClusterManager.setItems(markerArray);
  }
}
