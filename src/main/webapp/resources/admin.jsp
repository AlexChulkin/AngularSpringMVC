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
    <script type="application/javascript" src="resources/angular_v1.5.8.js"></script>
    <script src="resources/ngmodules/angular-route.js"></script>
    <script src="resources/ngmodules/angular-cookies.js"></script>
    <link href="resources/css/bootstrap.css" rel="stylesheet"/>
    <link href="resources/css/bootstrap-theme.css" rel="stylesheet"/>
    <link href="resources/css/home-styles.css" rel="stylesheet"/>
    <script>
        angular.module("packetAdminApp", ["customFilters", "ngRoute", "ngCookies"])
                .config(function ($routeProvider) {

                    $routeProvider.when("/login", {
                        templateUrl: contextPath + "/resources/views/adminLogin.html"
                    });

                    $routeProvider.when("/main", {
                        templateUrl: contextPath + "/resources/views/adminMain.html"
                    });

                    $routeProvider.otherwise({
                        redirectTo: "/login"
                    });
                });
    </script>
    <script type="application/javascript" src="resources/controllers/adminLoginControllers.js"></script>
    <script type="application/javascript" src="resources/controllers/adminMainControllers.js"></script>
    <script type="application/javascript" src="resources/controllers/packetsPanelControllers.js"></script>
    <script type="application/javascript" src="resources/controllers/comptsPanelControllers.js"></script>
    <script type="application/javascript" src="resources/filters/customFilters.js"></script>
    <script type="application/javascript" src="resources/services/exchangeService.js"></script>

    <script type="text/javascript" charset="utf-8">
        var contextPath = '${pageContext.request.contextPath}';
    </script>
</head>
<body ng-controller="authCtrl">
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

