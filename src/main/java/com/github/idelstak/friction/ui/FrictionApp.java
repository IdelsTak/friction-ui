package com.github.idelstak.friction.ui;

import com.github.idelstak.friction.ui.app.*;
import com.github.idelstak.friction.ui.effect.*;
import com.github.idelstak.friction.ui.mvu.*;
import com.github.idelstak.friction.ui.view.*;
import io.reactivex.rxjava3.schedulers.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;

public final class FrictionApp extends Application {

    private AppLoop loop;

    @Override
    public void start(Stage stage) {
        var view = new FxmlView();
        var ui = Schedulers.from(Platform::runLater);
        var relay = new LoopAct();
        var edge = new FlowEdge(Schedulers.io(), ui, relay);
        var kit = new LoopKit(new Update(), new RxEffect(new DemoReadPort(), new MapReadModel(), edge), view);
        loop = new AppLoop(new DefaultSeed().model(), kit);
        relay.bind(loop);
        var scene = new Scene(view.root(), 900, 520);
        scene.getStylesheets().add(FrictionApp.class.getResource("/com/github/idelstak/friction/ui/view/app.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Friction");
        stage.show();
        loop.put(new Action.LoadRequested());
    }

    @Override
    public void stop() {
        if (loop != null) {
            loop.close();
        }
    }

}
