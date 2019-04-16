package m.tribes.test.app.core.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Placemarks {
  @SerializedName("placemarks")
  public List<Placemark> item;
}
