<%-- 
    Document   : connexion
    Created on : 17 nov. 2017, 14:38:01
    Author     : tmejane
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="https://use.fontawesome.com/060219b6e5.js"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
        <title>Connexion</title>
    </head>
    <body>

        <form action="<c:url value="/Connexion" />" method="POST"> <!-- l'action par défaut est l'URL courant, qui va rappeler la servlet -->
        
        
        <div class="container">
            <div class="row" style='margin-top:100px;'>
                <div class="col-lg-2 offset-lg-4">
                    <i class="fa fa-user" style="margin-right:15px;" aria-hidden="true"></i>Login
                </div>

                <div class="col-lg-2">
                    <input name='login' style="width:100%">
                </div>
              </div>
            
            <div class="row" style="margin-top: 20px;">
                <div class="col-lg-2 offset-lg-4">
                    <i class="fa fa-key" style="margin-right:15px;" aria-hidden="true"></i>Password
                </div>

                <div class="col-lg-2">
                    <input name='password' type='password' pattern="[0-9]*" style="width:100%">
                </div>
            </div>
            
            <div class="row" style="margin-top: 20px;">
                <div class="col-lg-4 offset-lg-7">
                    <input type='submit' name='action' class='btn btn-primary' value='login'>
                    
                </div>
            </div>
            
            <div class="row" style="margin-top: 20px;">
                <div class="col-lg-4 offset-lg-4">
                    
                    <c:if test = "${not empty errorMessage}">
                        <div class="alert alert-danger" role="alert">${errorMessage}</div>
                    </c:if>
                    
                    
                </div>
            </div>
            
            
        </div>
        
    </form>

    </body>
</html>
