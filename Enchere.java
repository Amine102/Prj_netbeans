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
public class Enchere {

    private final StringProperty _id_enchere;
    private final StringProperty _prix_vente;
    private final StringProperty _enchere_util;
    private final StringProperty _titre;
     public Enchere(String id_enchere, String titre, String prix_vente, String enchere_util) {
        this._id_enchere = new SimpleStringProperty(id_enchere);
        this._titre = new SimpleStringProperty(titre);
        this._prix_vente = new SimpleStringProperty(prix_vente);
        this._enchere_util = new SimpleStringProperty(enchere_util);

    }
     
   //getters
     public String getId_enchere()
     {
         return this._id_enchere.get();
     }
     public String getTitre()
     {
         return this._titre.get();
     }

     
     public String getPrix_vente()
     {
         return this._prix_vente.get();
     }
     
     public String getEnchere_util()
     {
         return this._enchere_util.get();
     }
     
     //Setters
     
     public void setPrix_vente(String val)
     {
         _prix_vente.set(val);
     }
     
     public void setEnchere_util(String val)
     {
         _enchere_util.set(val);
     }
      public void setTitre(String val)
     {
         _titre.set(val);
     }
}
