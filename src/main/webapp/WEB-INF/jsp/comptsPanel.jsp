<div ng-controller="comptsPanelCtrl">
    <div class="alert alert-warning" ng-if="$parent.isDataLoadedProperly()
                    && !isPacketsNotLoaded() && isPacketSelected() && isComptsSelected()">
        The selected packet is empty. Please add new compts or select another one.
    </div>
    <div class="alert alert-warning" ng-if="$parent.isDataLoadedProperly() && !isPacketsNotLoaded()
    && !isPacketSelected() && isPacketNeverSelectedSoFar()">
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
                    <tr ng-repeat="compt in data.selectedCompts | filter:notNull | range:selectedPage:pageSize">
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
                    <a ng-repeat="page in data.selectedCompts | filter:notNull | pageCount:pageSize"
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