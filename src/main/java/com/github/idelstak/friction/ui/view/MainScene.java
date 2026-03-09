package com.github.idelstak.friction.ui.view;

import com.github.idelstak.friction.ui.effect.*;
import com.github.idelstak.friction.ui.mvu.*;
import com.github.idelstak.friction.ui.state.*;
import java.util.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public final class MainScene {

  @FXML
  private BorderPane root;
  @FXML
  private Label listState;
  @FXML
  private Label detailState;
  @FXML
  private ListView<FrictionItem> summary;
  @FXML
  private ListView<ObservationItem> detail;
  @FXML
  private Button reload;

  private final ObservableList<FrictionItem> summaries;
  private final ObservableList<ObservationItem> details;
  private Act act;

  public MainScene() {
    summaries = FXCollections.observableArrayList();
    details = FXCollections.observableArrayList();
    act = null;
  }

  @FXML
  void initialize() {
    summary.setItems(summaries);
    detail.setItems(details);
    summary.setCellFactory(_ -> new ListCell<>() {
      @Override
      protected void updateItem(FrictionItem item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty || item == null ? "" : item.title());
      }
    });
    detail.setCellFactory(_ -> new ListCell<>() {
      @Override
      protected void updateItem(ObservationItem item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty || item == null ? "" : item.excerpt());
      }
    });
    summary.getSelectionModel().selectedItemProperty().addListener((_, _, picked) -> {
      if (picked != null && act != null) {
        act.put(new Action.FrictionPicked(picked.id()));
      }
    });
  }

  @FXML
  void reload() {
    if (act != null) {
      act.put(new Action.LoadRequested());
    }
  }

  public Parent root() {
    return root;
  }

  public void bind(Act act) {
    this.act = act;
  }

  public void show(Model model) {
    if (!sameSummary(model.list().items())) {
      summaries.setAll(model.list().items());
    }
    if (!sameDetail(model.detail().items())) {
      details.setAll(model.detail().items());
    }
    listState.setText(mode(model.list().mode(), model.list().fault()));
    detailState.setText(mode(model.detail().mode(), model.detail().fault()));
  }

  private String mode(ScreenMode mode, Optional<FaultState> fault) {
    return switch (mode) {
      case ScreenMode.Idle _ -> "idle";
      case ScreenMode.Loading _ -> "loading";
      case ScreenMode.Ready _ -> "ready";
      case ScreenMode.Empty _ -> "empty";
      case ScreenMode.Failed _ -> "error:" + fault.map(FaultState::code).orElse("unknown");
    };
  }

  private boolean sameSummary(List<FrictionItem> items) {
    if (items.size() != summaries.size()) {
      return false;
    }
    for (var index = 0; index < items.size(); index++) {
      if (!items.get(index).equals(summaries.get(index))) {
        return false;
      }
    }
    return true;
  }

  private boolean sameDetail(List<ObservationItem> items) {
    if (items.size() != details.size()) {
      return false;
    }
    for (var index = 0; index < items.size(); index++) {
      if (!items.get(index).equals(details.get(index))) {
        return false;
      }
    }
    return true;
  }

}
