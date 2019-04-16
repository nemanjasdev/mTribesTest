package m.tribes.test.app.view.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.LocationManager;
import android.util.Log;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.content.ContentValues.TAG;

public class GPSUtils {

  private Context context;
  private LocationManager locationManager;
  private LocationRequest locationRequest;
  private LocationSettingsRequest mLocationSettingsRequest;
  private SettingsClient mSettingsClient;

  public GPSUtils(Context context) {
    this.context = context;
    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    mSettingsClient = LocationServices.getSettingsClient(context);

    locationRequest = LocationRequest.create();
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    locationRequest.setInterval(10 * 1000);
    locationRequest.setFastestInterval(2 * 1000);
    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
      .addLocationRequest(locationRequest);
    mLocationSettingsRequest = builder.build();
    builder.setAlwaysShow(true);
  }

  public void turnGPSOn(onGpsListener onGpsListener) {

    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
      if (onGpsListener != null) {
        onGpsListener.gpsStatus(true);
      }
    } else {
      mSettingsClient
        .checkLocationSettings(mLocationSettingsRequest)
        .addOnSuccessListener((Activity) context, new OnSuccessListener<LocationSettingsResponse>() {
          @SuppressLint("MissingPermission")
          @Override
          public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
            if (onGpsListener != null) {
              onGpsListener.gpsStatus(true);
            }
          }
        })
        .addOnFailureListener((Activity) context, e -> {
          int statusCode = ((ApiException) e).getStatusCode();
          switch (statusCode) {
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

              try {
                ResolvableApiException rae = (ResolvableApiException) e;
                rae.startResolutionForResult((Activity) context, 1001);
              } catch (IntentSender.SendIntentException sie) {
                Log.i(TAG, Constants.ErrorMessage.ERROR.toString());
              }
              break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
              Log.e(TAG, Constants.ErrorMessage.ERROR.toString());
          }
        });
    }
  }

  public interface onGpsListener {
    void gpsStatus(boolean isGPSEnable);
  }
}