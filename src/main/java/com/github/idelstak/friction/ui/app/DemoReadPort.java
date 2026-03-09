package com.github.idelstak.friction.ui.app;

import com.github.idelstak.friction.core.metrics.*;
import com.github.idelstak.friction.core.readmodel.*;
import com.github.idelstak.friction.ui.effect.*;
import java.time.*;
import java.util.*;

public final class DemoReadPort implements ReadPort {

  @Override
  public PullList list() {
    var one = new FrictionSummary(
        "f-1",
        "build pipeline unstable",
        new ActivityMetrics(7, 0.66, Duration.ofHours(12)),
        new TrendMetrics(Optional.of(0.15), Optional.of(0.75))
    );
    return new PullList.Ok(List.of(one));
  }

  @Override
  public PullDetail detail(String id) {
    if (!id.equals("f-1")) {
      return new PullDetail.Fail("missing-friction", "no detail found", id);
    }
    var detail = new ObservationDetail(
        "o-1",
        id,
        new DetailProvenance(
            "https://reddit.com/r/java/comments/one",
            Instant.parse("2026-03-09T10:00:00Z"),
            new KnownAuthor("autør")
        ),
        "pipeline flakes on merge"
    );
    return new PullDetail.Ok(id, List.of(detail));
  }
}
