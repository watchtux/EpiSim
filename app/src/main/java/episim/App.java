package episim;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.guice.MvvmfxGuiceApplication;
import episim.view.MainView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * Classe principale de l'application
 */
public class App extends MvvmfxGuiceApplication {
    public String getGreeting() {
        return "Hello World!";
    }

    @Override
    public void startMvvmfx(Stage primaryStage) throws Exception {
        Parent parent = FluentViewLoader.fxmlView(MainView.class).load().getView();
        var scene = new Scene(parent);

        // Apply metro theme
        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(scene);

        primaryStage.setTitle("Epidemic Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
