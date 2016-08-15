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
            display: inline-block;
        }

        .column {
            padding-right: 10px;
            padding-left: 10px;
        }

        .flex {
            display: inline-flex;
        }

        .aggregate-btns {
            padding: 10px 0;
        }
        .states {
            padding: 1% 0 1% 43%;
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
<div class="panel">
    <div class="col-xs-1 column">
        <div class="flex" ng-repeat="pkt in data.packets">
            <a ng-click="selectPacket(pkt)"
               ng-class="getPacketClass(pkt)">
                Packet#<span ng-bind="pkt.id"/>
                </a>
            <a ng-click="deletePacketLocally(pkt)"
               class="btn btn-sm btn-danger">
                Del
            </a>
            </div>
        <div class="btn-group aggregate-btns">
            <a ng-click="addPacketLocally()"
               class="btn btn-md btn-block btn-default">
                Add packet
            </a>
            <a ng-click="reloadRoute()"
               class="btn btn-md btn-block btn-warning">
                Reload from base
            </a>
            <a ng-click="saveAllToBase()"
               class="btn btn-md btn-block btn-success">
                Update the base
            </a>
            </div>
    </div>
    <div class="col-xs-11 column">
        <div ng-hide="data.loadError || data.loadEmpty || data.packetDeleted">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th ng-show="data.stateLabels"
                        ng-repeat="label in data.stateLabels"><span ng-bind="label"/></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="compt in data.selectedCompts | filter:notNull | range:selectedPage:pageSize">
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
                                          ng-change="updateComptLocally(compt)">
                                  </select>
                            </span>
                    </td>
                    <td>
                        <button ng-click="deleteComptLocally(compt)" class="btn btn-xs btn-primary">
                            Delete
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
            <div class="pull-right btn-group">
                <a ng-repeat="page in data.selectedCompts | filter:notNull | pageCount:pageSize"
                   ng-click="selectPage(page)" class="btn btn-default"
                   ng-class="getPageClass(page)">
                    <span ng-bind="page"></span>
                </a>
            </div>
            <div class="well">
                <div class="states">
                    <div class="state" ng-repeat="state in data.states track by $index">
                        <input type="radio"
                               ng-model="data.selectedStateIndex"
                               ng-value="$index+1">
                        <span ng-bind="data.stateLabels[$index+1]"></span>
                        </div>
                    </div>
                </div>
            <form name="form">
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
                                            ng-click="addComptLocally()"
                                            ng-disabled="form.$invalid">Add a component
                                    </button>
                                </span>
                        </div>
                    </div>
                    <div class="col-sm-2" ng-repeat="state in data.states track by $index">
                        <select class="special"
                                ng-options="el for el in data.comboDataDefaultSet"
                                ng-model="data.newValues[$index]">
                        </select>
                    </div>
                </div>
            </form>
        </div>
            <div class="inline">
            </div>
            <div class="alert alert-danger" ng-show="data.loadError">
                Error (<span ng-bind="data.loadError.status"></span>). The error occurred during the data loading.
                <button class="btn btn-danger" ng-click="reloadRoute()">Click here to try again</button>
            </div>
            <div class="alert alert-danger" ng-show="data.loadEmpty ">
                Error (<span ng-bind="data.loadEmpty.status"></span>). The loaded data is empty. Probably some
                database error occurred or the database is void.
                <button class="btn btn-danger" ng-click="reloadRoute()">Click here to try again</button>
            </div>
            <div class="alert alert-danger" ng-show="data.packetDeleted">
                The selected packet is deleted. Please select or add another one.
            </div>
        </div>
    </div>


</body>
</html>
