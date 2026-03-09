package com.github.idelstak.friction.ui.app;

import com.github.idelstak.friction.ui.effect.*;
import com.github.idelstak.friction.ui.mvu.*;
import com.github.idelstak.friction.ui.state.*;

public final class AppLoop implements Act, AutoCloseable {

  private final LoopKit kit;
  private Model model;

  public AppLoop(Model model, LoopKit kit) {
    this.model = model;
    this.kit = kit;
    kit.view().bind(this);
    kit.view().show(this.model);
  }

  @Override
  public void put(Action action) {
    var decision = kit.step().update(model, action);
    model = decision.model();
    kit.view().show(model);
    kit.effects().run(decision.effects());
  }

  @Override
  public void close() {
    kit.effects().close();
  }

  public Model model() {
    return model;
  }
}
