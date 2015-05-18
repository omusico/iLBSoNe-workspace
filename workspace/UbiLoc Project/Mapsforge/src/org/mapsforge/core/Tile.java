package org.mapsforge.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Tile
  implements Serializable
{
  public static final byte TILE_BYTES_PER_PIXEL = 2;
  public static final int TILE_SIZE = 256;
  public static final int TILE_SIZE_IN_BYTES = 131072;
  private static final long serialVersionUID = 1L;
  public final long tileX;
  public final long tileY;
  public final byte zoomLevel;
  private transient int hashCodeValue;
  private transient long pixelX;
  private transient long pixelY;

  public Tile(long tileX, long tileY, byte zoomLevel)
  {
    this.tileX = tileX;
    this.tileY = tileY;
    this.zoomLevel = zoomLevel;
    calculateTransientValues();
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (!(obj instanceof Tile))
      return false;

    Tile other = (Tile)obj;
    if (this.tileX != other.tileX)
      return false;
    if (this.tileY != other.tileY)
      return false;

    return (this.zoomLevel == other.zoomLevel);
  }

  public long getPixelX()
  {
    return this.pixelX;
  }

  public long getPixelY()
  {
    return this.pixelY;
  }

  public int hashCode()
  {
    return this.hashCodeValue;
  }

  public String toString()
  {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Tile [tileX=");
    stringBuilder.append(this.tileX);
    stringBuilder.append(", tileY=");
    stringBuilder.append(this.tileY);
    stringBuilder.append(", zoomLevel=");
    stringBuilder.append(this.zoomLevel);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }

  private int calculateHashCode()
  {
    int result = 7;
    result = 31 * result + (int)(this.tileX ^ this.tileX >>> 32);
    result = 31 * result + (int)(this.tileY ^ this.tileY >>> 32);
    result = 31 * result + this.zoomLevel;
    return result;
  }

  private void calculateTransientValues()
  {
    this.pixelX = (this.tileX * 256L);
    this.pixelY = (this.tileY * 256L);
    this.hashCodeValue = calculateHashCode();
  }

  private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
    objectInputStream.defaultReadObject();
    calculateTransientValues();
  }
}