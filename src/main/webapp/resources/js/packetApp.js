/**
 * Created by achulkin on 04.06.14.
 */
angular.module("packetControllers",[])
    .constant("packetId",1)
    .constant("labelLabel","Label")
    .controller("packetCtrl", function ($scope,$http,$filter,packetId,labelLabel) {

    var simpleConfig = {withCredentials:true};
    var complConfig = {withCredentials:true, params:{packetId:packetId}};

            $http.get(contextPath + '/compts',complConfig).success(function (data) {
                $scope.defaultComboData = ["VERY_WEAK", "WEAK", "MODERATE", "ADEQUATE", "STRONG", "VERY_STRONG"];
                $scope.newValues = [];
                $scope.compts = {};
                angular.forEach(data, function(key) {
                    $scope.compts[key.id] = key;
                });
                $scope.maximalIndex = data.length;
                $scope.persistedRecentlyRemovedItemIds = [];

                $http.get(contextPath + '/states',simpleConfig).success(function (data) {
                    $scope.states = data;

                    $scope.labels = [];
                    $scope.labels.push(labelLabel);
                    for(var j=0; j<$scope.states.length; j++) {
                        $scope.labels.push($scope.states[j].label);
                    }
                    $http.get(contextPath + '/packetState',complConfig).success(function (data) {
                        $scope.labels.defaultLabel = $scope.labels[data];
                        $http.get(contextPath + '/staticData',complConfig).success(function (data) {
                            $scope.defaultValues = {};
                            $scope.comboData = {};
                            for(var j = 0; j < data.length; j++) {
                                var comptId = data[j][1];
                                var stateId = data[j][2];
                                var label = data[j][3];
                                if (!$scope.comboData[comptId]) {
                                    $scope.comboData[comptId] = {};
                                }
                                if (!$scope.comboData[comptId][stateId]) {
                                    $scope.comboData[comptId][stateId] = [];
                                }
                                $scope.comboData[comptId][stateId].push(label);
                                var checked = data[j][4];
                                if(checked) {
                                    if (!$scope.defaultValues[comptId]) {
                                        $scope.defaultValues[comptId] = {};
                                    }
                                    $scope.defaultValues[comptId][stateId] = label;
                                }
                            }
                        }).error(function (error) {
                            $scope.errorData = error;
                            return;
                        });
                    })
                }).error(function (error) {
                    $scope.errorStates = error;
                });
            }).error(function (error) {
                $scope.errorCompts = error;
                return;
            });


            $scope.addNewCompt = function(newLabel) {
                var comptId = ++$scope.maximalIndex;
                $scope.compts[comptId] = { id: comptId, label: newLabel, new:true};
                // var newVals = [preCommiteeNewVal,inCommiteeNewVal,finalNewVal];

                $scope.comboData[comptId] = {};
                $scope.defaultValues[comptId] = {};
                for (var i = 1; i <= $scope.states.length; i++) {
                    $scope.comboData[comptId][i] = $scope.defaultComboData;
                    $scope.defaultValues[comptId][i] = $scope.newValues[i-1];
                }
            }
            $scope.deleteCompt = function (compt) {
                var id = compt.id;

                delete $scope.compts[id];
                delete $scope.defaultValues[id];
                delete $scope.comboData[id];

                if(compt.new!==true) {
                    $scope.persistedRecentlyRemovedItemIds.push(id);
                }
            }

            $scope.markAsUpdated = function (compt) {
                compt.updated = true;
            }
            $scope.save = function() {
                $scope.collectIdsForRemoval();
                for(var i=0;i<=$scope.maximalIndex; i++) {
                    if($scope.compts[i]) {
                        if ($scope.compts[i].new === true) {
                            $scope.addComptToBase($scope.compts[i].label, $scope.getDefaultValsForCompt($scope.compts[i]));
                            $scope.compts[i].new = false;
                        } else if ($scope.compts[i].updated === true) {
                            $scope.updateComptInBase($scope.compts[i].id, $scope.getDefaultValsForCompt($scope.compts[i]));
                            $scope.compts[i].updated = false;
                        }
                    }
                }
            }
            $scope.getDefaultValsForCompt = function(compt){
                var defaultVals=[];
                for(var i=1; i<=$scope.states.length; i++){
                    defaultVals.push($scope.defaultValues[compt.id][i]);
                }
                return defaultVals;
            }
            $scope.collectIdsForRemoval = function() {
                if(!$scope.persistedRecentlyRemovedItemIds) {
                    return;
                }

                $scope.removeComptsFromBase($scope.persistedRecentlyRemovedItemIds);
                $scope.persistedRecentlyRemovedItemIds = [];
            }
            $scope.removeComptsFromBase = function (ids) {
                if (!ids) {
                    return;
                }
                var removeConfig = {withCredentials:true, params:{idsToRemove:ids}};
                $http.post(contextPath + '/removeCompts',removeConfig).success(function (data) {
                }).error(function (error) {
                });
            }
            $scope.updateComptInBase = function(comptId, defaultVals) {
                var updateConfig = {withCredentials:true, params:{comptId:comptId,defaultVals:defaultVals}};
                $http.post(contextPath + '/updateCompt',updateConfig).success(function (data) {
                }).error(function (error) {
                });
            }
            $scope.addComptToBase = function(comptLabel, defaultVals) {
                var addConfig = {withCredentials:true, params:{comptLabel:comptLabel, packetId:packetId,defaultVals:defaultVals}};
                $http.post(contextPath + '/addCompt',addConfig).success(function (data) {
                }).error(function (error) {
                });
            }
    });

angular.module("packetFilters",[])
    .filter('applyFilter', function () {
        return function(comboData,comptId,stateId){
            // var returnArray = [];
            // for(var j = 0; j < comboData.length; j++) {
            //     if(comboData[comptId === comptId && comboData[j].stateId === stateId){
            //         returnArray.push(comboData[j].label);
            //     }
            // }
            return comboData[comptId][stateId];
        }
    });

angular.module("packetApp",["packetFilters", "packetControllers"]);
