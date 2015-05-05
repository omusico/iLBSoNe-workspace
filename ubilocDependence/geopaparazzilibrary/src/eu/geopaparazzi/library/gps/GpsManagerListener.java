/*
 * Geopaparazzi - Digital field mapping on Android based devices
 * Copyright (C) 2010  HydroloGIS (www.hydrologis.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.geopaparazzi.library.gps;

import android.location.GpsStatus;
import android.location.LocationListener;

/**
 * Listener for GPS.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 */
public interface GpsManagerListener extends LocationListener {

    public void gpsStart();

    public void gpsStop();

    public void onGpsStatusChanged( int event, GpsStatus status );

    /**
     * Check on available fix and data.
     * 
     * @return <code>true</code> if fix is available and data are valid.
     */
    public boolean hasFix();
}