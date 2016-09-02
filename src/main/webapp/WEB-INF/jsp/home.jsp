<!DOCTYPE html>
<html ng-app="packetApp">

<head>
    <title>
        AngularSpring
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="resources/css/bootstrap.css" type="text/css" rel="stylesheet"/>
    <link href="resources/css/bootstrap-theme.css" type="text/css" rel="stylesheet"/>
    <link href="resources/css/home-styles.css" type="text/css" rel="stylesheet"/>

    <script type="application/javascript" src="resources/angular_v1.5.8.js"></script>

    <!--<script>-->
    <!--angular.module("packetApp", ["customFilters", "ngRoute"])-->
    <!--.config(function ($routeProvider) {-->
    <!--//                    $routeProvider.when("/products", {-->
    <!--//                        templateUrl: "/views/productList.html"-->
    <!--//                    });-->
    <!--//-->
    <!--//                    $routeProvider.otherwise({-->
    <!--//                        templateUrl: "/WEB-INF/jsp/packdetsPanel.jsp"-->
    <!--//                    });-->
    <!--});-->
    <!--</script>-->
    <script>
        angular.module("packetApp", ["customFilters"]);
    </script>
    <script type="application/javascript" src="resources/controllers/packetApp.js"></script>
    <script type="application/javascript" src="resources/controllers/packetsPanelControllers.js"></script>
    <script type="application/javascript" src="resources/controllers/comptsPanelControllers.js"></script>
    <script type="application/javascript" src="resources/filters/customFilters.js"></script>
    <script type="application/javascript" src="resources/ngmodules/angular-route.js"></script>


    <script type="text/javascript" charset="utf-8">
        var contextPath = '${pageContext.request.contextPath}';
    </script>
</head>
<body ng-controller="packetCtrl">
<div class="navbar navbar-inverse">
    <a class="navbar-brand" href="#">THE PACKETS AND THEIR COMPONENTS</a>
</div>
<div class="panel" ng-if="isDataLoadedProperly()">
    <div class="col-xs-2 column">
        <ng-include src="WEB-INF/jsp/packetsPanel.jsp"></ng-include>
    </div>
    <div class="col-xs-10 column">
        <ng-include src="WEB-INF/jsp/comptsPanel.jsp"></ng-include>
    </div>
</div>

<div class="alert alert-danger" ng-if="isDataLoadError()">
    The error occurred during the data loading.
    <button class="btn btn-danger" ng-click="reloadRoute()">Click here to try again</button>
</div>
<div class="alert alert-danger" ng-if="isStatesNotLoaded()">
    The loaded states data is empty. Probably the database table is void.
    <button class="btn btn-danger" ng-click="reloadRoute()">Click here to try again</button>
</div>
<div class="alert alert-danger" ng-if="isComboDataNotLoaded()">
    The loaded comboData is empty. Probably the database table is void.
    <button class="btn btn-danger" ng-click="reloadRoute()">Click here to try again</button>
</div>
</body>
</html>
