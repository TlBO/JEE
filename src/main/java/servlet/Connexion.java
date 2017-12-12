/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.sql.SQLException;
import java.util.Calendar;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static javax.ws.rs.client.Entity.json;
import model.DAO;
import model.DAOException;
import model.DataSourceFactory;
import model.entity.Client;
import model.entity.Commande;
import model.entity.Produit;

/**
 *
 * @author tmejane
 */
public class Connexion extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, DAOException {
        
        DAO dao = new DAO(DataSourceFactory.getDataSource());
        
        response.setContentType("application/json;charset=UTF-8");
        
        // Quelle action a appelé cette servlet ?
        String action = request.getParameter("action");
        if (null != action) {
                switch (action) {
                        case "login":
                                checkLogin(request);
                                break;
                        case "logout":
                                doLogout(request);
                                break;
                        case "delete":
                                deleteCommande(request);
                                break;
                        case "add":
                                addCommande(request);
                                break;
                        case "affichUpdate" :
                                break;
                        case "update" :
                                updateCommande(request);
                                break;
                }
        }
        
        // Est-ce que l'utilisateur est connecté ?
        // On cherche l'attribut role dans la session
        String role = findUserInSession(request);
        String jspView;
        if (null == role) { // L'utilisateur n'est pas connecté
            // On choisit la page de login
            jspView = "connexion.jsp";
            
            // On continue vers la page JSP sélectionnée
            request.getRequestDispatcher("views/" + jspView).forward(request, response);

        } else if(role.equals("admin")) { // L'utilisateur est connecté en tant qu'administrateur
            
            String debut = request.getParameter("debut");
            String fin = request.getParameter("fin");
            String type = request.getParameter("type");
            
            if (null != debut && null != fin && type != null) { // Si des dates sont renseignées
                Date dateDebut = stringToDate(debut);
                Date dateFin = stringToDate(fin);
                Gson gson = new Gson();
                PrintWriter out = response.getWriter();
                
                switch (type) {
                    case "categorie":
                        HashMap<String, Double> categorieCA = dao.categorieCA(dateDebut, dateFin);
                        String gsonCategorie = gson.toJson(categorieCA);
                        out.println(gsonCategorie);
                        break;
                    case "zone":
                        HashMap<String, Double> zoneCA = dao.zoneCA(dateDebut, dateFin);
                        String gsonZone = gson.toJson(zoneCA);
                        out.println(gsonZone);
                        break;
                    case "client":
                        HashMap<Integer, Double> clientCA = dao.clientCA(dateDebut, dateFin);
                        String gsonClient = gson.toJson(clientCA);
                        out.println(gsonClient);
                        break;
                }      
                
            } else {
                // On choisit la page d'affichage
                jspView = "pageAdmin.jsp";    

                // On continue vers la page JSP sélectionnée
                request.getRequestDispatcher("views/" + jspView).forward(request, response);
            }
            
            
        } else { // L'utilisateur est connecté en tant que client
            
            List<String> listeFreightCompanies = dao.getFreightCompanies();
            request.setAttribute("listeFreightCompanies", listeFreightCompanies);
            
            List<Produit> listeProduits = dao.produitsAll();
            request.setAttribute("listeProduits", listeProduits);
            
            if (action != null && action.equals("affichUpdate")) {
                
                int idCommande =  Integer.parseInt(request.getParameter("idCommande"));
                
                Commande commande = dao.uneCommandesClient(idCommande);
                
                request.setAttribute("commande", commande);
                
                jspView = "pageModif.jsp";
                
            } else  {
                int idClient = Integer.parseInt(request.getParameter("password"));
                request.setAttribute("idClient", idClient);

                List<Commande> listeCommandesClient = dao.commandesClient(idClient);
                request.setAttribute("listeCommandesClient", listeCommandesClient);
                
                jspView = "pageClient.jsp";   
            }
            
            // On continue vers la page JSP sélectionnée
            request.getRequestDispatcher("views/" + jspView).forward(request, response);
        
        }

        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException | DAOException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException | DAOException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void checkLogin(HttpServletRequest request) throws SQLException, DAOException {
        
        DAO dao = new DAO(DataSourceFactory.getDataSource());

        // Les paramètres transmis dans la requête
        String login = request.getParameter("login");
        int password;
        try  { 
            password = Integer.parseInt(request.getParameter("password"));
        } catch (NumberFormatException e) {
            password = -1;
        }
        
        Client client = null;
        client = dao.infoClient(password);

        // Le login/password défini dans web.xml pour l'administrateur
        String loginAdmin = getInitParameter("loginAdmin");
        int passwordAdmin = Integer.parseInt(getInitParameter("passwordAdmin"));
        String userNameAdmin = getInitParameter("userNameAdmin");
        
        if (login.equals(loginAdmin) && password == passwordAdmin) {
            // Si l'utilisateur est connecté en tant qu'administrateur
            
                // On a trouvé la combinaison login / password de l'administrateur
                // On stocke l'information dans la session
                HttpSession session = request.getSession(true); // démarre la session
                session.setAttribute("role", "admin");
            
        } else if (client != null && login.equals(client.getEmail()) && password == client.getIdClient()) {
            // Si l'utilisateur est connecté en tant que client
            
                // On a trouvé la combinaison login / password du client
                // On stocke l'information dans la session
                HttpSession session = request.getSession(true); // démarre la session
                session.setAttribute("role", "client");
            
        } else {    // On positionne un message d'erreur pour l'afficher dans la JSP
            request.setAttribute("errorMessage", "Login/Password incorrect");
        }
    }

    private void doLogout(HttpServletRequest request) {
            // On termine la session
            HttpSession session = request.getSession(false);
            if (session != null) {
                    session.invalidate();
            }
    }
    
    private String findUserInSession(HttpServletRequest request) {
            HttpSession session = request.getSession(false);
            return (session == null) ? null : (String) session.getAttribute("role");
    }

    private void deleteCommande(HttpServletRequest request) {
        
        DAO dao = new DAO(DataSourceFactory.getDataSource());
        
        int idCommande = Integer.parseInt(request.getParameter("idCommande"));
        
        try {
            dao.SupprCommande(idCommande);
        } catch (DAOException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addCommande(HttpServletRequest request) throws DAOException {
        DAO dao = new DAO(DataSourceFactory.getDataSource());
        
        int idProduit = Integer.parseInt(request.getParameter("produit"));
        int quantite = Integer.parseInt(request.getParameter("quantite"));
        int idClient = Integer.parseInt(request.getParameter("password"));
        int prixLivraison = 15;
        Calendar calendar = Calendar.getInstance(); // Date du jour
        Date dateCommande =  new java.sql.Date(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_YEAR, 7); // Ajout d'une semaine
        Date shippingDate =  new java.sql.Date(calendar.getTimeInMillis());

        String freightCompany = request.getParameter("freightCompany");
        
        int numCommande = dao.numCommande() + 1;
        
        Commande commande = new Commande(numCommande, idClient, idProduit, quantite, prixLivraison, dateCommande, shippingDate, freightCompany);
        
        dao.addCommande(commande);
        
    }

    private void updateCommande(HttpServletRequest request) throws DAOException {
        DAO dao = new DAO(DataSourceFactory.getDataSource());
        
        int idProduit = Integer.parseInt(request.getParameter("produit"));
        int quantite = Integer.parseInt(request.getParameter("quantite"));
        String freightCompany = request.getParameter("freightCompany");
        int idCommande = Integer.parseInt(request.getParameter("idCommande"));
        
        dao.Update(idProduit, quantite, freightCompany, idCommande);
    }
    
    /**
     * Transforme une chaine de caractère en java.sql.date
     * @param date, la chaîne de caractères à transformer
     * @return la date de type java.sql.Date
     */
    private java.sql.Date stringToDate(String dateS) {
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = null;

        try {
            parsed = format.parse(dateS);
        } catch (ParseException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        java.sql.Date date = new java.sql.Date(parsed.getTime());
        
        return date;
    }
}
