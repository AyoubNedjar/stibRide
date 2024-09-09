package presentation;

import javafx.application.Platform;
import javafx.concurrent.Task;
import model.StibModel;
import model.dto.FavoriteDto;
import model.dto.StationDto;
import model.exception.RepositoryException;
import model.util.Observable;
import model.util.Observer;
import view.StibViewFXML;

import java.util.List;

public class Presenter implements Observer {

    private StibModel model;
    private StibViewFXML view;

    public Presenter(StibModel model, StibViewFXML view) {
        this.model = model;
        this.view = view;
        this.model.addObserver(this);
        this.view.setPresenter(this);
    }

    public void initialize() throws RepositoryException {
        view.initialize(model.getStations(), model.getShortestPath(), model.getFavorites());
    }

    public void getShortestPath(int source, int destination) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                model.shortestPath(source, destination);
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    view.setPathStations(model.getShortestPath());
                });
            }

            @Override
            protected void failed() {
                Throwable e = getException();
                Platform.runLater(() -> {
                    // Implement showError in the view to display this message
                    //view.showError("Error calculating the shortest path: " + e.getMessage());
                    e.printStackTrace();
                });
            }
        };

        new Thread(task).start();
    }

    public void addFav(String nom, int source, int destination) {
        try {
            StationDto s = model.getStation(source);
            StationDto d = model.getStation(destination);
            if (s != null && d != null) {
                FavoriteDto item = new FavoriteDto(nom, s, d);
                model.addFavorite(item);
                view.closeFavorite();
            } else {
                // Implement showError in the view to display this message
                //view.showError("Invalid station data");
            }
        } catch (RepositoryException e) {
            // view.showError("Failed to add favorite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onFavoriteDeleteRequested(String name) {
        try {
            model.deleteFav(name);
            view.updateFavoriteList(model.getFavorites());
            view.closeFavorite();
        } catch (RepositoryException e) {
            //  view.showError("Error deleting favorite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showFavoriteStage(String name) throws RepositoryException {
        FavoriteDto fav = model.getFavorite(name);
        if (fav != null) {
            getShortestPath(fav.getSource().getKey(), fav.getDestination().getKey());
            view.showFavorite(name, model.getShortestPath());
        }
    }

    public void changeLang(String lang) throws RepositoryException {
        model.changeLangage();
        view.initialize(model.getStations(), model.getShortestPath(), model.getFavorites());
        view.changeItemLang(model.getLanguage());
        view.closeFavorite();
    }

    @Override
    public void update(Observable observable, Object arg) {
        try {
            view.setPathStations(model.getShortestPath());
            view.updateFavoriteList(model.getFavorites());
        } catch (RepositoryException e) {
            //view.showError("Failed to update favorites: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
