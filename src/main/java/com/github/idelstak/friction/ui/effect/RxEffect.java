package com.github.idelstak.friction.ui.effect;

import com.github.idelstak.friction.ui.mvu.*;
import com.github.idelstak.friction.ui.state.*;
import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.disposables.*;
import java.util.List;

public final class RxEffect implements RunEffect {

  private final ReadPort port;
  private final ReadMap map;
  private final FlowEdge edge;
  private final CompositeDisposable bag;

  public RxEffect(ReadPort port, ReadMap map, FlowEdge edge) {
    this.port = port;
    this.map = map;
    this.edge = edge;
    bag = new CompositeDisposable();
  }

  @Override
  public void run(List<Effect> effects) {
    for (var effect : effects) {
      bag.add(next(effect).subscribeOn(edge.io()).observeOn(edge.ui()).subscribe(edge.act()::put));
    }
  }

  private Observable<Action> next(Effect effect) {
    return switch (effect) {
      case Effect.LoadList _ -> list();
      case Effect.LoadDetail one -> detail(one.id());
    };
  }

  private Observable<Action> list() {
    return Observable.fromCallable(port::list).map(result -> {
      if (result instanceof ReadPort.PullList.Ok ok) {
        return new Action.LoadSucceeded(map.summary(ok.items()));
      }
      if (result instanceof ReadPort.PullList.Fail fail) {
        return new Action.LoadFailed(new FaultState(fail.code(), fail.message(), fail.context()));
      }
      throw new IllegalStateException("unsupported pull list result");
    });
  }

  private Observable<Action> detail(String id) {
    return Observable.fromCallable(() -> port.detail(id)).map(result -> {
      if (result instanceof ReadPort.PullDetail.Ok ok) {
        return new Action.DetailSucceeded(ok.id(), map.detail(ok.items()));
      }
      if (result instanceof ReadPort.PullDetail.Fail fail) {
        return new Action.DetailFailed(new FaultState(fail.code(), fail.message(), fail.context()));
      }
      throw new IllegalStateException("unsupported pull detail result");
    });
  }

  @Override
  public void close() {
    bag.dispose();
  }
}
