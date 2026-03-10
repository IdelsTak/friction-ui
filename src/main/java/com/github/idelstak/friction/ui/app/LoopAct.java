package com.github.idelstak.friction.ui.app;

import com.github.idelstak.friction.ui.effect.*;
import com.github.idelstak.friction.ui.mvu.*;

public final class LoopAct implements Act {

    private AppLoop loop;

    public void bind(AppLoop loop) {
        this.loop = loop;
    }

    @Override
    public void put(Action action) {
        if (loop == null) {
            throw new IllegalStateException("loop not bound");
        }
        loop.put(action);
    }
}
