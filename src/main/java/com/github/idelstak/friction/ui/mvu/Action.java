package com.github.idelstak.friction.ui.mvu;

import com.github.idelstak.friction.ui.state.*;
import java.util.*;

public sealed interface Action {

  record LoadRequested() implements Action {
  }

  record LoadSucceeded(List<FrictionItem> items) implements Action {
  }

  record LoadFailed(FaultState fault) implements Action {
  }

  record FrictionPicked(String id) implements Action {
  }

  record DetailRequested(String id) implements Action {
  }

  record DetailSucceeded(String id, List<ObservationItem> items) implements Action {
  }

  record DetailFailed(FaultState fault) implements Action {
  }
}
