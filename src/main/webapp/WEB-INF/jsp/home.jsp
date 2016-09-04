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
<body ng-controller="mainCtrl">
<div class="navbar navbar-inverse">
    <a class="navbar-brand" href="#">THE PACKETS AND THEIR COMPONENTS</a>
</div>
<div class="panel" ng-if="isDataLoadedProperly()">
    <div class="col-xs-2 column">
        <%--<ng-include src="WEB-INF/jsp/packetsPanel.jsp"></ng-include>--%>
        <div ng-controller="packetsPanelCtrl">
            <div class="flex" ng-repeat="pkt in $parent.data.allPackets">
                <a ng-click="$parent.selectPacket(pkt)"
                   ng-class="getPacketClass(pkt)">
                    Packet#<span ng-bind="pkt.id"/>
                </a>
                <a ng-click="saveAllChangesToBase(pkt.id)"
                   class="btn btn-sm btn-success expanded-margin-bottom">
                    Save
                </a>
                <a ng-click="$parent.loadPacketById(pkt.id)"
                   class="btn btn-sm btn-warning expanded-margin-bottom">
                    Reload
                </a>
                <a ng-click="deletePacketLocally(pkt)"
                   class="btn btn-sm btn-danger expanded-margin-bottom">
                    Del
                </a>
            </div>
            <div class="btn-group" id="aggregate-btns" ng-if="showAggregateButtons()">
                <a ng-click="addPacketLocally()"
                   class="btn btn-md btn-block btn-default">
                    Add packet
                </a>
                <a ng-click="saveAllChangesToBase(null)"
                   class="btn btn-md btn-block btn-success">
                    Save changes
                </a>
                <a ng-click="reloadRoute()"
                   class="btn btn-md btn-block btn-warning">
                    Reload all
                </a>
            </div>
        </div>
    </div>
    <div class="col-xs-10 column">
        <%--<ng-include src="WEB-INF/jsp/comptsPanel.jsp"></ng-include>--%>
        <div ng-controller="comptsPanelCtrl">
            <div class="alert alert-warning" ng-if="$parent.isDataLoadedProperly()
                    && !isPacketsNotLoaded() && isPacketSelected() && !isComptsSelected()">
                The selected packet is empty. Please add new compts or select another one.
            </div>
            <div class="alert alert-warning" ng-if="$parent.isDataLoadedProperly() && !isPacketsNotLoaded()
                                && !isPacketSelected() && isPacketAlreadySelectedAtLeastOnce()">
                No packet is selected. Please select (or add and select) one.
            </div>
            <div class="alert alert-warning" ng-if="$parent.isDataLoadedProperly() && isPacketsNotLoaded()">
                There are no loaded packets. Please add one.
            </div>
            <div id="rightColumn" ng-show="isPacketSelected()">
                <div id="table-pagination-radio">
                    <div ng-show="isComptsSelected()">
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th ng-show="$parent.data.allStateLabels"
                                    ng-repeat="label in $parent.data.allStateLabels"><span ng-bind="label"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="compt in data.selectedCompts | filter:notNull
                                                    | range:$parent.data.selectedPage:data.pageSize">
                                <td width="43%">
                                    <span ng-bind="compt.label"/>
                                </td>
                                <td width="17%" ng-repeat="state in $parent.data.allStates">
                                    <span ng-bind="$parent.data.allCheckedComboData[compt.id][state.id]"
                                          ng-hide="$parent.data.selectedPacket.stateId == state.id"></span>
                                    <span ng-show="$parent.data.selectedPacket.stateId == state.id">
                                          <select class="standard"
                                                  ng-options=
                                                          "el for el in $parent.data.allComboData[compt.id][state.id]"
                                                  ng-model="$parent.data.allCheckedComboData[compt.id][state.id]"
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
                            <a ng-repeat="page in data.selectedCompts | filter:notNull | pageCount:data.pageSize"
                               ng-click="$parent.selectPage(page)" class="btn btn-default"
                               ng-class="getPageClass(page)">
                                <span ng-bind="page"></span>
                            </a>
                        </div>
                    </div>
                    <div class="well">
                        <div id="states">
                            <div class="state" ng-repeat="state in $parent.data.allStates track by $index">
                                <input type="radio"
                                       ng-model="$parent.data.selectedPacket.stateId"
                                       ng-value="$index+1">
                                <span ng-bind="$parent.data.allStateLabels[$index+1]"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <form name="form" ng-if="isPacketSelected()">
                    <div class="row grid-row">
                        <div class="col-sm-5">
                            <div class="form-group">
                                <label ng-class="label-primary">
                                    Enter new compt label:
                                    <input class="text"
                                           name="newLabelName"
                                           ng-model="data.newLabel"
                                           ng-trim="true"
                                           ng-maxlength="75"
                                           blacklist="blacklist"
                                           regex="regex"
                                           required/>
                                    <div style="color:maroon" role="alert">
                                        <div ng-show="form.newLabelName.$error.required">You did not enter a label</div>
                                        <div ng-show="form.newLabelName.$error.maxlength">Label is too long</div>
                                        <div ng-show="form.newLabelName.$error.blacklist">Label is not unique</div>
                                        <div ng-show="form.newLabelName.$error.regex">
                                            Label should contain latin letters, digits, underscore and spaces only.
                                        </div>
                                    </div>
                                </label>
                            </div>
                        </div>
                        <div class="col-sm-2" ng-repeat="state in $parent.data.allStates track by $index">
                            <select class="special"
                                    ng-options="el for el in $parent.data.comboDataDefaultSet"
                                    ng-model="$parent.data.newComptCheckedVals[$index]">
                            </select>
                        </div>
                        <div class="col-sm-1">
                            <span class="input-group-btn">
                                <button class="btn btn-xs btn-success" id="addBtn"
                                        ng-click="addComptLocally()"
                                        ng-disabled="form.$invalid">Add
                                </button>
                            </span>
                        </div>
                    </div>
                </form>
            </div>
        </div>
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
