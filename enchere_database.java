/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sys_enchere;

import java.sql.Connection;

/**
 *
 * @author BLHA
 */
public class enchere_database {

    static {
        try {

            java.sql.DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
            java.sql.Statement statement = java.sql.DriverManager.getConnection("jdbc:derby:enchere_database;create=true").createStatement();

//            statement.execute("drop table Vente");
//            statement.execute("drop table Enchere");
//            statement.execute("drop table Message");
//            statement.execute("drop table Produit");
//            statement.execute("drop table Utilisateur");
            statement.execute("create table Utilisateur(\n"
                    + "id_util char(10),\n"
                    + "nom varchar(30) not null,\n"
                    + "prenom varchar(30) not null,\n"
                    + "adresse_mail varchar(50) not null,\n"
                    + "pass_util varchar(100) not null,\n"
                    + "constraint Mail_Unique Unique(adresse_mail),\n"
                    + "constraint Utilisateur_key primary key(id_util))");

            statement.execute("create table Produit(\n"
                    + "id_produit char(10),\n"
                    + "titre varchar(30),\n"
                    + "enchere_dep int,\n"
                    + "produit_vendu boolean,\n"
                    + "produit_util char(10),\n"
                    + "constraint Produit_util foreign key(produit_util) references Utilisateur(id_util),\n"
                    + "constraint Produit_key primary key(id_produit))");

//            statement.execute("create table Historique_vente(\n"
//                    + "id_produit char(10),\n"
//                    + "titre varchar(30),\n"
//                    + "prix_vente int,\n"
//                    + "produit_util char(10),\n"
//                    + "constraint Produit_util foreign key(produit_util) references Utilisateur(id_util),\n"
//                    + "constraint Produit_key primary key(id_produit))");

            statement.execute("create table Enchere(\n"
                    + "id_enchere char(10),\n"
                    + "prix_vente double not null,\n"
                    + "enchere_util char(10) not null,\n"
                    + "enchere_produit char(10) not null,\n"
                    + "date_enchere varchar(10),\n"
                    + "constraint Enchere_prod foreign key(enchere_produit) references Produit(id_produit),\n"
                    + "constraint Enchere_util foreign key(enchere_util) references Utilisateur(id_util),\n"
                    + "constraint Enchere_key primary key(id_enchere))");
//
            statement.execute("create table Message(\n"
                    + "id_message char(10),\n"
                    + "id_destinataire char(10),\n"
                    + "date_envoie Date,\n"
                    + "constraint Message_key primary key(id_message),\n"
                    + "constraint Message_Utilisateur foreign key(id_destinataire) references Utilisateur(id_util))");

            statement.execute("create table Vente(\n"
                    + "id_vente char(10),\n"
                    + "vendeur varchar(10),\n"
                    + "date_vente Date,\n"
                    + "vente_util char(10),\n"
                    + "constraint Vente_util foreign key(vente_util) references Utilisateur(id_util),\n"
                    + "constraint Vente_key primary key(id_vente))"
            );
            
//             statement.execute("create table Utilisateur_banni(\n"
//                    + "id_util char(10),\n"
//                    + "date_vente Date,\n"
//                    + "vente_util char(10),\n"
//                    + "constraint Vente_util foreign key(vente_util) references Utilisateur(id_util),\n"
//                    + "constraint Ban_key primary key(id_util))"
//            );
             statement.execute("create table Message(\n"
                    + "id_message char(10),\n"
                    + "date_message Date,\n"
                    + "message_util char(10),\n"
                    + "constraint Message_util foreign key(message_util) references Utilisateur(id_util),\n"
                    + "constraint Message_key primary key(id_message))"
            );

        } catch (java.sql.SQLException sqle1) {
            System.err.println("'enchere_database'Already exists? " + sqle1.getMessage());
            java.sql.Statement clean_up;
            try {
                Connection connection = java.sql.DriverManager.getConnection("jdbc:derby:enchere_database");
                clean_up = connection.createStatement();
                connection.commit();
            } catch (java.sql.SQLException sqle2) {
                System.err.println("'enchere_database' persistent error: " + sqle2.getMessage());
                System.exit(-1);
            }
        }
    }
    private final Connection _connection;

    public enchere_database() throws java.sql.SQLException {
        _connection = java.sql.DriverManager.getConnection("jdbc:derby:enchere_database");
        _connection.setAutoCommit(false);

    }

    public java.sql.Connection activate_connection() throws Exception {
        if (_connection == null) {
            throw new Exception("Fatal error: JDBC not initialized");
        }
        return _connection;
    }

    public void disactivate_connection() throws Exception {
        if (_connection == null) {
            throw new Exception("Fatal error: JDBC not initialized");
        }
        _connection.commit();
        _connection.close();
    }

}
