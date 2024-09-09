package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Language;
import model.dto.FavoriteDto;
import model.dto.StationDto;
import model.exception.RepositoryException;
import presentation.Presenter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class StibViewFXML
        //  extends Application
{

    private StibController stibCtrl;
    private FavoriteController favoriteCtrl;
    private Stage favoriteStage;


    public StibViewFXML(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/stibView.fxml"));
        loader.setController(new StibController());
        Pane root = loader.load();

        stibCtrl = loader.getController();

        Scene scene = new Scene(root);

        stage.setResizable(false);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/logo.png"))));
        stage.setTitle("HE2B ESI - Projet STIB");
        stage.setScene(scene);
        stage.show();

        makeFavoriteStage(stage);
    }

    private void makeFavoriteStage(Stage stage) throws IOException {
        favoriteStage = new Stage();
        favoriteStage.initOwner(stage);

        FXMLLoader loaderFavorite = new FXMLLoader(getClass().getResource("/fxml/FavoriteFxml.fxml"));
        loaderFavorite.setController(new FavoriteController());

        Pane rootFavorite = loaderFavorite.load();
        Scene sceneFavorite = new Scene(rootFavorite);

        favoriteCtrl = loaderFavorite.getController();
        favoriteStage.setResizable(false);
        favoriteStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/logo.png"))));
        favoriteStage.setTitle("HE2B ESI - Gestion favoris");
        favoriteStage.setScene(sceneFavorite);
    }

    public void showFavorite(String name, List<StationDto> path) {
        favoriteCtrl.initialize(name, path);
        favoriteStage.show();
        //Initialiser tout les composants
    }


    public void initialize(List<StationDto> stations, List<StationDto> path, List<FavoriteDto> allFavorites) {

        stibCtrl.initialize(stations, path, allFavorites);
    }

    public void setPresenter(Presenter presenter) {
        stibCtrl.setPresenter(presenter);
        favoriteCtrl.setPresenter(presenter);
    }

    public void setPathStations(List<StationDto> newPath) {
         stibCtrl.setPathStations(newPath);
    }

    public void closeFavorite() {
        favoriteStage.close();
    }

    public void updateFavoriteList(List<FavoriteDto> favoriteList){
        stibCtrl.setMenuFavorite(favoriteList);
    }


    public void changeItemLang(Language lang){
        stibCtrl.changeItemLang(lang);
    }



}
