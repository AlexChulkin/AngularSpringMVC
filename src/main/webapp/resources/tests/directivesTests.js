/**
 * Created by alexc_000 on 2016-10-17.
 */
describe("Directive Tests", function () {
    var $rootScope,
        mockExchangeService,
        mockScope,
        el,
        $body = angular.element('<div>'),
        simpleHtml = '<input id="newComptLabel" class="text" name="newLabelName" ng-model="data.newLabel"' +
            'ng-trim="true" ng-maxlength="70" blacklist="blacklist" required/>';
    
    beforeEach(module("packetAdminApp"));

    beforeEach(inject(function ($injector, $compile, exchangeService) {
        $rootScope = $injector.get('$rootScope');
        mockScope = $rootScope.$new();
        el = $compile(angular.element(simpleHtml))(mockScope);
        mockExchangeService = {
            getSelectedComptLabels: function () {
            }
        };
        exchangeService.setSelectedComptLabels({'ROMA LOCUTA CAUSA FINITA': true, 'DURA LEX SED LEX': true});
    }));

    beforeEach(function () {
        $body.append(el);
        $rootScope.$digest();
    });

    it('blacklist directive positive test spaces', function () {
        el.val('Roma    locuta causa       finita');
        el.triggerHandler('input');
        expect(el.hasClass('ng-invalid')).toBeTruthy();
    });

    it('blacklist directive positive test letters case', function () {
        el.val('Roma LOCUTA causa fINITA');
        el.triggerHandler('input');
        expect(el.hasClass('ng-invalid')).toBeTruthy();
    });

    it('blacklist directive negative test', function () {
        el.val('Lorem ipsum');
        el.triggerHandler('input');
        expect(el.hasClass('ng-invalid')).toBeFalsy();
    });

    afterEach(function () {
        $body.empty();
    });
});