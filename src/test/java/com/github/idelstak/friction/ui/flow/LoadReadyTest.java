package com.github.idelstak.friction.ui.flow;

import com.github.idelstak.friction.ui.app.*;
import com.github.idelstak.friction.ui.effect.*;
import com.github.idelstak.friction.ui.mvu.*;
import com.github.idelstak.friction.ui.view.*;
import io.reactivex.rxjava3.schedulers.*;
import java.util.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.testfx.api.*;
import org.testfx.framework.junit5.*;
import org.testfx.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
final class LoadReadyTest {

  private AppLoop loop;

  @Start
  void start(Stage stage) {
    var id = UUID.randomUUID().toString();
    var view = new FxmlView();
    var edge = new FlowEdge(Schedulers.trampoline(), Schedulers.trampoline(), action -> loop.put(action));
    var kit = new LoopKit(new Update(), new RxEffect(new SuccessReadPort(id), new MapReadModel(), edge), view);
    loop = new AppLoop(new DefaultSeed().model(), kit);
    stage.setScene(new Scene(view.root(), 900, 520));
    stage.show();
    loop.put(new Action.LoadRequested());
  }

  @AfterEach
  void close() {
    loop.close();
  }

  @Test
  void showsReadyStateAfterInitialLoad(FxRobot robot) {
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals("ready", robot.lookup("#list-state").queryAs(Label.class).getText(), "list state did not become ready after load");
  }
}
