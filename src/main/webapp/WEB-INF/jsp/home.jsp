<html ng-app="packetApp" >

<head>
    <title>
        AngularSpring
    </title>

    <link href="resources/css/bootstrap.css"  type="text/css" rel="stylesheet"/>
    <link href="resources/css/bootstrap-theme.css" type="text/css" rel="stylesheet" />

    <script type="application/javascript" src="resources/js/angular.js"></script>
    <script type="application/javascript" src="resources/js/packetApp.js"></script>

    <script type="text/javascript" charset="utf-8">
        var contextPath = '${pageContext.request.contextPath}';
    </script>

    <style>
        select.standard {background-color: lightcyan; font-weight: bold; }
    </style>
</head>

<body>
<h1>
    The packet and the components
</h1>



<div id="packetDiv" ng-controller="packetCtrl">
    <div class="panel" ng-hide="errorCompts">
    
        <table class="table table-striped">
            <thead>
                <tr>
                    <th ng-show="labels" ng-repeat="label in labels"> <span ng-bind="label"/></th>
                </tr>
            </thead>
            <tbody>
            <tr ng-repeat="compt in compts" ng-hide="errorStates || errorData">
                <td>
                    <span ng-bind="compt.label" />
                </td>
                <td  ng-repeat="state in states">
                    <span  ng-bind="defaultValues[compt.id][state.id]" ng-hide="labels.defaultLabel===labels[state.id]"></span>
                    <span  ng-show="labels.defaultLabel===labels[state.id]">
                          <select class="standard"
                                  ng-options="el for el in comboData[compt.id][state.id]"
                                  ng-model="defaultValues[compt.id][state.id]"
                                  ng-change="markAsUpdated(compt.id)">
                          </select>
                    </span>
                </td>
                <td>
                    <button ng-click="deleteCompt(compt)" class="btn btn-xs btn-primary">
                        Delete
                    </button>
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
            <div class="inline">
                <div class="inline-radio" ng-repeat="state in states track by $index">
                    <input type="radio"
                           ng-model="labels.defaultLabel"
                           ng-value="labels[$index+1]">
                    <span ng-bind="labels[$index+1]"></span>
                </div>
            </div>
    </div>

    <div class="alert alert-danger" ng-show="errorStates">
        Error (<span ng-bind = "errorStates.status"></span>). The packet labels data was not loaded.
        <a href="/WEB-INF/jsp/home.jsp" class="alert-link">Click here to try again</a>
    </div>

        <form name="form">
            <label>
                Enter new label:
                <input class="text"
                       name="newLabelName"
                       ng-model="newLabel"
                       ng-maxlength="75"
                       required />
            </label>

            <div style="color:maroon" role="alert">
                <div ng-show="form.newLabelName.$error.required">You did not enter a field</div>
                <div ng-show="form.newLabelName.$error.maxlength">Your field is too long</div>
            </div>
            <div ng-repeat="state in states track by $index">
                <div class="form-group">
                    <label>Input new {{labels[$index+1]}} val:</label>
                    <select class="standard"
                            ng-options="el for el in defaultComboData"
                            ng-model="newValues[$index]">
                    </select>
                </div>
            </div>
            <div>
                <span class="input-group-btn">
                      <button class="btn btn-default" id="addBtn"
                              ng-click="addNewCompt(newLabel)"
                              ng-disabled="form.$invalid"
                      >Add</button>
                </span>
            </div>
            <div>
                <span class="input-group-btn">
                      <button class="btn btn-default" id="saveBtn"
                              ng-click="save()">Update the database</button>
                </span>
            </div>
        </form>

    </div>

</div>

</body>

</html>
