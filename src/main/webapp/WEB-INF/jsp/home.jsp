<html ng-app="packetApp" >

<head>
    <title>
        AngularSpring
    </title>

    <link href="resources/css/bootstrap.css"  type="text/css" rel="stylesheet"/>
    <link href="resources/css/bootstrap-theme.css" type="text/css" rel="stylesheet" />

    <script type="application/javascript" src="resources/js/angular_v1.5_min.js"></script>
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
    <div class="panel"
         ng-hide="errorCompts || errorComptsSupplData || errorStates || errorComboData || errorPacketState">
    
        <table class="table table-striped">
            <thead>
                <tr>
                    <th ng-show="stateLabels" ng-repeat="label in stateLabels"><span ng-bind="label"/></th>
                </tr>
            </thead>
            <tbody>
            <tr ng-repeat="compt in compts">
                <td>
                    <span ng-bind="compt.label" />
                </td>
                <td  ng-repeat="state in states">
                    <span ng-bind="defaultValues[compt.id][state.id]"
                          ng-hide="stateLabels.defaultIndex===state.id"></span>
                    <span ng-show="stateLabels.defaultIndex===state.id">
                          <select class="standard"
                                  ng-options="el for el in comboData[compt.id][state.id]"
                                  ng-model="defaultValues[compt.id][state.id]"
                                  ng-change="markComptAsUpdated(compt.id)">
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
        Error (<span ng-bind = "errorCompts.status"></span>). The packet compts data were not loaded.
        <button class="btn btn-danger" ng-click="reloadRoute()">Click here to try again</button>
    </div>

    <div class="alert alert-danger" ng-show="errorComptsSupplData">
        Error (<span ng-bind = "errorComptsSupplData.status"></span>). The packet supplement data for components rendering were not loaded.
        <button class="btn btn-danger" ng-click="reloadRoute()">Click here to try again</button>
    </div>

    <div class="alert alert-danger" ng-show="errorStates">
        Error (<span ng-bind="errorStates.status"></span>). The packet state stateLabels data were not loaded.
        <button class="btn btn-danger" ng-click="reloadRoute()">Click here to try again</button>
    </div>

    <div class="alert alert-danger" ng-show="errorComboData">
        Error (<span ng-bind="errorComboData.status"></span>). The combo data for the comboboxes were not loaded.
        <button class="btn btn-danger" ng-click="reloadRoute()">Click here to try again</button>
    </div>

    <div class="alert alert-danger" ng-show="errorPacketState">
        Error (<span ng-bind = "errorPacketState.status"></span>). The packet state was not loaded.
        <button class="btn btn-danger" ng-click="reloadRoute()">Click here to try again</button>
    </div>


    <div class="well" ng-hide="errorStates">
            <div class="inline">
                <div class="inline-radio" ng-repeat="state in states track by $index">
                    <input type="radio"
                           ng-model="stateLabels.defaultIndex"
                           ng-value="$index+1">
                    <span ng-bind="stateLabels[$index+1]"></span>
                </div>
            </div>
    </div>

    <form name="form"
          ng-hide="errorStates || errorComboData || errorCompts || errorComptsSupplData || errorPacketState">
        <label ng-class="label-primary">
            Enter new label:
            <input class="text"
                   name="newLabelName"
                   ng-model="newLabel"
                   ng-trim="true"
                   ng-maxlength="75"
                   blacklist="blacklist"
                   required />
            <div style="color:maroon" role="alert">
                <div ng-show="form.newLabelName.$error.required">You did not enter a label</div>
                <div ng-show="form.newLabelName.$error.maxlength">Your label is too long</div>
                <div ng-show="form.newLabelName.$error.blacklist">Your label is not unique</div>
            </div>
        </label>

        <div ng-repeat="state in states track by $index" >
            <div class="form-group">
                <label>Input new {{stateLabels[$index+1]}} val:</label>
                <select class="standard"
                        ng-options="el for el in defaultComboData"
                        ng-model="newValues[$index]">
                </select>
            </div>
        </div>
        <div>
            <span class="input-group-btn">
                  <button class="btn btn-primary" id="addBtn"
                          ng-click="addNewCompt()"
                          ng-disabled="form.$invalid"
                  >Add</button>
            </span>
                        <span class="input-group-btn">
                  <button class="btn btn-warning" id="reloadBtn"
                          ng-click="reloadRoute()"
                  >Reload</button>
            </span>
        </div>
        <div>
            <span class="input-group-btn">
                  <button class="btn btn-success" id="saveBtn"
                          ng-click="saveAllToBase()">Update the database</button>
            </span>
        </div>
    </form>

</div>

</body>

</html>
