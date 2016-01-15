# CampUs
Mini-projet Android


Info utile Google Map : 

http://gis.stackexchange.com/questions/49805/can-i-view-a-google-maps-map-by-bounding-latitude-and-longitude-coordinates

var southWest = new google.maps.LatLng(36.90731625763393,-86.51778523864743);
var northEast = new google.maps.LatLng(37.02763411292923,-86.37183015289304);
var bounds = new google.maps.LatLngBounds(southWest,northEast);
myMap.fitBounds(bounds);
