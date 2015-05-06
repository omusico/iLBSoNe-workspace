// Created by plusminus on 19:06:38 - 25.09.2008
package com.ubiloc.map.maputils;


import java.util.ArrayList;


/**
 * 
 * @author Nicolas Gramlich
 *
 */
public class BoundingBoxE6 {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final int mLatNorthE6;
	protected final int mLatSouthE6;
	protected final int mLonEastE6;
	protected final int mLonWestE6;  

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public BoundingBoxE6(final int northE6, final int eastE6, final int southE6, final int westE6){
		this.mLatNorthE6 = northE6;
		this.mLatSouthE6 = southE6;
		this.mLonWestE6 = westE6;
		this.mLonEastE6 = eastE6;
	}
	
	public BoundingBoxE6(final double north, final double east, final double south, final double west){
		this.mLatNorthE6 = (int)(north * 1E6);
		this.mLatSouthE6 = (int)(south * 1E6);
		this.mLonWestE6 = (int)(west * 1E6);
		this.mLonEastE6 = (int)(east * 1E6);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	
	
	public int getLatNorthE6() {
		return this.mLatNorthE6;
	}
	
	public int getLatSouthE6() {
		return this.mLatSouthE6;
	}
	
	public int getLonEastE6() {
		return this.mLonEastE6;
	}
	
	public int getLonWestE6() {
		return this.mLonWestE6;
	}

	public int getLatitudeSpanE6() {
		return Math.abs(this.mLatNorthE6 - this.mLatSouthE6);
	}
	
	public int getLongitudeSpanE6() {
		return Math.abs(this.mLonEastE6 - this.mLonWestE6);
	}
	/**
	 * 
	 * @param aLatitude
	 * @param aLongitude
	 * @param reuse
	 * @return relative position determined from the upper left corner.<br />
	 * {0,0} would be the upper left corner.
	 * {1,1} would be the lower right corner.
	 * {1,0} would be the lower left corner.
	 * {0,1} would be the upper right corner. 
	 */
	public float[] getRelativePositionOfGeoPointInBoundingBoxWithLinearInterpolation(final int aLatitude, final int aLongitude, final float[] reuse){
		float[] out = (reuse != null) ? reuse : new float[2];
		out[0] = ((float)(this.mLatNorthE6 - aLatitude) / getLatitudeSpanE6());
		out[1] = 1 - ((float)(this.mLonEastE6 - aLongitude) / getLongitudeSpanE6());
		return out;
	}
	
	
	
	
	@Override
	public String toString(){
		return new StringBuffer()
			.append("N:").append(this.mLatNorthE6)
			.append("; E:").append(this.mLonEastE6)
			.append("; S:").append(this.mLatSouthE6)
			.append("; W:").append(this.mLonWestE6)
			.toString();
	}



	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}

