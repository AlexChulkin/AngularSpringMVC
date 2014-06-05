/**
 * Created by achulkin on 04.06.14.
 */
angular.module("packetControllers",[])
    .constant("packetId",0)
    .controller("packetCtrl", function ($scope,$http,packetId) {

        var simpleConfig = {withCredentials:true};
        var complConfig = {withCredentials:true, params:{packetId:packetId}};

        $http.get(contextPath + '/web/compt/view.action',complConfig).success(function (data) {
            $scope.compts = data;
        }).error(function (error) {
            $scope.errorCompts = error;
        });

        $http.get(contextPath + '/web/data/view.action',complConfig).success(function (data) {
            $scope.data = data;
        }).error(function (error) {
            $scope.errorData = error;
        });
        $scope.defaultValues = [];
        var tempArray = [];
        var i = 0;
        for(var data in $scope.data) {
            if(data.checked === 1) {
                i++;
                tempArray.push(data);
                if(i===$scope.states.length) {
                    $scope.defaultValues.push(tempArray);
                    i=0;
                    var tempArray = [];
                }
            }
        }

        $http.get(contextPath + '/web/packetState/view.action',complConfig).success(function (data) {
            $scope.defaultState = data;
        }).error(function (error) {
            $scope.errorDefaultState = error;
        });

        $http.get(contextPath + '/web/states/view.action',simpleConfig).success(function (data) {
            $scope.states = data;
        }).error(function (error) {
            $scope.errorStates = error;
        });
        $scope.labels = [];
        for(var state in $scope.states){
            $scope.labels.push(state.label);
        }
        $scope.labels.defaultLabel = $scope.states[$scope.defaultState].label;

        $scope.allLabels = $scope.labels;
        $scope.allLabels.unshift('Label');
    });


angular.module("packetFilters",[])
    .filter('applyFilter', function () {
        return function(data,comptId,stateId){
            var returnArray = [];
            for (var d in data){
                if(d.comptId===comptId && d.stateId===stateId){
                    returnArray.push(d);
                }
            }
            return returnArray;
        }
    });

angular.module("packetApp",["packetFilters", "packetControllers"]);

angular.bootstrap(document.getElementById('packetDiv'), ['packetApp']);