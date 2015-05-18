package org.mapsforge.core;

public final class MercatorProjection
{
  public static final double EARTH_CIRCUMFERENCE = 40075016.685999997D;
  public static final double LATITUDE_MAX = 85.051128779806589D;
  public static final double LATITUDE_MIN = -85.051128779806589D;

  public static double calculateGroundResolution(double latitude, byte zoomLevel)
  {
    return (Math.cos(latitude * 0.017453292519943295D) * 40075016.685999997D / (256L << zoomLevel));
  }

  public static double deltaLat(double deltaPixel, double lat, byte zoom)
  {
    double pixelY = latitudeToPixelY(lat, zoom);
    double lat2 = pixelYToLatitude(pixelY + deltaPixel, zoom);

    return Math.abs(lat2 - lat);
  }

  public static double latitudeToPixelY(double latitude, byte zoomLevel)
  {
    double sinLatitude = Math.sin(latitude * 0.017453292519943295D);
    return ((0.5D - Math.log((1D + sinLatitude) / (1D - sinLatitude)) / 12.566370614359172D) * (256L << zoomLevel));
  }

  public static long latitudeToTileY(double latitude, byte zoomLevel)
  {
    return pixelYToTileY(latitudeToPixelY(latitude, zoomLevel), zoomLevel);
  }

  public static double limitLatitude(double latitude)
  {
    return Math.max(Math.min(latitude, 85.051128779806589D), -85.051128779806589D);
  }

  public static double limitLongitude(double longitude)
  {
    return Math.max(Math.min(longitude, 180.0D), -180.0D);
  }

  public static double longitudeToPixelX(double longitude, byte zoomLevel)
  {
    return ((longitude + 180.0D) / 360.0D * (256L << zoomLevel));
  }

  public static long longitudeToTileX(double longitude, byte zoomLevel)
  {
    return pixelXToTileX(longitudeToPixelX(longitude, zoomLevel), zoomLevel);
  }

  public static double pixelXToLongitude(double pixelX, byte zoomLevel)
  {
    return (360.0D * (pixelX / (256L << zoomLevel) - 0.5D));
  }

  public static long pixelXToTileX(double pixelX, byte zoomLevel)
  {
    return (long)Math.min(Math.max(pixelX / 256.0D, 0D), Math.pow(2.0D, zoomLevel) - 1D);
  }

  public static double pixelYToLatitude(double pixelY, byte zoomLevel)
  {
    double y = 0.5D - pixelY / (256L << zoomLevel);
    return (90.0D - 360.0D * Math.atan(Math.exp(-y * 6.2831853071795862D)) / 3.1415926535897931D);
  }

  public static long pixelYToTileY(double pixelY, byte zoomLevel)
  {
    return (long)Math.min(Math.max(pixelY / 256.0D, 0D), Math.pow(2.0D, zoomLevel) - 1D);
  }

  public static double tileXToLongitude(long tileX, byte zoomLevel)
  {
    return pixelXToLongitude(tileX * 256L, zoomLevel);
  }

  public static double tileYToLatitude(long tileY, byte zoomLevel)
  {
    return pixelYToLatitude(tileY * 256L, zoomLevel);
  }

  private MercatorProjection() {
    throw new IllegalStateException();
  }
}