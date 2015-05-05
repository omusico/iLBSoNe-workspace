package eu.geopaparazzi.spatialite.database.spatial;
/*
 * ���ݴ˶��������ʼ������ �� sqlite��ص�����
 * ����
 */
public class DataBaseBean {

    private String dataType;
    // ���ļ�����·����ȫ��Ҫ��
    private String fileAbsolutePath;
    // ����,��ʸ�����⣬ȫ��Ҫ��
    private String tableName;
    // ��С���𣬳�ʸ�����⣬ȫ��Ҫ��
    private int minScale;
    // ��󼶱��ʸ�����⣬ȫ��Ҫ��
    private int maxScale;
    // ���ĵ�γ�ȳ�ʸ�����⣬ȫ��Ҫ��
    private double centerLat;
    // ���ĵ㾭�ȳ�ʸ�����⣬ȫ��Ҫ��
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
