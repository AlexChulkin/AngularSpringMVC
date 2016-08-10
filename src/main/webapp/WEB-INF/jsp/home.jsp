<!DOCTYPE html>
<html ng-app="packetApp" >

<head>
    <title>
        AngularSpring
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="resources/css/bootstrap.css"  type="text/css" rel="stylesheet"/>
    <link href="resources/css/bootstrap-theme.css" type="text/css" rel="stylesheet" />

    <script type="application/javascript" src="resources/js/angular_v1.5.8.js"></script>

    <script>
        angular.module("packetApp", ["customFilters"]);
    </script>
    <script type="application/javascript" src="resources/js/controllers/packetApp.js"></script>
    <script type="application/javascript" src="resources/js/filters/customFilters.js"></script>
    <script type="application/javascript" src="resources/js/ngmodules/angular-route.js"></script>


    <script type="text/javascript" charset="utf-8">
        var contextPath = '${pageContext.request.contextPath}';
    </script>

    <style>
        .state {
            width: 31%;
        }

        .inline-block {
            display: inline-block;
        }

        .states {
            padding: 2% 0 1% 43%;
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
         ng-hide="data.loadError">
        <div class="col-xs-2">
            <a ng-repeat="pkt in data.packets"
               ng-click="selectPacket(pkt)" class="btn btn-lg btn-default"
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
                <tr ng-repeat="compt in data.selectedCompts | range:selectedPage:pageSize">
                    <td width="43%">
                        <span ng-bind="compt.label"/>
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
            <div class="pull-right btn-group">
                <a ng-repeat="page in data.selectedCompts | pageCount:pageSize"
                   ng-click="selectPage($index)" class="btn btn-default"
                   ng-class="getPageClass($index)">
                    {{$index}}
                </a>
            </div>
            <div class="well" ng-hide="data.loadError">
                <div class="states">
                    <div class="state inline-block" ng-repeat="state in data.states track by $index">
                        <input type="radio"
                               ng-model="data.selectedStateIndex"
                               ng-value="$index+1">
                        <span ng-bind="data.stateLabels[$index+1]"></span>
                    </div>
                </div>
            </div>
            <form name="form" ng-hide="data.loadError">
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
                                       blacklist="blacklist" ,
                                       regex="regex"
                                       required/>
                                <div style="color:maroon" role="alert">
                                    <div ng-show="form.newLabelName.$error.required">You did not enter a label</div>
                                    <div ng-show="form.newLabelName.$error.maxlength">Your label is too long</div>
                                    <div ng-show="form.newLabelName.$error.blacklist">Your label is not unique</div>
                                    <div ng-show="form.newLabelName.$error.regex">
                                        Your label should contain letters, digits, underscore and spaces only.
                                    </div>
                                </div>
                            </label>
                            <span class="input-group-btn">
                                <button class="btn btn-large btn-primary" name="addBtn"
                                        ng-click="addNewCompt()"
                                        ng-disabled="form.$invalid">Add a component
                                </button>
                            </span>
                        </div>
                    </div>
                    <div class="col-sm-2" ng-repeat="state in data.states track by $index">
                        <select class="special"
                                ng-options="el for el in data.allComboData"
                                ng-model="data.newValues[$index]">
                        </select>
                    </div>
                </div>
            </form>

            <div class="inline">

                        <span class="input-group-btn ">
                              <button class="btn btn-large btn-warning" id="reloadBtn"
                                      ng-click="reloadRoute()">Reload from base</button>
                        </span>
                        <span class="input-group-btn">
                              <button class="btn btn-large btn-success" id="saveBtn"
                                      ng-click="saveAllToBase()">Update the base</button>
                        </span>
            </div>
            <div class="alert alert-danger" ng-show="data.loadError">
                Error (<span ng-bind="data.loadError.status"></span>). The data were not loaded.
                <button class="btn btn-danger" ng-click="reloadRoute()">Click here to try again</button>
            </div>
        </div>
    </div>
</body>
</html>
