package org.mapsforge.android.maps;

import org.mapsforge.core.GeoPoint;
import org.mapsforge.core.MapPosition;
import org.mapsforge.core.MercatorProjection;

public class MapViewPosition
{
  private double latitude;
  private double longitude;
  private final MapView mapView;
  private byte zoomLevel;

  MapViewPosition(MapView mapView)
  {
    this.mapView = mapView;

    this.latitude = (0.0D / 0.0D);
    this.longitude = (0.0D / 0.0D);
    this.zoomLevel = -1;
  }

  public synchronized GeoPoint getMapCenter()
  {
    return new GeoPoint(this.latitude, this.longitude);
  }

  public synchronized MapPosition getMapPosition()
  {
    if (!(isValid()))
      return null;

    GeoPoint geoPoint = new GeoPoint(this.latitude, this.longitude);
    return new MapPosition(geoPoint, this.zoomLevel);
  }

  public synchronized byte getZoomLevel()
  {
    return this.zoomLevel;
  }

  public synchronized boolean isValid()
  {
    if (Double.isNaN(this.latitude))
      return false;
    if (this.latitude < -85.051128779806589D)
      return false;
    if (this.latitude > 85.051128779806589D) {
      return false;
    }

    if (Double.isNaN(this.longitude))
      return false;
    if (this.longitude < -180.0D)
      return false;

    return (this.longitude <= 180.0D);
  }

  public synchronized void moveMap(float moveHorizontal, float moveVertical)
  {
    double pixelX = MercatorProjection.longitudeToPixelX(this.longitude, this.zoomLevel);
    double pixelY = MercatorProjection.latitudeToPixelY(this.latitude, this.zoomLevel);

    this.latitude = MercatorProjection.pixelYToLatitude(pixelY - moveVertical, this.zoomLevel);
    this.latitude = MercatorProjection.limitLatitude(this.latitude);

    this.longitude = MercatorProjection.pixelXToLongitude(pixelX - moveHorizontal, this.zoomLevel);
    this.longitude = MercatorProjection.limitLongitude(this.longitude);
  }

  synchronized void setMapCenter(GeoPoint geoPoint) {
    this.latitude = MercatorProjection.limitLatitude(geoPoint.getLatitude());
    this.longitude = MercatorProjection.limitLongitude(geoPoint.getLongitude());
  }

  synchronized void setMapCenterAndZoomLevel(MapPosition mapPosition) {
    GeoPoint geoPoint = mapPosition.geoPoint;
    this.latitude = MercatorProjection.limitLatitude(geoPoint.getLatitude());
    this.longitude = MercatorProjection.limitLongitude(geoPoint.getLongitude());
    this.zoomLevel = this.mapView.limitZoomLevel(mapPosition.zoomLevel);
  }

  synchronized void setZoomLevel(byte zoomLevel) {
    this.zoomLevel = this.mapView.limitZoomLevel(zoomLevel);
  }
}