(function () {
    'use strict';

    angular.module('javaee-project-long-documentation', ['javaee-app-service', 'ngRoute'])
        .config(['$routeProvider', function ($routeProvider) {
            $routeProvider.when('/projects/documentation/:project', {
                templateUrl: 'app/page-project-long-documentation.html'
            });
        }])
        .controller('ProjectLongDocumentationController', [
            '$scope', '$routeParams', '$sce', '$templateRequest', 'javaeeAppService', '$timeout', '$location', '$anchorScroll',
            function ($scope, $routeParams, $sce, $templateRequest, javaeeAppService, $timeout, $location, $anchorScroll) {
                javaeeAppService.whenReady(function (data) {
                    var project = data.projects[$routeParams.project];
                    $scope.project = _.clone(project);
                    javaeeAppService.loadLongDocumentation($routeParams.project, function (data) {
                        var originalEl = angular.element(data);
                        var toc = originalEl.find('#user-content-toc');
                        toc.detach();
                        toc.find('#user-content-toctitle').detach();
                        $scope.longDocumentation = $sce.trustAsHtml(originalEl.html());
                        $scope.toc = $sce.trustAsHtml(toc.html());
                        $timeout(function () {
                            var hashValue = $location.hash();
                            if (!hashValue.startsWith('user-content-')) {
                                hashValue = 'user-content-' + hashValue;
                            }
                            $anchorScroll(hashValue);
                        });
                    });
                });
            }
        ]);
}());
