import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.StibModel;
import presentation.Presenter;
import view.StibViewFXML;

public class Main  extends Application {
    @Override
    public void start(Stage stage) throws Exception {



        StibModel model = new StibModel();
        Presenter presenter = new Presenter(model, new StibViewFXML(stage));
        presenter.initialize();


    }
}
