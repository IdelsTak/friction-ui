package com.github.idelstak.friction.ui.effect;

import com.github.idelstak.friction.core.readmodel.*;
import com.github.idelstak.friction.ui.state.*;
import java.time.format.*;
import java.util.*;

public final class MapReadModel implements ReadMap {

  @Override
  public List<FrictionItem> summary(List<FrictionSummary> items) {
    var mapped = new ArrayList<FrictionItem>();
    for (var item : items) {
      var slope = item.trend().slope().orElse(0.0);
      var trend = new TrendState(slope, direction(slope, 0.001));
      var stats = new ActivityState(item.activity().prevalence(), item.activity().intensity(), item.activity().persistence());
      mapped.add(new FrictionItem(item.frictionId(), item.descriptor(), stats, trend));
    }
    return List.copyOf(mapped);
  }

  @Override
  public List<ObservationItem> detail(List<ObservationDetail> items) {
    var mapped = new ArrayList<ObservationItem>();
    for (var item : items) {
      var author = switch (item.provenance().authorHandle()) {
        case KnownAuthor known -> known.value();
        case AnonymousAuthor _ -> "unknown";
      };
      var stamp = DateTimeFormatter.ISO_INSTANT.format(item.provenance().timestamp());
      var provenance = new SourceState(item.provenance().uri(), item.provenance().timestamp(), author, stamp);
      mapped.add(new ObservationItem(item.observationId(), provenance, item.contentExcerpt(), false));
    }
    return List.copyOf(mapped);
  }

  private String direction(double slope, double epsilon) {
    if (slope > epsilon) {
      return "UP";
    }
    if (slope < -epsilon) {
      return "DOWN";
    }
    return "FLAT";
  }
}
