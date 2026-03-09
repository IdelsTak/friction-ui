package com.github.idelstak.friction.ui.app;

import com.github.idelstak.friction.ui.state.*;
import java.util.*;

public final class DefaultSeed implements Seed {

  @Override
  public Model model() {
    return new Model(
        new ListState(new ScreenMode.Idle(), List.of(), Optional.empty(), Optional.empty()),
        new DetailState(new ScreenMode.Idle(), Optional.empty(), List.of(), Optional.empty())
    );
  }
}
