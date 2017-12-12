/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import model.entity.Client;
import model.entity.Commande;
import model.entity.Produit;
import servlet.Connexion;

/**
 *
 * @author tmejane
 */
public class DAO {
    
    private final DataSource myDataSource;

    /**
     * Construit le DAO avec sa source de données
     * @param dataSource la source de données à utiliser
     */
    public DAO(DataSource dataSource) {
            this.myDataSource = dataSource;
    }

    public List<Produit> produitsAll() throws SQLException {

            List<Produit> result = new LinkedList<>();

            String sql = "SELECT * FROM PRODUCT ORDER BY PRODUCT_ID";
            try (Connection connection = myDataSource.getConnection(); 
                 PreparedStatement stmt = connection.prepareStatement(sql)) {
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                            Produit c = new Produit(rs.getInt("PRODUCT_ID"), rs.getInt("MANUFACTURER_ID"), rs.getString("PRODUCT_CODE"), rs.getDouble("PURCHASE_COST"), rs.getInt("QUANTITY_ON_HAND"), rs.getDouble("MARKUP"), rs.getString("AVAILABLE"), rs.getString("DESCRIPTION"));
                            result.add(c);
                    }
            }
            return result;
    }

    /**
     * Permet de récupérer une liste de commandes pour un client en connaissant son id
     * @param idClient identifiant du client recherché
     * @return Liste des discount codes
     * @throws SQLException renvoyées par JDBC
     */
    public List<Commande> commandesClient(int idClient) throws SQLException {

            List<Commande> result = new LinkedList<>();

            String sql = "SELECT * FROM PURCHASE_ORDER WHERE CUSTOMER_ID = ? ORDER BY SALES_DATE, ORDER_NUM";

            try (Connection connection = myDataSource.getConnection(); 
                 PreparedStatement stmt = connection.prepareStatement(sql)) {

                    // Ajout du paramètre dans la requête
                    stmt.setInt(1, idClient);

                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                            Commande c = new Commande(rs.getInt("ORDER_NUM"), rs.getInt("CUSTOMER_ID"), rs.getInt("PRODUCT_ID"), rs.getInt("QUANTITY"), rs.getDouble("SHIPPING_COST"), rs.getDate("SALES_DATE"), rs.getDate("SHIPPING_DATE"), rs.getString("FREIGHT_COMPANY"));
                            result.add(c);
                    }
            } catch(Exception ex) {
                Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return result;
    }
    
    /**
     * Récupère les comapgnies de livraisons présentent dans la base de données
     * @return une liste de compagnies sous forme de liste de chaine de caractères
     * @throws SQLException 
     */
    public List<String> getFreightCompanies() throws SQLException {

         List<String> result = new LinkedList<>();

         String sql = "SELECT DISTINCT(FREIGHT_COMPANY) FROM PURCHASE_ORDER";

         try (Connection connection = myDataSource.getConnection(); 
              PreparedStatement stmt = connection.prepareStatement(sql)) {

                 ResultSet rs = stmt.executeQuery();

                 while (rs.next()) {
                         String freightCompany = rs.getString("FREIGHT_COMPANY");
                         result.add(freightCompany);
                 }
         } catch(Exception ex) {
             Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
         }
         return result;
    }
    
    /**
     * Permet de récupérer une commande en connaissant son id
     * @param idCommande identifiant de la commande
     * @return Liste des discount codes
     * @throws SQLException renvoyées par JDBC
     */
    public Commande uneCommandesClient(int idCommande) throws SQLException {

            String sql = "SELECT * FROM PURCHASE_ORDER WHERE ORDER_NUM = ?";
            
            Commande commande = null;

            try (Connection connection = myDataSource.getConnection(); 
                 PreparedStatement stmt = connection.prepareStatement(sql)) {

                    // Ajout du paramètre dans la requête
                    stmt.setInt(1, idCommande);

                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                            commande = new Commande(rs.getInt("ORDER_NUM"), rs.getInt("CUSTOMER_ID"), rs.getInt("PRODUCT_ID"), rs.getInt("QUANTITY"), rs.getDouble("SHIPPING_COST"), rs.getDate("SALES_DATE"), rs.getDate("SHIPPING_DATE"), rs.getString("FREIGHT_COMPANY"));
                    }
            }
            return commande;
    }
    
    /**
     * Retourne le client correspondant au numéro donné
     * @param customerId id du client
     * @return client, une entité du client rehcerché
     * @throws SQLException 
     * @throws model.DAOException 
     */
    public Client infoClient(int customerId) throws SQLException, DAOException {
        
        Client client = null;
        
        String sql = "SELECT * FROM APP.CUSTOMER WHERE CUSTOMER_ID = ?";
        
        try (Connection connection = myDataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Ajout du paramètre dans la requête préparée
            stmt.setInt(1, customerId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                    client = new Client(rs.getInt("CUSTOMER_ID"), rs.getString("NAME"), rs.getString("EMAIL"), rs.getString("PHONE") , rs.getString("ADDRESSLINE1"));
            }
        } catch (SQLException e) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, e);
            throw new DAOException(e.getMessage());
        }   
        return client;
    }
    
    /**
     * Visualiser les chiffres d'affaire par catégorie d'article, 
     * en choisissant la période (date de début / date de fin) sur 
     * laquelle doit porter la statistique.
     * @param debut
     * @param fin
     * @return table, HashMap key = le code produit & value = le CA
     * @throws model.DAOException
     */
    public HashMap<String, Double> categorieCA(Date debut, Date fin) throws DAOException {
        HashMap<String, Double> table = new HashMap<>();
        
        String sql = "SELECT PRODUCT_CODE, "
                + "SUM(PURCHASE_ORDER.QUANTITY * PRODUCT.PURCHASE_COST) AS CA "
                + "FROM PURCHASE_ORDER JOIN PRODUCT "
                + "ON PURCHASE_ORDER.PRODUCT_ID = PRODUCT.PRODUCT_ID "
                + "JOIN PRODUCT_CODE "
                + "ON PRODUCT.PRODUCT_CODE = PRODUCT_CODE.PROD_CODE "
                + "WHERE SALES_DATE BETWEEN ? AND ? GROUP BY PRODUCT_CODE";
        
        try (Connection connection = myDataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setDate(1, debut);
            stmt.setDate(2, fin);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                table.put(rs.getString("PRODUCT_CODE"), rs.getDouble("CA"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return table;
    }
    
    /**
     * Visualiser les chiffres d'affaire par zone géographique, en choisissant 
     * la période (date de début / date de fin) sur laquelle doit 
     * porter la statistique.
     * @param debut
     * @param fin
     * @return  table, HashMap key = la zone & value = le CA
     * @throws model.DAOException
     */
    public HashMap<String, Double> zoneCA(Date debut, Date fin) throws DAOException {
        HashMap<String, Double> table = new HashMap<>();
        
        String sql = "SELECT STATE, SUM(PURCHASE_ORDER.QUANTITY * PRODUCT.PURCHASE_COST) AS CA " +
                     "FROM PURCHASE_ORDER " +
                     "JOIN PRODUCT " +
                     "ON PURCHASE_ORDER.PRODUCT_ID = PRODUCT.PRODUCT_ID " +
                     "JOIN CUSTOMER " +
                     "ON PURCHASE_ORDER.CUSTOMER_ID = CUSTOMER.CUSTOMER_ID " +
                     "WHERE SALES_DATE BETWEEN ? AND ? " +
                     "GROUP BY STATE";
        
        try (Connection connection = myDataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setDate(1, debut);
            stmt.setDate(2, fin);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                table.put(rs.getString("STATE"), rs.getDouble("CA"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return table;
    }
    
    /**
     * Visualiser les chiffres d'affaire par client, en choisissant la période 
     * (date de début / date de fin) sur laquelle doit porter la statistique.
     * @param debut
     * @param fin
     * @return  table, HashMap key = l'identifiant du client & value = le CA
     * @throws model.DAOException
     */
    public HashMap<Integer, Double> clientCA(Date debut, Date fin) throws DAOException {
        HashMap<Integer, Double> table = new HashMap<>();
        
        String sql = "SELECT CUSTOMER_ID, SUM(PURCHASE_ORDER.QUANTITY * PRODUCT.PURCHASE_COST) AS CA " +
                     "FROM PURCHASE_ORDER " +
                     "JOIN PRODUCT " +
                     "ON PURCHASE_ORDER.PRODUCT_ID = PRODUCT.PRODUCT_ID " +
                     "WHERE SALES_DATE BETWEEN ? AND ? " +
                     "GROUP BY CUSTOMER_ID";
        
        try (Connection connection = myDataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setDate(1, debut);
            stmt.setDate(2, fin);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                table.put(rs.getInt("CUSTOMER_ID"), rs.getDouble("CA"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return table;
    }
    
    /**
     * Récupère le plus grand numéro de commande
     * @return num, le plus grand numéro de commande
     * @throws model.DAOException
     */
    public int numCommande() throws DAOException {
        String sql = "SELECT MAX(ORDER_NUM) AS maxId FROM PURCHASE_ORDER";
        int num = -1;
        
        try( Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    num = rs.getInt("maxId");
                }
            
        } catch (Exception e) {
            
        }
        
        return num;
    }
    
    
    /**
     * Ajouter une commande
     * @param Entity, objet commande de la commande à ajouter
     * @throws DAOException 
     */
    public void addCommande(Commande Entity) throws DAOException {
		List<Commande> result = new LinkedList<>();
		String sql = "INSERT INTO PURCHASE_ORDER (ORDER_NUM, CUSTOMER_ID, PRODUCT_ID, QUANTITY, SHIPPING_COST, SALES_DATE, SHIPPING_DATE, FREIGHT_COMPANY) VALUES(?,?,?,?,?,?,?,?)";
		try (	Connection connection = myDataSource.getConnection(); 
                    PreparedStatement stmt = connection.prepareStatement(sql)) {
                    
                        stmt.setInt(1, Entity.getIdCommande());
                        stmt.setInt(2,Entity.getIdClient());
                        stmt.setInt(3,Entity.getIdProduit());
                        stmt.setInt(4,Entity.getQuantite());
                        stmt.setDouble(5, Entity.getShippingCost());
                        stmt.setDate(6, (java.sql.Date) Entity.getDateComm());
                        stmt.setDate(7, (java.sql.Date) Entity.getShippingDate());
                        stmt.setString(8,Entity.getIdCompa());
                        
                        stmt.executeUpdate();
                        
		} catch (SQLException ex) {
                    Logger.getLogger("DAO").log(Level.SEVERE,null,ex);
			throw new DAOException(ex.getMessage());
		}
	}
    


     /**
      * Supprimer une commande
      * @param codeNum, l'identifiant de la commande à supprimer
      * @throws DAOException 
      */
    public void SupprCommande(int codeNum) throws DAOException {
		String sql = "DELETE FROM PURCHASE_ORDER WHERE ORDER_NUM = ?";
		try (	Connection connection = myDataSource.getConnection(); 
                    PreparedStatement stmt = connection.prepareStatement(sql)) {
                    
                        stmt.setInt(1,codeNum);
                        stmt.executeUpdate();
		} catch (SQLException ex) {
                    Logger.getLogger("DAO").log(Level.SEVERE,null,ex);
			throw new DAOException(ex.getMessage());
		}
    }


    /**
     * Modifier une commande
     * @param idProduct, nouveau produit
     * @param quantite, nouvelle quantité
     * @param freightCompany, nouvelle compagnie de livraison
     * @param idCommande, id de la commande à modifier
     * @throws DAOException 
     */
    public void Update(int idProduct, int quantite, String freightCompany, int idCommande) throws DAOException {
            String sql = "UPDATE PURCHASE_ORDER SET PRODUCT_ID = ?, QUANTITY = ?, FREIGHT_COMPANY = ? WHERE ORDER_NUM = ?";
            try (   Connection connection = myDataSource.getConnection(); 
                PreparedStatement stmt = connection.prepareStatement(sql)) {
                    

                    stmt.setInt(1,idProduct);
                    stmt.setInt(2, quantite);
                    stmt.setString(3, freightCompany);
                    stmt.setInt(4, idCommande);
                    
                    stmt.executeUpdate();
                }catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                    throw new DAOException(ex.getMessage());
                }
    }
}
