package m.tribes.test.app.core.entity;


import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import net.sharewire.googlemapsclustering.ClusterItem;

public class Placemark implements ClusterItem {
  private String address;
  private float[] coordinates;
  private String engineType;
  private String exterior;
  private int fuel;
  private String interior;
  private String name;
  private String vin;
  private LatLng location;

  public Placemark(@NonNull LatLng location, String name, String address) {
    //location = new LatLng(coordinates[1], coordinates[0]);
    this.location = location;
    this.name = name;
    this.address = address;
  }

  public Placemark(String address, float[] coordinates, String engineType,
                   String exterior, int fuel, String interior, String name, String vin) {
    super();
    this.address = address;
    this.coordinates = coordinates;
    this.engineType = engineType;
    this.exterior = exterior;
    this.fuel = fuel;
    this.interior = interior;
    this.name = name;
    this.vin = vin;
  }

  public String getAddress() {
    return address;
  }

  public float[] getCoordinates() {
    return coordinates;
  }

  public String getEngineType() {
    return engineType;
  }

  public String getExterior() {
    return exterior;
  }

  public int getFuel() {
    return fuel;
  }

  public String getInterior() {
    return interior;
  }

  public String getName() {
    return name;
  }

  public String getVin() {
    return vin;
  }

  public LatLng getLocation() {
    return location;
  }

  @Override
  public double getLatitude() {
    return coordinates[1];
  }

  @Override
  public double getLongitude() {
    return coordinates[0];
  }

  @Override
  public String getTitle() {
    return name;
  }

  @Override
  public String getSnippet() {
    return address;
  }
}


