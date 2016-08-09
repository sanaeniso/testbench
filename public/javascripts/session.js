
angular.module('Session', ['ngCookies'])



.factory('sessionService', ['$cookies', function($cookies) {
        // read Play session cookie
        var rawCookie = $cookies['PLAY_SESSION'];
        var rawData = rawCookie.substring(rawCookie.indexOf('-') + 1, rawCookie.length -1);
        var session = {};
        _.each(rawData.split("&"), function(rawPair) {
            var pair = rawPair.split('=');
            session[pair[0]] = pair[1];
        });
        return session;

    }])

