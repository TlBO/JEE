<%-- 
    Document   : pageAdmin
    Created on : 22 nov. 2017, 14:10:03
    Author     : tmejane
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
    
        <!--Load the AJAX API-->
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
        <script type="text/javascript" src="http://www.google.com/jsapi"></script>
        <script type="text/javascript">
            
            // Load the Visualization API and the corechart package.
            google.charts.load('current', {'packages':['corechart']});

            // Set a callback to run when the Google Visualization API is loaded.
            google.charts.setOnLoadCallback(drawChart);
            
            $(document).ready(function () {
                $('#btnChercher').click(function( event ) {
                    event.preventDefault();
                    diagramme();
                });
            });

            // Callback that creates and populates a data table,
            // instantiates the pie chart, passes in the data and
            // draws it.
            function drawChart(json, typeClef) {

                // Create the data table.
                var data = new google.visualization.DataTable();
                data.addColumn('string', typeClef);
                data.addColumn('number', 'Chiffre d\'affaire');
                
                for(key in json){
                    data.addRow([key+"",json[key]]);
                }
                
                // Set chart options
                var options = {'title':'Chiffre d\'affaire par ' + typeClef,
                               'width':400,
                               'height':300};

                // Instantiate and draw our chart, passing in some options.
                var chart = new google.visualization.PieChart(document.getElementById('chart_div_' + typeClef));
                chart.draw(data, options);
             }
            
            function diagramme(){
                var dateDebut = $('#debut').val();
                var dateFin = $('#fin').val();
                
                if (dateDebut != "" && dateFin != "") {
                    $.ajax({    // Catégorie
                        url: "Connexion",
                        data: "type=categorie" + "&debut=" + dateDebut + "&fin=" + dateFin,
                        dataType: "json",
                        async : false,
                        error: showError,
                        success:function(data){
                                drawChart(data, "Categorie");
                        }
                    });
                    
                    $.ajax({    // Zone
                        url: "Connexion",
                        data: "type=zone" + "&debut=" + dateDebut + "&fin=" + dateFin,
                        dataType: "json",
                        async : false,
                        error: showError,
                        success:function(data){
                                drawChart(data, "Zone");
                        }
                    }); 
                    
                    $.ajax({    // Client
                        url: "Connexion",
                        data: "type=client" + "&debut=" + dateDebut + "&fin=" + dateFin,
                        dataType: "json",
                        async : false,
                        error: showError,
                        success:function(data){
                                drawChart(data, "Client");
                        }
                    });                    
                }
                
            }
            
            // Fonction qui traite les erreurs de la requête
            function showError(xhr, status, message) {
                    alert("Erreur: " + status + " : " + message);
            }
          
        </script>
    
    </head>
    <body>
        
        <div class="container">
        
            <div class="row" style='margin-top:50px;'>
                <div class="col-lg-5 offset-lg-4">
                    <h1>Page Administrateur</h1>
                </div>                    
            </div>
            
                <form action="<c:url value="/Connexion"/>">
                    <div class="row" style='margin-top:50px;'>
                        <div class="col-lg-2 offset-lg-4">
                            <b>Début : </b>
                        </div>                    

                        <div class="col-lg-2">
                                <input type="date" id="debut">
                        </div>
                    </div>

                    <div class="row" style='margin-top:50px;'>
                        <div class="col-lg-2 offset-lg-4">
                            <b>Fin : </b>
                        </div>                    

                        <div class="col-lg-2">
                            <input type="date" id="fin">
                        </div>
                    </div>

                    <div class="row" style='margin-top:50px;'>                   
                        <div class="col-lg-2 offset-lg-6">
                            <input type="submit" value="Chercher" id="btnChercher">
                        </div>
                    </div>
            </form>
                    
            <div class="row" style='margin-top:50px;'>
                <div class="col-lg-3 offset-lg-1">
                      <div id="chart_div_Categorie"></div>  
                </div>
                
                <div class="col-lg-3">
                      <div id="chart_div_Zone"></div>   
                </div>
                
                <div class="col-lg-3">
                      <div id="chart_div_Client"></div>   
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
