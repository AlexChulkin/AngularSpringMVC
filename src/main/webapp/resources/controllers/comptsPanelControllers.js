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
            $scope.selectedCompts = data;
        });

        $scope.$on('selectedPage:update', function (event, data) {
            $scope.selectedPage = data;
        });

        $scope.$on('selectedPacket:update', function (event, data) {
            $scope.selectedPacket = data;
        });

        $scope.$on('allStates:update', function (event, data) {
            $scope.allStates = data;
        });

        $scope.$on('allStateLabels:update', function (event, data) {
            $scope.allStateLabels = data;
        });

        $scope.$on('allComboData:update', function (event, data) {
            $scope.allComboData = data;
        });

        $scope.$on('allCheckedComboData:update', function (event, data) {
            $scope.allCheckedComboData = data;
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

            var usualLabel = $scope.newLabel;
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
            for (var i = 1; i <= exchangeService.getAllStatesLength(); i++) {
                exchangeService.setAllComboData(exchangeService.getComboDataDefaultSet(), comptId, i);
                exchangeService.setAllCheckedComboData(exchangeService.getNewComptCheckedVals(i - 1), comptId, i);
            }
            $scope.newLabel = null;
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
                exchangeService.setComptIdsToDelete(exchangeService.getComptIdsToUpdate(pktId) || [], pktId);
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
            exchangeService.setComptIdsToUpdate(exchangeService.getComptIdsToUpdate(pktId) || {}, pktId);
            exchangeService.setComptIdsToUpdate(true, pktId, comptId);
        };

        $scope.getPageClass = function (page) {
            return exchangeService.getSelectedPage() === page ? pageListActiveClass : pageListNonActiveClass;
        };

        $scope.notNull = function (value) {
            return value;
        };

        var init = function () {
            $scope.pageSize = packetListPageCount;
            $scope.selectedCompts = exchangeService.getSelectedCompts();
            $scope.selectedPage = exchangeService.getSelectedPage();
            $scope.selectedPacket = exchangeService.getSelectedPacket();
            $scope.allStates = exchangeService.getAllStates();
            $scope.allStateLabels = exchangeService.getAllStateLabels();
            $scope.allComboData = exchangeService.getAllComboData();
            $scope.allCheckedComboData = exchangeService.getAllCheckedComboData();
        };

        init();
    });

app.directive('blacklist', function () {
    return {
        require: 'ngModel',
        link: function ($scope, elem, attr, ngModel, exchangeService) {
            ngModel.$parsers.unshift(function (label) {
                var updatedLabel = label.replace(/\s{2,}/g, " ");
                var upperCaseLabel = updatedLabel.toUpperCase();
                ngModel.$setValidity('blacklist', !exchangeService.getSelectedComptLabels(upperCaseLabel));
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