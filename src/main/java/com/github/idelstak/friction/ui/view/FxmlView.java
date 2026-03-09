package com.github.idelstak.friction.ui.view;

import com.github.idelstak.friction.ui.effect.*;
import com.github.idelstak.friction.ui.state.*;
import java.io.*;
import javafx.fxml.*;
import javafx.scene.*;

public final class FxmlView implements View {

  private final Parent root;
  private final MainScene scene;

  public FxmlView() {
    try {
      var loader = new FXMLLoader(FxmlView.class.getResource("/com/github/idelstak/friction/ui/view/main.fxml"));
      root = loader.load();
      scene = loader.getController();
    } catch (IOException problem) {
      throw new IllegalStateException("cannot load main.fxml", problem);
    }
  }

  @Override
  public Parent root() {
    return root;
  }

  @Override
  public void bind(Act act) {
    scene.bind(act);
  }

  @Override
  public void show(Model model) {
    scene.show(model);
  }
}
