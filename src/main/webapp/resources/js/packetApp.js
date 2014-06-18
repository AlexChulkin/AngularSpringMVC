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
                $scope.defaultLabelIds = [];
                for(var j=0; j<$scope.compts.length; j++) {
                    $scope.defaultLabelIds.push("lbl_"+j);
                }
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
                            $scope.defaultInputIds = [];
                            $scope.defaultComboIds = [];

                            $scope.comboData = [];
                            var defaultArray = [];
                            for(var j=0; j<$scope.staticData.length; j++) {
                                var comboEl = {};
                                comboEl.comptId =$scope.staticData[j][1];
                                comboEl.stateId = $scope.staticData[j][2];
                                comboEl.label = $scope.staticData[j][3];
                                $scope.comboData.push(comboEl);

                                if($scope.staticData[j][4] === 1) {
                                    $scope.defaultInputIds.push($scope.defaultValues.length+"_inp_"+defaultArray.length);
                                    $scope.defaultComboIds.push($scope.defaultValues.length+"_cmb_"+defaultArray.length);
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

                $scope.compts.push({ id: $scope.compts.length+1, label: newLabel});
                $scope.defaultValues.push([preCommiteeNewVal,inCommiteeNewVal,finalNewVal]);
                $scope.defaultLabelIds.push("lbl_"+$scope.defaultLabelIds.length);
                for(var j=0; j<dummyComboData.length; j++){
                    for (var i=1; i<=$scope.states.length; i++) {
                        if(j===1){
                            $scope.defaultInputIds.push(($scope.defaultValues.length-1)+"_inp_"+(i-1));
                            $scope.defaultComboIds.push(($scope.defaultValues.length-1)+"_cmb_"+(i-1));
                        }
                        var comboEl = {};
                        comboEl.comptId =$scope.compts.length;
                        comboEl.stateId = i;
                        comboEl.label = dummyComboData[j];
                        $scope.comboData.push(comboEl);
                    }
                }


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
