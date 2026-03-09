package com.github.idelstak.friction.ui.flow;

import com.github.idelstak.friction.ui.effect.*;

final class FailureReadPort implements ReadPort {

  @Override
  public PullList list() {
    return new PullList.Fail("source-fail", "upstream down", "load");
  }

  @Override
  public PullDetail detail(String id) {
    return new PullDetail.Fail("source-fail", "upstream down", id);
  }
}
