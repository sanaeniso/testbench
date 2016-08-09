// declare modules
//var session = angular.module('session', ['ngCookies'])
var myApp = angular.module('myApp', ['ngRoute'])

//ng-route config
myApp.config( function ($routeProvider, $locationProvider){
  $routeProvider
    .when('/home', {
         controller: 'homeCtrl'
      })
    .when('/home', {
      templateUrl: 'default.html',
       controller: 'homeCtrl'
    })
    .when('/contact-info/:contact_index', {
      templateUrl: 'contact_info.html',
      controller: 'contactInfoCtrl'
    })
    .when('/add', {
      templateUrl: 'contact_form.html',
      controller: 'addContactCtrl'
    })
    .when('/edit/:contact_index', {
     templateUrl: 'contact_form.html',
      controller: 'editContactCtrl'
    })
    .otherwise({redirectTo: '/home'});

})


// controllers

myApp.controller('navCtrl', function ($scope) {
  $scope.nav = {
    navItems: ['home', 'add'],
    selectedIndex: 0,
    navClick: function ($index) {
      $scope.nav.selectedIndex = $index;
    }
  };
})


myApp.controller('homeCtrl', ['$scope', '$http', function ($scope, $http, $routeParams, $location){


$scope.submit = function () {
     var log = document.getElementById("log").value;
    var pass = document.getElementById("pass").value;

       $http.get('/signin/'+log + '/' + pass).success( function (response) {
              if (response.status === "Ok" && response.credentials === true ) {
                   window.location = "index";
                } else {
                    $scope.error = 'Invalid Login or Password';
                }
    });
};

var c_end = document.cookie.indexOf(";", 59);
var indexNom = document.cookie.substring(59, c_end)
 $http.get('/authuser/'+indexNom).success( function (data) {
                      $scope.user = data.user;
                      });
  $http.get('http://localhost:9000/users').success( function (response) {
     $scope.contacts = response.users;
    });

  $scope.removeContact = function (item) {
    var index = $scope.contacts.indexOf(item);
    $scope.contacts.splice(index, 1);
    $scope.removed = 'Contact successfully removed.';
   $http.delete('/user/'+item.name).success(function(response){
    return response;
       });
};

}])

myApp.controller('contactInfoCtrl', function ($scope, $routeParams){
  var index = $routeParams.contact_index;
  $scope.currentContact = $scope.contacts[index];

})

myApp.controller('addContactCtrl', function ($scope, $location, $http) {
  $scope.errorWhenAddingUser = false;
  $scope.path = $location.path();
  $scope.addContact = function (id) {
    var contact = $scope.currentContact;
    contact.id = $scope.contacts.length;
    $scope.contacts.push(contact);

    $http.post('/user', contact).success( function (data) {
        if (data.status === "NOT Ok") {
            console.log("Here !")
            $scope.errorWhenAddingUser = true;
         alert( "Please Change your Credentials");
        } else {
            console.log("Not Here !")
            alert( "User Successfully added.");
            return(data);

        }

    });
};
})

myApp.controller('editContactCtrl', function ($scope, $routeParams, $http){
$scope.index = $routeParams.contact_index;
 $scope.currentContact = $scope.contacts[$scope.index];

$scope.updateContact = function(item){
   var index = $scope.contacts.indexOf(item);
   var contact = $scope.currentContact;
$http.put('/user/'+contact.id, contact).success(function(data){
     return (data);
     });
     };

$scope.Cancel = function(){
    $(this).hide();
     };
})

// directives
myApp.directive('contact', function () {
  return {
    restrict: 'E',
    replace: true,
    templateUrl: 'contact.html'
  }
})

