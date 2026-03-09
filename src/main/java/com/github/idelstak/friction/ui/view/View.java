package com.github.idelstak.friction.ui.view;

import com.github.idelstak.friction.ui.effect.*;
import com.github.idelstak.friction.ui.state.*;
import javafx.scene.*;

public interface View {

  Parent root();

  void bind(Act act);

  void show(Model model);
}
