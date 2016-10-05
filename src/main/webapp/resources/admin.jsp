<%--
  Created by IntelliJ IDEA.
  User: alexc_000
  Date: 2016-09-06
  Time: 7:16 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html ng-app="packetAdminApp">
<head>
    <title>Packet App Administration</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script type="application/javascript" src="resources/angular.js"></script>
    <script src="resources/ngmodules/angular-route.js"></script>
    <script src="resources/ngmodules/angular-cookies.js"></script>
    <link href="resources/css/bootstrap.css" rel="stylesheet"/>
    <link href="resources/css/bootstrap-theme.css" rel="stylesheet"/>
    <link href="resources/css/home-styles.css" rel="stylesheet"/>
    <script type="application/javascript" src="resources/adminScript.js"></script>
    <script type="application/javascript" src="resources/controllers/adminLoginControllers.js"></script>
    <script type="application/javascript" src="resources/controllers/adminMainControllers.js"></script>
    <script type="application/javascript" src="resources/controllers/packetsPanelControllers.js"></script>
    <script type="application/javascript" src="resources/controllers/comptsPanelControllers.js"></script>
    <script type="application/javascript" src="resources/directives/directives.js"></script>
    <script type="application/javascript" src="resources/services/services.js"></script>
    <script type="application/javascript" src="resources/filters/filters.js"></script>


<body ng-controller="loginCtrl">
<div class="navbar navbar-inverse">
    <span class="navbar-brand">THE PACKETS AND THEIR COMPONENTS</span>
    <div class="navbar-right" ng-show="isUserAuthorized()">
        <div class="navbar-text" ng-cloak>
            Hello, {{data.username}}
        </div>
        <button ng-click="logout()" class="btn btn-default navbar-btn">Logout</button>
    </div>
</div>
<ng-view/>
</body>
</html>

