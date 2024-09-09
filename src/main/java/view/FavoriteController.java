package view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Language;
import model.dto.StationDto;
import model.exception.RepositoryException;
import org.controlsfx.control.SearchableComboBox;
import presentation.Presenter;

import java.util.ArrayList;
import java.util.List;

public class FavoriteController {


    @FXML
    public TableView<StationDto> tableview;

    @FXML
    public TableColumn<StationDto, String> c1;

    @FXML
    public TableColumn<StationDto, String> c2;

    @FXML
    public Button deleteButton;

    @FXML
    public Text name;

    private Presenter presenter;


    public void initialize(String name, List<StationDto> stations) {
        //Liste de toutes les stations !!!
        //initialize the tableview
        c1.setCellValueFactory(new PropertyValueFactory<>("name"));
        c2.setCellValueFactory(new PropertyValueFactory<>("lines"));

        ObservableList<StationDto> pathStation = FXCollections.observableArrayList();

        pathStation.setAll(stations);
        tableview.setItems(pathStation);

        this.name.setText(name);
       // delete.setOnAction(presenter.deleteFav(name));
    }


    // Dans View
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
        deleteButton.setOnAction(e -> presenter.onFavoriteDeleteRequested(name.getText()));
    }


}
