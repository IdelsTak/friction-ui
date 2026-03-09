package com.github.idelstak.friction.ui.state;

import java.time.*;

public record ActivityState(int prevalence, double intensity, Duration persistence) {
}
