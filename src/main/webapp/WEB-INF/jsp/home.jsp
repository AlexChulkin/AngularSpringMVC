<html ng-app="packetApp" >

<head>
    <title>
        AngularSpring
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="resources/css/bootstrap.css"  type="text/css" rel="stylesheet"/>
    <link href="resources/css/bootstrap-theme.css" type="text/css" rel="stylesheet" />

    <script type="application/javascript" src="resources/js/angular_v1.5_min.js"></script>
    <script type="application/javascript" src="resources/js/packetApp.js"></script>

    <script type="text/javascript" charset="utf-8">
        var contextPath = '${pageContext.request.contextPath}';
    </script>

    <style>
        .inline-radio-state {
            display: inline-block;
            padding: 0 35px;
            width: 30%;
        }

        .inline-state {
            padding-left: 40%;
        }

        select.standard {
            background-color: lightcyan;
            font-weight: bold;
        }

        select.special {
            background-color: #b6fffd;
            font-weight: bold;
        }

        #gridContainer {
            padding: 20px;
        }

        .grid-row > div {
            padding: 0 25px;
        }
    </style>
</head>
<body>
<div id="packetDiv" ng-controller="packetCtrl">

    <div class="panel"
         ng-hide="errorCompts || errorComptsSupplData || errorStates || errorComboData || errorPacketState">
        <h1 class="panel-header" align="center" background-color="red">
            The packet and the components
        </h1>
    
        <table class="table table-striped">
            <thead>
                <tr>
                    <th ng-show="stateLabels" ng-repeat="label in stateLabels"><span ng-bind="label"/></th>
                </tr>
            </thead>
            <tbody>
            <tr ng-repeat="compt in compts">
                <td width="42%">
                    <span ng-bind="compt.label" />
                </td>
                <td width="17%" ng-repeat="state in states">
                    <span ng-bind="checkedVals[compt.id][state.id]"
                          ng-hide="stateLabels.defaultIndex===state.id"></span>
                    <span ng-show="stateLabels.defaultIndex===state.id">
                          <select class="standard"
                                  ng-options="el for el in comboData[compt.id][state.id]"
                                  ng-model="checkedVals[compt.id][state.id]"
                                  ng-change="markComptAsUpdated(compt)">
                          </select>
                    </span>
                </td>
                <td>
                    <button ng-click="deleteCompt(compt.label)" class="btn btn-xs btn-primary">
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
        <div class="inline-state">
            <div class="inline-radio-state" ng-repeat="state in states track by $index">
                    <input type="radio"
                           ng-model="stateLabels.defaultIndex"
                           ng-value="$index+1">
                    <span ng-bind="stateLabels[$index+1]"></span>
                </div>
            </div>
    </div>
    <form name="form"
          ng-hide="errorStates || errorComboData || errorCompts || errorComptsSupplData || errorPacketState">
        <div id="gridContainer">
            <div class="row grid-row">
                <div class="col-sm-5">
                    <div class="form-group">
                        <label ng-class="label-primary">
                            Enter new component:
                            <input class="text"
                                   name="newLabelName"
                                   ng-model="newLabel"
                                   ng-trim="true"
                                   ng-maxlength="75"
                                   blacklist="blacklist"
                                   required/>
                            <div style="color:maroon" role="alert">
                                <div ng-show="form.newLabelName.$error.required">You did not enter a label</div>
                                <div ng-show="form.newLabelName.$error.maxlength">Your label is too long</div>
                                <div ng-show="form.newLabelName.$error.blacklist">Your label is not unique</div>
                            </div>
                        </label>
                    </div>
                </div>
                <div class="col-sm-2" ng-repeat="state in states track by $index">
                    <select class="special"
                            ng-options="el for el in defaultComboData"
                            ng-model="newValues[$index]">
                    </select>
                </div>
            </div>
        </div>
        <div class="inline">
            <span class="input-group-btn">
                  <button class="btn btn-large btn-primary" id="addBtn"
                          ng-click="addNewCompt()"
                          ng-disabled="form.$invalid">Add a component
                  </button>
            </span>
            <span class="input-group-btn ">
                  <button class="btn btn-large btn-warning" id="reloadBtn"
                          ng-click="reloadRoute()">Reload from base</button>
            </span>
            <span class="input-group-btn">
                  <button class="btn btn-large btn-success" id="saveBtn"
                          ng-click="saveAllToBase()">Update the base</button>
            </span>
        </div>
    </form>
</div>
</body>
</html>
