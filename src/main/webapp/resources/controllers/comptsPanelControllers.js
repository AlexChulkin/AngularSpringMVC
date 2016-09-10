/**
 * Created by alexc_000 on 2016-08-30.
 */
var app = angular.module("packetAdminApp");
app
    .constant("pageListActiveClass", "btn-primary btn-sm")
    .constant("pageListNonActiveClass", "btn-sm")
    .constant("packetListPageCount", 10)
    .controller("comptsPanelCtrl", function ($scope, pageListActiveClass, pageListNonActiveClass,
                                             packetListPageCount, exchangeService) {

        var data;

        $scope.isComptsSelected = function () {
            return data.comptsIsSelected;
        };

        $scope.isPacketSelected = function () {
            return exchangeService.getSelectedPacket();
        };

        $scope.isPacketsNotLoaded = function () {
            return exchangeService.getLoadedNoPackets();
        };

        $scope.addComptLocally = function () {
            var comptId = exchangeService.getMaximalComptId() + 1;
            exchangeService.setMaximalComptId(comptId);

            var usualLabel = $scope.data.newLabel;
            var upperCaseLabel = usualLabel.toUpperCase();
            var newCompt = {id: comptId, label: usualLabel};
            exchangeService.setSelectedComptLabels(true, upperCaseLabel);
            exchangeService.setComptIdToInd(comptId, $scope.data.selectedCompts.length);
            exchangeService.pushToSelectedCompts(newCompt);
            var pktId = data.selectedPacketId;
            exchangeService.setNewPackets();
            exchangeService.setNewComptLabels($scope.$parent.data.newComptLabels[data.isSelectedPacketNew] || {},
                exchangeService.getIsSelectedPacketNew());
            exchangeService.setNewComptLabels($scope.$parent.data.newComptLabels[data.isSelectedPacketNew][pktId] || {},
                exchangeService.getIsSelectedPacketNew(), pktId);
            exchangeService.setNewComptLabels(
                $scope.$parent.data.newComptLabels[data.isSelectedPacketNew][pktId][comptId] || {},
                exchangeService.getIsSelectedPacketNew(),
                pktId,
                comptId
            );
            exchangeService.setAllComboData({}, comptId);
            exchangeService.setAllCheckedComboData({}, comptId);
            for (var i = 1; i <= exchangeService.getAllStatesLength(); i++) {
                exchangeService.setAllComboData(exchangeService.getComboDataDefaultSet(), comptId, i);
                exchangeService.setAllCheckedComboData(exchangeService.getNewComptCheckedVals(i - 1), comptId, i);
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