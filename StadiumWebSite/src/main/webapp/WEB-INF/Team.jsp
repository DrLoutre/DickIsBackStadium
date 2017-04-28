<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="beans.Match"%>
<html>
    <head>
        <title>Equipe</title>
        <meta charset="UTF-8">
        <link href="CSS/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
    </head>
    <% 
        ArrayList<Match> matchs = (ArrayList<Match>) request.getAttribute("matchs");
        HashMap<String, String> team = (HashMap<String, String>) request.getAttribute("team");
        Boolean correct = (Boolean) request.getAttribute("correct");
        Boolean time = (Boolean) request.getAttribute("time");
        int i = 0;
    %>
    <body>
        <br>
        <div class="container">
            <nav class="navbar navbar-default">
                <div class="container-fluid">
                    <div class="navbar-header">
                        <a class="navbar-brand">
                            DickStadium
                        </a>  
                    </div>
                    <ul class="nav navbar-nav">
                        <li> <a href="../../StadiumWebSite/">Accueil</a></li>
                        <li> <a href="../../StadiumWebSite/Registration">Inscription</a> </li>
                        <li> <a href="../../StadiumWebSite/Team">Equipes</a> </li>
                        <li> <a href="../../StadiumWebSite/Result">Résultats</a> </li>
                    </ul>
                </div>
            </nav>
            <div class="text-center">
                <header class="page-header">
                    <h1>Page récapitulative des équipes</h1>
                </header>
            </div>
            <% if(correct != null) {
                if (time || time == null) {
                    if (!correct) {%>
                        <div class="alert alert-danger fade in">
                            <Strong>Le nfc rentré est incorrect !</strong>
                        </div>
                    <%} else {%>
                        <div class="alert alert-success fade in">
                            <strong>Success!</strong> Vous êtes bien inscrit pour le match demandé !
                        </div>
                    <%}
                }   
            }%>
            <form method="Post" action="/StadiumWebSite/Team">
                <fieldset>
                    <legend>Création d'un match</legend> 
                    Le créateur du match sera inscrit dans la première équipe. <br> <br>
                    <label>Nom de la première équipe</label> : <br/> <input type="text" name="Team1" id="Team1" style="width: 500px;" required="true"/> <br/> <br/>
                    <label>Nom de la seconde équipe</label> : <br/> <input type="text" name="Team2" id="Team2" style="width: 500px;" required="true"/> <br/> <br/>
                    <%if ((time != null) && !time) {%>
                        <div class="form-group has-warning">
                            <label class="control-label" for="idWarning">Avertissement</label> <br>
                            <span class="help-block">La date rentrée est déjà dépassé, elle ne peut donc pas être enregistré.</span>
                        </div>
                    <%}%>
                    <label>Date de la rencontre</label> : <br/> <input type="date" name="Date" id="Date" onchange="verifdate(this.id);" required="true"> <br> <br>
                    <label>Heure de la rencontre</label> : <br/> <input type="time" name="Heure" id="Heure" required="true"> <br> <br>
                    <label>Identifiant de l'athlète</label> : <br/> <input type="text" name="nfc" id="nfc" style="width: 500px;" required="true"/> <br/> <br/>
                    <div class="row">
                        <div class="col-lg-offset-9 col-lg-9">
                            <input type="submit" class="btn btn-info btn-lg" value="Validation" name="Validation" style="width : 180px; height : 50px;"/>
                        </div>
                    </div>
                </fieldset> 
            </form>
            <%if(!matchs.isEmpty()) {%>
                <form method="Post" action="/StadiumWebSite/Team">
                    <fieldset>
                        <legend>Les différents matchs programmés</legend> 
                        <div class="center">
                            Ici, vous pouvez rejoindre une équipe grâce à votre identifiant : <input type="text" name="nfc" id="nfc" style="width: 150px;" required="true"/> <br> <br>
                            Attention, vous ne pourrez vous inscrire qu'à un match à la fois. <br> <br>
                        </div>
                        <%for (Match match : matchs) { i = i + 1;%> 
                            <div class="container">
                                <div class="row">
                                    <div class="col-lg-3"><u>Match <%=i%> :</u> <%=match.getDate()%></div> <br> <br>
                                    <div class="col-lg-1 col-lg-offset-1"><%=team.get(Integer.toString(match.getTeamID1()))%></div>
                                    <div class="col-lg-1">-</div>
                                    <div class="col-lg-1"><%=team.get(Integer.toString(match.getTeamID2()))%> </div>
                                </div>
                            </div> <br>
                            <div class="container">
                                <div class="row">
                                    <div class="col-lg-2 col-lg-offset-1"><input type="radio" name="match" id="match" value=<%=match.getTeamID1()%> required="true"/> <label for="match">Domicile</label></div>
                                    <div class="col-lg-2"><input type="radio" name="match" id="match" value=<%=match.getTeamID2()%> required="true"/> <label for="match">Visiteur</label></div>
                                </div>
                            </div>
                            <br> <br>
                        <%}%>
                        <div class="row">
                            <div class="col-lg-offset-9 col-lg-9">
                                <input type="submit" class="btn btn-info btn-lg" value="Rejoindre" name="Rejoindre" style="width : 180px; height : 50px;"/>
                            </div>
                        </div>
                    </fieldset>
                </form>
            <%}%>
        </div>  
    </body>
</html>
