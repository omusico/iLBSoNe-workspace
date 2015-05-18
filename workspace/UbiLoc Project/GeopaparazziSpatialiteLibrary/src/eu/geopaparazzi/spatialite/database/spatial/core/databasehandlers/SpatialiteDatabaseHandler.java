/*
* Geopaparazzi - Digital field mapping on Android based devices
* Copyright (C) 2010 HydroloGIS (www.hydrologis.com)
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package eu.geopaparazzi.spatialite.database.spatial.core.databasehandlers;

import static eu.geopaparazzi.spatialite.database.spatial.core.daos.GeopaparazziDatabaseProperties.createDefaultPropertiesForTable;
import static eu.geopaparazzi.spatialite.database.spatial.core.daos.GeopaparazziDatabaseProperties.createPropertiesTable;
import static eu.geopaparazzi.spatialite.database.spatial.core.daos.GeopaparazziDatabaseProperties.deleteStyleTable;
import static eu.geopaparazzi.spatialite.database.spatial.core.daos.GeopaparazziDatabaseProperties.getAllStyles;
import static eu.geopaparazzi.spatialite.database.spatial.core.daos.GeopaparazziDatabaseProperties.updateStyleName;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

import eu.geopaparazzi.library.GPApplication;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.util.ResourcesManager;
import eu.geopaparazzi.spatialite.database.spatial.core.daos.DaoSpatialite;
import eu.geopaparazzi.spatialite.database.spatial.core.daos.DatabaseCreationAndProperties;
import eu.geopaparazzi.spatialite.database.spatial.core.enums.GeometryType;
import eu.geopaparazzi.spatialite.database.spatial.core.enums.SpatialiteDatabaseType;
import eu.geopaparazzi.spatialite.database.spatial.core.enums.TableTypes;
import eu.geopaparazzi.spatialite.database.spatial.core.geometry.GeometryIterator;
import eu.geopaparazzi.spatialite.database.spatial.core.tables.AbstractSpatialTable;
import eu.geopaparazzi.spatialite.database.spatial.core.tables.SpatialRasterTable;
import eu.geopaparazzi.spatialite.database.spatial.core.tables.SpatialVectorTable;
import eu.geopaparazzi.spatialite.database.spatial.util.SpatialiteUtilities;
import eu.geopaparazzi.spatialite.database.spatial.util.Style;
import eu.geopaparazzi.spatialite.database.spatial.util.comparators.OrderComparator;

/**
 * An utility class to handle the spatial database.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
@SuppressWarnings("nls")
public class SpatialiteDatabaseHandler extends AbstractSpatialDatabaseHandler {

    private static final String METADATA_TABLE_GEOPACKAGE_CONTENTS = "geopackage_contents";
    private static final String METADATA_TABLE_TILE_MATRIX = "tile_matrix_metadata";
    private static final String METADATA_TABLE_RASTER_COLUMNS = "raster_columns";
    private static final String METADATA_TABLE_GEOMETRY_COLUMNS = "geometry_columns";

    private static final String METADATA_GEOPACKAGECONTENT_TABLE_NAME = "table_name";
    private static final String METADATA_TILE_TABLE_NAME = "t_table_name";
    private static final String METADATA_ZOOM_LEVEL = "zoom_level";
    private static final String METADATA_RASTER_COLUMN = "r_raster_column";
    private static final String METADATA_RASTER_TABLE_NAME = "r_table_name";
    private static final String METADATA_SRID = "srid";
    private static final String METADATA_GEOMETRY_TYPE4 = "geometry_type";
    private static final String METADATA_GEOMETRY_TYPE3 = "type";
    private static final String METADATA_GEOMETRY_COLUMN = "f_geometry_column";
    private static final String METADATA_TABLE_NAME = "f_table_name";

    private static final String NAME = "name";
    private static final String SIZE = "size";
    private static final String FILLCOLOR = "fillcolor";
    private static final String STROKECOLOR = "strokecolor";
    private static final String FILLALPHA = "fillalpha";
    private static final String STROKEALPHA = "strokealpha";
    private static final String SHAPE = "shape";
    private static final String WIDTH = "width";
    private static final String TEXTSIZE = "textsize";
    private static final String TEXTFIELD = "textfield";
    private static final String ENABLED = "enabled";
    private static final String ORDER = "layerorder";
    private static final String DECIMATION = "decimationfactor";

    private final String PROPERTIESTABLE = "dataproperties";
    private String dbPath;

    private String fileName;

    private String uniqueDbName4DataProperties = "";

    private Database dbJava;

    private HashMap<String, Paint> fillPaints = new HashMap<String, Paint>();
    private HashMap<String, Paint> strokePaints = new HashMap<String, Paint>();

    private List<SpatialVectorTable> vectorTableList;
    private List<SpatialRasterTable> rasterTableList;

    private SpatialiteDatabaseType databaseType = null;

    // List of all SpatialView of Database [view_name,view_data] - parse for
    // 'geometry_column;min_x,min_y,max_x,max_y'
    private HashMap<String, String> spatialVectorMap = new HashMap<String, String>();
    // List of all SpatialView of Database [view_name,view_data] - that have errors
    private HashMap<String, String> spatialVectorMapErrors = new HashMap<String, String>();

    public void exec( String sql ) throws Exception {
        this.dbJava.exec(sql, null);
    }

    /**
     * Constructor.
     *
     * @param dbPath the path to the database this handler connects to.
     * @throws IOException if something goes wrong.
     */
    public SpatialiteDatabaseHandler( String dbPath ) throws IOException {
        super(dbPath);
        this.dbPath = dbPath;
        try {
            try {
                Context context = GPApplication.getInstance();
                ResourcesManager resourcesManager = ResourcesManager.getInstance(context);
                File mapsDir = resourcesManager.getMapsDir();
                String mapsPath = mapsDir.getAbsolutePath();
                if (databasePath.startsWith(mapsPath)) {
                    // this should always be true
                    String relativePath = databasePath.substring(mapsPath.length());
                    StringBuilder sb = new StringBuilder();
                    if (relativePath.startsWith(File.separator)) {
                        relativePath = relativePath.substring(1);
                    }
                    sb.append(relativePath);
                    uniqueDbName4DataProperties = sb.toString();
                }
            } catch (java.lang.Exception e) {
                GPLog.androidLog(4, "SpatialiteDatabaseHandler[" + databaseFile.getAbsolutePath() + "]", e);
            }
            dbJava = new jsqlite.Database();
            try {
                dbJava.open(databasePath, jsqlite.Constants.SQLITE_OPEN_READWRITE | jsqlite.Constants.SQLITE_OPEN_CREATE);
                isDatabaseValid = true;
            } catch (Exception e) {
                GPLog.error(this, "Database marked as invalid: " + databasePath, e);
                isDatabaseValid = false;
                GPLog.androidLog(4, "SpatialiteDatabaseHandler[" + databaseFile.getAbsolutePath() + "].open has failed", e);
            }
            if (isValid()) {
                // check database and collect the views list
                try {
                    databaseType = DatabaseCreationAndProperties.checkDatabaseTypeAndValidity(dbJava, spatialVectorMap,
                            spatialVectorMapErrors);
                } catch (Exception e) {
                    isDatabaseValid = false;
                }
                switch( databaseType ) {
                /*
                  if (spatialVectorMap.size() == 0) for SPATIALITE3/4
                   --> DaoSpatialite.checkDatabaseTypeAndValidity will return SpatialiteDatabaseType.UNKNOWN
                   -- there is nothing to load (database empty)
                */
                case GEOPACKAGE:
                case SPATIALITE3:
                case SPATIALITE4:
                    isDatabaseValid = true;
                    break;
                default:
                    isDatabaseValid = false;
                }
            }
            if (!isValid()) {
                close();
            } else { // avoid call for invalid databases [SpatialiteDatabaseType.UNKNOWN]
                checkAndUpdatePropertiesUniqueNames();
            }
        } catch (Exception e) {
            GPLog.androidLog(4, "SpatialiteDatabaseHandler[" + databaseFile.getAbsolutePath() + "]", e);
        }
    }

    @Override
    public void open() {
    }

    /**
     * Is the database file considered valid?
     * <p/>
     * <br>- metadata table exists and has data
     * <br>- 'tiles' is either a table or a view and the correct fields exist
     * <br>-- if a view: do the tables map and images exist with the correct fields
     * <br>checking is done once when the 'metadata' is retrieved the first time [fetchMetadata()]
     *
     * @return true if valid, otherwise false
     */
    @Override
    public boolean isValid() {
        // return isDatabaseValid;
        return true;
    }

    /**
     * Checks if the table names in the properties table are defined properly.
     * <p/>
     * <p>The unique table name is a concatenation of:<br>
     * <b>dbPath#tablename#geometrytype</b>
     * <p>If the name doesn't start with the database path, it needs to
     * be updated. The rest is anyways unique inside the database.
     *
     * @throws Exception if something went wrong.
     */
    private void checkAndUpdatePropertiesUniqueNames() throws Exception {
        List<Style> allStyles = null;
        try {
            allStyles = getAllStyles(dbJava);
        } catch (java.lang.Exception e) {
            // ignore and create a default one
        }
        if (allStyles == null) {
            /*
            * something went wrong in the reading of the table,
            * which might be due to an upgrade of table structure.
            * Remove and recreate the table.
            */
            deleteStyleTable(dbJava);
            createPropertiesTable(dbJava);
        } else {
            for( Style style : allStyles ) {
                if (!style.name.startsWith(uniqueDbName4DataProperties + SpatialiteUtilities.UNIQUENAME_SEPARATOR)) {
                    // need to update the name in the style and also in the database
                    String[] split = style.name.split(SpatialiteUtilities.UNIQUENAME_SEPARATOR);
                    if (split.length == 3) {
                        String newName = uniqueDbName4DataProperties + SpatialiteUtilities.UNIQUENAME_SEPARATOR + split[1]
                                + SpatialiteUtilities.UNIQUENAME_SEPARATOR + split[2];
                        style.name = newName;
                        updateStyleName(dbJava, newName, style.id);
                    }
                }
            }
        }
    }

    public float[] getTableBounds( AbstractSpatialTable spatialTable ) throws Exception {
        return spatialTable.getTableBounds();
    }

    /**
     * Retrieve list of WKB geometries from the given table in the given bounds.
     *
     * @param destSrid the destination srid.
     * @param table    the vector table.
     * @param n        north bound.
     * @param s        south bound.
     * @param e        east bound.
     * @param w        west bound.
     * @return list of WKB geometries.
     */
    public List<byte[]> getWKBFromTableInBounds( String destSrid, SpatialVectorTable table, double n, double s, double e, double w ) {
        List<byte[]> list = new ArrayList<byte[]>();
        String query = SpatialiteUtilities.buildGeometriesInBoundsQuery(destSrid, false, table, n, s, e, w);
        try {
            Stmt stmt = dbJava.prepare(query);
            try {
                while( stmt.step() ) {
                    list.add(stmt.column_bytes(0));
                }
            } finally {
                stmt.close();
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Get the {@link GeometryIterator} of a table in a given bound.
     *
     * @param destSrid the srid to which to transform to.
     * @param table    the table to use.
     * @param n        north bound.
     * @param s        south bound.
     * @param e        east bound.
     * @param w        west bound.
     * @return the geometries iterator.
     */

    /**
     * Performs an intersection query on a vector table and returns a string info version of the result.
     *
     * @param boundsSrid          the srid of the bounds supplied.
     * @param spatialTable        the vector table to query.
     * @param n                   north bound.
     * @param s                   south bound.
     * @param e                   east bound.
     * @param w                   west bound.
     * @param resultStringBuilder the builder of the result.
     * @param indentStr           the indenting to use for formatting.
     * @throws Exception if something goes wrong.
     */

    /**
     * Get the query to run for a bounding box intersection.
     * <p/>
     * <p>This assures that the first element of the query is
     * the id field for the record as defined in {@link SpatialiteUtilities#SPATIALTABLE_ID_FIELD}.
     *
     * @param boundsSrid   the srid of the bounds requested.
     * @param spatialTable the {@link SpatialVectorTable} to query.
     * @param n            north bound.
     * @param s            south bound.
     * @param e            east bound.
     * @param w            west bound.
     * @return the query to run to get all fields.
     */
    public static String getIntersectionQueryBBOX( String boundsSrid, SpatialVectorTable spatialTable, double n, double s,
            double e, double w ) {
        boolean doTransform = false;
        String fieldNamesList = SpatialiteUtilities.SPATIALTABLE_ID_FIELD;
        // List of non-blob fields
        for( String field : spatialTable.getTableFieldNamesList() ) {
            boolean ignore = SpatialiteUtilities.doIgnoreField(field);
            if (!ignore)
                fieldNamesList += "," + field;
        }
        if (!spatialTable.getSrid().equals(boundsSrid)) {
            doTransform = true;
        }
        StringBuilder sbQ = new StringBuilder();
        sbQ.append("SELECT ");
        sbQ.append(fieldNamesList);
        sbQ.append(" FROM ").append(spatialTable.getTableName());
        sbQ.append(" WHERE ST_Intersects(");
        if (doTransform)
            sbQ.append("ST_Transform(");
        sbQ.append("BuildMBR(");
        sbQ.append(w);
        sbQ.append(",");
        sbQ.append(s);
        sbQ.append(",");
        sbQ.append(e);
        sbQ.append(",");
        sbQ.append(n);
        if (doTransform) {
            sbQ.append(",");
            sbQ.append(boundsSrid);
            sbQ.append("),");
            sbQ.append(spatialTable.getSrid());
        }
        sbQ.append("),");
        sbQ.append(spatialTable.getGeomName());
        sbQ.append(");");

        return sbQ.toString();
    }

    // public void intersectionToString4Polygon( String queryPointSrid, SpatialVectorTable
    // spatialTable, double n, double e,
    // StringBuilder sb, String indentStr ) throws Exception {
    // boolean doTransform = false;
    // if (!spatialTable.getSrid().equals(queryPointSrid)) {
    // doTransform = true;
    // }
    //
    // StringBuilder sbQ = new StringBuilder();
    // sbQ.append("SELECT * FROM ");
    // sbQ.append(spatialTable.getName());
    // sbQ.append(" WHERE ST_Intersects(");
    // sbQ.append(spatialTable.getGeomName());
    // sbQ.append(",");
    // if (doTransform)
    // sbQ.append("ST_Transform(");
    // sbQ.append("MakePoint(");
    // sbQ.append(e);
    // sbQ.append(",");
    // sbQ.append(n);
    // if (doTransform) {
    // sbQ.append(",");
    // sbQ.append(queryPointSrid);
    // sbQ.append("),");
    // sbQ.append(spatialTable.getSrid());
    // }
    // sbQ.append(")) = 1 ");
    // sbQ.append("AND ROWID IN (");
    // sbQ.append("SELECT ROWID FROM Spatialindex WHERE f_table_name ='");
    // sbQ.append(spatialTable.getName());
    // sbQ.append("'");
    // // if a table has more than 1 geometry, the column-name MUST be given, otherwise no results.
    // sbQ.append(" AND f_geometry_column = '");
    // sbQ.append(spatialTable.getGeomName());
    // sbQ.append("'");
    // sbQ.append(" AND search_frame = ");
    // if (doTransform)
    // sbQ.append("ST_Transform(");
    // sbQ.append("MakePoint(");
    // sbQ.append(e);
    // sbQ.append(",");
    // sbQ.append(n);
    // if (doTransform) {
    // sbQ.append(",");
    // sbQ.append(queryPointSrid);
    // sbQ.append("),");
    // sbQ.append(spatialTable.getSrid());
    // }
    // sbQ.append("));");
    // String query = sbQ.toString();
    //
    // Stmt stmt = db_java.prepare(query);
    // try {
    // while( stmt.step() ) {
    // int column_count = stmt.column_count();
    // for( int i = 0; i < column_count; i++ ) {
    // String cName = stmt.column_name(i);
    // if (cName.equalsIgnoreCase(spatialTable.getGeomName())) {
    // continue;
    // }
    //
    // String value = stmt.column_string(i);
    // sb.append(indentStr).append(cName).append(": ").append(value).append("\n");
    // }
    // sb.append("\n");
    // }
    // } finally {
    // stmt.close();
    // }
    // }

    /**
     * Load list of Table [Vector/Raster] for GeoPackage Files [gpkg]
     * <p/>
     * <b>THIS METHOD IS VERY EXPERIMENTAL AND A WORK IN PROGRESS</b>
     * - rasterTableList or vectorTableList will be created if == null
     * <br>- name of Field
     * <br> - type of field as defined in Database
     * <br>- OGC 12-128r9 from 2013-11-19
     * <br>-- older versions will not be supported
     * <br>- With SQLite versions 3.7.17 and later : 'PRAGMA application_id' [1196437808]
     * <br>-- older (for us invalid) SPL_Geopackage Files return 0
     */
    private void collectGpkgTables() throws Exception {
        String vector_key = ""; // term used when building the sql, used as map.key
        String vector_value = ""; // to retrieve map.value (=vector_data+vector_extent)
        for( Map.Entry<String, String> vector_entry : spatialVectorMap.entrySet() ) {
            // berlin_stadtteile
            vector_key = vector_entry.getKey();
            // soldner_polygon;14;3;2;3068;1;20847.6171111586,18733.613614603,20847.6171111586,18733.613614603
            vector_value = vector_entry.getValue();
            double[] boundsCoordinates = new double[]{0.0, 0.0, 0.0, 0.0};
            double[] centerCoordinate = new double[]{0.0, 0.0};
            HashMap<String, String> fields_list = new HashMap<String, String>();
            int i_geometry_type = 0;
            int i_view_read_only = 0;
            double horz_resolution = 0.0;
            String s_view_read_only = "";
            String[] sa_string = vector_key.split(";");
            // fromosm_tiles;tile_data;GeoPackage_tiles;漏 OpenStreetMap contributors, See
            // http://www.openstreetmap.org/copyright;OSM Tiles;
            // geonames;geometry;GeoPackage_features;Data from http://www.geonames.org/, under
            // Creative Commons Attribution 3.0 License;Geonames;
            if (sa_string.length == 5) {
                String table_name = sa_string[0]; // fromosm_tiles / geonames
                String geometry_column = sa_string[1]; // tile_data / geometry
                String layerType = sa_string[2]; // GeoPackage_tiles / GeoPackage_features
                String s_identifier = sa_string[3]; // short description
                String s_description = sa_string[4]; // long description
                sa_string = vector_value.split(";");
                // RGB;512;3068;1890 -
                // 1:17777;3;17903.0354299312,17211.5335278146,29889.8601630003,26582.2086184726;2014-05-09T09:18:07.230Z
                if (sa_string.length == 7) {
                    // 0;10;3857;0;
                    // 1;2;4326;0;
                    String s_geometry_type = sa_string[0]; // 1= POINT / OR min_zoom
                    String s_coord_dimension = sa_string[1]; // 2= XY / OR max_zoom
                    String s_srid = sa_string[2]; // 4326
                    String s_spatial_index_enabled = sa_string[3]; // 0
                    // -1;-75.5;18.0;-71.06667;20.08333;2013-12-24T16:32:14.000000Z
                    String s_row_count = sa_string[4]; // 0 = not possible as sub-query - but also
                    // not needed
                    String s_bounds = sa_string[5]; // -75.5;18.0;-71.06667;20.08333
                    String s_last_verified = sa_string[6]; // 2013-12-24T16:32:14.000000Z
                    sa_string = s_bounds.split(",");
                    if (sa_string.length == 4) {
                        try {
                            boundsCoordinates[0] = Double.parseDouble(sa_string[0]);
                            boundsCoordinates[1] = Double.parseDouble(sa_string[1]);
                            boundsCoordinates[2] = Double.parseDouble(sa_string[2]);
                            boundsCoordinates[3] = Double.parseDouble(sa_string[3]);
                        } catch (NumberFormatException e) {
                        }
                        if (!s_srid.equals("4326")) { // Transform into wsg84 if needed
                            SpatialiteUtilities.collectBoundsAndCenter(dbJava, s_srid, centerCoordinate, boundsCoordinates);
                        } else {
                            centerCoordinate[0] = boundsCoordinates[0] + (boundsCoordinates[2] - boundsCoordinates[0]) / 2;
                            centerCoordinate[1] = boundsCoordinates[1] + (boundsCoordinates[3] - boundsCoordinates[1]) / 2;
                        }
                        checkAndAdaptDatabaseBounds(boundsCoordinates, null);
                        if (vector_key.contains("GeoPackage_tiles")) {
                            int i_min_zoom = Integer.parseInt(s_geometry_type);
                            int i_max_zoom = Integer.parseInt(s_coord_dimension);
                            SpatialRasterTable table = new SpatialRasterTable(getDatabasePath(), "", s_srid, i_min_zoom,
                                    i_max_zoom, centerCoordinate[0], centerCoordinate[1], null, boundsCoordinates);
                            table.setMapType(layerType);
                            // table.setTableName(s_table_name);
                            table.setColumnName(geometry_column);
                            // setDescription(s_table_name);
                            // table.setDescription(this.databaseDescription);
                            if (rasterTableList == null)
                                rasterTableList = new ArrayList<SpatialRasterTable>();
                            rasterTableList.add(table);
                        } else {
                            if (vector_key.contains("GeoPackage_features")) {
                                // String table_name=sa_string[0]; // lakemead_clipped
                                // String geometry_column=sa_string[1]; // shape
                                i_view_read_only = 0; // always
                                i_geometry_type = Integer.parseInt(s_geometry_type);
                                GeometryType geometry_type = GeometryType.forValue(i_geometry_type);
                                s_geometry_type = geometry_type.toString();
                                int i_spatial_index_enabled = Integer.parseInt(s_spatial_index_enabled); // 0=no
                                // spatialiIndex
                                // for
                                // GeoPackage
                                // Files
                                int i_row_count = Integer.parseInt(s_row_count); // will always be 0
                                // no Zoom levels with
                                // vector data
                                if (i_spatial_index_enabled == 1) {
                                    SpatialVectorTable table = new SpatialVectorTable(getDatabasePath(), table_name,
                                            geometry_column, i_geometry_type, s_srid, centerCoordinate, boundsCoordinates,
                                            layerType);
                                    // compleate list of fields of
                                    // this table
                                    fields_list = DaoSpatialite.collectTableFields(dbJava, table_name);
                                    table.setFieldsList(fields_list, "ROWID", i_view_read_only);
                                    if (vectorTableList == null)
                                        vectorTableList = new ArrayList<SpatialVectorTable>();
                                    vectorTableList.add(table);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Load list of Table [Vector] for Spatialite4+ Files
     * - for Spaltialite4+ all needed information has been collected in DaoSpatialite.checkDatabaseTypeAndValidity()
     * - rasterTableList or vectorTableList will be created if == null
     * <br>- name of Field
     * <br>- type of field as defined in Database
     */
    private void collectVectorTables() throws Exception {
        String vector_key = ""; // term used when building the sql, used as map.key
        String vector_value = ""; // to retrieve map.value (=vector_data+vector_extent)
        for( Map.Entry<String, String> vector_entry : spatialVectorMap.entrySet() ) {
            // berlin_stadtteile
            vector_key = vector_entry.getKey();
            // soldner_polygon;14;3;2;3068;1;20847.6171111586,18733.613614603,20847.6171111586,18733.613614603
            vector_value = vector_entry.getValue();
            double[] boundsCoordinates = new double[]{0.0, 0.0, 0.0, 0.0};
            double[] centerCoordinate = new double[]{0.0, 0.0};
            HashMap<String, String> fields_list = new HashMap<String, String>();
            int i_geometry_type = 0;
            int i_view_read_only = 0;
            String s_view_read_only = "";
            String[] sa_string = vector_key.split(";");
            // berlin_postgrenzen.1890;LOSSY_WEBP;RasterLite2;Berlin Straube Postgrenzen;1890 -
            // 1:17777;
            if (sa_string.length == 5) {
                String table_name = sa_string[0];
                String geometry_column = sa_string[1];
                String layerType = sa_string[2];
                String s_ROWID_PK = sa_string[3];
                s_view_read_only = sa_string[4];
                sa_string = vector_value.split(";");
                // RGB;512;3068;1.13008623862252;3;17903.0354299312,17211.5335278146,29889.8601630003,26582.2086184726;2014-05-09T09:18:07.230Z
                if (sa_string.length == 7) {
                    String s_geometry_type = sa_string[0];
                    String s_coord_dimension = sa_string[1];
                    String s_srid = sa_string[2];
                    String s_spatial_index_enabled = sa_string[3];
                    String s_row_count_enabled = sa_string[4];
                    String s_bounds = sa_string[5];
                    String s_last_verified = sa_string[6];
                    sa_string = s_bounds.split(",");
                    if (sa_string.length == 4) {
                        try {
                            boundsCoordinates[0] = Double.parseDouble(sa_string[0]);
                            boundsCoordinates[1] = Double.parseDouble(sa_string[1]);
                            boundsCoordinates[2] = Double.parseDouble(sa_string[2]);
                            boundsCoordinates[3] = Double.parseDouble(sa_string[3]);
                        } catch (NumberFormatException e) {
                            // ignore
                        }
                        if (!s_srid.equals("4326")) { // Transform into wsg84 if needed
                            SpatialiteUtilities.collectBoundsAndCenter(dbJava, s_srid, centerCoordinate, boundsCoordinates);
                        } else {
                            centerCoordinate[0] = boundsCoordinates[0] + (boundsCoordinates[2] - boundsCoordinates[0]) / 2;
                            centerCoordinate[1] = boundsCoordinates[1] + (boundsCoordinates[3] - boundsCoordinates[1]) / 2;
                        }
                        checkAndAdaptDatabaseBounds(boundsCoordinates, null);
                        if (layerType.equals("RasterLite2")) {
                            // s_ROWID_PK == title [Berlin Straube Postgrenzen] - needed
                            // s_view_read_only == abstract [1890 - 1:17777] - needed
                            // s_geometry_type == pixel_type [RGB] - not needed
                            // s_coord_dimension == tile_width - maybe usefull
                            // geometry_column == compression [LOSSY_WEBP] - not needed
                            // s_row_count_enabled == num_bands [3] - not needed
                            // int i_tile_width = Integer.parseInt(s_coord_dimension);
                            // double horz_resolution = Double.parseDouble(s_spatial_index_enabled);
                            // int i_num_bands = Integer.parseInt(s_row_count_enabled);
                            // TODO in next version add RasterTable
                            // berlin_postgrenzen.1890
                            SpatialRasterTable table = new SpatialRasterTable(getDatabasePath(), table_name, s_srid, 0, 22,
                                    centerCoordinate[0], centerCoordinate[1], null, boundsCoordinates);
                            table.setMapType(layerType);
                            table.setTitle(s_ROWID_PK);
                            table.setDescription(s_view_read_only);
                            // prevent a possible double loading
                            if (rasterTableList == null)
                                rasterTableList = new ArrayList<SpatialRasterTable>();
                            rasterTableList.add(table);
                        }
                        if ((layerType.equals(TableTypes.SPATIALTABLE.getDescription()))
                                || (layerType.equals(TableTypes.SPATIALVIEW.getDescription()))) {
                            i_view_read_only = Integer.parseInt(s_view_read_only);
                            i_geometry_type = Integer.parseInt(s_geometry_type);
                            GeometryType geometry_type = GeometryType.forValue(i_geometry_type);
                            s_geometry_type = geometry_type.toString();
                            int i_spatial_index_enabled = Integer.parseInt(s_spatial_index_enabled); // should
                            // always
                            // be
                            // 1
                            int i_row_count = Integer.parseInt(s_row_count_enabled);
                            // no Zoom levels with
                            // vector data
                            if (i_spatial_index_enabled == 1) {
                                SpatialVectorTable table = new SpatialVectorTable(getDatabasePath(), table_name, geometry_column,
                                        i_geometry_type, s_srid, centerCoordinate, boundsCoordinates, layerType);
                                // compleate list of fields of
                                // this table
                                fields_list = DaoSpatialite.collectTableFields(dbJava, table_name);
                                table.setFieldsList(fields_list, s_ROWID_PK, i_view_read_only);
                                if (vectorTableList == null)
                                    vectorTableList = new ArrayList<SpatialVectorTable>();
                                vectorTableList.add(table);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks (and adapts) the overall database bounds based on the passed coordinates.
     * <p/>
     * <p>Goal: when painting the Geometries: check of viewport is inside these bounds.
     * <br>- if the Viewport is outside these Bounds: all Tables can be ignored
     * <br>-- this is called when the Tables are created
     *
     * @param boundsCoordinates bounds to check against the overall.
     */
    private void checkAndAdaptDatabaseBounds( double[] boundsCoordinates, int[] zoomLevels ) {
        if ((this.boundsWest == 0.0) && (this.boundsSouth == 0.0) && (this.boundsEast == 0.0) && (this.boundsNorth == 0.0)) {
            this.boundsWest = boundsCoordinates[0];
            this.boundsSouth = boundsCoordinates[1];
            this.boundsEast = boundsCoordinates[2];
            this.boundsNorth = boundsCoordinates[2];
        } else {
            if (boundsCoordinates[0] < this.boundsWest)
                this.boundsWest = boundsCoordinates[0];
            if (boundsCoordinates[1] < this.boundsSouth)
                this.boundsSouth = boundsCoordinates[1];
            if (boundsCoordinates[2] > this.boundsEast)
                this.boundsEast = boundsCoordinates[2];
            if (boundsCoordinates[3] < this.boundsNorth)
                this.boundsNorth = boundsCoordinates[3];
        }
        centerX = this.boundsWest + (this.boundsEast - this.boundsWest) / 2;
        centerY = this.boundsSouth + (this.boundsNorth - this.boundsSouth) / 2;
        if ((zoomLevels != null) && (zoomLevels.length == 2)) {
            if ((this.minZoom == 0) && (this.maxZoom == 0)) {
                this.minZoom = zoomLevels[0];
                this.maxZoom = zoomLevels[1];
            } else {
                if (zoomLevels[0] < this.minZoom)
                    this.minZoom = zoomLevels[0];
                if (zoomLevels[1] > this.maxZoom)
                    this.maxZoom = zoomLevels[1];
            }
        }
    }

    /**
     * Delete and recreate a default properties table for this database.
     *
     * @throws Exception if something goes wrong.
     */
    public void resetStyleTable() throws Exception {
        deleteStyleTable(dbJava);
        createPropertiesTable(dbJava);
        for( SpatialVectorTable spatialTable : vectorTableList ) {
            createDefaultPropertiesForTable(dbJava, spatialTable.getUniqueNameBasedOnDbFilePath(), spatialTable.getLabelField());
        }
    }

    /**
     * Getter for the spatialite db reference.
     *
     * @return the spatialite database reference.
     */
    public Database getDatabase() {
        return dbJava;
    }

    private void getCenterCoordinate4326( String tableName, double[] centerCoordinate ) {
        try {
            Stmt centerStmt = null;
            try {
                WKBReader wkbReader = new WKBReader();

                StringBuilder centerBuilder = new StringBuilder();
                centerBuilder.append("select ST_AsBinary(CastToXY(ST_Transform(MakePoint(");
                // centerBuilder.append("select AsText(ST_Transform(MakePoint(");
                centerBuilder.append("(min_x + (max_x-min_x)/2), ");
                centerBuilder.append("(min_y + (max_y-min_y)/2), ");
                centerBuilder.append(METADATA_SRID);
                centerBuilder.append("), 4326))) from ");
                centerBuilder.append(METADATA_TABLE_GEOPACKAGE_CONTENTS);
                centerBuilder.append(" where ");
                centerBuilder.append(METADATA_GEOPACKAGECONTENT_TABLE_NAME);
                centerBuilder.append("='");
                centerBuilder.append(tableName);
                centerBuilder.append("';");
                String centerQuery = centerBuilder.toString();

                centerStmt = dbJava.prepare(centerQuery);
                if (centerStmt.step()) {
                    // String geomBytes = centerStmt.column_string(0);
                    // System.out.println();
                    byte[] geomBytes = centerStmt.column_bytes(0);
                    Geometry geometry = wkbReader.read(geomBytes);
                    Coordinate coordinate = geometry.getCoordinate();
                    centerCoordinate[0] = coordinate.x;
                    centerCoordinate[1] = coordinate.y;
                }
            } finally {
                if (centerStmt != null)
                    centerStmt.close();
            }
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the available zoomlevels for a raster table.
     * 
     * @param tableName
     *            the raster table name.
     * @param zoomLevels
     *            the zoomlevels array to update with the min and max levels
     *            available.
     * @throws Exception
     */
    private void getZoomLevels( String tableName, int[] zoomLevels ) throws Exception {
        Stmt zoomStmt = null;
        try {
            StringBuilder zoomBuilder = new StringBuilder();
            zoomBuilder.append("SELECT min(");
            zoomBuilder.append(METADATA_ZOOM_LEVEL);
            zoomBuilder.append("),max(");
            zoomBuilder.append(METADATA_ZOOM_LEVEL);
            zoomBuilder.append(") FROM ");
            zoomBuilder.append(METADATA_TABLE_TILE_MATRIX);
            zoomBuilder.append(" WHERE ");
            zoomBuilder.append(METADATA_TILE_TABLE_NAME);
            zoomBuilder.append("='");
            zoomBuilder.append(tableName);
            zoomBuilder.append("';");
            String zoomQuery = zoomBuilder.toString();
            zoomStmt = dbJava.prepare(zoomQuery);
            if (zoomStmt.step()) {
                zoomLevels[0] = zoomStmt.column_int(0);
                zoomLevels[1] = zoomStmt.column_int(1);
            }
        } finally {
            if (zoomStmt != null)
                zoomStmt.close();
        }
    }

    /**
     * Check availability of style for the tables.
     * 
     * @throws Exception
     */
    private void checkPropertiesTable() throws Exception {
        String checkTableQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + PROPERTIESTABLE + "';";
        Stmt stmt = dbJava.prepare(checkTableQuery);
        boolean tableExists = false;
        try {
            if (stmt.step()) {
                String name = stmt.column_string(0);
                if (name != null) {
                    tableExists = true;
                }
            }
        } finally {
            stmt.close();
        }
        if (!tableExists) {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ");
            sb.append(PROPERTIESTABLE);
            sb.append(" (");
            sb.append(NAME).append(" TEXT, ");
            sb.append(SIZE).append(" REAL, ");
            sb.append(FILLCOLOR).append(" TEXT, ");
            sb.append(STROKECOLOR).append(" TEXT, ");
            sb.append(FILLALPHA).append(" REAL, ");
            sb.append(STROKEALPHA).append(" REAL, ");
            sb.append(SHAPE).append(" TEXT, ");
            sb.append(WIDTH).append(" REAL, ");
            sb.append(TEXTSIZE).append(" REAL, ");
            sb.append(TEXTFIELD).append(" TEXT, ");
            sb.append(ENABLED).append(" INTEGER, ");
            sb.append(ORDER).append(" INTEGER,");
            sb.append(DECIMATION).append(" REAL");
            sb.append(" );");
            String query = sb.toString();
            dbJava.exec(query, null);

            for( SpatialVectorTable spatialTable : vectorTableList ) {
                StringBuilder sbIn = new StringBuilder();
                sbIn.append("insert into ").append(PROPERTIESTABLE);
                sbIn.append(" ( ");
                sbIn.append(NAME).append(" , ");
                sbIn.append(SIZE).append(" , ");
                sbIn.append(FILLCOLOR).append(" , ");
                sbIn.append(STROKECOLOR).append(" , ");
                sbIn.append(FILLALPHA).append(" , ");
                sbIn.append(STROKEALPHA).append(" , ");
                sbIn.append(SHAPE).append(" , ");
                sbIn.append(WIDTH).append(" , ");
                sbIn.append(TEXTSIZE).append(" , ");
                sbIn.append(TEXTFIELD).append(" , ");
                sbIn.append(ENABLED).append(" , ");
                sbIn.append(ORDER).append(" , ");
                sbIn.append(DECIMATION);
                sbIn.append(" ) ");
                sbIn.append(" values ");
                sbIn.append(" ( ");
                Style style = new Style();
                style.name = spatialTable.getName();
                sbIn.append(style.insertValuesString());
                sbIn.append(" );");

                String insertQuery = sbIn.toString();
                dbJava.exec(insertQuery, null);
            }
        }
    }

    /**
     * Retrieve the {@link Style} for a given table.
     * 
     * @param tableName
     * @return
     * @throws Exception
     */
    public Style getStyle4Table( String tableName ) throws Exception {
        Style style = new Style();
        style.name = tableName;

        StringBuilder sbSel = new StringBuilder();
        sbSel.append("select ");
        sbSel.append(SIZE).append(" , ");
        sbSel.append(FILLCOLOR).append(" , ");
        sbSel.append(STROKECOLOR).append(" , ");
        sbSel.append(FILLALPHA).append(" , ");
        sbSel.append(STROKEALPHA).append(" , ");
        sbSel.append(SHAPE).append(" , ");
        sbSel.append(WIDTH).append(" , ");
        sbSel.append(TEXTSIZE).append(" , ");
        sbSel.append(TEXTFIELD).append(" , ");
        sbSel.append(ENABLED).append(" , ");
        sbSel.append(ORDER).append(" , ");
        sbSel.append(DECIMATION);
        sbSel.append(" from ");
        sbSel.append(PROPERTIESTABLE);
        sbSel.append(" where ");
        sbSel.append(NAME).append(" ='").append(tableName).append("';");

        String selectQuery = sbSel.toString();
        Stmt stmt = dbJava.prepare(selectQuery);
        try {
            if (stmt.step()) {
                style.size = (float) stmt.column_double(0);
                style.fillcolor = stmt.column_string(1);
                style.strokecolor = stmt.column_string(2);
                style.fillalpha = (float) stmt.column_double(3);
                style.strokealpha = (float) stmt.column_double(4);
                style.shape = stmt.column_string(5);
                style.width = (float) stmt.column_double(6);
                style.enabled = stmt.column_int(9);
                style.order = stmt.column_int(10);
                style.decimationFactor = (float) stmt.column_double(11);
            }
        } finally {
            stmt.close();
        }
        return style;
    }

    public float[] getTableBounds( SpatialVectorTable spatialTable, String destSrid ) throws Exception {
        boolean doTransform = false;
        if (!spatialTable.getSrid().equals(destSrid)) {
            doTransform = true;
        }

        StringBuilder geomSb = new StringBuilder();
        if (doTransform)
            geomSb.append("ST_Transform(");
        geomSb.append(spatialTable.getGeomName());
        if (doTransform) {
            geomSb.append(", ");
            geomSb.append(destSrid);
            geomSb.append(")");
        }
        String geom = geomSb.toString();

        StringBuilder qSb = new StringBuilder();
        qSb.append("SELECT Min(MbrMinX(");
        qSb.append(geom);
        qSb.append(")) AS min_x, Min(MbrMinY(");
        qSb.append(geom);
        qSb.append(")) AS min_y,");
        qSb.append("Max(MbrMaxX(");
        qSb.append(geom);
        qSb.append(")) AS max_x, Max(MbrMaxY(");
        qSb.append(geom);
        qSb.append(")) AS max_y");
        qSb.append(" FROM ");
        qSb.append(spatialTable.getName());
        qSb.append(";");

        String selectQuery = qSb.toString();
        Stmt stmt = dbJava.prepare(selectQuery);
        try {
            if (stmt.step()) {
                float w = (float) stmt.column_double(0);
                float s = (float) stmt.column_double(1);
                float e = (float) stmt.column_double(2);
                float n = (float) stmt.column_double(3);

                return new float[]{n, s, e, w};
            }
        } finally {
            stmt.close();
        }
        return null;
    }

    /**
     * Update a style definition.
     * 
     * @param style
     *            the {@link Style} to set.
     * @throws Exception
     */
    public void updateStyle( Style style ) throws Exception {
        StringBuilder sbIn = new StringBuilder();
        sbIn.append("update ").append(PROPERTIESTABLE);
        sbIn.append(" set ");
        // sbIn.append(NAME).append("='").append(style.name).append("' , ");
        sbIn.append(SIZE).append("=").append(style.size).append(" , ");
        sbIn.append(FILLCOLOR).append("='").append(style.fillcolor).append("' , ");
        sbIn.append(STROKECOLOR).append("='").append(style.strokecolor).append("' , ");
        sbIn.append(FILLALPHA).append("=").append(style.fillalpha).append(" , ");
        sbIn.append(STROKEALPHA).append("=").append(style.strokealpha).append(" , ");
        sbIn.append(SHAPE).append("='").append(style.shape).append("' , ");
        sbIn.append(WIDTH).append("=").append(style.width).append(" , ");
        sbIn.append(ENABLED).append("=").append(style.enabled).append(" , ");
        sbIn.append(ORDER).append("=").append(style.order).append(" , ");
        sbIn.append(DECIMATION).append("=").append(style.decimationFactor);
        sbIn.append(" where ");
        sbIn.append(NAME);
        sbIn.append("='");
        sbIn.append(style.name);
        sbIn.append("';");

        String updateQuery = sbIn.toString();
        dbJava.exec(updateQuery, null);
    }

    public Paint getFillPaint4Style( Style style ) {
        Paint paint = fillPaints.get(style.name);
        if (paint == null) {
            paint = new Paint();
            fillPaints.put(style.name, paint);
        }
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(style.fillcolor));
        float alpha = style.fillalpha * 255f;
        paint.setAlpha((int) alpha);
        return paint;
    }

    public Paint getStrokePaint4Style( Style style ) {
        Paint paint = strokePaints.get(style.name);
        if (paint == null) {
            paint = new Paint();
            strokePaints.put(style.name, paint);
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Cap.ROUND);
        paint.setStrokeJoin(Join.ROUND);
        paint.setColor(Color.parseColor(style.strokecolor));
        float alpha = style.strokealpha * 255f;
        paint.setAlpha((int) alpha);
        paint.setStrokeWidth(style.width);
        return paint;
    }

    @Override
    public byte[] getRasterTile( String query ) {
        try {
            Stmt stmt = dbJava.prepare(query);
            try {
                if (stmt.step()) {
                    byte[] bytes = stmt.column_bytes(0);
                    return bytes;
                }
            } finally {
                stmt.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public GeometryIterator getGeometryIteratorInBounds( String destSrid, SpatialVectorTable table, double n, double s, double e,
            double w ) {
        String query = buildGeometriesInBoundsQuery(destSrid, table, n, s, e, w);
        return new GeometryIterator(dbJava, query);
    }

    public int getColumnCount( String destSrid, SpatialVectorTable table, String where ) {
        if (where != null && !"".equals(where)) {
            where = " where " + where;
        }
        String query = "select * from " + table.getName() + where;
        try {
            Stmt stmt = dbJava.prepare(query);
            if (stmt.step()) {
                int size = stmt.column_count();
                return size;
            }
        } catch (Exception ex) {

        }
        return 0;
    }

    private String buildGeometriesInBoundsQueryFromWhere( String destSrid, SpatialVectorTable table, double n, double s,
            double e, double w, String where ) {
        boolean doTransform = false;
        if (!table.getSrid().equals(destSrid)) {
            doTransform = true;
        }

        StringBuilder mbrSb = new StringBuilder();
        if (doTransform)
            mbrSb.append("ST_Transform(");
        mbrSb.append("BuildMBR(");
        mbrSb.append(w);
        mbrSb.append(", ");
        mbrSb.append(n);
        mbrSb.append(", ");
        mbrSb.append(e);
        mbrSb.append(", ");
        mbrSb.append(s);
        if (doTransform) {
            mbrSb.append(", ");
            mbrSb.append(destSrid);
            mbrSb.append("), ");
            mbrSb.append(table.getSrid());
        }
        mbrSb.append(")");
        String mbr = mbrSb.toString();

        StringBuilder qSb = new StringBuilder();
        qSb.append("SELECT ST_AsBinary(CastToXY(");
        if (doTransform)
            qSb.append("ST_Transform(");
        qSb.append(table.getGeomName());
        if (doTransform) {
            qSb.append(", ");
            qSb.append(destSrid);
            qSb.append(")");
        }
        qSb.append("))");
        // qSb.append(", AsText(");
        // if (doTransform)
        // qSb.append("ST_Transform(");
        // qSb.append(table.geomName);
        // if (doTransform) {
        // qSb.append(", ");
        // qSb.append(destSrid);
        // qSb.append(")");
        // }
        // qSb.append(")");
        qSb.append(" FROM ");
        qSb.append(table.getName());

        qSb.append(" WHERE ST_Intersects(");
        qSb.append(table.getGeomName());
        qSb.append(", ");
        qSb.append(mbr);
        qSb.append(") = 1");/*
                             * qSb.append("   AND ROWID IN ("); qSb.append(
                             * "     SELECT ROWID FROM Spatialindex WHERE f_table_name ='"
                             * ); qSb.append(table.getName()); qSb.append("'");
                             * qSb.append("     AND search_frame = ");
                             * qSb.append(mbr); qSb.append(" );");
                             */
        if (!"".equalsIgnoreCase(where) && where != null) {
            qSb.append(" AND ");
            qSb.append(where);
        }
        String q = qSb.toString();

        return q;
    }
    private String buildGeometriesInBoundsQueryFromWhereWithTableName( String destSrid, SpatialVectorTable table,
            String tableName, double n, double s, double e, double w, String where ) {
        boolean doTransform = false;
        if (!table.getSrid().equals(destSrid)) {
            doTransform = true;
        }

        StringBuilder mbrSb = new StringBuilder();
        if (doTransform)
            mbrSb.append("ST_Transform(");
        mbrSb.append("BuildMBR(");
        mbrSb.append(w);
        mbrSb.append(", ");
        mbrSb.append(n);
        mbrSb.append(", ");
        mbrSb.append(e);
        mbrSb.append(", ");
        mbrSb.append(s);
        if (doTransform) {
            mbrSb.append(", ");
            mbrSb.append(destSrid);
            mbrSb.append("), ");
            mbrSb.append(table.getSrid());
        }
        mbrSb.append(")");
        String mbr = mbrSb.toString();

        StringBuilder qSb = new StringBuilder();
        qSb.append("SELECT ST_AsBinary(CastToXY(");
        if (doTransform)
            qSb.append("ST_Transform(");
        qSb.append(table.getGeomName());
        if (doTransform) {
            qSb.append(", ");
            qSb.append(destSrid);
            qSb.append(")");
        }
        qSb.append("))");
        qSb.append(" FROM ");
        qSb.append(tableName);

        qSb.append(" WHERE ST_Intersects(");
        qSb.append(table.getGeomName());
        qSb.append(", ");
        qSb.append(mbr);
        qSb.append(") = 1");
        if (!"".equalsIgnoreCase(where) && where != null) {
            qSb.append(" AND ");
            qSb.append(where);
        }
        String q = qSb.toString();

        return q;
    }
    private String buildGeometriesInBoundsQueryFromWhere( String destSrid, SpatialVectorTable table, String where ) {
        boolean doTransform = false;
        if (!table.getSrid().equals(destSrid)) {
            doTransform = true;
        }

        StringBuilder qSb = new StringBuilder();
        qSb.append("SELECT ST_AsBinary(CastToXY(");
        if (doTransform)
            qSb.append("ST_Transform(");
        qSb.append(table.getGeomName());
        if (doTransform) {
            qSb.append(", ");
            qSb.append(destSrid);
            qSb.append(")");
        }
        qSb.append("))");
        // qSb.append(", AsText(");
        // if (doTransform)
        // qSb.append("ST_Transform(");
        // qSb.append(table.geomName);
        // if (doTransform) {
        // qSb.append(", ");
        // qSb.append(destSrid);
        // qSb.append(")");
        // }
        // qSb.append(")");
        qSb.append(" FROM ");
        qSb.append(table.getName());

        if (!"".equalsIgnoreCase(where) && where != null) {
            qSb.append(" where ");
            qSb.append(where);
        }
        String q = qSb.toString();

        return q;
    }

    private String buildGeometriesInBoundsQueryFromWhereLimit( String destSrid, SpatialVectorTable table, double n, double s,
            double e, double w, String where, String limit ) {
        boolean doTransform = false;
        if (!table.getSrid().equals(destSrid)) {
            doTransform = true;
        }

        StringBuilder mbrSb = new StringBuilder();
        if (doTransform)
            mbrSb.append("ST_Transform(");
        mbrSb.append("BuildMBR(");
        mbrSb.append(w);
        mbrSb.append(", ");
        mbrSb.append(n);
        mbrSb.append(", ");
        mbrSb.append(e);
        mbrSb.append(", ");
        mbrSb.append(s);
        if (doTransform) {
            mbrSb.append(", ");
            mbrSb.append(destSrid);
            mbrSb.append("), ");
            mbrSb.append(table.getSrid());
        }
        mbrSb.append(")");
        String mbr = mbrSb.toString();

        StringBuilder qSb = new StringBuilder();
        qSb.append("SELECT ST_AsBinary(CastToXY(");
        if (doTransform)
            qSb.append("ST_Transform(");
        qSb.append(table.getGeomName());
        if (doTransform) {
            qSb.append(", ");
            qSb.append(destSrid);
            qSb.append(")");
        }
        qSb.append("))");
        // qSb.append(", AsText(");
        // if (doTransform)
        // qSb.append("ST_Transform(");
        // qSb.append(table.geomName);
        // if (doTransform) {
        // qSb.append(", ");
        // qSb.append(destSrid);
        // qSb.append(")");
        // }
        // qSb.append(")");
        qSb.append(" FROM ");
        qSb.append(table.getName());

        qSb.append(" WHERE ST_Intersects(");
        qSb.append(table.getGeomName());
        qSb.append(", ");
        qSb.append(mbr);
        qSb.append(") = 1");/*
                             * qSb.append("   AND ROWID IN ("); qSb.append(
                             * "     SELECT ROWID FROM Spatialindex WHERE f_table_name ='"
                             * ); qSb.append(table.getName()); qSb.append("'");
                             * qSb.append("     AND search_frame = ");
                             * qSb.append(mbr); qSb.append(" );");
                             */
        if (!"".equalsIgnoreCase(where)) {
            qSb.append(" AND ");
            qSb.append(where);
        }
        qSb.append(" " + limit);
        String q = qSb.toString();

        return q;
    }

    private String buildGeometriesInBoundsQuery( String destSrid, SpatialVectorTable table, double n, double s, double e, double w ) {
        boolean doTransform = false;
        if (!table.getSrid().equals(destSrid)) {
            doTransform = true;
        }

        StringBuilder mbrSb = new StringBuilder();
        if (doTransform)
            mbrSb.append("ST_Transform(");
        mbrSb.append("BuildMBR(");
        mbrSb.append(w);
        mbrSb.append(", ");
        mbrSb.append(n);
        mbrSb.append(", ");
        mbrSb.append(e);
        mbrSb.append(", ");
        mbrSb.append(s);
        if (doTransform) {
            mbrSb.append(", ");
            mbrSb.append(destSrid);
            mbrSb.append("), ");
            mbrSb.append(table.getSrid());
        }
        mbrSb.append(")");
        String mbr = mbrSb.toString();

        StringBuilder qSb = new StringBuilder();
        qSb.append("SELECT ST_AsBinary(CastToXY(");
        if (doTransform)
            qSb.append("ST_Transform(");
        qSb.append(table.getGeomName());
        if (doTransform) {
            qSb.append(", ");
            qSb.append(destSrid);
            qSb.append(")");
        }
        qSb.append("))");
        // qSb.append(", AsText(");
        // if (doTransform)
        // qSb.append("ST_Transform(");
        // qSb.append(table.geomName);
        // if (doTransform) {
        // qSb.append(", ");
        // qSb.append(destSrid);
        // qSb.append(")");
        // }
        // qSb.append(")");
        qSb.append(" FROM ");
        qSb.append(table.getName());

        qSb.append(" WHERE ST_Intersects(");
        qSb.append(table.getGeomName());
        qSb.append(", ");
        qSb.append(mbr);
        qSb.append(") = 1");/*
                             * qSb.append("   AND ROWID IN ("); qSb.append(
                             * "     SELECT ROWID FROM Spatialindex WHERE f_table_name ='"
                             * ); qSb.append(table.getName()); qSb.append("'");
                             * qSb.append("     AND search_frame = ");
                             * qSb.append(mbr); qSb.append(" );");
                             */
        String q = qSb.toString();

        return q;
    }

    public void close() throws Exception {
        if (dbJava != null) {
            dbJava.close();
        }
    }

    public void intersectionToStringBBOX( String boundsSrid, SpatialVectorTable spatialTable, double n, double s, double e,
            double w, StringBuilder sb, String indentStr ) throws Exception {
        boolean doTransform = false;
        if (!spatialTable.getSrid().equals(boundsSrid)) {
            doTransform = true;
        }

        String query = null;

        // SELECT che-cazzo-ti-pare-a-te
        // FROM qualche-tavola
        // WHERE ROWID IN (
        // SELECT ROWID
        // FROM SpatialIndex
        // WHERE f_table_name = 'qualche-tavola'
        // AND search_frame = il-tuo-bbox
        // );

        // {
        // StringBuilder sbQ = new StringBuilder();
        // sbQ.append("SELECT ");
        // sbQ.append("*");
        // sbQ.append(" from ").append(spatialTable.name);
        // sbQ.append(" where ROWID IN (");
        // sbQ.append(" SELECT ROWID FROM Spatialindex WHERE f_table_name ='");
        // sbQ.append(spatialTable.name);
        // sbQ.append("' AND search_frame = ");
        // if (doTransform)
        // sbQ.append("ST_Transform(");
        // sbQ.append("BuildMBR(");
        // sbQ.append(w);
        // sbQ.append(", ");
        // sbQ.append(s);
        // sbQ.append(", ");
        // sbQ.append(e);
        // sbQ.append(", ");
        // sbQ.append(n);
        // if (doTransform) {
        // sbQ.append(", ");
        // sbQ.append(boundsSrid);
        // }
        // sbQ.append(")");
        // if (doTransform) {
        // sbQ.append(",");
        // sbQ.append(spatialTable.srid);
        // sbQ.append(")");
        // }
        // sbQ.append(");");
        //
        // query = sbQ.toString();
        // Logger.i(this, query);
        // }
        {
            StringBuilder sbQ = new StringBuilder();
            sbQ.append("SELECT ");
            sbQ.append("*");
            sbQ.append(" from ").append(spatialTable.getName());
            sbQ.append(" where ST_Intersects(");
            if (doTransform)
                sbQ.append("ST_Transform(");
            sbQ.append("BuildMBR(");
            sbQ.append(w);
            sbQ.append(", ");
            sbQ.append(s);
            sbQ.append(", ");
            sbQ.append(e);
            sbQ.append(", ");
            sbQ.append(n);
            if (doTransform) {
                sbQ.append(", ");
                sbQ.append(boundsSrid);
                sbQ.append("),");
                sbQ.append(spatialTable.getSrid());
            }
            sbQ.append("),");
            sbQ.append(spatialTable.getGeomName());
            sbQ.append(");");

            query = sbQ.toString();

            // Logger.i(this, query);
        }

        Stmt stmt = dbJava.prepare(query);
        try {
            while( stmt.step() ) {
                int column_count = stmt.column_count();
                for( int i = 0; i < column_count; i++ ) {
                    String cName = stmt.column_name(i);

                    if (cName.equalsIgnoreCase(spatialTable.getGeomName())) {
                        continue;
                    }

                    String value = stmt.column_string(i);
                    sb.append(indentStr).append(cName).append(": ").append(value).append("\n");
                }
                sb.append("\n");
            }
        } finally {
            stmt.close();
        }
    }

    public void intersectionToStringBBOXFromWhere( String boundsSrid, String sql, SpatialVectorTable spatialTable, double n,
            double s, double e, double w, List<LinkedHashMap<String, String>> m, String indentStr, String where )
            throws Exception {
        boolean doTransform = false;
        if (!spatialTable.getSrid().equals(boundsSrid)) {
            doTransform = true;
        }

        String query = null;

        // SELECT che-cazzo-ti-pare-a-te
        // FROM qualche-tavola
        // WHERE ROWID IN (
        // SELECT ROWID
        // FROM SpatialIndex
        // WHERE f_table_name = 'qualche-tavola'
        // AND search_frame = il-tuo-bbox
        // );

        // {
        // StringBuilder sbQ = new StringBuilder();
        // sbQ.append("SELECT ");
        // sbQ.append("*");
        // sbQ.append(" from ").append(spatialTable.name);
        // sbQ.append(" where ROWID IN (");
        // sbQ.append(" SELECT ROWID FROM Spatialindex WHERE f_table_name ='");
        // sbQ.append(spatialTable.name);
        // sbQ.append("' AND search_frame = ");
        // if (doTransform)
        // sbQ.append("ST_Transform(");
        // sbQ.append("BuildMBR(");
        // sbQ.append(w);
        // sbQ.append(", ");
        // sbQ.append(s);
        // sbQ.append(", ");
        // sbQ.append(e);
        // sbQ.append(", ");
        // sbQ.append(n);
        // if (doTransform) {
        // sbQ.append(", ");
        // sbQ.append(boundsSrid);
        // }
        // sbQ.append(")");
        // if (doTransform) {
        // sbQ.append(",");
        // sbQ.append(spatialTable.srid);
        // sbQ.append(")");
        // }
        // sbQ.append(");");
        //
        // query = sbQ.toString();
        // Logger.i(this, query);
        // }
        {
            StringBuilder sbQ = new StringBuilder();
            sbQ.append(sql);
            sbQ.append(" where ST_Intersects(");
            if (doTransform)
                sbQ.append("ST_Transform(");
            sbQ.append("BuildMBR(");
            sbQ.append(w);
            sbQ.append(", ");
            sbQ.append(s);
            sbQ.append(", ");
            sbQ.append(e);
            sbQ.append(", ");
            sbQ.append(n);
            if (doTransform) {
                sbQ.append(", ");
                sbQ.append(boundsSrid);
                sbQ.append("),");
                sbQ.append(spatialTable.getSrid());
            }
            sbQ.append("),");
            sbQ.append(spatialTable.getGeomName());
            sbQ.append(")");

            if ("".equals(where)) {

            } else {
                sbQ.append(" AND " + where);
            }
            query = sbQ.toString();

            // Logger.i(this, query);
        }

        Stmt stmt = dbJava.prepare(query);
        try {
            while( stmt.step() ) {
                int column_count = stmt.column_count();
                LinkedHashMap<String, String> m1 = new LinkedHashMap<String, String>();
                for( int i = 0; i < column_count; i++ ) {
                    String cName = stmt.column_name(i);

                    if (cName.equalsIgnoreCase(spatialTable.getGeomName())) {
                        continue;
                    }

                    String value = stmt.column_string(i);
                    m1.put(cName, value);

                }
                m.add(m1);
            }
        } finally {
            stmt.close();
        }
    }

    public void intersectionToString4Polygon( String queryPointSrid, SpatialVectorTable spatialTable, double n, double e,
            StringBuilder sb, String indentStr ) throws Exception {
        boolean doTransform = false;
        if (!spatialTable.getSrid().equals(queryPointSrid)) {
            doTransform = true;
        }

        StringBuilder sbQ = new StringBuilder();
        sbQ.append("SELECT * FROM ");
        sbQ.append(spatialTable.getName());
        sbQ.append(" WHERE ST_Intersects(");
        sbQ.append(spatialTable.getGeomName());
        sbQ.append(", ");
        if (doTransform)
            sbQ.append("ST_Transform(");
        sbQ.append("MakePoint(");
        sbQ.append(e);
        sbQ.append(",");
        sbQ.append(n);
        if (doTransform) {
            sbQ.append(", ");
            sbQ.append(queryPointSrid);
            sbQ.append("), ");
            sbQ.append(spatialTable.getSrid());
        }
        sbQ.append(")) = 1 ");
        sbQ.append("AND ROWID IN (");
        sbQ.append("SELECT ROWID FROM Spatialindex WHERE f_table_name ='");
        sbQ.append(spatialTable.getName());
        sbQ.append("' AND search_frame = ");
        if (doTransform)
            sbQ.append("ST_Transform(");
        sbQ.append("MakePoint(");
        sbQ.append(e);
        sbQ.append(",");
        sbQ.append(n);
        if (doTransform) {
            sbQ.append(", ");
            sbQ.append(queryPointSrid);
            sbQ.append("), ");
            sbQ.append(spatialTable.getSrid());
        }
        sbQ.append("));");
        String query = sbQ.toString();

        Stmt stmt = dbJava.prepare(query);
        try {
            while( stmt.step() ) {
                int column_count = stmt.column_count();
                for( int i = 0; i < column_count; i++ ) {
                    String cName = stmt.column_name(i);
                    if (cName.equalsIgnoreCase(spatialTable.getGeomName())) {
                        continue;
                    }

                    String value = stmt.column_string(i);
                    sb.append(indentStr).append(cName).append(": ").append(value).append("\n");
                }
                sb.append("\n");
            }
        } finally {
            stmt.close();
        }
    }

    public List<LinkedHashMap<String, String>> getTableInfo( SpatialVectorTable table, String sql, String where )
            throws Exception {
        // 获取表中内容

        List<LinkedHashMap<String, String>> lm = new ArrayList<LinkedHashMap<String, String>>();
        String query = null;
        StringBuilder sbQ = new StringBuilder();
        sbQ.append(sql);
        sbQ.append(" " + where);
        query = sbQ.toString();
        Stmt stmt = dbJava.prepare(query);
        try {
            while( stmt.step() ) {
                LinkedHashMap<String, String> m = new LinkedHashMap<String, String>();
                int column_count = stmt.column_count();
                for( int i = 0; i < column_count; i++ ) {
                    String cName = stmt.column_name(i);
                    if (cName.equalsIgnoreCase(table.getGeomName())) {
                        continue;
                    }
                    String value = stmt.column_string(i);
                    m.put(cName, value);
                }
                lm.add(m);

            }
        } finally {
            stmt.close();
        }

        return lm;

    }

    public Point getCenterPoint( SpatialVectorTable dbName, String tableName, String where ) throws Exception {
        if (!"".equals(where)) {
            if (where.contains("where")) {

            } else {
                where = " where " + where;
            }

        }
        String query = "SELECT ST_AsBinary(CastToXY(" + dbName.getGeomName() + ")) FROM " + tableName + " " + where;
        GeometryIterator g = new GeometryIterator(dbJava, query);

        while( g.hasNext() ) {
            Geometry geom = g.next();
            if (geom != null) {
                Point p = geom.getCentroid();
                return p;
            }
        }
        return null;
    }

    public Point getCenterPoint( SpatialVectorTable dbName, String where ) throws Exception {
        return getCenterPoint(dbName, dbName.getGeomName(), where);
    }

    public List<String> getArrStr( SpatialVectorTable table, String sql ) throws Exception {
        List<String> ls = new ArrayList<String>();
        Stmt stmt = dbJava.prepare(sql);
        try {
            while( stmt.step() ) {
                int column_count = stmt.column_count();
                for( int i = 0; i < column_count; i++ ) {
                    String cName = stmt.column_name(i);
                    if (cName.equalsIgnoreCase(table.getGeomName())) {
                        continue;
                    }
                    String value = stmt.column_string(i);
                    ls.add(value);
                }

            }
        } finally {
            stmt.close();
        }

        return ls;
    }

    public GeometryIterator getGeometryIteratorInBoundsAndWhere( String destSrid, SpatialVectorTable table, double n, double s,
            double e, double w, String where ) {
        String query = buildGeometriesInBoundsQueryFromWhere(destSrid, table, n, s, e, w, where);
        return new GeometryIterator(dbJava, query);
    }
    public GeometryIterator getGeometryIteratorInBoundsAndWhereWithTableName( String destSrid, SpatialVectorTable table,
            String tableName, double n, double s, double e, double w, String where ) {
        String query = buildGeometriesInBoundsQueryFromWhereWithTableName(destSrid, table, tableName, n, s, e, w, where);
        return new GeometryIterator(dbJava, query);
    }
    public List<Geometry> getGeometryIteratorInBoundsAndWhere2( String destSrid, SpatialVectorTable table, double n, double s,
            double e, double w, String where ) throws Exception {
        String query = buildGeometriesInBoundsQueryFromWhere(destSrid, table, n, s, e, w, where);
        Stmt stmt = dbJava.prepare(query);
        List<Geometry> geormetrys = new ArrayList<Geometry>();
        WKBReader wkbReader = new WKBReader();
        try {
            while( stmt.step() ) {
                byte[] geomBytes = stmt.column_bytes(0);
                Geometry geometry = null;
                try {
                    geometry = wkbReader.read(geomBytes);
                } catch (ParseException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                geormetrys.add(geometry);

            }
            return geormetrys;

        } catch (Exception e1) {
            stmt.close();
        } finally {
            stmt.close();
        }

        return null;
    }

    public Geometry getGeometryIteratorInBoundsAndWhere2( String destSrid, SpatialVectorTable table, String where )
            throws Exception {
        String query = buildGeometriesInBoundsQueryFromWhere(destSrid, table, where);
        Stmt stmt = dbJava.prepare(query);
        Geometry geometry = null;
        WKBReader wkbReader = new WKBReader();
        try {
            while( stmt.step() ) {
                byte[] geomBytes = stmt.column_bytes(0);
                try {
                    geometry = wkbReader.read(geomBytes);
                } catch (ParseException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }

        } catch (Exception e1) {
            stmt.close();
        } finally {
            stmt.close();
        }
        return geometry;
    }

    public GeometryIterator getGeometryIteratorInBoundsAndWhereLimit( String destSrid, SpatialVectorTable table, double n,
            double s, double e, double w, String where, String limit ) {
        String query = buildGeometriesInBoundsQueryFromWhereLimit(destSrid, table, n, s, e, w, where, limit);
        return new GeometryIterator(dbJava, query);
    }

    public List<LinkedHashMap<String, String>> getGggyTableInfo( SpatialVectorTable table, String sql, String where, String time )
            throws Exception {
        // 获取表中内容

        List<LinkedHashMap<String, String>> lm = new ArrayList<LinkedHashMap<String, String>>();
        String query = null;
        StringBuilder sbQ = new StringBuilder();
        sbQ.append(sql);
        sbQ.append(" " + where);
        query = sbQ.toString();
        Stmt stmt = dbJava.prepare(query);
        try {
            while( stmt.step() ) {
                LinkedHashMap<String, String> m = new LinkedHashMap<String, String>();
                int column_count = stmt.column_count();
                for( int i = 0; i < column_count; i++ ) {
                    String cName = stmt.column_name(i);
                    if (cName.equalsIgnoreCase(table.getGeomName())) {
                        continue;
                    }
                    String value = stmt.column_string(i);
                    m.put(cName, value);
                }

                if (time != null && !"".equals(time)) {
                    // 如果有条件，则符合条件的加
                    // 判断m中是否包含time
                    String columnValue = m.get("xczsj");
                    boolean isAdd = false;
                    if (columnValue != null && !"".equals(columnValue)) {
                        if (columnValue.contains(",")) {
                            // 字段中有多个月份，并且用逗号分隔
                            if (time.contains(";")) {
                                // 条件有多个，用逗号隔开
                                String columnValue_split[] = columnValue.split(",");
                                String time_split[] = time.split(";");
                                for( int i = 0; i < columnValue_split.length; i++ ) {
                                    if (isAdd)
                                        break;
                                    for( int j = 0; j < time_split.length; j++ ) {
                                        if (columnValue_split[i].equals(time_split[j])) {
                                            isAdd = true;
                                            break;
                                        }
                                    }

                                }

                            } else {
                                // 条件只有一个
                                String columnValue_split[] = columnValue.split(",");
                                for( int i = 0; i < columnValue_split.length; i++ ) {
                                    if (columnValue_split[i].equals(time)) {
                                        isAdd = true;
                                        break;
                                    }
                                }

                            }

                        } else {
                            // 字段里只有一个月份
                            if (time.contains(";")) {
                                // 条件有多个，用逗号隔开
                                String time_split[] = time.split(";");
                                for( int i = 0; i < time_split.length; i++ ) {
                                    if (time_split[i].equals(columnValue)) {
                                        isAdd = true;
                                        break;
                                    }
                                }

                            } else {
                                // 条件只有一个
                                if (columnValue.equals(time))
                                    isAdd = true;
                            }
                        }
                    }

                    if (isAdd)
                        lm.add(m);

                } else {
                    // 如果没有条件，直接加
                    lm.add(m);
                }

            }
        } finally {
            stmt.close();
        }

        return lm;

    }

    public void intersectionToGggy( String boundsSrid, String sql, SpatialVectorTable spatialTable, double n, double s, double e,
            double w, List<LinkedHashMap<String, String>> m, String indentStr, String where, String time ) throws Exception {
        boolean doTransform = false;
        if (!spatialTable.getSrid().equals(boundsSrid)) {
            doTransform = true;
        }

        String query = null;

        // SELECT che-cazzo-ti-pare-a-te
        // FROM qualche-tavola
        // WHERE ROWID IN (
        // SELECT ROWID
        // FROM SpatialIndex
        // WHERE f_table_name = 'qualche-tavola'
        // AND search_frame = il-tuo-bbox
        // );

        // {
        // StringBuilder sbQ = new StringBuilder();
        // sbQ.append("SELECT ");
        // sbQ.append("*");
        // sbQ.append(" from ").append(spatialTable.name);
        // sbQ.append(" where ROWID IN (");
        // sbQ.append(" SELECT ROWID FROM Spatialindex WHERE f_table_name ='");
        // sbQ.append(spatialTable.name);
        // sbQ.append("' AND search_frame = ");
        // if (doTransform)
        // sbQ.append("ST_Transform(");
        // sbQ.append("BuildMBR(");
        // sbQ.append(w);
        // sbQ.append(", ");
        // sbQ.append(s);
        // sbQ.append(", ");
        // sbQ.append(e);
        // sbQ.append(", ");
        // sbQ.append(n);
        // if (doTransform) {
        // sbQ.append(", ");
        // sbQ.append(boundsSrid);
        // }
        // sbQ.append(")");
        // if (doTransform) {
        // sbQ.append(",");
        // sbQ.append(spatialTable.srid);
        // sbQ.append(")");
        // }
        // sbQ.append(");");
        //
        // query = sbQ.toString();
        // Logger.i(this, query);
        // }
        {
            StringBuilder sbQ = new StringBuilder();
            sbQ.append(sql);
            sbQ.append(" where ST_Intersects(");
            if (doTransform)
                sbQ.append("ST_Transform(");
            sbQ.append("BuildMBR(");
            sbQ.append(w);
            sbQ.append(", ");
            sbQ.append(s);
            sbQ.append(", ");
            sbQ.append(e);
            sbQ.append(", ");
            sbQ.append(n);
            if (doTransform) {
                sbQ.append(", ");
                sbQ.append(boundsSrid);
                sbQ.append("),");
                sbQ.append(spatialTable.getSrid());
            }
            sbQ.append("),");
            sbQ.append(spatialTable.getGeomName());
            sbQ.append(")");

            if ("".equals(where)) {

            } else {
                sbQ.append(" AND " + where);
            }
            query = sbQ.toString();

            // Logger.i(this, query);
        }

        Stmt stmt = dbJava.prepare(query);
        try {
            while( stmt.step() ) {
                int column_count = stmt.column_count();
                LinkedHashMap<String, String> m1 = new LinkedHashMap<String, String>();
                for( int i = 0; i < column_count; i++ ) {
                    String cName = stmt.column_name(i);

                    if (cName.equalsIgnoreCase(spatialTable.getGeomName())) {
                        continue;
                    }

                    String value = stmt.column_string(i);
                    m1.put(cName, value);

                }

                if (time != null && !"".equals(time) && !"...".equals(time)) {
                    String columnValue = m1.get("xczsj");
                    boolean isAdd = false;
                    if (columnValue != null && !"".equals(columnValue)) {
                        if (columnValue.contains(",")) {
                            // 字段中有多个月份，并且用逗号分隔
                            if (time.contains(";")) {
                                // 条件有多个，用逗号隔开
                                String columnValue_split[] = columnValue.split(",");
                                String time_split[] = time.split(";");
                                for( int i = 0; i < columnValue_split.length; i++ ) {
                                    if (isAdd)
                                        break;
                                    for( int j = 0; j < time_split.length; j++ ) {
                                        if (columnValue_split[i].equals(time_split[j])) {
                                            isAdd = true;
                                            break;
                                        }
                                    }

                                }

                            } else {
                                // 条件只有一个
                                String columnValue_split[] = columnValue.split(",");
                                for( int i = 0; i < columnValue_split.length; i++ ) {
                                    if (columnValue_split[i].equals(time)) {
                                        isAdd = true;
                                        break;
                                    }
                                }

                            }

                        } else {
                            // 字段里只有一个月份
                            if (time.contains(";")) {
                                // 条件有多个，用逗号隔开
                                String time_split[] = time.split(";");
                                for( int i = 0; i < time_split.length; i++ ) {
                                    if (time_split[i].equals(columnValue)) {
                                        isAdd = true;
                                        break;
                                    }
                                }

                            } else {
                                // 条件只有一个
                                if (columnValue.equals(time))
                                    isAdd = true;
                            }
                        }
                    }

                    if (isAdd) {
                        m.add(m1);
                    }

                } else {
                    // 如果没有条件，直接加
                    m.add(m1);
                }
            }
        } finally {
            stmt.close();
        }

    }

    public GeometryIterator getGeometryIteratorInBoundsAndInGGgy( String destSrid, SpatialVectorTable table, double n, double s,
            double e, double w, String where, String time ) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getTablePKUID( String boundsSrid, SpatialVectorTable spatialTable, double n, double s, double e, double w,
            String where, String time ) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        boolean doTransform = false;
        if (!spatialTable.getSrid().equals(boundsSrid)) {
            doTransform = true;
        }

        String query = null;

        {
            StringBuilder sbQ = new StringBuilder();
            sbQ.append(" select * from   ");
            sbQ.append(spatialTable.getName());
            sbQ.append(" where ST_Intersects(");
            if (doTransform)
                sbQ.append("ST_Transform(");
            sbQ.append("BuildMBR(");
            sbQ.append(w);
            sbQ.append(", ");
            sbQ.append(s);
            sbQ.append(", ");
            sbQ.append(e);
            sbQ.append(", ");
            sbQ.append(n);
            if (doTransform) {
                sbQ.append(", ");
                sbQ.append(boundsSrid);
                sbQ.append("),");
                sbQ.append(spatialTable.getSrid());
            }
            sbQ.append("),");
            sbQ.append(spatialTable.getGeomName());
            sbQ.append(")");

            if ("".equals(where)) {

            } else {
                sbQ.append(" AND " + where);
            }
            query = sbQ.toString();

            // Logger.i(this, query);
        }

        Stmt stmt = dbJava.prepare(query);
        try {
            while( stmt.step() ) {
                int column_count = stmt.column_count();

                Map<String, String> m = new HashMap<String, String>();
                for( int i = 0; i < column_count; i++ ) {
                    String cName = stmt.column_name(i);
                    String value = stmt.column_string(i);
                    m.put(cName, value);
                }
                // 如果有条件，则符合条件的加
                // 判断m中是否包含time
                String columnValue = m.get("xczsj");
                boolean isAdd = false;
                if (columnValue != null && !"".equals(columnValue)) {
                    if (columnValue.contains(",")) {
                        // 字段中有多个月份，并且用逗号分隔
                        if (time.contains(";")) {
                            // 条件有多个，用逗号隔开
                            String columnValue_split[] = columnValue.split(",");
                            String time_split[] = time.split(";");
                            for( int i = 0; i < columnValue_split.length; i++ ) {
                                if (isAdd)
                                    break;
                                for( int j = 0; j < time_split.length; j++ ) {
                                    if (columnValue_split[i].equals(time_split[j])) {
                                        isAdd = true;
                                        break;
                                    }
                                }

                            }

                        } else {
                            // 条件只有一个
                            String columnValue_split[] = columnValue.split(",");
                            for( int i = 0; i < columnValue_split.length; i++ ) {
                                if (columnValue_split[i].equals(time)) {
                                    isAdd = true;
                                    break;
                                }
                            }

                        }

                    } else {
                        // 字段里只有一个月份
                        if (time.contains(";")) {
                            // 条件有多个，用逗号隔开
                            String time_split[] = time.split(";");
                            for( int i = 0; i < time_split.length; i++ ) {
                                if (time_split[i].equals(columnValue)) {
                                    isAdd = true;
                                    break;
                                }
                            }

                        } else {
                            // 条件只有一个
                            if (columnValue.equals(time))
                                isAdd = true;
                        }
                    }
                }

                if (isAdd) {
                    sb.append(m.get("PK_UID"));
                    sb.append(",");
                }

            }
            if (sb.toString().lastIndexOf(",") != -1)
                sb.delete(sb.toString().lastIndexOf(","), sb.toString().length());
            sb.append(")");
            return sb.toString();
        } finally {
            stmt.close();
        }
    }

    public String getTablePKUID( String boundsSrid, SpatialVectorTable spatialTable, String where, String time ) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("(");

        String query = null;

        {
            StringBuilder sbQ = new StringBuilder();
            sbQ.append(" select * from   ");
            sbQ.append(spatialTable.getName());

            if ("".equals(where)) {

            } else {
                sbQ.append(" where " + where);
            }
            query = sbQ.toString();

            // Logger.i(this, query);
        }

        Stmt stmt = dbJava.prepare(query);
        try {
            while( stmt.step() ) {
                int column_count = stmt.column_count();

                Map<String, String> m = new HashMap<String, String>();
                for( int i = 0; i < column_count; i++ ) {
                    String cName = stmt.column_name(i);
                    String value = stmt.column_string(i);
                    m.put(cName, value);
                }
                // 如果有条件，则符合条件的加
                // 判断m中是否包含time
                String columnValue = m.get("xczsj");
                boolean isAdd = false;
                if (columnValue != null && !"".equals(columnValue)) {
                    if (columnValue.contains(",")) {
                        // 字段中有多个月份，并且用逗号分隔
                        if (time.contains(";")) {
                            // 条件有多个，用逗号隔开
                            String columnValue_split[] = columnValue.split(",");
                            String time_split[] = time.split(";");
                            for( int i = 0; i < columnValue_split.length; i++ ) {
                                if (isAdd)
                                    break;
                                for( int j = 0; j < time_split.length; j++ ) {
                                    if (columnValue_split[i].equals(time_split[j])) {
                                        isAdd = true;
                                        break;
                                    }
                                }

                            }

                        } else {
                            // 条件只有一个
                            String columnValue_split[] = columnValue.split(",");
                            for( int i = 0; i < columnValue_split.length; i++ ) {
                                if (columnValue_split[i].equals(time)) {
                                    isAdd = true;
                                    break;
                                }
                            }

                        }

                    } else {
                        // 字段里只有一个月份
                        if (time.contains(";")) {
                            // 条件有多个，用逗号隔开
                            String time_split[] = time.split(";");
                            for( int i = 0; i < time_split.length; i++ ) {
                                if (time_split[i].equals(columnValue)) {
                                    isAdd = true;
                                    break;
                                }
                            }

                        } else {
                            // 条件只有一个
                            if (columnValue.equals(time))
                                isAdd = true;
                        }
                    }
                }

                if (isAdd) {
                    sb.append(m.get("PK_UID"));
                    sb.append(",");
                }

            }
            if (sb.toString().lastIndexOf(",") != -1)
                sb.delete(sb.toString().lastIndexOf(","), sb.toString().length());
            sb.append(")");
            return sb.toString();
        } finally {
            stmt.close();
        }

    }

    public void intersectionToStringBBOXFromWhere( String boundsSrid, String sql, SpatialVectorTable spatialTable,
            List<LinkedHashMap<String, String>> m, String where ) throws Exception {
        String query;
        {
            StringBuilder sbQ = new StringBuilder();
            sbQ.append(sql);

            query = sbQ.toString();

            // Logger.i(this, query);
        }

        Stmt stmt = dbJava.prepare(query);
        try {
            while( stmt.step() ) {
                int column_count = stmt.column_count();
                LinkedHashMap<String, String> m1 = new LinkedHashMap<String, String>();
                for( int i = 0; i < column_count; i++ ) {
                    String cName = stmt.column_name(i);

                    if (cName.equalsIgnoreCase(spatialTable.getGeomName())) {
                        continue;
                    }

                    String value = stmt.column_string(i);
                    m1.put(cName, value);

                }
                m.add(m1);
            }
        } finally {
            stmt.close();
        }

    }

    @Override
    public List<SpatialVectorTable> getSpatialVectorTables( boolean forceRead ) throws Exception {
        if (vectorTableList == null || forceRead) {
            vectorTableList = new ArrayList<SpatialVectorTable>();

            StringBuilder sb3 = new StringBuilder();
            sb3.append("select ");
            sb3.append(METADATA_TABLE_NAME);
            sb3.append(", ");
            sb3.append(METADATA_GEOMETRY_COLUMN);
            sb3.append(", ");
            sb3.append(METADATA_GEOMETRY_TYPE3);
            sb3.append(",");
            sb3.append(METADATA_SRID);
            sb3.append(" from ");
            sb3.append(METADATA_TABLE_GEOMETRY_COLUMNS);
            sb3.append(";");
            String query3 = sb3.toString();

            boolean is3 = true;
            Stmt stmt = null;
            try {
                stmt = dbJava.prepare(query3);
            } catch (java.lang.Exception e) {
                // try with spatialite 4 syntax
                StringBuilder sb4 = new StringBuilder();
                sb4.append("select ");
                sb4.append(METADATA_TABLE_NAME);
                sb4.append(", ");
                sb4.append(METADATA_GEOMETRY_COLUMN);
                sb4.append(", ");
                sb4.append(METADATA_GEOMETRY_TYPE4);
                sb4.append(",");
                sb4.append(METADATA_SRID);
                sb4.append(" from ");
                sb4.append(METADATA_TABLE_GEOMETRY_COLUMNS);
                sb4.append(";");
                String query4 = sb4.toString();
                stmt = dbJava.prepare(query4);
                is3 = false;
            }
            try {
                while( stmt.step() ) {
                    String name = stmt.column_string(0);
                    String geomName = stmt.column_string(1);

                    int geomType = 0;
                    if (is3) {
                        String type = stmt.column_string(2);
                        geomType = GeometryType.forValue(type);
                    } else {
                        geomType = stmt.column_int(2);
                    }

                    String srid = String.valueOf(stmt.column_int(3));
                    double[] center = new double[]{116, 40};
                    double[] bound = new double[4];
                    SpatialVectorTable table = new SpatialVectorTable(dbPath, name, geomName, geomType, srid, center, bound, "");
                    vectorTableList.add(table);
                }
            } finally {
                stmt.close();
            }

            // now read styles
            // checkPropertiesTable();

            // assign the styles
            for( SpatialVectorTable spatialTable : vectorTableList ) {
                /*Style style4Table = getStyle4Table(spatialTable.getName());
                if (style4Table == null) {
                    Style style = new Style();
                    spatialTable.setStyle(style);
                } else {
                    spatialTable.setStyle(style4Table);
                }*/
                Style style = new Style();
                spatialTable.setStyle(style);
            }
        }
        OrderComparator orderComparator = new OrderComparator();
        Collections.sort(vectorTableList, orderComparator);

        return vectorTableList;
    }

    @Override
    public List<SpatialRasterTable> getSpatialRasterTables( boolean forceRead ) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
