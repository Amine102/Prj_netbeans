/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sys_enchere;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author BLHA
 */
public class Ajout_UIController implements Initializable {

    @FXML
    private TextField box_nom_prod;

    @FXML
    private Button btn_ajout_image;

    @FXML
    private TextField box_enchère_dep;

    @FXML
    private Button btn_confirm_ajout;

    @FXML
    private ChoiceBox<?> categorie_picker;

    @FXML
    private TextField text_image;

    @FXML
    private ImageView _image;
    private enchere_database db;
    private File _file;
    private String id_produit;

    public static void copyFile(File from, File to) throws IOException {
        Files.copy(from.toPath(), to.toPath());
    }
    private void insert_picture(File _file) throws IOException {
        if (_file != null) {
            File source = new File(_file.getAbsolutePath());
            System.out.println("HERE "+ _file.getAbsolutePath());
            File dest = new File("./Image/" + _file.getName());
            Image image = new Image(_file.toURI().toString());
            _image.setImage(image);
            System.out.println(dest);
            copyFile(source, dest);
            File oldName = new File("./Image/" + _file.getName());
            FXMLController obj = new FXMLController();
            System.out.println("ID: "+FXMLController.getId_util());
            File newName = new File("./Image/" + FXMLController.getId_util().replaceAll("\\s","") + "_" + id_produit + "." + getFileExtension(_file).replaceAll("\\s",""));

            if (oldName.renameTo(newName)) {
                System.out.println(oldName + " renamed into" + newName);
            } else {
                System.err.println("ERROR while renaming " + oldName);
            }
        } else {
            System.out.println("no picture inserted for now...");
        }

    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    @FXML
    private void photo_pic(ActionEvent event) {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            if ("jpg".equals(getFileExtension(selectedFile)) || "png".equals(getFileExtension(selectedFile))) {
                text_image.setText(selectedFile.getName());
                _file = selectedFile;
                System.out.println(_file.getAbsolutePath());
                Image image = new Image(_file.toURI().toString());
                System.out.println("IMAGE: " + image);
                if (image != null) {
                    _image.setImage(image);
                    System.out.println("File Added in the list");
                }

            } else {
                System.err.println("FILE ERROR: Wrong file extension!");
            }
        } else {
            System.err.println("FILE ERROR: File is not valid!");
        }
    }

    private void enableAjout() {
        box_nom_prod.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            boolean isEnabled = (box_nom_prod.getText() == null
                    || box_nom_prod.getText().trim().isEmpty()
                    || box_enchère_dep.getText() == null
                    || box_enchère_dep.getText().trim().isEmpty());
            btn_confirm_ajout.setDisable(isEnabled);
        });
        box_enchère_dep.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            boolean isEnabled = (box_nom_prod.getText() == null
                    || box_nom_prod.getText().trim().isEmpty()
                    || box_enchère_dep.getText() == null
                    || box_enchère_dep.getText().trim().isEmpty());
            btn_confirm_ajout.setDisable(isEnabled);
        });
    }

    @FXML
    private void ajout_produit(ActionEvent event) throws SQLException, Exception {
        db = new enchere_database();
        Connection conn = db.activate_connection();
        Statement stmt = conn.createStatement();
        id_produit = UUID.randomUUID().toString().substring(0, 6);
        stmt.execute("INSERT INTO Produit VALUES('" + id_produit + "','" + box_nom_prod.getText() + "'," + box_enchère_dep.getText() + "," + false + ",'" + FXMLController.getId_util() + "')");
        insert_picture(_file);
        System.out.println("Product inserted!");
        Stage stage = (Stage) btn_confirm_ajout.getScene().getWindow();
        stage.close();
        db.disactivate_connection();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("ID_UTIL: " + FXMLController.getId_util());
        btn_confirm_ajout.setDisable(true);
        enableAjout();

    }
}
