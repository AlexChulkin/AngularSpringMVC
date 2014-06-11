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
    <div class="panel" ng-hide="errorCompts ">
        <table class="table table-striped">
            <thead>
            <tr>
                <th ng-show="labels" ng-repeat="label in allLabels"> <span ng-bind="label"/></th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="compt in compts" ng-hide="errorStates || errorData">
                <td>
                    <span ng-bind="compt.label" />
                </td>
                <td  ng-repeat="state in states">
                    <span ng-bind="defaultValues[compt.id-1][state.id-1]" ng-hide="labels.defaultLabel===defaultValues[compt.id-1][state.id-1]"></span>
                            <span ng-show="labels.defaultLabel===defaultValues[compt.id-1][state.id-1]">
                                <select ng-options="el.label for el in data | applyFilter:compt.id:state.id" ng-model="defaultValues[compt.id-1][state.id-1]">
                                </select>
                            </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div class="alert alert-danger" ng-show="errorCompts">
        Error (<span ng-bind = "errorCompts.status"></span>). The packet compts data was not loaded.
        <a href="/WEB-INF/jsp/home.jsp" class="alert-link">Click here to try again</a>
    </div>

    <div class="well" ng-hide="errorStates">
        <!--<input type="radio"  ng-repeat="label in labels" ng-model="labels.defaultLabel"  ng-value="label">  <span ng-bind="label"></span>-->
        <input type="radio"  ng-model="labels.defaultLabel"  ng-value="labels[1]">  <span ng-bind="labels[1]"></span>
        <input type="radio"  ng-model="labels.defaultLabel"  ng-value="labels[2]">  <span ng-bind="labels[2]"></span>
        <input type="radio"  ng-model="labels.defaultLabel"  ng-value="labels[3]">  <span ng-bind="labels[3]"></span>
    </div>

    <div class="alert alert-danger" ng-show="errorStates">
        Error (<span ng-bind = "errorStates.status"></span>). The packet labels data was not loaded.
        <a href="/WEB-INF/jsp/home.jsp" class="alert-link">Click here to try again</a>
    </div>

</div>

</body>

</html>
