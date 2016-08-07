<html ng-app="packetApp" >

<head>
    <title>
        AngularSpring
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="resources/css/bootstrap.css"  type="text/css" rel="stylesheet"/>
    <link href="resources/css/bootstrap-theme.css" type="text/css" rel="stylesheet" />

    <script type="application/javascript" src="resources/js/angular_v1.5_min.js"></script>
    <script type="application/javascript" src="resources/js/controllers/packetApp.js"></script>

    <script type="text/javascript" charset="utf-8">
        var contextPath = '${pageContext.request.contextPath}';
    </script>

    <style>
        .inline-radio-state {
            display: inline-block;
            width: 31%;
        }

        .inline-state {
            padding-left: 43%;
        }

        select.standard {
            background-color: lightcyan;
            font-weight: bold;
        }
        select.special {
            background-color: #b6fffd;
            font-weight: bold;
            width: 80%;
        }
        .grid-row > div {
            padding-left: 24px;
        }
    </style>
</head>
<body ng-controller="packetCtrl">
<div class="navbar navbar-inverse">
    <a class="navbar-brand" href="#">THE PACKETS AND THEIR COMPONENTS</a>
    </div>
<div id="packetDiv">
    <div class="panel"
         ng-hide="data.errorCompts || data.errorComptsSupplData || data.errorStates
             || data.errorComboData || data.errorPacketState || data.errorPackets">
        <div class="col-xs-2">
            <a ng-repeat="pkt in data.packets"
               ng-click="selectPacket(pkt)" class="btn btn-default"
               ng-class="getPacketClass(pkt)">
                Packet#<span ng-bind="pkt.id"/>
            </a>
        </div>
        <div class="col-xs-10">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th ng-show="data.stateLabels"
                        ng-repeat="label in data.stateLabels"><span ng-bind="label"/></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="compt in data.selectedCompts">
                    <td width="35%">
                        <span ng-bind="compt.label"/>
                    </td>
                    <td width="4%">
                        <span ng-bind="compt.id"/>
                    </td>
                    <td width="4%">
                        <span ng-bind="compt.packetId"/>
                    </td>
                    <td width="17%" ng-repeat="state in data.states">
                        <span ng-bind="data.checkedVals[compt.id][state.id]"
                              ng-hide="data.selectedStateIndex==state.id"></span>
                        <span ng-show="data.selectedStateIndex==state.id">
                              <select class="standard"
                                      ng-options="el for el in data.comboData[compt.id][state.id]"
                                      ng-model="data.checkedVals[compt.id][state.id]"
                                      ng-change="markComptAsUpdated(compt)">
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

            <div class="well" ng-hide="data.errorStates">
                <div class="inline-state">
                    <div class="inline-radio-state" ng-repeat="state in data.states track by $index">
                        <input type="radio"
                               ng-model="data.selectedStateIndex"
                               ng-value="$index+1">
                        <span ng-bind="data.stateLabels[$index+1]"></span>
                    </div>
                </div>
                </div>
            <form name="form"
                  ng-hide="data.errorStates || data.errorComboData || data.errorCompts
                      || data.errorComptsSupplData || data.errorPacketState || data.errorPackets">
                <div id="gridContainer">
                    <div class="row grid-row">
                        <div class="col-sm-5">
                            <div class="form-group">
                                <label ng-class="label-primary">
                                    Enter new component:
                                    <input class="text"
                                           name="newLabelName"
                                           ng-model="data.newLabel"
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
                        <div class="col-sm-2" ng-repeat="state in data.states track by $index">
                            <select class="special"
                                    ng-options="el for el in data.defaultComboData"
                                    ng-model="newValues[$index]">
                            </select>
                        </div>
                    </div>
                    </div>
                <div class="inline">
                        <span class="input-group-btn">
                              <button class="btn btn-large btn-primary" id="addBtn"
                                      ng-click="addNewCompt()"
                                      ng-disabled="form.$invalid || isAddingNotAllowed()">Add a component
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
            <div name="errorsDiv">
                <div class="alert alert-danger" ng-show="data.errorPackets">
                    Error (<span ng-bind="data.errorPackets.status"></span>). The packets were not loaded.
                    <button class="btn btn-danger" ng-click="reloadRoute()">Click here to try again</button>
                </div>
                <div class="alert alert-danger" ng-show="data.errorCompts">
                    Error (<span ng-bind="data.errorCompts.status"></span>). The packet compts data were not loaded.
                    <button class="btn btn-danger" ng-click="reloadRoute()">Click here to try again</button>
                </div>

                <div class="alert alert-danger" ng-show="data.errorStates">
                    Error (<span ng-bind="data.errorStates.status"></span>). The packet state stateLabels data were not
                    loaded.
                    <button class="btn btn-danger" ng-click="reloadRoute()">Click here to try again</button>
                </div>

                <div class="alert alert-danger" ng-show="data.errorPacketState">
                    Error (<span ng-bind="data.errorPacketState.status"></span>). The packet state was not loaded.
                    <button class="btn btn-danger" ng-click="reloadRoute()">Click here to try again</button>
                </div>

                <div class="alert alert-danger" ng-show="data.errorComboData">
                    Error (<span ng-bind="data.errorComboData.status"></span>). The combo data for the comboboxes were
                    not loaded.
                    <button class="btn btn-danger" ng-click="reloadRoute()">Click here to try again</button>
                </div>


                <div class="alert alert-danger" ng-show="data.errorComptsSupplData">
                    Error (<span ng-bind="data.errorComptsSupplData.status"></span>). The packet supplement data for
                    components rendering were not loaded.
                    <button class="btn btn-danger" ng-click="reloadRoute()">Click here to try again</button>
                </div>

            </div>
            </div>
    </div>
</body>
</html>
