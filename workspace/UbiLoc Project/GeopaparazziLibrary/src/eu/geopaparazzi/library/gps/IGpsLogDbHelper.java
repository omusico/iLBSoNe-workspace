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

import java.io.IOException;
import java.sql.Date;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Interface that helps making the {@link GpsDatabaseLogger} add point to external databases. 
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 */
public interface IGpsLogDbHelper {

    public SQLiteDatabase getDatabase( Context context ) throws Exception;

    public long addGpsLog( Context context, Date startTs, Date endTs, String text, float width, String color, boolean visible )
            throws IOException;

    public void addGpsLogDataPoint( SQLiteDatabase sqliteDatabase, long gpslogId, double lon, double lat, double altim,
            Date timestamp ) throws IOException;

    public void deleteGpslog( Context context, long id ) throws IOException;

    public void setEndTs( Context context, long logid, Date end ) throws IOException;
}
