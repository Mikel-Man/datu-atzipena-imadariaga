/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.Artist;
import model.Eragiketak;

/**
 * FXML Controller class
 *
 * @author Pablo
 */
public class ArtistController implements Initializable {

    @FXML
    private Label izenburua;

    @FXML
    private TableView<Artist> tableview_artist;

    @FXML
    private TableColumn<Artist, Integer> artistIdCol;

    @FXML
    private TableColumn<Artist, String> nameCol;

    @FXML
    private HBox botoiak;

    @FXML
    private TextField addName;

    @FXML
    private Button btn_artistGehitu;

    @FXML
    private Button btn_artistEzabatu;

    private static ObservableList<Artist> artistOL;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        artistOL = Eragiketak.artistakKargatu();

        tableview_artist.setItems(artistOL);

        artistIdCol.setCellValueFactory(new PropertyValueFactory<Artist, Integer>("artistId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Artist, String>("name"));

        artistIdCol.setCellFactory(TextFieldTableCell.<Artist, Integer>forTableColumn(new IntegerStringConverter()));
        artistIdCol.setEditable(false);

        nameCol.setCellFactory(TextFieldTableCell.<Artist>forTableColumn());
        nameCol.setOnEditCommit((TableColumn.CellEditEvent<Artist, String> t) -> {
            int zenbakia = ((Artist) t.getTableView().getItems().get(t.getTablePosition().getRow())).getArtistId();
            if (Eragiketak.artistaAldatu(zenbakia, t.getNewValue())) {
                ((Artist) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
            } else {
                System.out.println("Errore bat gertatu da.");
            }
        });
    }

    @FXML
    private void artistaGehitu(ActionEvent event) {
        try {
            if (!addName.getText().equals("")) {
                Artist a = new Artist(addName.getText());
                if (Eragiketak.artistaGehitu(a)) {
                    artistOL = Eragiketak.artistakKargatu();
                    tableview_artist.setItems(artistOL);
                    addName.setText("");
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Errore bat gertatu da");
                    alert.setContentText("Errore ezezagun bat gertatu da.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Kontuz!!");
                alert.setHeaderText("Eremu guztiak bete, mesedez");
                alert.showAndWait();

            }
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore bat gertatu da");
            alert.setContentText("Errore ezezagun bat gertatu da.");
            alert.showAndWait();
        }

    }

    @FXML
    private void artistaEzabatu(ActionEvent event) {
        try {
            Artist a = tableview_artist.getSelectionModel().getSelectedItem();
            if (a != null) {
                if (Eragiketak.artistaEzabatu(a)) {
                    tableview_artist.getItems().remove(a);
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Errore bat gertatu da");
                    alert.setContentText("Ezin da erregistroa ezabatu.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Kontuz!!");
                alert.setHeaderText("Artista bat aukeratu, mesedez.");
                alert.showAndWait();
            }
        } catch (Exception ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Errore bat gertatu da");
            alert.setContentText("Errore ezezagun bat gertatu da.");
            alert.showAndWait();
        }

    }

    @FXML
    private void albumArtistaBakoitzekoMenuIreki(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/AlbumArtistaBakoitzeko.fxml"));

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.getIcons().add(new Image("icon.png"));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Album Kopurua Artista Bakoitzeko");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
