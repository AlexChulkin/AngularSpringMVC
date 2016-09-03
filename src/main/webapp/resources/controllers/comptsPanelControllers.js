/**
 * Created by alexc_000 on 2016-08-30.
 */
var app = angular.module("packetApp");

app.constant("packetListActiveClass", "btn-primary btn-sm")
    .constant("packetListNonActiveClass", "btn-sm")
    .controller("comptsPanelCtrl", function ($scope, $http, $window, packetListActiveClass, packetListNonActiveClass) {

        var data;
        $scope.$watch('$parent.data.selectedPacket', function (newValue, oldValue) {
            if (newValue) {
                if (oldValue) {
                    var oldSelectedPacketId = oldValue.id;
                    $scope.$parent.data.compts[$scope.$parent.data.packetIdToInd[oldSelectedPacketId]]
                        = $scope.data.selectedCompts;
                    $scope.$parent.data.comptLabels[oldSelectedPacketId] = $scope.data.selectedComptLabels;
                }
                data.selectedPacketId = newValue.id;
                data.packetIsNotSelectedSoFar = false;
                data.isSelectedPacketNew = data.selectedPacketId in $scope.$parent.data.newPackets;
                $scope.data.selectedCompts
                    = $scope.$parent.data.compts[$scope.$parent.data.packetIdToInd[data.selectedPacketId]] || [];
                $scope.data.selectedComptLabels = $scope.$parent.data.comptLabels[data.selectedPacketId] || {};
            } else {
                $scope.data.selectedCompts = [];
                $scope.data.selectedComptLabels = {};
                data.isSelectedPacketNew = null;
                data.selectedPacketId = null;
            }
        });

        $scope.$watchCollection('data.selectedComptLabels', function (value) {
            data.comptsIsSelected = !angular.equals({}, value);
        });

        $scope.isComptsSelected = function () {
            return data.comptsIsSelected;
        };

        $scope.isPacketSelected = function () {
            return $scope.$parent.data.selectedPacket;
        };

        $scope.isPacketsNotLoaded = function () {
            return $scope.$parent.data.loadedNoPackets;
        };

        $scope.isPacketNeverSelectedSoFar = function () {
            return data.packetIsNotSelectedSoFar;
        };

        $scope.addComptLocally = function () {
            var comptId = ++$scope.$parent.data.maximalComptId;
            var usualLabel = $scope.data.newLabel;
            var upperCaseLabel = usualLabel.toUpperCase();
            var newCompt = {id: comptId, label: usualLabel};
            $scope.data.selectedComptLabels[upperCaseLabel] = true;
            $scope.$parent.data.comptIdToInd[comptId] = $scope.data.selectedCompts.length;
            $scope.data.selectedCompts.push(newCompt);
            var pktId = data.selectedPacketId;
            data.newComptLabels[$scope.$parent.data.isSelectedPacketNew]
                = data.newComptLabels[$scope.$parent.data.isSelectedPacketNew] || {};
            data.newComptLabels[$scope.$parent.data.isSelectedPacketNew][pktId]
                = data.newComptLabels[$scope.$parent.data.isSelectedPacketNew][pktId] || {};
            data.newComptLabels[$scope.$parent.data.isSelectedPacketNew][pktId][comptId] = usualLabel;
            $scope.$parent.data.allComboData[comptId] = {};
            $scope.$parent.data.allCheckedComboData[comptId] = {};
            for (var i = 1; i <= $scope.data.allStates.length; i++) {
                $scope.$parent.data.allComboData[comptId][i] = $scope.data.comboDataDefaultSet;
                $scope.$parent.data.allCheckedComboData[comptId][i] = $scope.$parent.data.newComptCheckedVals[i - 1];
            }
            $scope.data.newLabel = null;
        };

        $scope.deleteComptLocally = function (compt) {
            var comptLabel = compt.label;
            var comptId = compt.id;
            var pktId = data.selectedPacketId;

            delete $scope.data.selectedComptLabels[comptLabel.toUpperCase()];
            $scope.data.selectedCompts[$scope.$parent.data.comptIdToInd[comptId]] = null;
            delete $scope.$parent.data.allCheckedComboData[comptId];
            delete $scope.$parent.data.allComboData[comptId];
            if (data.comptIdsToUpdate[pktId]) {
                delete data.comptIdsToUpdate[pktId][comptId];
            }
            if (data.newComptLabels[$scope.$parent.data.isSelectedPacketNew] &&
                data.newComptLabels[$scope.$parent.data.isSelectedPacketNew][pktId]) {
                delete data.newComptLabels[$scope.$parent.data.isSelectedPacketNew][pktId][comptId];
            } else {
                data.comptIdsTaggedToDelete[pktId]
                    = data.comptIdsTaggedToDelete[pktId] || [];
                data.comptIdsTaggedToDelete[pktId].push(comptId);
            }
        };

        $scope.updateComptLocally = function (compt) {
            var pktId = data.selectedPacketId;
            var comptId = compt.id;
            if (data.newComptLabels[$scope.$parent.data.isSelectedPacketNew][pktId] &&
                data.newComptLabels[$scope.$parent.data.isSelectedPacketNew][pktId][comptId]) {
                return;
            }
            data.comptIdsToUpdate[pktId] = data.comptIdsToUpdate[pktId] || {};
            data.comptIdsToUpdate[pktId][comptId] = true;
        };

        $scope.getPageClass = function (page) {
            return $scope.selectedPage === page ? packetListActiveClass : packetListNonActiveClass;
        };

        $scope.notNull = function (item) {
            return item;
        };

        var init = function () {
            data = {};
            data.packetIsNotSelectedSoFar = true;
            data.comptIdsTaggedToDelete = {};
            data.comptIdsToUpdate = {};
            data.newComptLabels = {true: {}, false: {}};
            data.comptsIsSelected = null;
            $scope.$parent.data.newPackets = {};
        };

        init();
    });

app.directive('blacklist', function () {
    return {
        require: 'ngModel',
        link: function ($scope, elem, attr, ngModel) {
            ngModel.$parsers.unshift(function (label) {
                var label = label.replace(/\s{2,}/g, " ");
                var upperCaseLabel = label.toUpperCase();
                ngModel.$setValidity('blacklist', !$scope.data.selectedComptLabels[upperCaseLabel]);
                return label;
            });
        }
    };
});

app.directive('regex', function () {
    return {
        require: 'ngModel',
        link: function ($scope, elem, attr, ngModel) {
            ngModel.$parsers.unshift(function (label) {
                ngModel.$setValidity('regex', label.search(/[^\w\s]/g) == -1);
                return label;
            });
        }
    };
});