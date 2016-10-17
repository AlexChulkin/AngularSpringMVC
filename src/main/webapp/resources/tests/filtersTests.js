/**
 * Created by alexc_000 on 2016-10-17.
 */
describe("Filters Tests", function () {
    var rangeFilter;
    var pageCountFilter;

    beforeEach(
        module("packetAdminApp")
    );

    beforeEach(inject(function ($filter) {
        rangeFilter = $filter("range");
        pageCountFilter = $filter("pageCount");
    }));

    it("Range filter test", function () {
        var fakeData = 2;
        var fakePage = 1;
        var fakeSize = 3;
        expect(rangeFilter(fakeData)).toEqual(fakeData);
        expect(rangeFilter(fakeData, fakePage)).toEqual(fakeData);
        expect(rangeFilter(fakeData, fakePage, fakeSize)).toEqual(fakeData);
        fakePage = 'a';
        expect(rangeFilter(fakeData, fakePage)).toEqual(fakeData);
        expect(rangeFilter(fakeData, fakePage, fakeSize)).toEqual(fakeData);
        fakePage = 1;
        fakeSize = 'b';
        expect(rangeFilter(fakeData, fakePage)).toEqual(fakeData);
        expect(rangeFilter(fakeData, fakePage, fakeSize)).toEqual(fakeData);
        fakeSize = 3;
        fakeData = ['a', 'b', 'c'];
        expect(rangeFilter(fakeData, fakePage, fakeSize)).toEqual(fakeData);
        fakeSize = 2;
        expect(rangeFilter(fakeData, fakePage, fakeSize)).toEqual(['a', 'b']);
        fakeSize = 1;
        expect(rangeFilter(fakeData, fakePage, fakeSize)).toEqual(['a']);
        fakeSize = 3;
        fakePage = 2;
        expect(rangeFilter(fakeData, fakePage, fakeSize)).toEqual([]);
        fakeSize = 2;
        expect(rangeFilter(fakeData, fakePage, fakeSize)).toEqual(['c']);
    });

    it("Range filter test", function () {
        var fakeData = 2;
        var fakeSize = 3;
        expect(pageCountFilter(fakeData)).toEqual(fakeData);
        expect(pageCountFilter(fakeData, fakeSize)).toEqual(fakeData);
        var fakeData = ['a', 'b', 'c', 'd'];
        expect(pageCountFilter(fakeData, fakeSize)).toEqual([1, 2]);
        var fakeSize = 2;
        expect(pageCountFilter(fakeData, fakeSize)).toEqual([1, 2]);
        var fakeSize = 1;
        expect(pageCountFilter(fakeData, fakeSize)).toEqual([1, 2, 3, 4]);
    });
});
