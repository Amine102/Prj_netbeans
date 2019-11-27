/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sys_enchere;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Pagination;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author BLHA
 */
public class Interface_utilController implements Initializable {
    
    /*BOUTTON*/
    @FXML
    private Button btn_deconnexion;
    @FXML
    private Button btn_ajout;
    @FXML
    private Button btn_modifier_produit;
    @FXML
    private Button btn_encherir_produit;
    @FXML
    private Button btn_suppression;
    
    /*PANE*/
    @FXML
    private TabPane tabPane;
    @FXML
    private TabPane tabPane_achat;
    @FXML
    private TabPane tabPane_vente;
    
    /*TAB*/
    @FXML
    private Tab tab_achat;
    @FXML
    private Tab tab_achat_enchere;
    @FXML
    private Tab tab_achat_produit_dispo;
    @FXML
    private Tab tab_vente;
    @FXML
    private Tab tab_vente_produit_mis_vente;
    @FXML
    private Tab tab_vente_produit_vendus;
    
    /*TABLEVIEW*/
    @FXML
    private TableView<Produit> table_achat_produit;
    @FXML
    private TableView<Enchere> table_achat_enchere;
    @FXML
    private TableView<Produit> table_vente_produit_vendu;
    @FXML
    private TableView<Produit> table_vente_produit_en_vente;
    
    /*COLONNE*/
    @FXML
    private TableColumn<Produit, String> col_achat_produit_dispo_titre_produit;
    @FXML
    private TableColumn<Produit, String> col_achat_produit_dispo_enchere_dep;
    @FXML
    private TableColumn<Produit, String> col_achat_enchere_titre;
    @FXML
    private TableColumn<Produit, String> col_achat_enchere_prix;
    @FXML
    private TableColumn<Produit, String> col_vente_produit_mis_titre;
    @FXML
    private TableColumn<Produit, String> col_vente_produit_mis_enchere_dep;
    @FXML
    private TableColumn<Produit, String> col_achat_enchere_vendeur;
    @FXML
    private TableColumn<Produit, String> col_vente_produit_vendu_titre;
    @FXML
    private TableColumn<Produit, String> col_vente_produit_vendu_acheteur;
    
    /*IMAGE*/
    @FXML
    private ImageView _image;
    
    /*CHAMP_TEXTE*/
    @FXML
    private TextField filterField;

    private volatile static boolean autoRefresh_shutdown = false;

    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private static String id_produit;
    private static String enchere_dep;
    private static Produit list_produit;
    private static Enchere list_enchere;

    private enchere_database db;
    private Runnable refresh_thread;

    private ObservableList<Produit> info;
    private ObservableList<Produit> info2;
    private ObservableList<Enchere> info_enchere;

    private String selected_prod;
    private String selected_enchere;

    public static String getEnchere_dep() {
        return enchere_dep;
    }

    public static Produit getList_produit() {
        return list_produit;
    }

    public static Enchere getList_enchere() {
        return list_enchere;
    }

    public static void autoRefresh_shutdown() {
        System.out.println("STOPPING");
        executor.shutdownNow();
        autoRefresh_shutdown = true;
    }

    private void print_filter() {   //print the filter inside the terminal (the value displayed is the old typed value and the new typed value).
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            String lowerCaseFilter = newValue.toLowerCase();
            System.out.println(oldValue + " " + newValue);
        });
    }

    @FXML
    private void ajout_produit(ActionEvent event) {
        try {
            // do what you have to do
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ajout_UI.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();

            stage.setTitle("Nouveau produit");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.show();
            stage.setOnCloseRequest((WindowEvent e) -> {
                autoRefresh_shutdown();
                Platform.exit();
            });
        } catch (IOException exception) {
            System.err.println("FILE/WINDOW ERROR: Can't load \"ajout_UI.fxml\" file: " + exception);
            exception.printStackTrace();
        }
    }

    private void update_table_vente_produit_mis(ResultSet rs) throws SQLException {
        while (rs.next()) {
            info.add(new Produit(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));

        }

        col_vente_produit_mis_titre.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        col_vente_produit_mis_enchere_dep.setCellValueFactory(new PropertyValueFactory<>("Enchere_dep"));

        table_vente_produit_en_vente.setItems(info);
    }

    private void update_table_achat_produit_en_vente(ResultSet rs) throws SQLException {
        while (rs.next()) {
            info2.add(new Produit(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));

        }

        col_achat_produit_dispo_titre_produit.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        col_achat_produit_dispo_enchere_dep.setCellValueFactory(new PropertyValueFactory<>("Enchere_dep"));
        table_achat_produit.setItems(info2);
    }

    private void update_table_achat_enchere(ResultSet rs) throws SQLException {
        while (rs.next()) {
            info_enchere.add(new Enchere(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
        col_achat_enchere_titre.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        col_achat_enchere_prix.setCellValueFactory(new PropertyValueFactory<>("Prix_vente"));
        col_achat_enchere_vendeur.setCellValueFactory(new PropertyValueFactory<>("Enchere_util"));

        table_achat_enchere.setItems(info_enchere);

    }

    @FXML
    public void refresh(ActionEvent event) throws SQLException {
        Connection conn = null;
        try {
            db = new enchere_database();
            conn = db.activate_connection();
        } catch (Exception ex) {
            Logger.getLogger(Interface_utilController.class.getName()).log(Level.SEVERE, null, ex);
        }
        info = FXCollections.observableArrayList();
        info2 = FXCollections.observableArrayList();
        info_enchere = FXCollections.observableArrayList();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id_produit, titre, enchere_dep, produit_vendu, produit_util FROM Utilisateur, Produit where produit_vendu=false and adresse_mail=" + "'" + FXMLController.getMail() + "'" + "and produit_util=id_util");
        update_table_vente_produit_mis(rs);
        ResultSet rs2 = stmt.executeQuery("SELECT DISTINCT id_produit, titre, enchere_dep, produit_vendu, produit_util FROM Produit WHERE id_produit NOT IN (SELECT id_produit FROM Utilisateur, Produit WHERE adresse_mail = " + "'" + FXMLController.getMail() + "' AND id_util = produit_util)");
        update_table_achat_produit_en_vente(rs2);
        ResultSet rs3 = stmt.executeQuery("SELECT titre, prix_vente, nom  FROM Utilisateur, Produit, Enchere WHERE enchere_produit=id_produit AND produit_util = id_util");
        update_table_achat_enchere(rs3);

    }

    public void refresh() throws SQLException {
        refresh_thread = new Runnable() {

            @Override
            public void run() {
                if (!autoRefresh_shutdown) {
                    System.out.println("autoRefresh_shutdown: " + autoRefresh_shutdown);
                    try {
                        Connection conn = null;
                        try {
                            db = new enchere_database();
                            conn = db.activate_connection();
                        } catch (Exception ex) {
                            Logger.getLogger(Interface_utilController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        info = FXCollections.observableArrayList();
                        info2 = FXCollections.observableArrayList();
                        info_enchere = FXCollections.observableArrayList();
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT id_produit, titre, enchere_dep, produit_vendu, produit_util FROM Utilisateur, Produit WHERE produit_vendu=false and adresse_mail=" + "'" + FXMLController.getMail() + "'" + "and produit_util=id_util");
                        update_table_vente_produit_mis(rs);
                        ResultSet rs2 = stmt.executeQuery("SELECT DISTINCT id_produit, titre, enchere_dep, produit_vendu, produit_util FROM Produit WHERE id_produit NOT IN (SELECT id_produit FROM Utilisateur, Produit WHERE adresse_mail = " + "'" + FXMLController.getMail() + "' AND id_util = produit_util)");
                        update_table_achat_produit_en_vente(rs2);
                        ResultSet rs3 = stmt.executeQuery("SELECT id_enchere, titre, prix_vente, nom  FROM Utilisateur, Produit, Enchere WHERE enchere_produit=id_produit AND produit_util = id_util");
                        update_table_achat_enchere(rs3);
                    } catch (SQLException ex) {
                        Logger.getLogger(Interface_utilController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

        };
        executor.scheduleAtFixedRate(refresh_thread, 0, 3, TimeUnit.SECONDS);

    }

    @FXML
    public void retraction(ActionEvent event) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ATTENTION");
        alert.setHeaderText("La rétraction est \n irreversible!");
        alert.setContentText("Êtes vous sure de vouloir continuer?");
        ButtonType buttonTypeAgree = new ButtonType("Oui");
        ButtonType buttonTypeCancel = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeAgree, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeAgree) {
            Connection conn = db.activate_connection();
            Statement stmt = conn.createStatement();
            stmt.execute("DELETE FROM Enchere WHERE id_Enchere='" + getList_enchere().getId_enchere() + "'");
            System.out.println("Selected element deleted.");
            info = FXCollections.observableArrayList();
            conn.commit();
            try {
                refresh();
            } catch (SQLException ex) {
                Logger.getLogger(Interface_utilController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    void deconnexion_util(ActionEvent event) {
        try {
            Stage stage1 = (Stage) btn_deconnexion.getScene().getWindow();
            autoRefresh_shutdown();
            stage1.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXML.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();

            stage.setTitle("encherePhyne");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.show();
            stage.setOnCloseRequest((WindowEvent e) -> {
                autoRefresh_shutdown();
                Platform.exit();
            });

        } catch (IOException exception) {
            System.err.println("FILE/WINDOW ERROR: Can't load \"interface_util.fxml\" file: " + exception);
            exception.printStackTrace();
        }

    }

    public static String getId_Produit() {
        return id_produit;
    }

    @FXML
    private void handleClickTableView(Event event) throws ParseException, IOException {
        TableView<Produit> table;
        TableView<Enchere> table_enchere;

        if (tabPane.getSelectionModel().getSelectedItem() == tab_vente) {
            btn_encherir_produit.setDisable(true);
            if (tabPane_vente.getSelectionModel().getSelectedItem() == tab_vente_produit_mis_vente) {
                btn_suppression.setDisable(false);
                btn_modifier_produit.setDisable(false);
                table = table_vente_produit_en_vente;
                list_produit = table.getSelectionModel().getSelectedItem();
                selected_prod = list_produit.getId_produit();
                System.out.println(tabPane.getSelectionModel().getSelectedItem());

                if (list_produit != null) {
                    System.out.println(list_produit.getTitre() + " " + list_produit.getEnchere_dep());
                    File file = new File("Image/" + FXMLController.getId_util().replaceAll("\\s", "") + "_" + list_produit.getId_produit().replaceAll("\\s", "") + ".jpg");
                    System.out.println(list_produit.getId_produit());
                    if (file.exists()) {
                        System.out.println(file.getAbsolutePath());
                        if (file != null) {
                            Image image = new Image(file.toURI().toString());
                            _image.setImage(image);
                        }

                    } else {
                        File file2 = new File("Image/" + FXMLController.getId_util().replaceAll("\\s", "") + "_" + list_produit.getId_produit().replaceAll("\\s", "") + ".png");
                        if (file2 != null) {
                            Image image = new Image(file2.toURI().toString());
                            _image.setImage(image);
                        } else {
                            _image.setImage(null);

                        }
                    }

                }
            } else {
                btn_suppression.setDisable(true);
                btn_modifier_produit.setDisable(true);
                table = table_vente_produit_vendu;
            }

        } else if (tabPane.getSelectionModel().getSelectedItem() == tab_achat) {
            btn_suppression.setDisable(true);
            btn_modifier_produit.setDisable(true);
            if (tabPane_achat.getSelectionModel().getSelectedItem() == tab_achat_produit_dispo) {
                btn_encherir_produit.setDisable(false);
                table = table_achat_produit;
                Produit list = table.getSelectionModel().getSelectedItem();
                id_produit = list.getId_produit();
                enchere_dep = list.getEnchere_dep();
            } else {
                btn_encherir_produit.setDisable(true);
                table_enchere = table_achat_enchere;
                ObservableList<Enchere> items = table_enchere.getItems();
                list_enchere = table_enchere.getSelectionModel().getSelectedItem();
                selected_enchere = list_enchere.getId_enchere();
                if (!items.isEmpty()) {
                    btn_encherir_produit.setDisable(false);
                }

            }
        }
    }

    @FXML
    private void suppression_produit(ActionEvent event) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ATTENTION");
        alert.setHeaderText("La suppression d'un produit \n est irreversible!");
        alert.setContentText("Êtes vous sure de vouloir continuer?");
        ButtonType buttonTypeAgree = new ButtonType("Oui");
        ButtonType buttonTypeCancel = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeAgree, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeAgree) {
            Connection conn = db.activate_connection();
            Statement stmt = conn.createStatement();
            stmt.execute("DELETE FROM Produit WHERE id_produit='" + selected_prod + "'");
            System.out.println("Selected element deleted.");
            info = FXCollections.observableArrayList();
            conn.commit();
            try {
                refresh();
            } catch (SQLException ex) {
                Logger.getLogger(Interface_utilController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    public void modifier_produit(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("modif_UI.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();

        stage.setTitle("enchere");
        stage.setScene(new Scene(root1));
        stage.setResizable(false);
        stage.show();

    }

    @FXML
    public void encherir_produit(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("enchere_ui.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();

        stage.setTitle("enchere");
        stage.setScene(new Scene(root1));
        stage.setResizable(false);
        stage.show();

    }

    @FXML
    private void filter_table() {
        if (tabPane.getSelectionModel().getSelectedItem() == tab_vente) {
            if (tabPane_vente.getSelectionModel().getSelectedItem() == tab_vente_produit_mis_vente) {
                FilteredList<Produit> filteredData = new FilteredList<>(info, p -> true);

                filterField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(Produit -> {
                        // If filter text is empty, display all persons.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare first name and last name of every person with filter text.
                        String lowerCaseFilter = newValue.toLowerCase();
                        if (Produit.getTitre().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches first name.
                        } else if (Produit.getEnchere_dep().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches last name.
                        }
                        return false; // Does not match.
                    });
                });
                //printing filter in the terminal before setting it inside the table
                print_filter();

                // 3. Wrap the FilteredList in a SortedList.
                SortedList<Produit> sortedData = new SortedList<>(filteredData);

                // 4. Bind the SortedList comparator to the TableView comparator.
                sortedData.comparatorProperty().bind(table_vente_produit_en_vente.comparatorProperty());
                // 5. Add sorted (and filtered) data to the table.
                table_vente_produit_en_vente.setItems(sortedData);
            } else {
                FilteredList<Produit> filteredData = new FilteredList<>(info, p -> true);

                filterField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(Produit -> {
                        // If filter text is empty, display all persons.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare first name and last name of every person with filter text.
                        String lowerCaseFilter = newValue.toLowerCase();
                        if (Produit.getTitre().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches first name.
                        } else if (Produit.getEnchere_dep().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches last name.
                        }
                        return false; // Does not match.
                    });
                });
                //printing filter in the terminal before setting it inside the table
                print_filter();

                // 3. Wrap the FilteredList in a SortedList.
                SortedList<Produit> sortedData = new SortedList<>(filteredData);

                // 4. Bind the SortedList comparator to the TableView comparator.
                sortedData.comparatorProperty().bind(table_vente_produit_vendu.comparatorProperty());
                // 5. Add sorted (and filtered) data to the table.
                table_vente_produit_vendu.setItems(sortedData);
            }
        } else {
            if (tabPane_vente.getSelectionModel().getSelectedItem() == tab_achat_produit_dispo) {
                FilteredList<Produit> filteredData = new FilteredList<>(info, p -> true);

                filterField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(Produit -> {
                        // If filter text is empty, display all persons.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare first name and last name of every person with filter text.
                        String lowerCaseFilter = newValue.toLowerCase();
                        if (Produit.getTitre().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches first name.
                        } else if (Produit.getEnchere_dep().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches last name.
                        }
                        return false; // Does not match.
                    });
                });
                //printing filter in the terminal before setting it inside the table
                print_filter();

                // 3. Wrap the FilteredList in a SortedList.
                SortedList<Produit> sortedData = new SortedList<>(filteredData);

                // 4. Bind the SortedList comparator to the TableView comparator.
                sortedData.comparatorProperty().bind(table_achat_produit.comparatorProperty());
                // 5. Add sorted (and filtered) data to the table.
                table_achat_produit.setItems(sortedData);
            } else {
                FilteredList<Enchere> filteredData = new FilteredList<>(info_enchere, p -> true);

                filterField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(Enchere -> {
                        // If filter text is empty, display all persons.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare first name and last name of every person with filter text.
                        String lowerCaseFilter = newValue.toLowerCase();
                        if (Enchere.getTitre().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches first name.
                        } else if (Enchere.getPrix_vente().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches last name.
                        }
                        return false; // Does not match.
                    });
                });
                //printing filter in the terminal before setting it inside the table
                print_filter();

                // 3. Wrap the FilteredList in a SortedList.
                SortedList<Enchere> sortedData = new SortedList<>(filteredData);

                // 4. Bind the SortedList comparator to the TableView comparator.
                sortedData.comparatorProperty().bind(table_achat_enchere.comparatorProperty());
                // 5. Add sorted (and filtered) data to the table.
                table_achat_enchere.setItems(sortedData);
            }
        }
    }
    
    @FXML
    public void closeApp(ActionEvent event) {
        autoRefresh_shutdown();
        Platform.exit();
        System.exit(0);
    }
    @FXML
    public void ouvrir_messagerie(ActionEvent event) {
        
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_suppression.setDisable(true);
        btn_encherir_produit.setDisable(true);
        try {
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(Interface_utilController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
