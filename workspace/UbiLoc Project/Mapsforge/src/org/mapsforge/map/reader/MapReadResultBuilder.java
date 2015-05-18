package org.mapsforge.map.reader;

import java.util.ArrayList;
import java.util.List;

class MapReadResultBuilder
{
  boolean isWater;
  final List<PointOfInterest> pointOfInterests;
  final List<Way> ways;

  MapReadResultBuilder()
  {
    this.pointOfInterests = new ArrayList();
    this.ways = new ArrayList();
  }

  void add(PoiWayBundle poiWayBundle) {
    this.pointOfInterests.addAll(poiWayBundle.pois);
    this.ways.addAll(poiWayBundle.ways);
  }

  MapReadResult build() {
    return new MapReadResult(this);
  }
}