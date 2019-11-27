/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sys_enchere;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author BLHA
 */
public class FXMLController implements Initializable {

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @FXML
    private TextField pass_text;
    @FXML
    private TextField pass_text_2;
    @FXML
    private PasswordField box_connection_pass;

    @FXML
    private CheckBox check_affich_pass1;
    @FXML
    private TextField box_connection_mail;

    @FXML
    private Button btn_connection;

    @FXML
    private TextField box_inscript_nom;

    @FXML
    private TextField box_inscript_prenom;

    @FXML
    private TextField box_inscript_mail;

    @FXML
    private PasswordField box_inscript_pass;

    @FXML
    private CheckBox check_affich_pass;

    @FXML
    private Button btn_inscription;

    @FXML
    private Label error_connect_label;

    @FXML
    private CheckBox check_accept_conditions;
    private enchere_database db;
    private static String mail_util;
    private static String id_util;

    public static void println_data(ResultSet rs) throws SQLException {//Print Data inside a variable of type ResultSet
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        for (int i = 1; i <= columnsNumber; i++) {
            if (i > 1) {
                System.out.print(",  ");
            }
            String columnValue = rs.getString(i);
            System.out.print(columnValue + " " + rsmd.getColumnName(i));
        }
        System.out.println("");
    }

    public static String getMail() {
        return mail_util;
    }

    public static boolean isEmptyResultSet(ResultSet rsCheck) {
        boolean empty = false;
        try {
            empty = !rsCheck.next();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return empty;
    }

    public static String getId_util() {
        return id_util;
    }

    private void open_user_interface(String fileName) {
        try {
            Stage stage1 = (Stage) btn_connection.getScene().getWindow();
            stage1.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fileName));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();

            stage.setTitle("encherePhyne");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.show();
            stage.setOnCloseRequest((WindowEvent e) -> {
                Interface_utilController.autoRefresh_shutdown();
                Platform.exit();
            });

        } catch (IOException exception) {
            System.err.println("FILE/WINDOW ERROR: Can't load \"" + fileName + "\" file!: " + exception);
            exception.printStackTrace();
        }

    }

    @FXML
    void aide_util(ActionEvent event) {

    }

    @FXML
    public void togglevisiblePassword_connect(ActionEvent event) {
        if (check_affich_pass1.isSelected()) {
            pass_text_2.setText(box_connection_pass.getText());
            pass_text_2.setVisible(true);
            box_connection_pass.setVisible(false);
            return;
        }
        box_connection_pass.setText(pass_text_2.getText());
        box_connection_pass.setVisible(true);
        pass_text_2.setVisible(false);
    }

    @FXML
    public void togglevisiblePassword(ActionEvent event) {
        if (check_affich_pass.isSelected()) {
            pass_text.setText(box_inscript_pass.getText());
            pass_text.setVisible(true);
            box_inscript_pass.setVisible(false);
            return;
        }
        box_inscript_pass.setText(pass_text.getText());
        box_inscript_pass.setVisible(true);
        pass_text.setVisible(false);
    }

    private void set_btn_connection_active() {
        box_connection_mail.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            boolean isDisabled = (((box_connection_mail.getText() == null
                    || box_connection_mail.getText().trim().isEmpty())
                    || box_connection_pass.getText() == null)
                    || box_connection_pass.getText().trim().isEmpty());
            btn_connection.setDisable(isDisabled);
        });
        box_connection_pass.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            boolean isDisabled = (((box_connection_mail.getText() == null
                    || box_connection_mail.getText().trim().isEmpty())
                    || box_connection_pass.getText() == null)
                    || box_connection_pass.getText().trim().isEmpty());
            btn_connection.setDisable(isDisabled);
        });

    }

    private void set_btn_inscript_active() {
        box_inscript_nom.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            boolean isDisabled = ((((box_inscript_nom.getText() == null
                    || box_inscript_nom.getText().trim().isEmpty())
                    || box_inscript_prenom.getText() == null)
                    || box_inscript_prenom.getText().trim().isEmpty())
                    || box_inscript_mail.getText() == null
                    || box_inscript_mail.getText().trim().isEmpty()
                    || box_inscript_pass.getText() == null
                    || box_inscript_pass.getText().trim().isEmpty()
                    || !check_accept_conditions.isSelected());
            btn_inscription.setDisable(isDisabled);
        });
        box_inscript_prenom.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            boolean isDisabled = ((((box_inscript_nom.getText() == null
                    || box_inscript_nom.getText().trim().isEmpty())
                    || box_inscript_prenom.getText() == null)
                    || box_inscript_prenom.getText().trim().isEmpty())
                    || box_inscript_mail.getText() == null
                    || box_inscript_mail.getText().trim().isEmpty()
                    || box_inscript_pass.getText() == null
                    || box_inscript_pass.getText().trim().isEmpty()
                    || !check_accept_conditions.isSelected());
            btn_inscription.setDisable(isDisabled);
        });
        box_inscript_mail.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            boolean isDisabled = ((((box_inscript_nom.getText() == null
                    || box_inscript_nom.getText().trim().isEmpty())
                    || box_inscript_prenom.getText() == null)
                    || box_inscript_prenom.getText().trim().isEmpty())
                    || box_inscript_mail.getText() == null
                    || box_inscript_mail.getText().trim().isEmpty()
                    || box_inscript_pass.getText() == null
                    || box_inscript_pass.getText().trim().isEmpty()
                    || !check_accept_conditions.isSelected());
            btn_inscription.setDisable(isDisabled);
        });
        box_inscript_pass.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            boolean isDisabled = ((((box_inscript_nom.getText() == null
                    || box_inscript_nom.getText().trim().isEmpty())
                    || box_inscript_prenom.getText() == null)
                    || box_inscript_prenom.getText().trim().isEmpty())
                    || box_inscript_mail.getText() == null
                    || box_inscript_mail.getText().trim().isEmpty()
                    || box_inscript_pass.getText() == null
                    || box_inscript_pass.getText().trim().isEmpty()
                    || !check_accept_conditions.isSelected());
            btn_inscription.setDisable(isDisabled);
        });
        check_accept_conditions.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (check_accept_conditions.isSelected()) {
                    boolean isDisabled = ((((box_inscript_nom.getText() == null
                            || box_inscript_nom.getText().trim().isEmpty())
                            || box_inscript_prenom.getText() == null)
                            || box_inscript_prenom.getText().trim().isEmpty())
                            || box_inscript_mail.getText() == null
                            || box_inscript_mail.getText().trim().isEmpty()
                            || box_inscript_pass.getText() == null
                            || box_inscript_pass.getText().trim().isEmpty());
                    btn_inscription.setDisable(isDisabled);
                } else {
                    btn_inscription.setDisable(true);
                }
            }

        }
        );

    }

    @FXML
    void connection_util(ActionEvent event) throws SQLException, Exception {
        error_connect_label.setText("");
        String verif_mail = box_connection_mail.getText();
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"; //expression stricte d'une adresse mail 
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(verif_mail);
        if (!matcher.matches()) {
            System.err.println("SYNTAX ERROR: Expression d'adresse mail incorrecte: veuillez saisir une adresse mail valide (ex. exemple@domaine.org).\n");
            error_connect_label.setText("Expression d'adresse mail incorrecte: veuillez saisir une adresse mail valide (ex. exemple@domaine.org).");

        } else {
            Connection conn = db.activate_connection(); //Creation d'une connection a la base
            Statement stmt = conn.createStatement();       //Creation d'une requête SQL
            ResultSet rs = stmt.executeQuery("SELECT adresse_mail FROM Utilisateur WHERE adresse_mail='" + verif_mail + "'");
            if (isEmptyResultSet(rs)) {
                String error = "l'adresse mail " + verif_mail + " n'existe pas dans notre base de donnée, veuillez vous enregistrer afin de bénificier de nos services.";
                System.err.println("SYNTAX ERROR: " + error + "\n");
                int MAX_LENGTH = 165;
                int DEFAULT_FONT = 13;
                if (error.length() % MAX_LENGTH == 0) {
                    error_connect_label.setFont(new Font((MAX_LENGTH - error.length()) + DEFAULT_FONT));
                } else {
                    error_connect_label.setFont(new Font(DEFAULT_FONT));
                }
                System.out.println("length= " + error.length());
                error_connect_label.setText(error);

            } else {
                mail_util = verif_mail;
                rs = stmt.executeQuery("SELECT id_util FROM Utilisateur WHERE adresse_mail='" + verif_mail + "'");
                if (rs.next()) {
                    id_util = rs.getString(1);
                    System.out.println(id_util);
                }
                String verif_pass = box_connection_pass.getText();
                rs = stmt.executeQuery("SELECT pass_util FROM Utilisateur WHERE adresse_mail='" + verif_mail + "'");
                if (rs.next()) {
                    if (!rs.getString(1).equals(verif_pass)) {
                        System.err.println("PASSWORD ERROR: Mot de passe incorrect, veuillez réessayer\n");
                    } else {
                        mail_util = verif_mail;
                        open_user_interface("interface_util.fxml");
                        //Implementation a faire pour l'interface (UI) d'un utilisateur
                        //ouverture de l'UI standard utilisateur avec toute ces informations chargé de la base
                    }
                }

            }
        }

    }

    @FXML
    void inscription_util(ActionEvent event) throws SQLException, Exception {
        String nom = box_inscript_nom.getText();
        String prenom = box_inscript_prenom.getText();
        String mail = box_inscript_mail.getText();
        String pass = box_inscript_pass.getText();
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"; //expression stricte d'une adresse mail 
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mail);
        if (matcher.matches()) {
            String id_util = UUID.randomUUID().toString().substring(0, 6); //genere un identifiant de 10 charactère unique aléatoirement. 
            Connection conn = db.activate_connection(); //Creation d'une connection a la base
            Statement stmt = conn.createStatement();       //Creation d'une requête SQL
            stmt.execute("INSERT INTO Utilisateur VALUES('" + id_util + "','" + nom + "','" + prenom + "','" + mail + "','" + pass + "')");
            conn.commit();
            System.out.println("Inscription réussie!");
            mail_util = mail;
            open_user_interface("interface_util.fxml");
        } else {
            System.err.println("Expression d'adresse mail incorrecte: veuillez saisir une adresse mail valide (ex. exemple@domaine.org)");

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.togglevisiblePassword_connect(null);
        this.togglevisiblePassword(null);
        try {
            btn_connection.setDisable(true);
            btn_inscription.setDisable(true);
            set_btn_connection_active();
            set_btn_inscript_active();
            db = new enchere_database();
            Connection conn = db.activate_connection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Produit");
            while (rs.next()) {
                println_data(rs);

            }
        } catch (Exception ex) {

            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
