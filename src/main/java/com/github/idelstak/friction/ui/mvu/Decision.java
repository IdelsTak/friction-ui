package com.github.idelstak.friction.ui.mvu;

import com.github.idelstak.friction.ui.state.*;
import java.util.*;

public record Decision(Model model, List<Effect> effects) {
}
