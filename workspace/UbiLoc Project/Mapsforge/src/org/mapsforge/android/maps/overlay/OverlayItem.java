package org.mapsforge.android.maps.overlay;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import org.mapsforge.core.GeoPoint;

public class OverlayItem
{
  public Drawable marker;
  public GeoPoint point;
  public String snippet;
  public String title;
  public Point cachedMapPosition;
  public byte cachedZoomLevel;

  public OverlayItem()
  {
    this.cachedZoomLevel = -128;
  }

  public OverlayItem(GeoPoint point, String title, String snippet)
  {
    this.point = point;
    this.title = title;
    this.snippet = snippet;
    this.cachedZoomLevel = -128;
  }

  public OverlayItem(GeoPoint point, String title, String snippet, Drawable marker)
  {
    this.point = point;
    this.title = title;
    this.snippet = snippet;
    this.marker = marker;
    this.cachedZoomLevel = -128;
  }

  public synchronized Drawable getMarker()
  {
    return this.marker;
  }

  public synchronized GeoPoint getPoint()
  {
    return this.point;
  }

  public synchronized String getSnippet()
  {
    return this.snippet;
  }

  public synchronized String getTitle()
  {
    return this.title;
  }

  public synchronized void setMarker(Drawable marker)
  {
    this.marker = marker;
  }

  public synchronized void setPoint(GeoPoint point)
  {
    this.point = point;
    this.cachedZoomLevel = -128;
  }

  public synchronized void setSnippet(String snippet)
  {
    this.snippet = snippet;
  }

  public synchronized void setTitle(String title)
  {
    this.title = title;
  }
}