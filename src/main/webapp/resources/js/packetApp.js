/**
 * Created by achulkin on 04.06.14.
 */
angular.module("packetControllers",[])
    .constant("packetId",1)
    .constant("labelLabel","Label")
    .controller("packetCtrl", function ($scope,$http,packetId,labelLabel) {

    var simpleConfig = {withCredentials:true};
    var complConfig = {withCredentials:true, params:{packetId:packetId}};
    var dummyComboData = ["VERY_WEAK", "WEAK", "MODERATE", "ADEQUATE", "STRONG", "VERY_STRONG"];

            $http.get(contextPath + '/compts',complConfig).success(function (data) {
                $scope.compts = data;
                $scope.removeList = [];


                $http.get(contextPath + '/states',simpleConfig).success(function (data) {
                    $scope.states = data;

                    $scope.labels = [];

                    for(var j=0; j<$scope.states.length; j++) {
                        $scope.labels.push($scope.states[j].label);
                    }
                    $scope.labels.unshift(labelLabel);
                    $http.get(contextPath + '/packetState',complConfig).success(function (data) {
                        $scope.labels.defaultLabel = $scope.labels[data];
                        $http.get(contextPath + '/staticData',complConfig).success(function (data) {
                            $scope.staticData = data;
                            $scope.defaultValues = [];

                            $scope.comboData = [];
                            var defaultArray = [];
                            for(var j=0; j<$scope.staticData.length; j++) {
                                var comboEl = {};
                                comboEl.comptId =$scope.staticData[j][1];
                                comboEl.stateId = $scope.staticData[j][2];
                                comboEl.label = $scope.staticData[j][3];
                                $scope.comboData.push(comboEl);

                                if($scope.staticData[j][4] === 1) {
                                    defaultArray.push(comboEl.label);
                                    if(defaultArray.length===$scope.states.length) {
                                        $scope.defaultValues.push(defaultArray);
                                        var defaultArray = [];
                                    }
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


            $scope.addNewCompt = function(newLabel, preCommiteeNewVal, inCommiteeNewVal, finalNewVal) {

                $scope.compts.push({ id: $scope.compts.length+1, label: newLabel, new:true});
                $scope.defaultValues.push([preCommiteeNewVal,inCommiteeNewVal,finalNewVal]);
                var comboEl;
                for(var j=0; j<dummyComboData.length; j++){
                    for (var i=1; i<=$scope.states.length; i++) {
                        comboEl = {};
                        comboEl.comptId =$scope.compts.length;
                        comboEl.stateId = i;
                        comboEl.label = dummyComboData[j];
                        $scope.comboData.push(comboEl);
                    }
                }
            }
            $scope.deleteCompt = function (compt) {
                $scope.compts.splice($scope.compts.indexOf(compt), 1);
                if(compt.new!==true) {
                    $scope.removeList.push(compt.id);
                }
            }
            $scope.markAsUpdated = function (compt) {
                compt.updated = true;
            }
            $scope.save = function() {
                $scope.collectIdsForRemoval();
                for(var compt in $scope.compts) {
                    if(compt.new===true){
                        $scope.addComptToBase(compt.label, $scope.getDefaultValsForCompt(compt));
                        compt.new = false;
                    }else if (compt.updated===true) {
                        $scope.updateComptInBase(compt.id,  $scope.getDefaultValsForCompt(compt));
                        compt.updated=false;
                    }
                }
            }
            $scope.getDefaultValsForCompt = function(compt){
                var defaultVals=[];
                for(var i=0; i<$scope.states.length; i++){
                    defaultVals.push($scope.defaultValues[compt.id-1][i]);
                }
                return defaultVals;
            }
            $scope.collectIdsForRemoval = function() {
                var ids = "";
                for( var id in $scope.removeList) {
                    ids=ids+id+","
                }
                ids=ids.substring(0,ids.length-1);
                $scope.removeComptsFromBase(ids);
            }

            $scope.removeComptsFromBase = function (ids) {
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
            var returnArray = [];
            for(var j=0; j<comboData.length; j++) {
                if(comboData[j].comptId===comptId && comboData[j].stateId===stateId){
                    returnArray.push(comboData[j].label);
                }
            }
            return returnArray;
        }
    });

angular.module("packetApp",["packetFilters", "packetControllers"]);
