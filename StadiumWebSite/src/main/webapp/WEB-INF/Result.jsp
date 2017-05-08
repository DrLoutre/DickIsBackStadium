<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="beans.Match"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="shortcut icon" href="CSS/18221949_10211207804182298_8640115247712280419_n.jpg" type="image/x-icon" />
        <link rel="icon" href="CSS/18221949_10211207804182298_8640115247712280419_n.jpg" type="image/x-icon" />
        <link href="CSS/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
        <title>Résultat</title>
    </head>
    <% 
        ArrayList<Match> matchs = (ArrayList<Match>) request.getAttribute("matchs");
        HashMap<String, String> team = (HashMap<String, String>) request.getAttribute("team");
    %>
    <body>
        <br>
        <div class="container">
            <nav class="navbar navbar-default">
                <div class="container-fluid">
                    <div class="navbar-header">
                        <div class="glyphicon navbar-header">
                            <img src="CSS/18221949_10211207804182298_8640115247712280419_n.jpg" height="48px" >
                        </div>
                        <a class="navbar-brand">
                            &nbsp;&nbsp;Smartdium
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
                    <h1>Résultats des différents matchs</h1>
                </header>
            </div>            
            <table width="800" height="128" align="center" cellspacing="0" style="border-collapse:collapse;">
                <tr height="20">
                    <td width="500" height="42" align="center" valign="middle" style="border-width:3; border-color:black; border-style:solid;">
                        <p align="center"><u><span style="font-size:18pt;">Domicile</span></u></p>
                    </td>
                    <td width="1000" height="42" align="center" valign="middle" style="border-width:3; border-color:black; border-style:solid;">
                        <p align="center"><span style="font-size:18pt;"><u>Score</u></span></p>
                    </td>
                    <td width="500" height="42" align="center" valign="middle" style="border-width:3; border-color:black; border-style:solid;">
                        <p align="center"><span style="font-size:18pt;"><u>Visiteur</u></span></p>
                    </td>
                </tr>
                <%for (Match match : matchs) {%> 
                <tr height="20">
                    <td width="242" align="center" valign="middle" height="31" style="border-width:1; border-color:black; border-style:solid;">
                        <p align="center"><%=team.get(Integer.toString(match.getTeamID1()))%></p>
                   </td>
                    <td width="800" height="42" align="center" valign="middle" style="border-width:3; border-color:black; border-style:solid;">
                        <p align="center"><span style="font-size:18pt;"><%=match.getGoals1()%> - <%=match.getGoals2()%></span></p>
                    </td>
                    <td width="242" align="center" valign="middle" height="31" style="border-width:1; border-color:black; border-style:solid;">
                        <p align="center"><%=team.get(Integer.toString(match.getTeamID2()))%></p>
                    </td>
                </tr>
                <%}%>
            </table>
        </div>
    </body>
</html>
