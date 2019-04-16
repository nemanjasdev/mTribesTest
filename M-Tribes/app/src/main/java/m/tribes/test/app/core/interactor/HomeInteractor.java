package m.tribes.test.app.core.interactor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import m.tribes.test.app.core.entity.Placemarks;
import m.tribes.test.app.core.entity.adapter.PlacemarkAdapter;
import m.tribes.test.app.core.entity.api.Client;
import m.tribes.test.app.core.entity.api.Service;
import m.tribes.test.app.view.map.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeInteractor {
  public Placemarks placemarks = null;
  Context context;

  public void findListItems(Context context, RecyclerView recyclerView) {
    loadData(context, recyclerView);
  }

  public void loadData(Context context, RecyclerView recyclerView) {
    this.context = context;
    Call<Placemarks> call;
    try {
      Service request = Client.retrofit.create(Service.class);
      call = request.getFlowers();
      call.enqueue(new Callback<Placemarks>() {
        @Override
        public void onResponse(Call<Placemarks> call, Response<Placemarks> response) {
          placemarks = response.body();
          setDataAdapter(recyclerView, context);
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

  private void setDataAdapter(RecyclerView recyclerView, Context context) {
    recyclerView.setAdapter(new PlacemarkAdapter(context, placemarks));
  }
}
