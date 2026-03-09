package com.github.idelstak.friction.ui.state;

import java.util.*;

public record ListState(ScreenMode mode, List<FrictionItem> items, Optional<String> picked, Optional<FaultState> fault) {
}
