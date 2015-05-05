package org.mapsforge.android.maps.overlay;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.MapViewPosition;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.core.GeoPoint;

public abstract class Overlay extends Thread
{
  private static final String THREAD_NAME = "Overlay";
  private boolean changedSize;
  private boolean hasValidDimensions;
  private final Matrix matrix;
  private float matrixScaleFactor;
  private Bitmap overlayBitmap1;
  private Bitmap overlayBitmap2;
  private final Canvas overlayCanvas;
  private final Point point;
  private Point positionAfterDraw;
  private Point positionBeforeDraw;
  private boolean redraw;
  protected MapView internalMapView;

  protected Overlay()
  {
    this.overlayCanvas = new Canvas();
    this.matrix = new Matrix();
    this.point = new Point();
    this.positionBeforeDraw = new Point();
    this.positionAfterDraw = new Point();
  }

  public final void draw(Canvas canvas)
  {
    synchronized (this.matrix) {
      if (this.overlayBitmap1 != null)
        canvas.drawBitmap(this.overlayBitmap1, this.matrix, null);
    }
  }

  public final void matrixPostScale(float scaleX, float scaleY, float pivotX, float pivotY)
  {
    synchronized (this.matrix) {
      this.matrix.postScale(scaleX, scaleY, pivotX, pivotY);
    }
  }

  public final void matrixPostTranslate(float translateX, float translateY)
  {
    synchronized (this.matrix) {
      this.matrix.postTranslate(translateX, translateY);
    }
  }

  public boolean onLongPress(GeoPoint geoPoint, MapView mapView)
  {
    return false;
  }

  public final void onSizeChanged()
  {
    synchronized (this) {
      this.changedSize = true;
      super.notify();
    }
  }

  public boolean onTap(GeoPoint geoPoint, MapView mapView)
  {
    return false;
  }

  public final void requestRedraw()
  {
    synchronized (this) {
      this.redraw = true;
      super.notify();
    }
  }

  public final void run()
  {
    setName(getThreadName());

    while (!isInterrupted()) {
      synchronized (this) {
        while ((!isInterrupted()) && (!this.changedSize) && (!this.redraw)) {
          try {
            super.wait();
          }
          catch (InterruptedException e) {
            interrupt();
          }
        }
      }

      if (isInterrupted()) {
        break;
      }

      if (this.changedSize) {
        changeSize();
      }

      if (this.redraw);
      redrawOverlay();
    }

    if (this.overlayBitmap1 != null) {
      this.overlayBitmap1.recycle();
    }

    if (this.overlayBitmap2 != null)
      this.overlayBitmap2.recycle();
  }

  public final void setupOverlay(MapView mapView)
  {
    if ((isInterrupted()) || (!isAlive())) {
      throw new IllegalThreadStateException("overlay thread already destroyed");
    }
    this.internalMapView = mapView;
    onSizeChanged();
  }

  private void redrawOverlay() {
    this.redraw = false;

    if (!this.hasValidDimensions)
    {
      return;
    }

    Projection mapViewProjection = this.internalMapView.getProjection();

    if(this.overlayBitmap2!=null){
    	if(overlayBitmap2.isRecycled())
    		 this.overlayBitmap2 = Bitmap.createBitmap(this.internalMapView.getWidth(), this.internalMapView.getHeight(), Bitmap.Config.ARGB_4444);
    this.overlayBitmap2.eraseColor(0);
    this.overlayCanvas.setBitmap(this.overlayBitmap2);
    }

    this.overlayCanvas.setMatrix(this.overlayCanvas.getMatrix());
    byte zoomLevelBeforeDraw;
    synchronized (this.internalMapView) {
      zoomLevelBeforeDraw = this.internalMapView.getMapPosition().getZoomLevel();
      this.positionBeforeDraw = mapViewProjection.toPoint(this.internalMapView.getMapPosition().getMapCenter(), this.positionBeforeDraw, zoomLevelBeforeDraw);
    }

    this.point.x = (this.positionBeforeDraw.x - (this.overlayCanvas.getWidth() >> 1));
    this.point.y = (this.positionBeforeDraw.y - (this.overlayCanvas.getHeight() >> 1));

    if ((isInterrupted()) || (sizeHasChanged()))
    {
      return;
    }

    drawOverlayBitmap(this.overlayCanvas, this.point, mapViewProjection, zoomLevelBeforeDraw);

    if ((isInterrupted()) || (sizeHasChanged()))
    {
      return;
    }
    byte zoomLevelAfterDraw;
    synchronized (this.internalMapView) {
      zoomLevelAfterDraw = this.internalMapView.getMapPosition().getZoomLevel();
      this.positionAfterDraw = mapViewProjection.toPoint(this.internalMapView.getMapPosition().getMapCenter(), this.positionAfterDraw, zoomLevelBeforeDraw);
    }

    if (this.internalMapView.isZoomAnimatorRunning())
    {
      return;
    }

    synchronized (this.matrix) {
      this.matrix.reset();
      this.matrix.postTranslate(this.positionBeforeDraw.x - this.positionAfterDraw.x, this.positionBeforeDraw.y - this.positionAfterDraw.y);

      byte zoomLevelDiff = (byte)(zoomLevelAfterDraw - zoomLevelBeforeDraw);
      if (zoomLevelDiff > 0)
      {
        this.matrixScaleFactor = (1 << zoomLevelDiff);
        this.matrix.postScale(this.matrixScaleFactor, this.matrixScaleFactor, this.overlayCanvas.getWidth() >> 1, this.overlayCanvas.getHeight() >> 1);
      }
      else if (zoomLevelDiff < 0)
      {
        this.matrixScaleFactor = (1.0F / (1 << -zoomLevelDiff));
        this.matrix.postScale(this.matrixScaleFactor, this.matrixScaleFactor, this.overlayCanvas.getWidth() >> 1, this.overlayCanvas.getHeight() >> 1);
      }

      Bitmap overlayBitmapSwap = this.overlayBitmap1;
      this.overlayBitmap1 = this.overlayBitmap2;
      this.overlayBitmap2 = overlayBitmapSwap;
    }

    if ((isInterrupted()) || (sizeHasChanged()))
    {
      return;
    }

    this.internalMapView.postInvalidate();
  }

  protected abstract void drawOverlayBitmap(Canvas paramCanvas, Point paramPoint, Projection paramProjection, byte paramByte);

  protected String getThreadName()
  {
    return "Overlay";
  }

  public final void changeSize()
  {
    this.changedSize = false;

    if (this.overlayBitmap1 != null) {
      this.overlayBitmap1.recycle();
    }
    if (this.overlayBitmap2 != null) {
      this.overlayBitmap2.recycle();
    }

    int width = this.internalMapView.getWidth();
    int height = this.internalMapView.getHeight();

    if ((width > 0) && (height > 0))
    {
    try{
      this.overlayBitmap1 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
      this.overlayBitmap2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
    }catch(OutOfMemoryError e){
    	
    }
      this.redraw = true;
      this.hasValidDimensions = true;
    } else {
      this.hasValidDimensions = false;
    }
  }

  public boolean sizeHasChanged()
  {
    return this.changedSize;
  }

  public void dispose() {
    if (this.overlayBitmap1 != null) {
      this.overlayBitmap1.recycle();
    }
    if (this.overlayBitmap2 != null)
      this.overlayBitmap2.recycle();
  }

  protected static enum EventType
  {
    LONG_PRESS, 

    TAP;
  }
}