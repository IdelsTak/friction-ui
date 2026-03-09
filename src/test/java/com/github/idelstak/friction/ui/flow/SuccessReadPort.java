package com.github.idelstak.friction.ui.flow;

import com.github.idelstak.friction.core.metrics.*;
import com.github.idelstak.friction.core.readmodel.*;
import com.github.idelstak.friction.ui.effect.*;
import java.time.*;
import java.util.*;

final class SuccessReadPort implements ReadPort {

  private final String id;

  SuccessReadPort(String id) {
    this.id = id;
  }

  @Override
  public PullList list() {
    var one = new FrictionSummary(
        id,
        "rétry flow unstable",
        new ActivityMetrics(3, 0.4, Duration.ofMinutes(20)),
        new TrendMetrics(Optional.of(0.2), Optional.of(0.8))
    );
    return new PullList.Ok(List.of(one));
  }

  @Override
  public PullDetail detail(String friction) {
    var one = new ObservationDetail(
        UUID.randomUUID().toString(),
        friction,
        new DetailProvenance("https://reddit.com/x", Instant.now(), new KnownAuthor("åuthor")),
        "detail arrives"
    );
    return new PullDetail.Ok(friction, List.of(one));
  }
}
