/*
 * Copyright 2010, 2011, 2012 mapsforge.org
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.ubiloc.overlays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.overlay.ItemizedOverlay;
import org.mapsforge.android.maps.overlay.Overlay;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.android.maps.overlay.OverlayWay;
import org.mapsforge.core.GeoPoint;
import org.mapsforge.core.Tile;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.donal.wechat.R;
import com.ubiloc.map.maputils.BoundingBoxE6;
import com.ubiloc.map.maputils.MapUtil;
import com.ubiloc.map.maputils.MyMath;

import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.gps.GpsManager;
import eu.geopaparazzi.library.util.Utilities;
import eu.geopaparazzi.spatialite.database.spatial.SpatialDatabasesManager;
import eu.geopaparazzi.spatialite.database.spatial.core.databasehandlers.AbstractSpatialDatabaseHandler;
import eu.geopaparazzi.spatialite.database.spatial.core.geometry.GeometryIterator;
import eu.geopaparazzi.spatialite.database.spatial.core.tables.SpatialRasterTable;
import eu.geopaparazzi.spatialite.database.spatial.core.tables.SpatialVectorTable;

/**
 * GeopaparazziOverlay is an abstract base class to display {@link OverlayWay
 * OverlayWays}. The class defines some methods to access the backing data
 * structure of deriving subclasses.
 * <p>
 * The overlay may be used to show additional ways such as calculated routes.
 * Closed polygons, for example buildings or areas, are also supported. A way
 * node sequence is considered as a closed polygon if the first and the last way
 * node are equal.
 * 
 * @param <Generic>
 *            the type of ways handled by this overlay.
 */
public abstract class BaseOverlay extends Overlay {

	List<String> dbABSPaths = null;
	HashMap<String, List<SpatialVectorTable>> my_spatialTables = null;

	private Map<String, GeometryIterator> mCurrentGeometryIteratorMap;

	private MapView mapView;
	// ����ʮ�ּ�
	private Path crossPath;
	private Paint crossPaint = new Paint();

	private List<Integer> visibleItems;
	private List<Integer> visibleItemsRedraw;
	private Point itemPosition;
	private Drawable itemMarker;
	private Drawable itemDefaultMarker;
	private int itemBottom;
	private int left;
	private int right;
	private int top;
	private Context context;
	private int crossSize = 10;
	SpatialDatabasesManager sdManager;
	private GpsData overlayGps;
	private OverlayWay gpslogOverlay;
	private Paint textPaint;
	private Paint textHaloPaint;
	private int tileSize = 256;
	private int zoomLevel = 0;
	private Paint imagePaint;

	/*
	 * gps stuff
	 */
	private Point circlePosition;
	private Path path;

	private Drawable gpsMarker;

	private Path gpsPath;
	private Paint gpsTrackPaintYellow;
	private Paint gpsTrackPaintBlack;
	private Paint gpsOutline;
	private Paint gpsFill;

	private Path gpsStatusPath;
	private Paint gpsRedFill;
	private Paint gpsOrangeFill;
	private Paint gpsGreenFill;
	private Paint gpsBlueFill;
	private List<GeoPoint> currentGpsLog = new ArrayList<GeoPoint>();
	private int inset = 5;
	private boolean isNotesTextVisible;
	private boolean doNotesTextHalo;

	// ========================================================
	// ������γ��
	private GeoPoint GpsPosition;
	private float GpsAccuracy;
	public static final float SMALL_SEZE = 5;
	public static final float NORMAL_SEZE = 10;
	public static final float LARGE_SEZE = 15;
	private static final String TAG = "BaseOverlay";

	/**
	 * 存储图层
	 */
	private LinkedHashMap<String, BaseOverlayItem> mOverlayItems = new LinkedHashMap<String, BaseOverlayItem>();
	// =========================================================
	// ���Ƶ���������Ҫ��������Ϣ
	// private String coords =
	// "116.75557503421 40.3066720566464,116.755324502335 40.3064612343892,116.755325830472 40.3060931467768,116.755676581329 40.3057927231198,116.756092650115 40.3055593616439,116.756595110572 40.305610614,116.75702955199 40.3063477080872,116.757394978513 40.3064965190393,116.758162496233 40.3064324812688,116.759255624579 40.3062873247287,116.757846381791 40.3023081166316,116.758325379711 40.3021026307501,116.757250337449 40.2991423999448,116.756241954774 40.2962823687995,116.756158885499 40.2960726495417,116.756093698275 40.2959190209083,116.755843035208 40.2954176013723,116.756323998624 40.294806283054,116.756662711861 40.2944303183849,116.757039660308 40.293945649209,116.757192835345 40.2937647208832,116.757328881938 40.2934601697938,116.757048232272 40.2928722637623,116.757104733808 40.2922843385,116.75705291089 40.292183520484,116.756987408007 40.2920528158659,116.756958772536 40.2919353731549,116.738917499695 40.2911998770214,116.739955934318 40.2925167386487,116.742326430357 40.2955225883324,116.744827297697 40.2987116679938,116.747475924899 40.3020390409592,116.749242150637 40.3042576702702,116.752408138663 40.3030734016426,116.753622286797 40.3046150230091,116.754875447623 40.3062252533426,116.755243448949 40.3066980939514,116.755266823411 40.3066962584891,116.75557503421 40.3066720566464";

	private List<GeoPoint> coords = new ArrayList<GeoPoint>();
	private Paint polygonPaint;

	public GeoPoint getGpsPosition() {
		return GpsPosition;
	}

	public void setGpsPosition(GeoPoint gpsPosition) {
		GpsPosition = gpsPosition;
	}

	public float getGpsAccuracy() {
		return GpsAccuracy;
	}

	public void setGpsAccuracy(float gpsAccuracy) {
		GpsAccuracy = gpsAccuracy;
	}

	// ===========================================================
	/**
	 * Create a {@link OverlayWay} wrapped type.
	 */
	public BaseOverlay(Context context, MapView mapView) {
		super();

		this.mapView = mapView;
		this.context = context;
		this.itemPosition = new Point();
		this.visibleItems = new ArrayList<Integer>(8);
		this.visibleItemsRedraw = new ArrayList<Integer>(8);
		// �ڴ˳�ʼ��������ɫ��
		crossPath = new Path();
		crossPaint.setAntiAlias(true);
		crossPaint.setColor(Color.RED);
		crossPaint.setStrokeWidth(10);
		crossPaint.setStyle(Paint.Style.STROKE);
		sdManager = SpatialDatabasesManager.getInstance();
		// ��ʵ���е��������ݿ��Ϊ�ַ���
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setColor(Color.BLACK);
		textPaint.setTextSize(16);

		textHaloPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textHaloPaint.setStyle(Paint.Style.STROKE);
		textHaloPaint.setStrokeWidth(3);
		textHaloPaint.setColor(Color.WHITE);
		textHaloPaint.setTextSize(16);

		imagePaint = new Paint();
		imagePaint.setAlpha(90);

		this.gpsPath = new Path();
		overlayGps = new GpsData();
		gpsMarker = context.getResources().getDrawable(R.drawable.circle_press);
		gpsFill = new Paint(Paint.ANTI_ALIAS_FLAG);
		gpsFill.setStyle(Paint.Style.FILL);
		gpsFill.setColor(Color.BLUE);
		gpsFill.setAlpha(48);

		gpsOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
		gpsOutline.setStyle(Paint.Style.STROKE);
		gpsOutline.setColor(Color.BLUE);
		gpsOutline.setAlpha(128);
		gpsOutline.setStrokeWidth(2);

		gpsTrackPaintYellow = new Paint(Paint.ANTI_ALIAS_FLAG);
		gpsTrackPaintYellow.setStyle(Paint.Style.STROKE);
		gpsTrackPaintYellow.setColor(Color.YELLOW);
		gpsTrackPaintYellow.setStrokeWidth(3);

		gpsTrackPaintBlack = new Paint(Paint.ANTI_ALIAS_FLAG);
		gpsTrackPaintBlack.setStyle(Paint.Style.STROKE);
		gpsTrackPaintBlack.setColor(Color.BLACK);
		gpsTrackPaintBlack.setStrokeWidth(5);

		Resources resources = context.getResources();

		gpsStatusPath = new Path();
		gpsRedFill = new Paint(Paint.ANTI_ALIAS_FLAG);
		gpsRedFill.setStyle(Paint.Style.FILL);
		gpsRedFill.setColor(resources.getColor(R.color.gpsred_fill));
		gpsOrangeFill = new Paint(Paint.ANTI_ALIAS_FLAG);
		gpsOrangeFill.setStyle(Paint.Style.FILL);
		gpsOrangeFill.setColor(resources.getColor(R.color.gpsorange_fill));
		gpsGreenFill = new Paint(Paint.ANTI_ALIAS_FLAG);
		gpsGreenFill.setStyle(Paint.Style.FILL);
		gpsGreenFill.setColor(resources.getColor(R.color.gpsgreen_fill));
		gpsBlueFill = new Paint(Paint.ANTI_ALIAS_FLAG);
		gpsBlueFill.setStyle(Paint.Style.FILL);
		gpsBlueFill.setColor(resources.getColor(R.color.gpsblue_fill));
		gpsMarker = ItemizedOverlay.boundCenter(gpsMarker);
		this.circlePosition = new Point();
		gpslogOverlay = new OverlayWay(null, gpsOutline);
		this.path = new Path();

		polygonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		polygonPaint.setStyle(Paint.Style.STROKE);
		polygonPaint.setColor(Color.RED);
		polygonPaint.setStrokeWidth(5);
	}

	public static Drawable boundCenter(Drawable balloon) {
		balloon.setBounds(balloon.getIntrinsicWidth() / -2,
				balloon.getIntrinsicHeight() / -2,
				balloon.getIntrinsicWidth() / 2,
				balloon.getIntrinsicHeight() / 2);
		return balloon;
	}

	public void setLayer() {

	}

	/**
	 * Checks whether an item has been tapped.
	 */
	@Override
	public boolean onTap(GeoPoint geoPoint, MapView mapView) {
		// �����͵��ʱ�г�ͻ������ʱ���ж�
		return false;
	}

	public abstract int waySize();

	public abstract int itemSize();

	protected abstract OverlayWay createWay(int index);

	@Override
	protected void drawOverlayBitmap(Canvas canvas, Point drawPosition,
			Projection projection, byte drawZoomLevel) {
		int canvasHeight = canvas.getHeight();
		int canvasWidth = canvas.getWidth();

		drawPolygon(canvas, projection, polygonPaint);
		drawOverlayItems(canvas, projection, polygonPaint);
	}

	/**
	 * 绘制图层信息
	 * 
	 * @param canvas
	 * @param projection
	 * @param polygonPaint
	 */
	private void drawOverlayItems(Canvas canvas, Projection projection,
			Paint paint) {
		Set<String> keys = mOverlayItems.keySet();
		for (String key : keys) {
			BaseOverlayItem item = mOverlayItems.get(key);
			item.draw(canvas, projection, paint);
		}
	}

	/**
	 * 
	 * @param canvas
	 * @param projection
	 * @param paint
	 */
	private void drawPolygon(Canvas canvas, Projection projection, Paint paint) {
		if (coords != null && coords.size() > 0) {
			if (coords.size() > 1) {
				paint.setColor(Color.RED);
				paint.setAlpha(130);
				if (coords.size() == 2) {
					Log.i("nk", "coords----->" + coords);
					GeoPoint geoPoint = coords.get(0);
					Point pp = new Point();
					projection.toPixels(geoPoint, pp);

					GeoPoint geoPoint2 = coords.get(1);
					Point pp2 = new Point();
					projection.toPixels(geoPoint2, pp2);
					canvas.drawLine(pp.x, pp.y, pp2.x, pp2.y, paint);

				} else {
					Path path = new Path();
					GeoPoint geoPoint = coords.get(0);
					Point pp = new Point();
					projection.toPixels(geoPoint, pp);
					path.moveTo(pp.x, pp.y);
					for (int i = 0; i < coords.size(); i++) {
						GeoPoint geoPoint2 = coords.get(i);
						Point p = new Point();
						projection.toPixels(geoPoint2, p);
						path.lineTo(p.x, p.y);
					}
					// path.close();
					canvas.drawPath(path, paint);
					PathEffect effect = new DashPathEffect(new float[] { 1, 2,
							4, 8 }, 1);
					paint.setAntiAlias(true);
					paint.setPathEffect(effect);
					GeoPoint geoPoint2 = coords.get(coords.size() - 1);
					Point pp2 = new Point();
					projection.toPixels(geoPoint2, pp2);
					canvas.drawLine(pp.x, pp.y, pp2.x, pp2.y, paint);
					PathEffect effect1 = new DashPathEffect(
							new float[] { 0, 0 }, 0);

					paint.setPathEffect(effect1);
				}
			} else {
				if (coords.size() == 1) {
					GeoPoint geoPoint = coords.get(0);
					Point pp = new Point();
					projection.toPixels(geoPoint, pp);
					canvas.drawCircle(pp.x, pp.y, 5, paint);
				}
			}
		}
	}

	private void drawFromSpatialite(Canvas canvas, Point drawPosition,
			Projection projection, byte drawZoomLevel) {
		GeoPoint zeroPoint = projection.fromPixels(0, 0);
		GeoPoint whPoint = projection.fromPixels(canvas.getWidth(),
				canvas.getHeight());
		double n, w, s, e;
		n = zeroPoint.getLatitude();
		w = zeroPoint.getLongitude();
		s = whPoint.getLatitude();
		e = whPoint.getLongitude();

	}

	private void drawFromImage(Canvas canvas, Point drawPosition,
			Projection projection, byte drawZoomLevel) {
		try {
			SpatialRasterTable rasterTable = SpatialDatabasesManager
					.getInstance().getRasterTableByName("beijingdianzi");
			SpatialDatabasesManager sdManager = SpatialDatabasesManager
					.getInstance();
			AbstractSpatialDatabaseHandler spatialDatabaseHandler = sdManager
					.getRasterHandler(rasterTable);
			this.zoomLevel = drawZoomLevel;
			final int viewWidth = mapView.getWidth();
			final int viewHeight = mapView.getHeight();
			// ��ȡ��Ļ����tile�� x y
			final int[] centerMapTileCoords = MapUtil
					.getMapTileFromCoordinates(mapView.getMapPosition()
							.getMapCenter().latitudeE6, mapView
							.getMapPosition().getMapCenter().longitudeE6,
							zoomLevel, null);

			final Point upperLeftCornerOfCenterMapTile = getUpperLeftCornerOfCenterMapTileInScreen(
					centerMapTileCoords, tileSize, null);

			final int centerMapTileScreenLeft = upperLeftCornerOfCenterMapTile.x;
			final int centerMapTileScreenTop = upperLeftCornerOfCenterMapTile.y;

			final int centerMapTileScreenRight = centerMapTileScreenLeft
					+ tileSize;
			final int centerMapTileScreenBottom = centerMapTileScreenTop
					+ tileSize;

			final int additionalTilesNeededToLeftOfCenter = (int) Math
					.ceil((float) centerMapTileScreenLeft / tileSize); // i.e.

			final int additionalTilesNeededToRightOfCenter = (int) Math
					.ceil((float) (viewWidth - centerMapTileScreenRight)
							/ tileSize);
			final int additionalTilesNeededToTopOfCenter = (int) Math
					.ceil((float) centerMapTileScreenTop / tileSize); // i.e.

			final int additionalTilesNeededToBottomOfCenter = (int) Math
					.ceil((float) (viewHeight - centerMapTileScreenBottom)
							/ tileSize);

			final int mapTileUpperBound = (int) Math.pow(2, zoomLevel);
			final int[] mapTileCoords = new int[] { centerMapTileCoords[0],
					centerMapTileCoords[1] };

			for (int y = -additionalTilesNeededToTopOfCenter; y <= additionalTilesNeededToBottomOfCenter; y++) {
				for (int x = -additionalTilesNeededToLeftOfCenter; x <= additionalTilesNeededToRightOfCenter; x++) {

					mapTileCoords[0] = MyMath.mod(centerMapTileCoords[0] + y,
							mapTileUpperBound);
					mapTileCoords[1] = MyMath.mod(centerMapTileCoords[1] + x,
							mapTileUpperBound);

					// ��ȡx��y��z
					/*
					 * �������sqltedb ��ô���� xyz����
					 */
					int tile_x = mapTileCoords[1];
					int tile_y = mapTileCoords[0];
					int tile_z = 17 - zoomLevel;

					/**
					 * ���������WMS������ôxyz����
					 */

					double[] tileBounds = Utilities.tileLatLonBounds(tile_x,
							tile_y, zoomLevel, Tile.TILE_SIZE);
					String tilePart = "http:xxxx/BBOX=XXX,YYY,XXX,YYY";
					String tmpTilePart = tilePart.replaceFirst(
							"XXX", String.valueOf(tileBounds[0])); //$NON-NLS-1$
					tmpTilePart = tmpTilePart.replaceFirst(
							"YYY", String.valueOf(tileBounds[1])); //$NON-NLS-1$
					tmpTilePart = tmpTilePart.replaceFirst(
							"XXX", String.valueOf(tileBounds[2])); //$NON-NLS-1$
					tmpTilePart = tmpTilePart.replaceFirst(
							"YYY", String.valueOf(tileBounds[3])); //$NON-NLS-1$

					/**
					 * ����Ǳ���TMSɢƬ�����N xyz����
					 */
					int[] tmsTiles = Utilities.googleTile2TmsTile(tile_x,
							tile_y, zoomLevel);
					int tms_tile_x = tmsTiles[0];
					int tms_tile_y = tmsTiles[1];
					int tms_tile_z = zoomLevel;

					/**
					 * �����mbtiles�⣬��ôxyz����
					 */

					int[] mbTiles = Utilities.googleTile2TmsTile(tile_x,
							tile_y, zoomLevel);
					int mb_tile_x = tmsTiles[0];
					int mb_tile_y = tmsTiles[1];
					int mb_tile_z = zoomLevel;

					String query = tile_z + "," + tile_x + "," + tile_y;
					byte[] b = spatialDatabaseHandler.getRasterTile(query);

					Bitmap currentMapTile = null;
					try {
						if (b != null)
							currentMapTile = BitmapFactory.decodeByteArray(b,
									0, b.length);
					} catch (Exception e) {

					} finally {
						b = null;
					}

					if (currentMapTile != null) {
						final int tileLeft = 0 + centerMapTileScreenLeft
								+ (x * tileSize);
						final int tileTop = 0 + centerMapTileScreenTop
								+ (y * tileSize);
						final Rect r = new Rect(tileLeft, tileTop, tileLeft
								+ tileSize, tileTop + tileSize);
						canvas.drawBitmap(currentMapTile, null, r,
								this.imagePaint);
						currentMapTile.recycle();
					}
				}
			}
		} catch (Exception e) {

		}
	}

	private void drawWayPathOnCanvas(Canvas canvas, OverlayWay overlayWay) {

	}

	private void drawGpsWayPathOnCanvas(Canvas canvas, OverlayWay overlayWay) {

	}

	@Override
	protected String getThreadName() {
		return "";
	}

	protected final void populate() {
		super.requestRedraw();
	}

	protected abstract OverlayItem createItem(int index);

	protected boolean checkItemHit(GeoPoint geoPoint, MapView mapView,
			EventType eventType) {
		Projection projection = mapView.getProjection();
		Point eventPosition = projection.toPixels(geoPoint, null);
		Context context = mapView.getContext();
		// check if the translation to pixel coordinates has failed
		if (eventPosition == null) {
			return false;
		}

		Point checkItemPoint = new Point();

		synchronized (this.visibleItems) {
			// iterate over all visible items
			for (int i = this.visibleItems.size() - 1; i >= 0; --i) {
				Integer itemIndex = this.visibleItems.get(i);

				// get the current item
				OverlayItem checkOverlayItem = createItem(itemIndex.intValue());
				if (checkOverlayItem == null) {
					continue;
				}

				synchronized (checkOverlayItem) {
					// make sure that the current item has a position
					if (checkOverlayItem.getPoint() == null) {
						continue;
					}

					checkItemPoint = projection.toPixels(
							checkOverlayItem.getPoint(), checkItemPoint);
					// check if the translation to pixel coordinates has failed
					if (checkItemPoint == null) {
						continue;
					}

					// select the correct marker for the item and get the
					// position
					Rect checkMarkerBounds;
					if (checkOverlayItem.getMarker() == null) {
						if (this.itemDefaultMarker == null) {
							// no marker to draw the item
							continue;
						}
						checkMarkerBounds = this.itemDefaultMarker.getBounds();
					} else {
						checkMarkerBounds = checkOverlayItem.getMarker()
								.getBounds();
					}

					// calculate the bounding box of the marker
					int checkLeft = checkItemPoint.x + checkMarkerBounds.left;
					int checkRight = checkItemPoint.x + checkMarkerBounds.right;
					int checkTop = checkItemPoint.y + checkMarkerBounds.top;
					int checkBottom = checkItemPoint.y
							+ checkMarkerBounds.bottom;

					// check if the event position is within the bounds of the
					// marker
					if (checkRight >= eventPosition.x
							&& checkLeft <= eventPosition.x
							&& checkBottom >= eventPosition.y
							&& checkTop <= eventPosition.y) {
						switch (eventType) {
						case LONG_PRESS:
							if (onLongPress(itemIndex.intValue())) {
								return true;
							}
							break;

						case TAP:
							if (onTap(context, itemIndex.intValue())) {
								return true;
							}
							break;
						}
					}
				}
			}
		}

		// no hit
		return false;
	}

	protected boolean onLongPress(int index) {
		return false;
	}

	protected boolean onTap(Context context, int index) {

		return false;
	}

	@Override
	public void dispose() {

		super.dispose();
	}

	private Point getUpperLeftCornerOfCenterMapTileInScreen(
			final int[] centerMapTileCoords, final int tileSizePx,
			final Point reuse) {
		final Point out = (reuse != null) ? reuse : new Point();

		final int viewWidth = mapView.getWidth();
		final int viewWidth_2 = viewWidth / 2;
		final int viewHeight = mapView.getHeight();
		final int viewHeight_2 = viewHeight / 2;

		final BoundingBoxE6 bb = MapUtil.getBoundingBoxFromMapTile(
				centerMapTileCoords, zoomLevel, 1);
		final float[] relativePositionInCenterMapTile = bb
				.getRelativePositionOfGeoPointInBoundingBoxWithLinearInterpolation(
						mapView.getMapPosition().getMapCenter().latitudeE6,
						mapView.getMapPosition().getMapCenter().longitudeE6,
						null);

		final int centerMapTileScreenLeft = viewWidth_2
				- (int) (0.5f + (relativePositionInCenterMapTile[1] * tileSizePx));
		final int centerMapTileScreenTop = viewHeight_2
				- (int) (0.5f + (relativePositionInCenterMapTile[0] * tileSizePx));
		out.set(centerMapTileScreenLeft, centerMapTileScreenTop);
		return out;
	}

	private Paint getPolygonFillPaint(UbilocSymbol symbol) {
		Paint fill = new Paint();
		// ������ɫ��Ĭ����Ϊ��ɫ
		int color = Color.BLUE;
		try {
			color = Color.parseColor(symbol.getFillColor());
		} catch (Exception e) {

		}
		fill.setColor(color);

		int alpha = 0;
		try {
			alpha = Integer.parseInt(symbol.getFillAlpha());
		} catch (Exception e) {

		}
		fill.setAlpha(alpha);

		return fill;
	}

	private Paint getPolygonStrokePaint(UbilocSymbol symbol) {
		Paint stroke = new Paint();
		// ��ȡ�߿�
		float width = 5;
		try {
			width = Float.parseFloat(symbol.getOutLineWidth());
		} catch (Exception e) {

		}
		stroke.setStrokeWidth(10);

		// ��ȡ͸����
		int alpha = 0;
		try {
			alpha = Integer.parseInt(symbol.getOutLineAlpha());
		} catch (Exception e) {

		}
		stroke.setAlpha(alpha);

		// ��ȡ��ɫ
		int color = Color.BLUE;
		try {
			color = Color.parseColor(symbol.getOutLineColor());
		} catch (Exception e) {

		}
		stroke.setColor(color);
		// ��ʸ���߽�����
		// PathEffect effect = new DashPathEffect(new float[] {
		// 1, 2, 4, 8
		// }, 1);
		// stroke.setAntiAlias(true);
		// stroke.setPathEffect(effect);
		return stroke;
	}

	private Paint getLineFillPaint(UbilocSymbol symbol) {
		Paint fill = new Paint();

		// ��ȡ��ɫ
		int color = Color.BLUE;
		try {
			color = Color.parseColor(symbol.getLineColor());
		} catch (Exception e) {

		}
		fill.setColor(color);

		// ��ȡalpha
		int alpha = 0;
		try {
			alpha = Integer.parseInt(symbol.getAlpha());
		} catch (Exception e) {

		}
		fill.setAlpha(alpha);

		// ��ȡ�߿�
		float width = 5;
		try {
			width = Float.parseFloat(symbol.getLineWidth());
		} catch (Exception e) {

		}
		fill.setStrokeWidth(width);

		return fill;
	}

	private Paint getPointFillPaint(UbilocSymbol symbol) {
		Paint fill = new Paint();
		int color = Color.BLUE;
		try {
			color = Color.parseColor(symbol.getColor());
		} catch (Exception e) {

		}
		fill.setColor(color);
		fill.setAlpha(190);
		fill.setAntiAlias(true);
		return fill;
	}

	private String getPointShape(UbilocSymbol symbol) {
		String shape = "";
		shape = "POP";
		return shape;
	}

	private int getPointSize(UbilocSymbol symbol) {
		int size = 5;
		try {
			size = Integer.parseInt(symbol.getSize());
		} catch (Exception e) {

		}
		return size * 2;
	}

	private void drawGpsOnCanvas(Canvas canvas, GpsData gpsCircle) {
		canvas.drawPath(this.path, gpsOutline);
		canvas.drawPath(this.path, gpsFill);
	}

	@SuppressWarnings("nls")
	public void setGpsPosition(GeoPoint position, float accuracy) {
		this.setGpsPosition(position);
		this.setGpsAccuracy(accuracy);
		GpsManager gpsManager = GpsManager.getInstance(context);
		if (gpsManager.isDatabaseLogging()) {
			currentGpsLog.add(position);
		} else {
			currentGpsLog.clear();
		}
		if (GPLog.LOG_ABSURD)
			GPLog.addLogEntry(this, "Set gps data: " + position.getLongitude()
					+ "/" + position.getLatitude() + "/" + accuracy);
		overlayGps.setCircleData(position, accuracy);
	}

	public BaseOverlay setCoords(List<GeoPoint> coords) {
		this.coords = coords;
		return this;
	}

	public void clear() {
		// style_Map = null;
		// table_Map = null;
		// where_Map = null;
		dbABSPaths = null;
		my_spatialTables = null;

		mapView = null;
		// ����ʮ�ּ�
		crossPath = null;
		crossPaint = null;

		visibleItems = null;
		visibleItemsRedraw = null;
		itemPosition = null;
		itemMarker = null;
		itemDefaultMarker = null;
		context = null;
		sdManager = null;
		overlayGps = null;
		gpslogOverlay = null;
		textPaint = null;
		textHaloPaint = null;
		imagePaint = null;

		/*
		 * gps stuff
		 */
		circlePosition = null;
		path = null;

		gpsMarker = null;

		gpsPath = null;
		gpsTrackPaintYellow = null;
		gpsTrackPaintBlack = null;
		gpsOutline = null;
		gpsFill = null;

		gpsStatusPath = null;
		gpsRedFill = null;
		gpsOrangeFill = null;
		gpsGreenFill = null;
		gpsBlueFill = null;
		currentGpsLog = null;
		// mapactivity = null;

		// ========================================================
		// ������γ��
		GpsPosition = null;
	}

	public HashMap<String, BaseOverlayItem> getmOverlayItems() {
		return mOverlayItems;
	}

	/**
	 * 添加一个图层信息
	 * 
	 * @param overlayItem
	 */
	public void addOverlayItem(BaseOverlayItem overlayItem) {
		mOverlayItems.put(overlayItem.getKey(), overlayItem);
	}

	/**
	 * 移除一个图层信息
	 * 
	 * @param overLayItemKey
	 */
	public void removeOverlayItem(BaseOverlayItem overlayItem) {
		mOverlayItems.remove(overlayItem.getKey());
	}

	/**
	 * 移除一个图层信息
	 * 
	 * @param overLayItemKey
	 */
	public void removeAllOverlayItems() {
		mOverlayItems.clear();
	}
}