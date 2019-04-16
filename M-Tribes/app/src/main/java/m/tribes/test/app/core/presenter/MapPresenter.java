package m.tribes.test.app.core.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;

import m.tribes.test.app.core.interactor.HomeInteractor;
import m.tribes.test.app.core.interactor.MapInteractor;

public class MapPresenter {

  private IPresenter presenter;
  public MapInteractor mapInteractor;

  public MapPresenter(IPresenter presenter, MapInteractor mapInteractor) {
    this.presenter = presenter;
    this.mapInteractor = mapInteractor;
  }

  public void returnPlacemarksMap(Context context, GoogleMap map, FusedLocationProviderClient mFusedLocationClient) {
    mapInteractor.findMapItems(context, mFusedLocationClient, map);
  }

  public void returnCarsByName(String title) {
    mapInteractor.displayCarsByName(title);
  }

  public void onDestroy() {
    presenter = null;
  }
}
