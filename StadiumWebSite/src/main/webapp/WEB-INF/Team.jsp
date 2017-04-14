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
        <link href="CSS/style.css" rel="stylesheet" type="text/css" media="all" />
    </head>
    <% 
        ArrayList<Match> matchs = (ArrayList<Match>) request.getAttribute("matchs");
        HashMap<String, String> team = (HashMap<String, String>) request.getAttribute("team");
        int i = 0;
    %>
    <body>
        <div class="center">
            <header>
                <nav>
                    <ul>
                        <li> <a href="../../StadiumWebSite/">Accueil</a></li>
                        <li> <a href="../../StadiumWebSite/Registration">Inscription</a> </li>
                        <li> <a href="../../StadiumWebSite/Team">Equipes</a> </li>
                        <li> <a href="../../StadiumWebSite/Result">Résultats</a> </li>
                    </ul>
                </nav>
            </header>
        </div>
        <div class="center">
            <h1>Page récapitulative des équipes</h1>
        </div>
        <div class="marge">
            <form method="Post" action="/StadiumWebSite/Team">
                <fieldset>
                    <legend>Création d'un match</legend> <br>
                    Le créateur du match sera inscrit dans la première équipe. <br> <br>
                    <label>Nom de la première équipe</label> : <br/> <input type="text" name="Team1" id="Team1" style="width: 500px;" required="true"/> <br/> <br/>
                    <label>Nom de la seconde équipe</label> : <br/> <input type="text" name="Team2" id="Team2" style="width: 500px;" required="true"/> <br/> <br/>
                    <label>Nfc de l'athlète</label> : <br/> <input type="text" name="nfc" id="nfc" style="width: 500px;" required="true"/> <br/> <br/>
                    <div class="rightw">
                        <input type="submit" value="Validation" name="Validation" style="width : 180px; height : 50px;"/>
                    </div>
                </fieldset> <br> <br>
            </form>
            <form method="Post" action="/StadiumWebSite/Team">
                <fieldset>
                    <legend>Les différents matchs programmés</legend> <br>
                    <div class="center">
                        Ici, vous pouvez rejoindre une équipe grâce à votre identifiant : <input type="text" name="nfc" id="nfc" style="width: 150px;" required="true"/> <br>
                        Attention, vous ne pourrez vous inscrire qu'à un match à la fois. <br> <br>
                    </div>
                    <%for (Match match : matchs) { i = i + 1;%> 
                        <u>Match <%=i%> :</u> <%=team.get(Integer.toString(match.getTeamID1()))%> - <%=team.get(Integer.toString(match.getTeamID2()))%> 
                        <br> <br>
                        <input type="radio" name="match" id="match" value=<%=match.getTeamID1()%> required="true"/> <label for="match">Domicile</label>
                        <input type="radio" name="match" id="match" value=<%=match.getTeamID2()%> required="true"/> <label for="match">Visiteur</label> 
                        <br> <br>
                    <%}%>
                    <div class="rightw">
                        <input type="submit" value="Rejoindre" name="Rejoindre" style="width : 180px; height : 50px;"/>
                    </div>
                </fieldset>
            </form>
        </div> 
    </body>
</html>
