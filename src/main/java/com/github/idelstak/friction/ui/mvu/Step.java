package com.github.idelstak.friction.ui.mvu;

import com.github.idelstak.friction.ui.state.*;

public interface Step {

  Decision update(Model model, Action action);
}
