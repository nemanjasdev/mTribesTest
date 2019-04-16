package m.tribes.test.app.core.entity.api;

import m.tribes.test.app.core.entity.Placemarks;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {
  @GET("locations.json")
  Call<Placemarks> getFlowers();
}