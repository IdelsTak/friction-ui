package com.github.idelstak.friction.ui.state;

import java.util.*;

public record DetailState(ScreenMode mode, Optional<String> id, List<ObservationItem> items, Optional<FaultState> fault) {
}
