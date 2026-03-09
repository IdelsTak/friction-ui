package com.github.idelstak.friction.ui.state;

import java.time.*;

public record SourceState(String uri, Instant at, String author, String clock) {
}
