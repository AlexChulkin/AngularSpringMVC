/**
 * Created by alexc_000 on 2016-08-30.
 */
var app = angular.module("packetAdminApp");
app
    .constant("pageListActiveClass", "btn-primary btn-sm")
    .constant("pageListNonActiveClass", "btn-sm")
    .constant("packetListPageCount", 10)
    .controller("comptsPanelCtrl", function ($scope, pageListActiveClass, pageListNonActiveClass,
                                             packetListPageCount) {

        var data;

        $scope.$watch('$parent.data.packetIsSelectedOrSelectedPacketIsReloaded', function (flag) {
            var oldValue = flag.oldVal;
            var newValue = flag.newVal;
            if (newValue !== oldValue) {
                if (newValue) {
                    if (oldValue) {
                        var oldSelectedPacketId = oldValue.id;
                        $scope.$parent.data.compts[$scope.$parent.data.packetIdToInd[oldSelectedPacketId]]
                            = $scope.data.selectedCompts;
                        $scope.$parent.data.comptLabels[oldSelectedPacketId] = $scope.data.selectedComptLabels;
                    }
                    data.selectedPacketId = newValue.id;
                    $scope.$parent.data.packetIsAlreadySelectedAtLeastOnce = true;
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
            } else {
                $scope.data.selectedCompts
                    = $scope.$parent.data.compts[$scope.$parent.data.packetIdToInd[data.selectedPacketId]] || [];
                $scope.data.selectedComptLabels = $scope.$parent.data.comptLabels[data.selectedPacketId] || {};
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

        $scope.addComptLocally = function () {
            var comptId = ++$scope.$parent.data.maximalComptId;
            var usualLabel = $scope.data.newLabel;
            var upperCaseLabel = usualLabel.toUpperCase();
            var newCompt = {id: comptId, label: usualLabel};
            $scope.data.selectedComptLabels[upperCaseLabel] = true;
            $scope.$parent.data.comptIdToInd[comptId] = $scope.data.selectedCompts.length;
            $scope.data.selectedCompts.push(newCompt);
            var pktId = data.selectedPacketId;
            $scope.$parent.data.newComptLabels[data.isSelectedPacketNew]
                = $scope.$parent.data.newComptLabels[data.isSelectedPacketNew] || {};
            $scope.$parent.data.newComptLabels[data.isSelectedPacketNew][pktId]
                = $scope.$parent.data.newComptLabels[data.isSelectedPacketNew][pktId] || {};
            $scope.$parent.data.newComptLabels[data.isSelectedPacketNew][pktId][comptId] = usualLabel;
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
            if ($scope.$parent.data.comptIdsToUpdate[pktId]) {
                delete $scope.$parent.data.comptIdsToUpdate[pktId][comptId];
            }
            if ($scope.$parent.data.newComptLabels[data.isSelectedPacketNew][pktId]
                && $scope.$parent.data.newComptLabels[data.isSelectedPacketNew][pktId][comptId]) {

                delete $scope.$parent.data.newComptLabels[data.isSelectedPacketNew][pktId][comptId];
                
            } else {
                $scope.$parent.data.comptIdsTaggedToDelete[pktId]
                    = $scope.$parent.data.comptIdsTaggedToDelete[pktId] || [];
                $scope.$parent.data.comptIdsTaggedToDelete[pktId].push(comptId);
            }
        };

        $scope.updateComptLocally = function (compt) {
            var pktId = data.selectedPacketId;
            var comptId = compt.id;
            if ($scope.$parent.data.newComptLabels[data.isSelectedPacketNew][pktId] &&
                $scope.$parent.data.newComptLabels[data.isSelectedPacketNew][pktId][comptId]) {
                return;
            }
            $scope.$parent.data.comptIdsToUpdate[pktId] = $scope.$parent.data.comptIdsToUpdate[pktId] || {};
            $scope.$parent.data.comptIdsToUpdate[pktId][comptId] = true;
        };

        $scope.getPageClass = function (page) {
            return $scope.$parent.data.selectedPage === page ? pageListActiveClass : pageListNonActiveClass;
        };

        $scope.notNull = function (item) {
            return item;
        };

        var init = function () {
            data = {};
            data.isSelectedPacketNew = null;
            $scope.data.selectedCompts = [];
            $scope.data.selectedComptLabels = {};
            $scope.data.pageSize = packetListPageCount;
        };

        init();
    });

app.directive('blacklist', function () {
    return {
        require: 'ngModel',
        link: function ($scope, elem, attr, ngModel) {
            ngModel.$parsers.unshift(function (label) {
                var updatedLabel = label.replace(/\s{2,}/g, " ");
                var upperCaseLabel = updatedLabel.toUpperCase();
                ngModel.$setValidity('blacklist', !$scope.data.selectedComptLabels[upperCaseLabel]);
                return updatedLabel;
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