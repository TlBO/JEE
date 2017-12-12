/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import model.entity.Client;
import model.entity.Commande;
import model.entity.Produit;
import org.junit.Before;
import org.junit.Test;
import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;
import static org.junit.Assert.*;

/**
 *
 * @author thibaut
 */
public class DAOTest {
    
    private DAO dao; // L'objet à tester
    private DataSource myDataSource; // La source de données à utiliser
    private java.sql.Date date;
    private static Connection myConnection ;
    
    @Before
    public void setUp() throws IOException, SQLException, SqlToolError {
        myDataSource = DataSourceFactory.getDataSource();
        myConnection = myDataSource.getConnection();
        
        
        String sqlFilePathD = DAOTest.class.getResource("deleteData.sql").getFile();
        SqlFile sqlFileD = new SqlFile(new File(sqlFilePathD));

        sqlFileD.setConnection(myConnection);
        sqlFileD.execute();
        sqlFileD.closeReader();
        
        dao = new DAO(myDataSource);
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = null;
        try {
            parsed = format.parse("2011-05-24");
        } catch (ParseException ex) {
            Logger.getLogger(DAOTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        date = new java.sql.Date(parsed.getTime());
    }
    
    /**
     * Test of produitsAll method, of class DAO.
     */
    @Test
    public void testProduitsAll() throws Exception {
        List<Produit> expResult = new LinkedList<>();
        List<Produit> result = dao.produitsAll();
        assertEquals(948933, result.get(0).getProductId());    // Test du premier produit récupérer
        assertEquals(30, result.size());    // Test du nombre de produits récupérés
        assertEquals(988765, result.get(29).getProductId());    // Test du dernier produit récupérer
    }

    /**
     * Test of commandesClient method, of class DAO.
     */
    @Test
    public void testCommandesClient() throws Exception {
        int idClient = 2;   // Id client = 2

        List<Commande> result = dao.commandesClient(idClient);
        assertEquals(10398002, result.get(0).getIdCommande());
        assertEquals(10398003, result.get(1).getIdCommande());
        assertEquals(2, result.size());    // Test du nombre de commandes récupérées

    }

    /**
     * Test of getFreightCompanies method, of class DAO.
     */
    @Test
    public void testGetFreightCompanies() throws Exception {
        List<String> result = dao.getFreightCompanies();
        assertEquals("Coastal Freight", result.get(0));    // Première compagnie
        assertEquals(7, result.size());    // Test du nombre de compagnies récupérées
        assertEquals("Southern Delivery Service", result.get(6));    // Dernière compagnie

    }

    /**
     * Test of uneCommandesClient method, of class DAO.
     */
    @Test
    public void testUneCommandesClient() throws Exception {
        int idCommande = 10398002;
        Commande result = dao.uneCommandesClient(idCommande);
        assertEquals(10398002, result.getIdCommande());
    }

    /**
     * Test of infoClient method, of class DAO.
     */
    @Test
    public void testInfoClient() throws Exception {
        int customerId = 2;
        Client result = dao.infoClient(customerId);
        assertEquals(2, result.getIdClient());
    }

    /**
     * Test of categorieCA method, of class DAO.
     */
    @Test
    public void testCategorieCA() throws Exception {
        HashMap<String, Double> result = dao.categorieCA(date, date);
        assertEquals(5, result.size());    // Taille de la HashMap
    }
    
        /**
     * Test of zoneCA method, of class DAO.
     */
    @Test
    public void testZoneCA() throws Exception {
        HashMap<String, Double> result = dao.zoneCA(date, date);
        assertEquals(5, result.size());    // Taille de la HashMap
    }

    /**
     * Test of clientCA method, of class DAO.
     */
    @Test
    public void testClientCA() throws Exception {
        HashMap<Integer, Double> result = dao.clientCA(date, date);
        assertEquals(12, result.size());    // Taille de la HashMap

    }
    
    /**
     * Test of numCommande method, of class DAO.
     */
    @Test
    public void testNumCommande() throws Exception {
        int result = dao.numCommande();
        assertEquals(30298004, result);
    }

    /**
     * Test of addCommande method, of class DAO.
     */
    @Test
    public void testAddCommande() throws Exception {
        Commande Entity = new Commande(dao.numCommande() + 1, 2, 948933, 12, 15.0, null, null, null);
        int beforeMaxId = dao.numCommande();
        dao.addCommande(Entity);
        Commande result = dao.uneCommandesClient(beforeMaxId + 1);
        assertEquals(beforeMaxId + 1, result.getIdCommande());
        dao.SupprCommande(dao.numCommande());
    }

    /**
     * Test of SupprCommande method, of class DAO.
     */
    @Test
    public void testSupprCommande() throws Exception {
        int beforeMaxId = dao.numCommande();
        Commande Entity = new Commande(dao.numCommande() + 1, 2, 948933, 12, 15.0, null, null, null);
        dao.addCommande(Entity);
        dao.SupprCommande(dao.numCommande());
        assertEquals(beforeMaxId, dao.numCommande());
    }

    /**
     * Test of Update method, of class DAO.
     */
    @Test
    public void testUpdate() throws Exception {
        int idProduct = 0;
        int quantite = 0;
        String freightCompany = "";
        int idCommande = 0;
        // Ajout d'une commande test
        Commande Entity = new Commande(9999, 2, 948933, 12, 15.0, null, null, "Southern Delivery Service");
        // Modification de la commande de test
        dao.Update(980005, 2, "Poney Express", dao.numCommande());
        // Récupération de la commande modifiée
        Commande modif = dao.uneCommandesClient(dao.numCommande());
        // Test des modifications de la commande
        assertEquals("Poney Express", modif.getIdCompa());
        assertEquals(2, modif.getQuantite());
        assertEquals(980005, modif.getIdProduit());
        // Suppression de la commande de test
        dao.SupprCommande(9999);
    }
    
}
