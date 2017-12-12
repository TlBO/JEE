<%-- 
    Document   : pageClient
    Created on : 21 nov. 2017, 16:19:41
    Author     : tmejane
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Client</title>
        <script src="https://use.fontawesome.com/060219b6e5.js"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">

    </head>
    <body>
        
        <div class="container">
            
            <div class="row" style='margin-top:100px;'>
                <div class="col-lg-4 offset-lg-1">
                    <h1>Numéro client : ${idClient}</h1>
                </div>
            </div>
            
            <!-- Formulaire d'ajout d'une commande -->       
                
            <form action="<c:url value="/Connexion"/>" method="POST">
                
                <div class="row" style='margin-top:50px;'>
                    <div class="col-lg-2 offset-lg-1">
                        <b>Produit : </b>
                    </div>                    
                    
                    <div class="col-lg-2">
                        
                        <select name='produit' class="custom-select">
                            
                            <option selected disabled>-- Produits --</option>
                            
                            <c:forEach items="${listeProduits}" var="produit">
                                <option name="produit" value="${produit.productId}" selected>${produit.productId}</option>
                            </c:forEach>
                        </select>
                        
                        
                    </div>
                </div>
                
                
                <div class="row" style='margin-top:15px;'>
                    <div class="col-lg-2 offset-lg-1">
                        <b>Quantité : </b>
                    </div>                    
                    
                    <div class="col-lg-2">
                        <input name="quantite" type="number" step="1" min="1" size="5">
                    </div>
                </div>
                
                <div class="row" style='margin-top:15px;'>
                    <div class="col-lg-2 offset-lg-1">
                        <b>Livraison : </b>
                    </div>                    
                    
                    <div class="col-lg-2">
                        
                        <select name='freightCompany' class="custom-select">
                            
                            <option selected disabled>-- Livraison --</option>
                            
                            <c:forEach items="${listeFreightCompanies}" var="freightCompany">
                                <option name="freightCompany" value="${freightCompany}" selected>${freightCompany}</option>
                            </c:forEach>
                        </select>
                        
                        
                    </div>
                </div>
                
                
                <div class="row" style='margin-top:15px;'>
                    <div class="col-lg-1 offset-lg-4" style="padding-right: 0px;" >
                        <input type='hidden' name='password' value='${idClient}'>
                        <input type='submit' name='action' value='add' class="btn btn-primary pull-right">
                    </div>
                </div>                

            </form>
                
            
            <!-- Tableau des commandes -->    
                
            <div class="row" style='margin-top:5px;'>
                
                <div class="col-lg-10 offset-lg-1">
                    <hr/>
                        <table border = "1"  style="width:100%">
                            <tr>
                                <th>IdCommande</th>
                                <th>IdProduit</th>
                                <th>Quantite</th>
                                <th>ShippingCost</th>
                                <th>DateComm</th>
                                <th>ShippingDate</th>
                                <th>IdCompa</th>
                                <th>Modifier</th>
                                <th>Supprimer</th>
                            </tr>

                            <c:forEach items="${listeCommandesClient}" var="commandeClient">
                                <tr> 
                                     <td>${commandeClient.idCommande}</td> 
                                     <td>${commandeClient.idProduit}</td>
                                     <td>${commandeClient.quantite}</td>
                                     <td>${commandeClient.shippingCost}</td>
                                     <td>${commandeClient.dateComm}</td>
                                     <td>${commandeClient.shippingDate}</td>
                                     <td>${commandeClient.idCompa}</td>
                                     <td><a href="Connexion?action=affichUpdate&idCommande=${commandeClient.idCommande}"><i class="fa fa-cogs" aria-hidden="true"></i></a></td>
                                     <td><a href="Connexion?action=delete&password=${commandeClient.idClient}&idCommande=${commandeClient.idCommande}"><i class="fa fa-trash" aria-hidden="true"></i></a></td>
                                </tr>
                            </c:forEach>

                        </table>
                </div>
            </div>
                            
            <div class="row" style='margin-top:50px;'>
                <div class="col-lg-1 offset-lg-10">
                    <form action="<c:url value="/Connexion"/>" method="POST"> 
                        <input type='submit' name='action' value='logout' class="btn btn-secondary">
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
