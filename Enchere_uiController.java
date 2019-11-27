/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sys_enchere;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author BLHA
 */
public class Enchere_uiController implements Initializable {

    @FXML
    private TextField box_propos_enchere;
    @FXML
    private Button btn_confirmation;
    @FXML
    private Label error_lab;

    @FXML
    void confirmer_enchere(ActionEvent event) throws SQLException, Exception {
        Double enchere_propose = Double.parseDouble(box_propos_enchere.getText());
        if (enchere_propose <= Double.parseDouble(Interface_utilController.getEnchere_dep())) {
            String error = "Veuillez proposez un prix supérieur \n à celui donné par le vendeur.";
            error_lab.setText(error);

            System.err.println(error);
        } else {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            enchere_database db = new enchere_database();
            Connection conn = db.activate_connection();
            Statement stmt = conn.createStatement();
            String id_enchere = UUID.randomUUID().toString().substring(0, 6);
            stmt.execute("INSERT INTO Enchere VALUES('" + id_enchere + "'," + box_propos_enchere.getText() + ",'" + FXMLController.getId_util() + "','" + Interface_utilController.getId_Produit() + "','" + localDate + "')");
            System.out.println("Enchere inserted!");
            Stage stage = (Stage) btn_confirmation.getScene().getWindow();
            stage.close();
            db.disactivate_connection();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
