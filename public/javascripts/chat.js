//var Session = angular.module('session', ['ngCookies'])
var app = angular.module('app', ['pubnub.angular.service'])

.controller('ChatCtrl', ['$scope', '$http', 'Pubnub', function($scope, $http, Pubnub) {
  $http.get('http://localhost:9000/msgs').success( function (response) {
             $scope.messages = response.messages;
            });

var c_end = document.cookie.indexOf(";", 59);
var indexNom = document.cookie.substring(59, c_end);

$http.get('/authuser/'+indexNom).success( function (data) {
                      $scope.user = data.user;
                           // var json = JSON.parse(data);
                            // var nom = json.name;

                      });

   $scope.name = indexNom ;
   $scope.channel = 'messages-channel';
    $scope.messageContent = '';
    // Generating a random uuid between 1 and 100 using utility function from lodash library.
    $scope.uuid = _.random(1000000).toString();
    // Please signup to PubNub to use your own keys: https://admin.pubnub.com/
    Pubnub.init({
        publish_key: 'pub-c-a1cd7ac1-585e-478e-925b-65d17ce62f7d',
        subscribe_key: 'sub-c-204f063e-c559-11e5-b764-02ee2ddab7fe',
        ssl: true,
        uuid: $scope.uuid
    });

    // Fetching a uniq random avatar from the robohash.org service.
  /*  $scope.avatarUrl = function(uuid) {
        return '//robohash.org/' + uuid + '?set=set2&bgset=bg2&size=70x70';

    };*/

    // Send the messages over PubNub Network
    $scope.sendMessage = function() {
       // Don't send an empty message
       if (!$scope.messageContent ||
            $scope.messageContent === '') {
            return;
        }

        Pubnub.publish({

            channel: $scope.channel,
            message: {
                date: new Date(),
                message: $scope.messageContent,
                sender_uuid: $scope.uuid,
                sender_name: $scope.name


            },
            callback: function(m) {
                            console.log(m);
                        }

        });
        // Reset the messageContent input
        $scope.messageContent = '';
    }


    // Subscribe to messages channel
    Pubnub.subscribe({
        channel: $scope.channel,
        triggerEvents: ['callback']
    });

    // Make it possible to scrollDown to the bottom of the messages container
    $scope.scrollDown = function(time) {
        var $elem = $('.collection');
        $('body').animate({
            scrollTop: $elem.height()
        }, time);
    };
$scope.scrollDown(400);
// Listenning to messages sent.
    $scope.$on(Pubnub.getMessageEventNameFor($scope.channel), function(ngEvent, m, id) {
        $scope.$apply(function() {
         m.id = ($scope.messages.length) + 1;
            $scope.messages.push(m);
            $http.post('http://localhost:9000/msg', m).success( function (data) {
                     return (data);
                     });
        });
        $scope.scrollDown(400);
    });

}])

.directive('contact', function () {
  return {
    restrict: 'E',
    require: ngModel,
    replace: true
  }
})