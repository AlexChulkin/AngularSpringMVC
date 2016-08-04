/**
 * Created by achulkin on 04.06.14.
 */
var app = angular.module("packetApp", [])
    .constant("packetId",1)
    .constant("labelLabel", "Label");

app.controller("packetCtrl", function ($scope, $http, $window, packetId, labelLabel) {
    var simpleConfig = {withCredentials: true};
    var complConfig = {withCredentials: true, params: {packetId: packetId}};

    $http.get(contextPath + '/compts', complConfig).success(function (data) {
        $scope.newLabel = null;
        $scope.compts = {};
        $scope.labels = [];
        $scope.sortType = 'id';
        $scope.maximalIndex = 0;
        data.forEach(function (el) {
            var id = el.id;
            var label = el.label;
            $scope.compts[label] = {id: id, label: label};
            if (id > $scope.maximalIndex) {
                $scope.maximalIndex = id;
            }
        });

        $scope.defaultComboData = [];
        $scope.persistedRecentlyRemovedItemIds = [];
        $scope.updatedItemIds = [];
        $scope.newComptLabels = {};

        $http.get(contextPath + '/states', simpleConfig).success(function (data) {
            if (!data) {
                $scope.errorStates = true;
                return;
            }
            $scope.states = data;

            $scope.stateLabels = [];
            $scope.stateLabels.push(labelLabel);
            $scope.newValues = [];
            $scope.states.forEach(function (state) {
                $scope.stateLabels.push(state.label);
            });
            $http.get(contextPath + '/defaultComboData', simpleConfig).success(function (data) {
                if (!data) {
                    $scope.errorComboData = true;
                    return;
                }
                data.forEach(function (sd) {
                    $scope.defaultComboData.push(sd.label);
                });
                $scope.states.forEach(function () {
                    $scope.newValues.push($scope.defaultComboData[0]);
                });
                $http.get(contextPath + '/packetStateId', complConfig).success(function (data) {
                    if (!data) {
                        $scope.errorPacketState = true;
                        return;
                    }
                    $scope.stateLabels.defaultIndex = data;
                    $http.get(contextPath + '/comptsSupplInfo', complConfig).success(function (data) {
                        if (!data) {
                            $scope.errorComptsSupplData = true;
                            return;
                        }
                        $scope.checkedVals = {};
                        $scope.comboData = {};
                        data.forEach(function (el) {
                            var comptId = el.comptId;
                            var stateId = el.stateId;
                            var label = el.label;
                            if (!$scope.comboData[comptId]) {
                                $scope.comboData[comptId] = {};
                            }
                            if (!$scope.comboData[comptId][stateId]) {
                                $scope.comboData[comptId][stateId] = [];
                            }
                            $scope.comboData[comptId][stateId].push(label);
                            var checked = el.checked;
                            if (checked) {
                                if (!$scope.checkedVals[comptId]) {
                                    $scope.checkedVals[comptId] = {};
                                }
                                $scope.checkedVals[comptId][stateId] = label;
                            }
                        });
                    }).error(function (error) {
                        $scope.errorComptsSupplData = error;
                    });
                }).error(function (error) {
                    $scope.errorPacketState = error;
                });
            }).error(function (error) {
                $scope.errorComboData = error;
            });
        }).error(function (error) {
            $scope.errorStates = error;
        });
    }).error(function (error) {
        $scope.errorCompts = error;
    });

    $scope.addNewCompt = function () {
        var comptId = ++$scope.maximalIndex;
        $scope.compts[$scope.newLabel] = {id: comptId, label: $scope.newLabel, new: true};
        $scope.newComptLabels[$scope.newLabel] = comptId;
        $scope.newLabel = null;

        $scope.comboData[comptId] = {};
        $scope.checkedVals[comptId] = {};
        for (var i = 1; i <= $scope.states.length; i++) {
            $scope.comboData[comptId][i] = $scope.defaultComboData;
            $scope.checkedVals[comptId][i] = $scope.newValues[i - 1];
        }
    };

    $scope.deleteCompt = function (label) {
        var id = $scope.compts[label].id;
        var isNew = $scope.compts[label].new;
        
        delete $scope.compts[label];
        delete $scope.checkedVals[id];
        delete $scope.comboData[id];
        delete $scope.updatedItemIds[id];

        if (!isNew) {
            $scope.persistedRecentlyRemovedItemIds.push(id);
        }
    };

    $scope.markComptAsUpdated = function (comptId) {
        $scope.updatedItemIds.push(comptId);
    };

    $scope.saveAllToBase = function () {
        $scope.updatePacketStateInBase(packetId, $scope.stateLabels.defaultIndex);

        $scope.removeComptsFromBase();
        $scope.persistedRecentlyRemovedItemIds = [];

        var newCompts = [];
        for (var lbl in $scope.newComptLabels) {
            if ($scope.newComptLabels.hasOwnProperty(lbl)) {
                var id = $scope.newComptLabels[lbl];
                newCompts.push({label: lbl, vals: $scope.getCheckedValsForCompt(id)});
                $scope.compts[lbl].new = false;
            }
        }
        $scope.addComptsToBase(newCompts);
        $scope.newComptLabels = {};

        var updatedCompts = [];
        $scope.updatedItemIds.forEach(function (id) {
            updatedCompts.push({id: id, vals: $scope.getCheckedValsForCompt(id)});
        });
        $scope.updateComptsInBase(updatedCompts);
        $scope.updatedItemIds = [];
    };

    $scope.getCheckedValsForCompt = function (comptId) {
        var checkedVals = [];
        $scope.states.forEach(function (state, ind) {
            checkedVals.push($scope.checkedVals[comptId][ind + 1]);
        });
        return checkedVals;
    };

    $scope.removeComptsFromBase = function () {
        if (!$scope.persistedRecentlyRemovedItemIds) {
            return;
        }

        var removeConfig = {withCredentials: true, params: {idsToRemove: $scope.persistedRecentlyRemovedItemIds}};
        $http.post(contextPath + '/removeCompts', removeConfig).success(function (data) {
        }).error(function (error) {
        });
    };

    $scope.updateComptsInBase = function (updatedCompts) {
        if (!updatedCompts) {
            return;
        }
        var updateConfig = {withCredentials: true, params: {comptsParamsList: updatedCompts}};
        $http.post(contextPath + '/updateCompts', updateConfig).success(function (data) {
        }).error(function (error) {
        });
    };

    $scope.addComptsToBase = function (newCompts) {
        if (!newCompts) {
            return;
        }
        var addConfig = {withCredentials: true, params: {packetId: packetId, comptsParamsList: newCompts}};
        $http.post(contextPath + '/addCompts', addConfig).success(function (data) {
        }).error(function (error) {
        });
    };

    $scope.updatePacketStateInBase = function (packetId, newStateId) {
        var updateConfig = {withCredentials: true, params: {packetId: packetId, newStateId: newStateId}};
        $http.post(contextPath + '/updatePacketState', updateConfig).success(function (data) {
        }).error(function (error) {
        });
    };

    $scope.reloadRoute = function () {
        $window.location.reload();
    };
});

app.directive('blacklist', function () {
    return {
        require: 'ngModel',
        link: function ($scope, elem, attr, ngModel) {
            ngModel.$parsers.unshift(function (value) {
                ngModel.$setValidity('blacklist', !$scope.compts[value]);
                return value;
            });
        }
    };
});