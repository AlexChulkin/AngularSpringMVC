<html ng-app="packetApp" >

<head>
    <title>
        Home page title
    </title>

    <link href="resources/css/bootstrap.css"  type="text/css" rel="stylesheet"/>
    <link href="resources/css/bootstrap-theme.css" type="text/css" rel="stylesheet" />

    <script type="application/javascript" src="resources/js/angular.js"></script>
    <script type="application/javascript" src="resources/js/packetApp.js"></script>

    <script type="text/javascript" charset="utf-8">
        var contextPath = '${pageContext.request.contextPath}';
    </script>

</head>

<body>
<h1>
    Home page
</h1>


<div id="packetDiv" ng-controller="packetCtrl">
    {{test}}
    {{compts}}


</div>

</body>

</html>
