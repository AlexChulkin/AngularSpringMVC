/**
 * Created by alexc_000 on 2016-08-30.
 */
'use strict';

var app = angular.module("packetAdminApp");
app
    .constant("pageListActiveClass", "btn-primary btn-sm")
    .constant("pageListNonActiveClass", "btn-sm")
    .constant("packetListPageCount", 10)
    .constant("formLabelPattern", "^[\\w\\s]*$")
    .controller("comptsPanelCtrl", function ($scope, $filter, pageListActiveClass, pageListNonActiveClass,
                                             packetListPageCount, exchangeService, formLabelPattern) {

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

        $scope.$on('initialAllCheckedComboData:update', function (event, data) {
            $scope.data.initialAllCheckedComboData = data;
        });

        $scope.$on('comboDataDefaultSet:update', function (event, data) {
            $scope.data.comboDataDefaultSet = data;
        });

        $scope.$on('newComptCheckedVals:update', function (event, data) {
            $scope.data.newComptCheckedVals = data;
        });

        $scope.isSelectedPacketEmpty = function () {
            return exchangeService.getSelectedPacketIsEmpty();
        };

        $scope.isPacketSelected = function () {
            return exchangeService.getSelectedPacket();
        };

        $scope.isPacketsNotLoaded = function () {
            return exchangeService.getLoadedNoPackets();
        };

        $scope.isSelectedPacketNotLoaded = function () {
            return exchangeService.getLoadedNoSelectedPacket();
        };

        $scope.addComptLocally = function () {
            var comptId = exchangeService.getMaximalComptId() + 1;
            exchangeService.setMaximalComptId(comptId);

            var usualLabel = $scope.data.newLabel;
            var upperCaseLabel = usualLabel.toUpperCase();
            var newCompt = {id: comptId, label: usualLabel};
            exchangeService.setSelectedComptLabels(true, upperCaseLabel);
            exchangeService.setComptIdToInd(exchangeService.getSelectedComptsLength(), comptId);
            exchangeService.pushToSelectedCompts(newCompt);
            var pktId = exchangeService.getSelectedPacketId();
            exchangeService.setNewComptLabels(exchangeService.getNewComptLabels() || {});
            exchangeService.setNewComptLabels(exchangeService.getNewComptLabels(pktId) || {},
                pktId);
            exchangeService.setNewComptLabels(usualLabel, pktId, comptId);
            exchangeService.setAllComboData(false, {}, comptId);
            exchangeService.setAllCheckedComboData(true, false, {}, comptId);
            var allStatesLength = exchangeService.getAllStatesLength();
            for (var i = 1; i <= allStatesLength; i++) {
                exchangeService.setAllComboData(i === allStatesLength, $scope.data.comboDataDefaultSet, comptId);
                exchangeService.setAllCheckedComboData(true, false, $scope.data.newComptCheckedVals[i - 1], comptId, i);
            }
            $scope.data.newLabel = null;
        };

        $scope.deleteComptLocally = function (compt) {
            var comptLabel = compt.label;
            var comptId = compt.id;
            var pktId = exchangeService.getSelectedPacketId();

            exchangeService.deleteSelectedComptLabels(comptLabel.toUpperCase());
            exchangeService.setSelectedCompt(exchangeService.getComptIdToInd(comptId), null, $scope.data.pageSize);

            if (exchangeService.getComptIdsToUpdate(pktId)) {
                exchangeService.deleteComptIdsToUpdate(pktId, comptId);
            }
            if (exchangeService.getNewComptLabels(pktId)
                && exchangeService.getNewComptLabels(pktId, comptId)) {

                exchangeService.deleteNewComptLabels(pktId, comptId);
                
            } else {
                var comptIdsToDelete = exchangeService.getComptIdsToDelete(pktId) || [];
                exchangeService.setComptIdsToDelete(comptIdsToDelete, pktId);
                exchangeService.pushToComptIdsToDelete(comptId, pktId);
            }
        };

        $scope.updateComptLocally = function (compt, stateId) {
            var pktId = exchangeService.getSelectedPacketId();
            var comptId = compt.id;
            if (exchangeService.getNewComptLabels(pktId) && exchangeService.getNewComptLabels(pktId, comptId)) {
                return;
            }
            if ($scope.data.initialAllCheckedComboData[comptId]
                && $scope.data.allCheckedComboData[comptId][stateId]
                === $scope.data.initialAllCheckedComboData[comptId][stateId]) {
                exchangeService.deleteComptIdsToUpdate(pktId, comptId);
                return;
            }
            var comptIdsToUpdate = exchangeService.getComptIdsToUpdate(pktId) || {};
            if (angular.equals({}, comptIdsToUpdate)) {
                exchangeService.setComptIdsToUpdate({}, pktId);
            }
            exchangeService.setComptIdsToUpdate(true, pktId, comptId);
        };

        $scope.getPageClass = function (page) {
            return exchangeService.getSelectedPage() === page ? pageListActiveClass : pageListNonActiveClass;
        };

        $scope.notNull = function (value) {
            return value !== null;
        };

        $scope.getError = function (input, errorType) {
            var error = input.$error;
            var resultMap = {};
            if (angular.isDefined(error)) {
                if (errorType == "required" && error.required && input.$dirty) {
                    return "You did not enter a label";
                } else if (errorType == "maxlength" && error.maxlength) {
                    return "Label is too long";
                } else if (errorType == "blacklist" && error.blacklist) {
                    return "Label is not unique";
                } else if (errorType == "pattern" && error.pattern) {
                    return "Label should contain latin letters, digits, underscore and spaces only";
                }
            }
        };

        var init = function () {
            $scope.data = {};
            $scope.data.pageSize = packetListPageCount;
            $scope.comptLabelMatchPattern = new RegExp(formLabelPattern);
        };

        init();
    });
