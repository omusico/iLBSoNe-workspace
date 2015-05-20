package org.mapsforge.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class BoundingBox
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  public final int maxLatitudeE6;
  public final int maxLongitudeE6;
  public final int minLatitudeE6;
  public final int minLongitudeE6;
  private transient int hashCodeValue;

  public static BoundingBox fromString(String boundingBoxString)
  {
    double[] coordinates = Coordinates.parseCoordinateString(boundingBoxString, 4);
    int minLat = Coordinates.degreesToMicrodegrees(coordinates[0]);
    int minLon = Coordinates.degreesToMicrodegrees(coordinates[1]);
    int maxLat = Coordinates.degreesToMicrodegrees(coordinates[2]);
    int maxLon = Coordinates.degreesToMicrodegrees(coordinates[3]);
    return new BoundingBox(minLat, minLon, maxLat, maxLon);
  }

  private static boolean isBetween(int number, int min, int max) {
    return ((min <= number) && (number <= max));
  }

  private static void validateCoordinates(int minLatitudeE6, int minLongitudeE6, int maxLatitudeE6, int maxLongitudeE6) {
    Coordinates.validateLatitude(Coordinates.microdegreesToDegrees(minLatitudeE6));
    Coordinates.validateLongitude(Coordinates.microdegreesToDegrees(minLongitudeE6));
    Coordinates.validateLatitude(Coordinates.microdegreesToDegrees(maxLatitudeE6));
    Coordinates.validateLongitude(Coordinates.microdegreesToDegrees(maxLongitudeE6));

    if (minLatitudeE6 > maxLatitudeE6) {
      throw new IllegalArgumentException("invalid latitude range: " + minLatitudeE6 + ' ' + maxLatitudeE6);
    }

    if (minLongitudeE6 > maxLongitudeE6)
      throw new IllegalArgumentException("invalid longitude range: " + minLongitudeE6 + ' ' + maxLongitudeE6);
  }

  public BoundingBox(int minLatitudeE6, int minLongitudeE6, int maxLatitudeE6, int maxLongitudeE6)
  {
    validateCoordinates(minLatitudeE6, minLongitudeE6, maxLatitudeE6, maxLongitudeE6);

    this.minLatitudeE6 = minLatitudeE6;
    this.minLongitudeE6 = minLongitudeE6;
    this.maxLatitudeE6 = maxLatitudeE6;
    this.maxLongitudeE6 = maxLongitudeE6;
    this.hashCodeValue = calculateHashCode();
  }

  public boolean contains(GeoPoint geoPoint)
  {
    return ((isBetween(geoPoint.latitudeE6, this.minLatitudeE6, this.maxLatitudeE6)) && (isBetween(geoPoint.longitudeE6, this.minLongitudeE6, this.maxLongitudeE6)));
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (!(obj instanceof BoundingBox))
      return false;

    BoundingBox other = (BoundingBox)obj;
    if (this.maxLatitudeE6 != other.maxLatitudeE6)
      return false;
    if (this.maxLongitudeE6 != other.maxLongitudeE6)
      return false;
    if (this.minLatitudeE6 != other.minLatitudeE6)
      return false;

    return (this.minLongitudeE6 == other.minLongitudeE6);
  }

  public GeoPoint getCenterPoint()
  {
    int latitudeOffset = (this.maxLatitudeE6 - this.minLatitudeE6) / 2;
    int longitudeOffset = (this.maxLongitudeE6 - this.minLongitudeE6) / 2;
    return new GeoPoint(this.minLatitudeE6 + latitudeOffset, this.minLongitudeE6 + longitudeOffset);
  }

  public double getMaxLatitude()
  {
    return Coordinates.microdegreesToDegrees(this.maxLatitudeE6);
  }

  public double getMaxLongitude()
  {
    return Coordinates.microdegreesToDegrees(this.maxLongitudeE6);
  }

  public double getMinLatitude()
  {
    return Coordinates.microdegreesToDegrees(this.minLatitudeE6);
  }

  public double getMinLongitude()
  {
    return Coordinates.microdegreesToDegrees(this.minLongitudeE6);
  }

  public int hashCode()
  {
    return this.hashCodeValue;
  }

  public String toString()
  {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("BoundingBox [minLatitudeE6=");
    stringBuilder.append(this.minLatitudeE6);
    stringBuilder.append(", minLongitudeE6=");
    stringBuilder.append(this.minLongitudeE6);
    stringBuilder.append(", maxLatitudeE6=");
    stringBuilder.append(this.maxLatitudeE6);
    stringBuilder.append(", maxLongitudeE6=");
    stringBuilder.append(this.maxLongitudeE6);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }

  private int calculateHashCode()
  {
    int result = 7;
    result = 31 * result + this.maxLatitudeE6;
    result = 31 * result + this.maxLongitudeE6;
    result = 31 * result + this.minLatitudeE6;
    result = 31 * result + this.minLongitudeE6;
    return result;
  }

  private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
    objectInputStream.defaultReadObject();
    this.hashCodeValue = calculateHashCode();
  }
}