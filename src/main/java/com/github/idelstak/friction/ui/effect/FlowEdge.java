package com.github.idelstak.friction.ui.effect;

import io.reactivex.rxjava3.core.*;

public record FlowEdge(Scheduler io, Scheduler ui, Act act) {
}
