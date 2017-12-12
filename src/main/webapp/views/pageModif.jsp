<%-- 
    Document   : pageModif
    Created on : 29 nov. 2017, 13:34:15
    Author     : madjovi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Acces Modification Client</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
    </head>
    <body>
        
        <div class="row" style='margin-top:50px;'>
            <div class="col-lg-5 offset-lg-4">
                <h1> Modification de la commande ${commande.idCommande}</h1>
            </div>                    
        </div>
        
        <!-- Formulaire de modification d'une commande -->    
        
        <form action="<c:url value="/Connexion"/>" method="GET"> 
            
                <div class="row" style='margin-top:50px;'>
                    <div class="col-lg-2 offset-lg-4">
                        <b>Produit : </b>
                    </div>                    
                    
                    <div class="col-lg-2">
                        
                        <select name='produit' class="custom-select" style="width:100%;">
                            
                            <c:forEach items="${listeProduits}" var="produit">
                                <option name="produit" value="${produit.productId}" ${commande.idProduit == produit.productId ? "selected" : ""}>${produit.productId}</option>
                            </c:forEach>
                        </select>
                        
                        
                    </div>
                </div>
            
                <div class="row" style='margin-top:15px;'>
                    <div class="col-lg-2 offset-lg-4">
                        <b>Quantité : </b>
                    </div>                    
                    
                    <div class="col-lg-2">
                        <input name="quantite" type="number" step="1" min="1" size="5" style="width:100%;" value="${commande.quantite}">
                    </div>
                </div>
            
                <div class="row" style='margin-top:15px;'>
                    <div class="col-lg-2 offset-lg-4">
                        <b>Livraison : </b>
                    </div>                    
                    
                    <div class="col-lg-2">
                        
                        <select name='freightCompany' class="custom-select" style="width:100%;">
                            
                            <c:forEach items="${listeFreightCompanies}" var="freightCompany">
                                <option name="freightCompany" value="${freightCompany}" ${commande.idCompa == freightCompany ? "selected" : ""}  >${freightCompany}</option>
                            </c:forEach>
                        </select>
                        
                        
                    </div>
                </div>
            
                <div class="row" style='margin-top:15px;'>
                    <div class="col-lg-2 offset-lg-6" style="padding-right: 15px;" >
                        <input type='hidden' name='password' value='${commande.idClient}'>
                        <input type='hidden' name='idCommande' value='${commande.idCommande}'>
                        <input type='submit' name='action' value='annuler' class="btn btn-secondary pull-right">
                        <input type='submit' name='action' value='update' class="btn btn-primary">
                    </div>
                </div>   
            
        </form>
            
    </body>
</html>

