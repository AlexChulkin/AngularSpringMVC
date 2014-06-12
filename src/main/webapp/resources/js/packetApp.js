/**
 * Created by achulkin on 04.06.14.
 */
angular.module("packetControllers",[])
    .constant("packetId",1)
    .constant("labelLabel","Label")
    .controller("packetCtrl", function ($scope,$http,packetId,labelLabel) {

    var simpleConfig = {withCredentials:true};
    var complConfig = {withCredentials:true, params:{packetId:packetId}};

            $http.get(contextPath + '/compts',complConfig).success(function (data) {
                $scope.compts = data;
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
                    }).error(function (error) {
                        $scope.errorDefaultState = error;
                    });


                }).error(function (error) {
                    $scope.errorStates = error;
                });
            }).error(function (error) {
                $scope.errorCompts = error;
                return;
            });

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
