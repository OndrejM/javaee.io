angular.module('tribe-project-details', [])
    .controller('ProjectDetailsController', [
        '$scope', '$routeParams', '$sce', '$timeout', 'tribeProjectsService',
        function ($scope, $routeParams, $sce, $timeout, tribeProjectsService) {
            tribeProjectsService.onLoad(function (projects) {
                var project = projects.getByName($routeParams.project);
                $scope.project = {
                    name: project.name,
                    friendlyName: project.friendlyName,
                    shortDescription: project.shortDescription,
                    documentation: $sce.trustAsHtml(project.documentation),
                    icon: project.icon,
                    contributors: _.isArray(project.contributors) ? project.contributors : [project.contributors],
                    tags: _.isArray(project.tags) ? project.tags : [project.tags]
                };
                $scope.otherProjects = _.filter(projects.getAll(), function (item) {
                    return item.name !== project.name;
                });
                $timeout(function () {
                    $scope.$apply();
                }, 0);
            });
        }]);
