package org.mapsforge.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class GeoPoint
  implements Comparable<GeoPoint>, Serializable
{
  private static final double EQUATORIAL_RADIUS = 6378137.0D;
  private static final long serialVersionUID = 1L;
  public final int latitudeE6;
  public final int longitudeE6;
  private transient int hashCodeValue;

  public static GeoPoint fromString(String geoPointString)
  {
    double[] coordinates = Coordinates.parseCoordinateString(geoPointString, 2);
    return new GeoPoint(coordinates[0], coordinates[1]);
  }

  public static double latitudeDistance(int meters)
  {
    return (meters * 360 / 40075016.685578488D);
  }

  public static double longitudeDistance(int meters, double latitude)
  {
    return (meters * 360 / 40075016.685578488D * Math.cos(Math.toRadians(latitude)));
  }

  private static void validateCoordinates(int latitudeE6, int longitudeE6) {
    Coordinates.validateLatitude(Coordinates.microdegreesToDegrees(latitudeE6));
    Coordinates.validateLongitude(Coordinates.microdegreesToDegrees(longitudeE6));
  }

  public GeoPoint(double latitude, double longitude)
  {
    this(Coordinates.degreesToMicrodegrees(latitude), Coordinates.degreesToMicrodegrees(longitude));
  }

  public GeoPoint(int latitudeE6, int longitudeE6)
  {
    validateCoordinates(latitudeE6, longitudeE6);

    this.latitudeE6 = latitudeE6;
    this.longitudeE6 = longitudeE6;
    this.hashCodeValue = calculateHashCode();
  }

  public int compareTo(GeoPoint geoPoint)
  {
    if (this.longitudeE6 > geoPoint.longitudeE6)
      return 1;
    if (this.longitudeE6 < geoPoint.longitudeE6)
      return -1;
    if (this.latitudeE6 > geoPoint.latitudeE6)
      return 1;
    if (this.latitudeE6 < geoPoint.latitudeE6)
      return -1;

    return 0;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (!(obj instanceof GeoPoint))
      return false;

    GeoPoint other = (GeoPoint)obj;
    if (this.latitudeE6 != other.latitudeE6)
      return false;

    return (this.longitudeE6 == other.longitudeE6);
  }

  public double getLatitude()
  {
    return Coordinates.microdegreesToDegrees(this.latitudeE6);
  }

  public double getLongitude()
  {
    return Coordinates.microdegreesToDegrees(this.longitudeE6);
  }

  public int hashCode()
  {
    return this.hashCodeValue;
  }

  public String toString()
  {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("GeoPoint [latitudeE6=");
    stringBuilder.append(this.latitudeE6);
    stringBuilder.append(", longitudeE6=");
    stringBuilder.append(this.longitudeE6);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }

  private int calculateHashCode()
  {
    int result = 7;
    result = 31 * result + this.latitudeE6;
    result = 31 * result + this.longitudeE6;
    return result;
  }

  private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
    objectInputStream.defaultReadObject();
    this.hashCodeValue = calculateHashCode();
  }
}