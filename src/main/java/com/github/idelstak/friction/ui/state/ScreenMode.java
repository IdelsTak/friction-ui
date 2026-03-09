package com.github.idelstak.friction.ui.state;

public sealed interface ScreenMode {

    record Idle() implements ScreenMode {
    }

    record Loading() implements ScreenMode {
    }

    record Ready() implements ScreenMode {
    }

    record Empty() implements ScreenMode {
    }

    record Failed() implements ScreenMode {
    }
}
