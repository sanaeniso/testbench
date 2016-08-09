var privateApp = angular.module('privateApp', [])

// controllers

privateApp.controller('privateCtrl', function ($scope, $http) {
  $http.get('http://localhost:9000/users').success( function (response) {
      $scope.contacts = response.users;;
     });

     $http.get('http://localhost:9000/comms').success( function (response) {
           $scope.communications = response.communications;
          });

$scope.chatMessages = [];

  $scope.formatChat = function(text) {
    var chat = {};

    chat.text = text;
    return chat;
  }

  $scope.addChat = function() {
    if ($scope.newChatMsg != "") {
      var chat = $scope.formatChat($scope.newChatMsg );

      $scope.chatMessages.push(chat);
      $scope.newChatMsg = "";
    }
    else {

    }
  }

$scope.findchat = function($routeParams){
 var index = $routeParams.contact_index;
  $scope.currentContact = $scope.contacts[index];

}


$('.left .person').mousedown(function(){
    if ($(this).hasClass('.active')) {
        return false;
    } else {
        var findChat = $(this).attr('data-chat');
        var personName = $(this).find('.name').text();
        $('.right .top .name').html(personName);
        $('.chat').removeClass('active-chat');
        $('.left .person').removeClass('active');
        $(this).addClass('active');
        $('.chat[data-chat = '+findChat+']').addClass('active-chat');
    }
});

})
