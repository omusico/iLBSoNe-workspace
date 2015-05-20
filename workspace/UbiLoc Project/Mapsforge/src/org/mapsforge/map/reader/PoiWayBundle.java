package org.mapsforge.map.reader;

import java.util.List;

class PoiWayBundle
{
  final List<PointOfInterest> pois;
  final List<Way> ways;

  PoiWayBundle(List<PointOfInterest> pois, List<Way> ways)
  {
    this.pois = pois;
    this.ways = ways;
  }
}