package com.github.idelstak.friction.ui.effect;

import com.github.idelstak.friction.core.readmodel.*;
import java.util.*;

public interface ReadPort {

  PullList list();

  PullDetail detail(String id);

  sealed interface PullList {

    record Ok(List<FrictionSummary> items) implements PullList {
    }

    record Fail(String code, String message, String context) implements PullList {
    }
  }

  sealed interface PullDetail {

    record Ok(String id, List<ObservationDetail> items) implements PullDetail {
    }

    record Fail(String code, String message, String context) implements PullDetail {
    }
  }
}
