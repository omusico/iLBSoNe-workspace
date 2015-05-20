package org.mapsforge.android.maps.mapgenerator.tiledownloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;
import org.mapsforge.android.maps.mapgenerator.MapGeneratorJob;
import org.mapsforge.core.GeoPoint;
import org.mapsforge.core.Tile;

public abstract class TileDownloader
  implements MapGenerator
{
  private static final Logger LOGGER = Logger.getLogger(TileDownloader.class.getName());
  private static final GeoPoint START_POINT = new GeoPoint(51.329999999999998D, 10.449999999999999D);
  private static final Byte START_ZOOM_LEVEL = 5;
  protected final int[] pixels;

  protected TileDownloader()
  {
    this.pixels = new int[65536];
  }

  public final void cleanup()
  {
  }

  public boolean executeJob(MapGeneratorJob mapGeneratorJob, Bitmap bitmap)
  {
    Tile tile;
    try {
      tile = mapGeneratorJob.tile;
      URL url = new URL(getProtocol(), getHostName(), getTilePath(tile));
      InputStream inputStream = url.openStream();
      Bitmap decodedBitmap = BitmapFactory.decodeStream(inputStream);
      inputStream.close();

      if (decodedBitmap == null) {
        return false;
      }

      decodedBitmap.getPixels(this.pixels, 0, 256, 0, 0, 256, 256);
      decodedBitmap.recycle();

      bitmap.setPixels(this.pixels, 0, 256, 0, 0, 256, 256);
      return true;
    } catch (UnknownHostException e) {
      LOGGER.log(Level.SEVERE, null, e);
      return false;
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, null, e); }
    return false;
  }

  public abstract String getHostName();

  public abstract String getProtocol();

  public GeoPoint getStartPoint()
  {
    return START_POINT;
  }

  public Byte getStartZoomLevel()
  {
    return START_ZOOM_LEVEL;
  }

  public abstract String getTilePath(Tile paramTile);

  public boolean requiresInternetConnection()
  {
    return true;
  }
}