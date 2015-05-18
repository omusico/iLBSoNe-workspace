// Created by plusminus on 17:53:07 - 25.09.2008
package com.ubiloc.map.maputils;

/**
 * 
 * @author Nicolas Gramlich
 * 
 */
public class MapUtil {
	 public static String SdcardPath="storage/extSdCard/21at/";
		public static BoundingBoxE6 getBoundingBoxFromMapTile(final int[] aMapTile,
			final int zoom, final int aProjection) {
		final int y = aMapTile[0];
		final int x = aMapTile[1];
		return new BoundingBoxE6(tile2lat(y, zoom, aProjection), tile2lon(
				x + 1, zoom), tile2lat(y + 1, zoom, aProjection), tile2lon(x,
				zoom));
	}
	public static int[] getMapTileFromCoordinates(final int aLat, final int aLon, final int zoom, final int[] reuse) {
		return getMapTileFromCoordinates(aLat / 1E6, aLon / 1E6, zoom, reuse);
	}
	
	

	public static int[] getMapTileFromCoordinates(final double aLat, final double aLon, final int zoom, final int[] aUseAsReturnValue) {
		final int[] out = (aUseAsReturnValue != null) ? aUseAsReturnValue : new int[2];

		
		
				out[0] = (int) Math.floor((1 - Math
						.log(Math.tan(aLat * Math.PI / 180) + 1
								/ Math.cos(aLat * Math.PI / 180))
						/ Math.PI)
						/ 2 * (1 << zoom));
			

			out[1] = (int) Math.floor((aLon + 180) / 360
					* (1 << zoom));
	

		return out;
	}
	
	private static double tile2lon(int x, int aZoom) {
		return (x / Math.pow(2.0, aZoom) * 360.0) - 180;
	}

	private static double tile2lat(int y, int aZoom, final int aProjection) {

		if (aProjection == 1) {
			final double n = Math.PI
					- ((2.0 * Math.PI * y) / Math.pow(2.0, aZoom));
			return 180.0 / Math.PI
					* Math.atan(0.5 * (Math.exp(n) - Math.exp(-n)));
		} else {
			final double MerkElipsK = 0.0000001;
			final long sradiusa = 6378137;
			final long sradiusb = 6356752;
			final double FExct = (double) Math.sqrt(sradiusa * sradiusa
					- sradiusb * sradiusb)
					/ sradiusa;
			final int TilesAtZoom = 1 << aZoom;
			double result = (y - TilesAtZoom / 2)
					/ -(TilesAtZoom / (2 * Math.PI));
			result = (2 * Math.atan(Math.exp(result)) - Math.PI / 2) * 180
					/ Math.PI;
			double Zu = result / (180 / Math.PI);
			double yy = ((y) - TilesAtZoom / 2);

			double Zum1 = Zu;
			Zu = Math
					.asin(1
							- ((1 + Math.sin(Zum1)) * Math.pow(
									1 - FExct * Math.sin(Zum1), FExct))
							/ (Math.exp((2 * yy)
									/ -(TilesAtZoom / (2 * Math.PI))) * Math
										.pow(1 + FExct * Math.sin(Zum1), FExct)));
			while (Math.abs(Zum1 - Zu) >= MerkElipsK) {
				Zum1 = Zu;
				Zu = Math
						.asin(1
								- ((1 + Math.sin(Zum1)) * Math.pow(1 - FExct
										* Math.sin(Zum1), FExct))
								/ (Math.exp((2 * yy)
										/ -(TilesAtZoom / (2 * Math.PI))) * Math
											.pow(1 + FExct * Math.sin(Zum1),
													FExct)));
			}

			result = Zu * 180 / Math.PI;

			return result;
		}
	}

	public static int x2lon(int x, int aZoom, final int MAPTILE_SIZEPX) {
		int px = MAPTILE_SIZEPX * (1 << aZoom);
		if (x < 0)
			x = px + x;
		if (x > px)
			x = x - px;
		return (int) (1E6 * (((double) x / px * 360.0) - 180));
	}

	public static double y2lat(int y, int aZoom, final int MAPTILE_SIZEPX) {
		// final int aProjection = 1;

		// if (aProjection == 1) {
		final double n = Math.PI
				- ((2.0 * Math.PI * y) / MAPTILE_SIZEPX * Math.pow(2.0, aZoom));
		return 180.0 / Math.PI * Math.atan(0.5 * (Math.exp(n) - Math.exp(-n)));
		// } else {
		// final double MerkElipsK = 0.0000001;
		// final long sradiusa = 6378137;
		// final long sradiusb = 6356752;
		// final double FExct = (double) Math.sqrt(sradiusa * sradiusa
		// - sradiusb * sradiusb)
		// / sradiusa;
		// final int TilesAtZoom = 1 << aZoom;
		// double result = (y - TilesAtZoom / 2)
		// / -(TilesAtZoom / (2 * Math.PI));
		// result = (2 * Math.atan(Math.exp(result)) - Math.PI / 2) * 180
		// / Math.PI;
		// double Zu = result / (180 / Math.PI);
		// double yy = ((y) - TilesAtZoom / 2);
		//
		// double Zum1 = Zu;
		// Zu = Math
		// .asin(1
		// - ((1 + Math.sin(Zum1)) * Math.pow(1 - FExct
		// * Math.sin(Zum1), FExct))
		// / (Math.exp((2 * yy)
		// / -(TilesAtZoom / (2 * Math.PI))) * Math
		// .pow(1 + FExct * Math.sin(Zum1), FExct)));
		// while (Math.abs(Zum1 - Zu) >= MerkElipsK) {
		// Zum1 = Zu;
		// Zu = Math
		// .asin(1
		// - ((1 + Math.sin(Zum1)) * Math.pow(1 - FExct
		// * Math.sin(Zum1), FExct))
		// / (Math.exp((2 * yy)
		// / -(TilesAtZoom / (2 * Math.PI))) * Math
		// .pow(1 + FExct * Math.sin(Zum1), FExct)));
		// }
		//
		// result = Zu * 180 / Math.PI;
		//
		// return result;
		// }
	} 

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
