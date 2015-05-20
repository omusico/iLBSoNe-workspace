package org.mapsforge.android.maps.mapgenerator;

import android.graphics.Bitmap;
import org.mapsforge.core.GeoPoint;

public abstract interface MapGenerator
{
  public abstract void cleanup();

  public abstract boolean executeJob(MapGeneratorJob paramMapGeneratorJob, Bitmap paramBitmap);

  public abstract GeoPoint getStartPoint();

  public abstract Byte getStartZoomLevel();

  public abstract byte getZoomLevelMax();

  public abstract boolean requiresInternetConnection();
}