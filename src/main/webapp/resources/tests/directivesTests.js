/**
 * Created by alexc_000 on 2016-10-17.
 */
xdescribe("Directive Tests", function () {
    var $rootScope,
        $controller,
        compileService,
        mockExchangeService,
        mockScope,
        controller,
        el,
    // $body = document.getElementById('body'),
        simpleHtml = '<input id="newComptLabel" class="text" name="newLabelName" ng-model="data.newLabel"' + +'ng-trim="true" ng-maxlength="70" blacklist="blacklist" required/>';
    beforeEach(module("packetAdminApp"));
    beforeEach(inject(function ($injector, $compile, $controller, exchangeService) {
        $rootScope = $injector.get($rootScope);
        // $controller = $injector.get($controller);
        compileService = $injector.get($compile);
        mockScope = $rootScope.$new();
        el = $compile(angular.element(simpleHtml))(mockScope);
        mockExchangeService = {
            getSelectedComptLabels: function () {
            }
        };
        spyOn(mockExchangeService, 'getSelectedComptLabels').and.returnValue({'a': true, 'b': true});
        controller = $controller("comptsPanelCtrl", {
            $scope: mockScope,
            exchangeService: mockExchangeService
        });
    }));
    beforeEach(function () {
        // $body.append(el);
        // el =  compileService(simpleHtml)(mockScope);
        $rootScope.$digest();
    });
    it('blacklist directive test', function () {
        el.val('a');
        el.trigger('input');
        mockScope.digest();
        expect(el.hasClass('ng-invalid')).toBeTruthy();
    });

    afterEach(function () {
        // $body.empty();
    });
});