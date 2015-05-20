package org.mapsforge.map.reader;

import java.util.List;

import org.mapsforge.core.GeoPoint;
import org.mapsforge.core.Tag;

public class Way
{
  public final GeoPoint labelPosition;
  public final byte layer;
  public final List<Tag> tags;
  public final float[][] wayNodes;

  Way(byte layer, List<Tag> tags, float[][] wayNodes, GeoPoint labelPosition)
  {
    this.layer = layer;
    this.tags = tags;
    this.wayNodes = wayNodes;
    this.labelPosition = labelPosition;
  }
}