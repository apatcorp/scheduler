package utility;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScreenHandler {
    private static Map<String, ScreenInfo<?>> screenInfoMap = new HashMap<>();

    private static ScreenHandler screenHandler = null;

    public static ScreenHandler getInstance () {
        if (screenHandler == null) screenHandler = new ScreenHandler();

        return screenHandler;
    }

    public void add(String name, String url) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(url));
        try {
            fxmlLoader.setClassLoader(getClass().getClassLoader());
            ScreenInfo<? extends Controller> screenInfo = new ScreenInfo<>(fxmlLoader.load(), fxmlLoader.getController());
            screenInfoMap.put(name, screenInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remove(String name) {
       screenInfoMap.remove(name);
    }

    public ScreenInfo getScreenInfo (String name) {
        return screenInfoMap.getOrDefault(name, null);
    }

    public static class ScreenInfo<T extends Controller> {
        private Pane pane;
        private T controller;

        ScreenInfo(Pane pane, T controller) {
            this.pane = pane;
            this.controller = controller;
        }

        public Pane getPane() {
            return pane;
        }

        public T getController() {
            return controller;
        }
    }
}
