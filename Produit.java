/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sys_enchere;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author BLHA
 */
public class Produit {
    private final StringProperty _id_produit;
    private final StringProperty _titre;
    private final StringProperty _enchere_dep;
    private final StringProperty _produit_vendu;
    private final StringProperty _produit_util;

    //constructeur par defaut
    public Produit(String id_produit, String titre, String enchere_dep, String produit_vendu, String produit_util) {
        this._id_produit = new SimpleStringProperty(id_produit);
        this._titre = new SimpleStringProperty(titre);
        this._enchere_dep = new SimpleStringProperty(enchere_dep);
        this._produit_vendu = new SimpleStringProperty(produit_vendu);
        this._produit_util = new SimpleStringProperty(produit_util);

    }

    //getters
    public String getProduit_util() {
        return _id_produit.get();
    }

    public String getTitre() {
        return _titre.get();
    }

    public String getProduit_vendu() {
        return _produit_vendu.get();
    }

    public String getEnchere_dep() {
        return _enchere_dep.get();
    }

    public String getId_produit() {
        return _id_produit.get();
    }

    //setters
     public void set_produit_util(String util_id) {
        _produit_util.set(util_id);
    }

     public void set_titre(String titre) {
        _titre.set(titre);
    }

    public void set_produit_vendu(String val) {
        _produit_vendu.set(val);
    }

    public void set_enchere_dep(String val) {
        _enchere_dep.set(val);
    }

    public void set_id_produit(String id) {
        _id_produit.set(id);
    }

    //valeur propriétée
    public StringProperty propriete_titre() {
        return _titre;
    }

    public StringProperty propriete_enchere_dep() {
        return _enchere_dep;
    }

    public StringProperty propriete_id_produit() {
        return _id_produit;
    }
    
}
