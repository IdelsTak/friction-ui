package com.github.idelstak.friction.ui.mvu;

import com.github.idelstak.friction.ui.state.*;
import java.util.*;

public final class Update implements Step {

  @Override
  public Decision update(Model model, Action action) {
    return switch (action) {
      case Action.LoadRequested _ -> load(model);
      case Action.LoadSucceeded ok -> loaded(model, ok.items());
      case Action.LoadFailed fail -> loadFailed(model, fail.fault());
      case Action.FrictionPicked pick -> picked(model, pick.id());
      case Action.DetailRequested detail -> detail(model, detail.id());
      case Action.DetailSucceeded ok -> detailed(model, ok.id(), ok.items());
      case Action.DetailFailed fail -> detailFailed(model, fail.fault());
    };
  }

  private Decision load(Model model) {
    var next = new Model(new ListState(new ScreenMode.Loading(), model.list().items(), model.list().picked(), Optional.empty()), model.detail());
    return new Decision(next, List.of(new Effect.LoadList()));
  }

  private Decision loaded(Model model, List<FrictionItem> items) {
    var mode = items.isEmpty() ? new ScreenMode.Empty() : new ScreenMode.Ready();
    var next = new Model(new ListState(mode, items, Optional.empty(), Optional.empty()), model.detail());
    return new Decision(next, List.of());
  }

  private Decision loadFailed(Model model, FaultState fault) {
    var next = new Model(new ListState(new ScreenMode.Failed(), List.of(), Optional.empty(), Optional.of(fault)), model.detail());
    return new Decision(next, List.of());
  }

  private Decision picked(Model model, String id) {
    var list = new ListState(model.list().mode(), model.list().items(), Optional.of(id), Optional.empty());
    var detail = new DetailState(new ScreenMode.Loading(), Optional.of(id), List.of(), Optional.empty());
    return new Decision(new Model(list, detail), List.of(new Effect.LoadDetail(id)));
  }

  private Decision detail(Model model, String id) {
    var detail = new DetailState(new ScreenMode.Loading(), Optional.of(id), List.of(), Optional.empty());
    return new Decision(new Model(model.list(), detail), List.of(new Effect.LoadDetail(id)));
  }

  private Decision detailed(Model model, String id, List<ObservationItem> items) {
    var mode = items.isEmpty() ? new ScreenMode.Empty() : new ScreenMode.Ready();
    var next = new Model(model.list(), new DetailState(mode, Optional.of(id), items, Optional.empty()));
    return new Decision(next, List.of());
  }

  private Decision detailFailed(Model model, FaultState fault) {
    var next = new Model(model.list(), new DetailState(new ScreenMode.Failed(), model.detail().id(), List.of(), Optional.of(fault)));
    return new Decision(next, List.of());
  }
}
