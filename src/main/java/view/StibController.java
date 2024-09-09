package view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Language;
import model.dto.FavoriteDto;
import model.dto.StationDto;
import model.exception.RepositoryException;
import org.controlsfx.control.SearchableComboBox;
import presentation.Presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StibController {

    @FXML
    public Menu menuFavorite;

    @FXML
    public CustomMenuItem menuScroll;

    @FXML
    public TextField favText;

    private Presenter presenter;

    private Map<String, Integer> stationMap;

    ObservableList<StationDto> pathStation = FXCollections.observableArrayList();

    @FXML
    public SearchableComboBox<String> sourceStation;

    @FXML
    public SearchableComboBox<String> destinationStation;

    @FXML
    public TableView<StationDto> tableview;

    @FXML
    public TableColumn<StationDto, String> c1;

    @FXML
    public TableColumn<StationDto, String> c2;

    @FXML
    private MenuItem quit;

    @FXML
    private MenuItem lang;


    public void initialize(List<StationDto> stations, List<StationDto> path, List<FavoriteDto> allFavorites) {
        pathStation.clear();

        stationMap = new HashMap<>();
        List<String> stringifyStations = new ArrayList<>();
        for (var station : stations) {
            stringifyStations.add(station.getName());
            stationMap.put(station.getName(), station.getKey());
        }

        ObservableList<String> stationsName = FXCollections.observableArrayList(stringifyStations);

        //initialize the tableview
        c1.setCellValueFactory(new PropertyValueFactory<>("name"));
        c2.setCellValueFactory(new PropertyValueFactory<>("lines"));

        sourceStation.setItems(stationsName);
        destinationStation.setItems(stationsName);
        sourceStation.setValue("DE BROUCKERE");
        destinationStation.setValue("DE BROUCKERE");


        pathStation.addAll(path);
        tableview.setItems(pathStation);

        setMenuFavorite(allFavorites);

        quit.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void setPathStations(List<StationDto> newPath) {
        pathStation.clear();
        pathStation.setAll(newPath);
    }

    public void handleFind() throws RepositoryException {
        int source = stationMap.get(sourceStation.getValue());
        int destination = stationMap.get(destinationStation.getValue());
        presenter.getShortestPath(source, destination);
    }

    public void handleFav() throws RepositoryException {
        var nom = favText.getCharacters().toString();
        if (!nom.trim().isEmpty()) {
            Integer sourceId = stationMap.get(sourceStation.getValue());
            Integer destinationId = stationMap.get(destinationStation.getValue());
            if (sourceId != null && destinationId != null) {
                presenter.addFav(nom, sourceId, destinationId);
            } else {
                // Handle case where source or destination is invalid
            }
        }
    }

    public void changeLang() throws RepositoryException {
        presenter.changeLang(lang.getText());
    }

    public void changeItemLang(Language language) {
        lang.setText(language.name());
    }

    public void setMenuFavorite(List<FavoriteDto> allFavorites) {
        menuFavorite.getItems().clear();
        if (allFavorites.isEmpty()) {
            MenuItem menuItem = new MenuItem("Aucun trajet pour l'instant");
            menuFavorite.getItems().add(menuItem);
            menuItem.setDisable(true);
        } else {
            allFavorites.forEach(v -> {
                var item = new MenuItem(v.getKey());
                item.setOnAction(e -> {
                    try {
                        presenter.showFavoriteStage(v.getKey());
                    } catch (RepositoryException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                menuFavorite.getItems().add(item);
            });
        }
    }
}
