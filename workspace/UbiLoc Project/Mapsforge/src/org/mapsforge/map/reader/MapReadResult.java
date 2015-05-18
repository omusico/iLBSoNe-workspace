package org.mapsforge.map.reader;

import java.util.List;

public class MapReadResult
{
  public final boolean isWater;
  public final List<PointOfInterest> pointOfInterests;
  public final List<Way> ways;

  MapReadResult(MapReadResultBuilder mapReadResultBuilder)
  {
    this.pointOfInterests = mapReadResultBuilder.pointOfInterests;
    this.ways = mapReadResultBuilder.ways;
    this.isWater = mapReadResultBuilder.isWater;
  }
}