<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="CSS/style.css" rel="stylesheet" type="text/css" media="all" />
        <title>Inscription</title>
    </head>
    <body>
        <div class="center">
            <header>
                <nav>
                    <ul>
                        <li> <a href="../../StadiumWebSite/">Accueil</a></li>
                        <li> <a href="../../StadiumWebSite/Registration">Inscription</a> </li>
                        <li> <a href="../../StadiumWebSite/Result">Résultats</a>
                    </ul>
                </nav>
            </header>
        </div>
        <div class="center">
            <h1>Inscription pour une compétition</h1>
            <div class="font-size20">
                Vous pouvez vous inscrire en remplissant ce formulaire. <br> <br>
            </div>
        </div>
        <div class="littlemarge">
            <form method="Post" action="/StadiumWebSite/Registration">
                <fieldset>
                    <legend>Vos informations personnelles</legend>
                    <label>Nom du sportif</label> : <br/> <input type="text" name="Nom" id="Nom" style="width: 500px;" required="true"/> <br/> <br/>
                    <label>Prénom du sportif</label> : <br/> <input type="text" name="Prenom" id="Prenom" style="width: 500px;" required="true"/> <br/> <br/>
                    <label>Âge du sportif</label> : <br/> <input type="number" name="Age" id="Age" required="true"/> <br/> <br/>
                    <label>Votre mot de passe</label> : <br/> <input type="password" name="MDP" id="MDP" style="width: 500px;" required="true"/> <br/> <br/>
                    Veuillez sélectionner votre sexe :<br/> <br/>
                    <input type="radio" name="Sexe" id="Sexe" value="F" required="true"/> <label for="Dame">Dame</label>
                    <input type="radio" name="Sexe" id="Sexe" value="H" required="true"/> <label for="Homme">Homme</label> <br/>
                    <div class="rightw">
                        <input type="submit" value="Validation" id="Validation" style="width : 180px; height : 50px;"/>
                    </div>
                </fieldset>
            </form>
        </div>
    </body>
    <% if(Boolean.getBoolean(String.valueOf(request.getAttribute("correct")))) { %> <script> alert("Le sportif a bien été ajouté.");</script><%}%>
</html>
