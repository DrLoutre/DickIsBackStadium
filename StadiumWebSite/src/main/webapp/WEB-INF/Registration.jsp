<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="shortcut icon" href="CSS/18221949_10211207804182298_8640115247712280419_n.jpg" type="image/x-icon" />
        <link rel="icon" href="CSS/18221949_10211207804182298_8640115247712280419_n.jpg" type="image/x-icon" />
        <link href="CSS/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
        <title>Inscription</title>
    </head>
    <body>
        <br>
        <div class="container">
            <header>
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
            </header>
            <% 
                Boolean correct = (Boolean) request.getAttribute("correct");
                HashMap<String, String> value = (HashMap<String, String>) request.getAttribute("value");
                if(value == null) value = new HashMap<String, String>();
            %>
            <div class="text-center">
                <header class="page-header">
                    <h1>Inscription au stade</h1>
                </header>
            </div>
            <form method="Post" action="/StadiumWebSite/Registration">
                <fieldset>
                    <legend>Vos informations personnelles</legend>
                    <% if(correct != null) {
                        if (!correct) {%>
                            <div class="alert alert-danger fade in">
                                <Strong>Impossible de vous inscrire au stade</strong>, une des informations rentrées doit être incorrect !
                            </div>
                        <%} else {%>
                            <div class="alert alert-success fade in">
                                <strong>Vous êtes bien inscrit dans le stade !</strong>
                            </div>
                        <%}
                    }%>
                    <%if(!value.containsKey("Nom") && (correct != null)) {%>
                        <div class="form-group has-warning">
                            <label class="control-label" for="idWarning">Avertissement</label> <br>
                            <label>Nom du sportif</label> : <br/> <input type="text" name="Nom" id="Nom" style="width: 500px;" required="true"/> <br/> 
                            <span class="help-block">Il y a un problème dans la saisie</span>
                        </div>
                    <%} else {%>
                        <label>Nom du sportif</label> : <br/> <input type="text" name="Nom" id="Nom" style="width: 500px;" required="true"/> <br/> <br/>
                    <%}%>
                    <%if(!value.containsKey("Prenom") && (correct != null)) {%>
                        <div class="form-group has-warning">
                            <label class="control-label" for="idWarning">Avertissement</label> <br>
                            <label>Prénom du sportif</label> : <br/> <input type="text" name="Prenom" id="Prenom" style="width: 500px;" required="true"/> <br/>
                            <span class="help-block">Il y a un problème dans la saisie</span>
                        </div>
                    <%} else {%>
                        <label>Prénom du sportif</label> : <br/> <input type="text" name="Prenom" id="Prenom" style="width: 500px;" required="true"/> <br/> <br/>
                    <%}%>
                    <%if(!value.containsKey("Age") && (correct != null)) {%>
                        <div class="form-group has-warning">
                            <label class="control-label" for="idWarning">Avertissement</label> <br>
                            <label>Âge du sportif</label> : <br/> <input type="number" name="Age" id="Age" required="true"/> <br/> 
                            <span class="help-block">Il y a un problème dans la saisie</span>
                        </div>
                    <%} else {%>
                            <label>Âge du sportif</label> : <br/> <input type="number" name="Age" id="Age" required="true"/> <br/> <br/>
                    <%}%>
                    <%if(!value.containsKey("MDP") && (correct != null)) {%>
                        <div class="form-group has-warning">
                            <label class="control-label" for="idWarning">Avertissement</label> <br>
                            <label>Votre mot de passe</label> : <br/> <input type="password" name="MDP" id="MDP" style="width: 500px;" required="true"/> <br/> 
                            <span class="help-block">Il y a un problème dans la saisie</span>
                        </div>
                    <%} else {%>
                        <label>Votre mot de passe</label> : <br/> <input type="password" name="MDP" id="MDP" style="width: 500px;" required="true"/> <br/> <br/>
                    <%}%>
                    <label>Veuillez sélectionner votre sexe</label> :<br/> <br/>
                    <input type="radio" name="Sexe" id="Sexe" value="F" required="true"/> <label for="Dame">Dame</label>    
                    <input type="radio" name="Sexe" id="Sexe" value="H" required="true"/> <label for="Homme">Homme</label> <br/> <br>
                    <div class="row">
                        <div class="col-lg-offset-9 col-lg-9">
                            <input type="submit" class="btn btn-info btn-lg" value="Validation" id="Validation" style="width : 180px; height : 50px;"/>
                        </div>
                    </div>
                </fieldset>
            </form>
        </div>
    </body>
</html>
