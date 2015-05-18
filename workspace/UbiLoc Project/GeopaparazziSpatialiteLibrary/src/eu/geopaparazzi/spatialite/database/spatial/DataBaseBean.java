package eu.geopaparazzi.spatialite.database.spatial;
/*
 * 根据此对象数组初始化所有 与 sqlite相关的数据
 * 其中
 */
public class DataBaseBean {

    private String dataType;
    // 库文件绝对路径，全部要有
    private String fileAbsolutePath;
    // 表名,除矢量表外，全部要有
    private String tableName;
    // 最小级别，除矢量表外，全部要有
    private int minScale;
    // 最大级别除矢量表外，全部要有
    private int maxScale;
    // 中心点纬度除矢量表外，全部要有
    private double centerLat;
    // 中心点经度除矢量表外，全部要有
    private double centerLng;
    public String getFileAbsolutePath() {
        return fileAbsolutePath;
    }
    public void setFileAbsolutePath( String fileAbsolutePath ) {
        this.fileAbsolutePath = fileAbsolutePath;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName( String tableName ) {
        this.tableName = tableName;
    }
    public int getMinScale() {
        return minScale;
    }
    public void setMinScale( int minScale ) {
        this.minScale = minScale;
    }
    public int getMaxScale() {
        return maxScale;
    }
    public void setMaxScale( int maxScale ) {
        this.maxScale = maxScale;
    }
    public double getCenterLat() {
        return centerLat;
    }
    public void setCenterLat( double centerLat ) {
        this.centerLat = centerLat;
    }
    public double getCenterLng() {
        return centerLng;
    }
    public void setCenterLng( double centerLng ) {
        this.centerLng = centerLng;
    }
    public String getDataType() {
        return dataType;
    }
    public void setDataType( String dataType ) {
        this.dataType = dataType;
    }

}
