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

        $scope.$on('selectedCompts:update', function (event, data) {
            $scope.data.selectedCompts = data;
        });

        $scope.$on('selectedPage:update', function (event, data) {
            $scope.data.selectedPage = data;
        });

        $scope.$on('selectedPacket:update', function (event, data) {
            $scope.data.selectedPacket = data;
        });

        $scope.$on('allStates:update', function (event, data) {
            $scope.data.allStates = data;
        });

        $scope.$on('allStateLabels:update', function (event, data) {
            $scope.data.allStateLabels = data;
        });

        $scope.$on('allComboData:update', function (event, data) {
            $scope.data.allComboData = data;
        });

        $scope.$on('allCheckedComboData:update', function (event, data) {
            $scope.data.allCheckedComboData = data;
        });

        $scope.$on('comboDataDefaultSet:update', function (event, data) {
            $scope.data.comboDataDefaultSet = data;
        });

        $scope.$on('newComptCheckedVals:update', function (event, data) {
            $scope.data.newComptCheckedVals = data;
        });

        $scope.isComptsSelected = function () {
            return exchangeService.getComptsIsSelected();
        };

        $scope.isPacketSelected = function () {
            return exchangeService.getSelectedPacket();
        };

        $scope.isPacketsNotLoaded = function () {
            return exchangeService.getLoadedNoPackets();
        };

        $scope.addComptLocally = function () {
            var comptId = exchangeService.getMaximalComptId() + 1;
            var isSelectedPacketNew = exchangeService.getIsSelectedPacketNew();
            exchangeService.setMaximalComptId(comptId);

            var usualLabel = $scope.data.newLabel;
            var upperCaseLabel = usualLabel.toUpperCase();
            var newCompt = {id: comptId, label: usualLabel};
            exchangeService.setSelectedComptLabels(true, upperCaseLabel);
            exchangeService.setComptIdToInd(comptId, exchangeService.getSelectedComptsLength());
            exchangeService.pushToSelectedCompts(newCompt);
            var pktId = exchangeService.getSelectedPacketId();
            exchangeService.setNewComptLabels(exchangeService.getNewComptLabels(isSelectedPacketNew) || {},
                isSelectedPacketNew);
            exchangeService.setNewComptLabels(exchangeService.getNewComptLabels(isSelectedPacketNew, pktId) || {},
                isSelectedPacketNew, pktId);
            exchangeService.setNewComptLabels(usualLabel, isSelectedPacketNew, pktId, comptId);
            exchangeService.setAllComboData({}, comptId);
            exchangeService.setAllCheckedComboData({}, comptId);
            var allStatesLength = exchangeService.getAllStatesLength();
            for (var i = 1; i <= allStatesLength; i++) {
                exchangeService.setAllComboData($scope.data.comboDataDefaultSet, comptId, i);
                exchangeService.setAllCheckedComboData($scope.data.newComptCheckedVals[i - 1], comptId, i);
            }
            $scope.data.newLabel = null;
        };

        $scope.deleteComptLocally = function (compt) {
            var comptLabel = compt.label;
            var comptId = compt.id;
            var pktId = exchangeService.getSelectedPacketId();
            var isSelectedPacketNew = exchangeService.getIsSelectedPacketNew();

            exchangeService.deleteSelectedComptLabels(comptLabel.toUpperCase());
            exchangeService.setSelectedCompts(null, exchangeService.getComptIdToInd(comptId));

            if (exchangeService.getComptIdsToUpdate(pktId)) {
                exchangeService.deleteComptIdsToUpdate(pktId, comptId);
            }
            if (exchangeService.getNewComptLabels(isSelectedPacketNew, pktId)
                && exchangeService.getNewComptLabels(isSelectedPacketNew, pktId, comptId)) {

                exchangeService.deleteNewComptLabels(isSelectedPacketNew, pktId, comptId);
                
            } else {
                var comptIdsToDelete = exchangeService.getComptIdsToDelete(pktId) || [];
                exchangeService.setComptIdsToDelete(comptIdsToDelete, pktId);
                exchangeService.pushToComptIdsToDelete(comptId, pktId);
            }
        };

        $scope.updateComptLocally = function (compt) {
            var pktId = exchangeService.getSelectedPacketId();
            var isSelectedPacketNew = exchangeService.getIsSelectedPacketNew();
            var comptId = compt.id;
            if (exchangeService.getNewComptLabels(isSelectedPacketNew, pktId) &&
                exchangeService.getNewComptLabels(isSelectedPacketNew, pktId, comptId)) {
                return;
            }
            var comptIdsToUpdate = exchangeService.getComptIdsToUpdate(pktId) || {};
            exchangeService.setComptIdsToUpdate(comptIdsToUpdate, pktId);
            exchangeService.setComptIdsToUpdate(true, pktId, comptId);
        };

        $scope.getPageClass = function (page) {
            return exchangeService.getSelectedPage() === page ? pageListActiveClass : pageListNonActiveClass;
        };

        $scope.notNull = function (value) {
            return value;
        };

        var init = function () {
            $scope.data = {};
            $scope.data.pageSize = packetListPageCount;
            $scope.data.selectedCompts = exchangeService.getSelectedCompts();
            $scope.data.selectedPage = exchangeService.getSelectedPage();
            $scope.data.selectedPacket = exchangeService.getSelectedPacket();
            $scope.data.allStates = exchangeService.getAllStates();
            $scope.data.allStateLabels = exchangeService.getAllStateLabels();
            $scope.data.allComboData = exchangeService.getAllComboData();
            $scope.data.allCheckedComboData = exchangeService.getAllCheckedComboData();
            $scope.data.comboDataDefaultSet = exchangeService.getComboDataDefaultSet();
            $scope.data.newComptCheckedVals = exchangeService.getNewComptCheckedVals();
        };

        init();
    });

app.directive('blacklist', ['exchangeService', function (exchangeService) {
    return {
        require: 'ngModel',
        link: function ($scope, elem, attr, ngModel) {
            ngModel.$parsers.unshift(function (label) {
                var updatedLabel = label.replace(/\s{2,}/g, " ");
                var upperCaseLabel = updatedLabel.toUpperCase();
                ngModel.$setValidity('blacklist', !exchangeService.getSelectedComptLabels(upperCaseLabel));
                return updatedLabel;
            });
        }
    };
}]);

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