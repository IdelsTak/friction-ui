package com.github.idelstak.friction.ui.effect;

import com.github.idelstak.friction.core.readmodel.*;
import com.github.idelstak.friction.ui.state.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public interface ReadMap {

  List<FrictionItem> summary(List<FrictionSummary> items);

  List<ObservationItem> detail(List<ObservationDetail> items);
}
