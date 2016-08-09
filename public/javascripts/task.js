//Define angular app
var app = angular.module('TaskManager', []);

//controllers
app.controller('taskController', function($scope, $http) {
    $scope.today = new Date();
    $scope.saved = localStorage.getItem('taskItems');
    //$scope.taskItem = (localStorage.getItem('taskItems')!==null) ?
   // JSON.parse($scope.saved) : [ {description: "Why not add a task?", date: $scope.today, complete: false}];
   // localStorage.setItem('taskItems', JSON.stringify($scope.taskItem));
 $http.get('http://localhost:9000/alltasks').success( function (response) {
     $scope.taskItem = response.tasks;
    });
$scope.newNumber = null;
    $scope.newTask = null;
    $scope.newTaskDate = null;
    $scope.categories = [
        {name: 'Complete'},
        {name: 'InProgress'},
        {name: 'Other'}
    ];
    $scope.newTaskCategory = $scope.categories;
    $scope.addNew = function (id) {

               $scope.taskItem.push({
                 number: $scope.newNumber,
                 description: $scope.newTask,
                 category: $scope.newTaskCategory.name,
                 date: $scope.newTaskDate
            });

                id = $scope.taskItem.length + 1;
                var myJson = {'id' : id, 'number' : $scope.newNumber, 'description' : $scope.newTask, 'category' : $scope.newTaskCategory.name, 'date' : $scope.newTaskDate}
                 $http.post('http://localhost:9000/task', myJson).success(function (response) {
                         return response;
                            });

        $scope.newTask = '';
        $scope.newTaskDate = '';
        $scope.newTaskCategory = $scope.categories;
       // localStorage.setItem('taskItems', JSON.stringify($scope.taskItem));
    };
    $scope.deleteTask = function () {
        var completedTask = $scope.taskItem;
        $scope.taskItem = [];
        $scope.complete = false;
        angular.forEach(completedTask, function (taskItem) {
            if (!taskItem.complete) {
                $scope.taskItem.push(taskItem);
             }
                 else {
                 $http.delete('http://localhost:9000/task/'+taskItem.number).success(function(response){
                                                                               return response;
                                                                                });
            }
        });
    };

    $scope.save = function () {
        //localStorage.setItem('taskItems', JSON.stringify($scope.taskItem));

    }
});

