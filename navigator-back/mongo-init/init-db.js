// init-db.js
db = db.getSiblingDB('starmap');

db.StarSystems.insertMany([
    {"_id": "A", "name": "Solar System"},
    {"_id": "B", "name": "Alpha Centauri"},
    {"_id": "C", "name": "Sirius"},
    {"_id": "D", "name": "Betelgeuse"},
    {"_id": "E", "name": "Vega"}
]);

db.Routes.insertMany([
    {"_id": "1", "startSystem": "Solar System", "endSystem": "Alpha Centauri", "travelTime": 5},
    {"_id": "2", "startSystem": "Alpha Centauri", "endSystem": "Sirius", "travelTime": 4},
    {"_id": "3", "startSystem": "Sirius", "endSystem": "Betelgeuse", "travelTime": 8},
    {"_id": "4", "startSystem": "Betelgeuse", "endSystem": "Sirius", "travelTime": 8},
    {"_id": "5", "startSystem": "Betelgeuse", "endSystem": "Vega", "travelTime": 6},
    {"_id": "6", "startSystem": "Solar System", "endSystem": "Betelgeuse", "travelTime": 5},
    {"_id": "7", "startSystem": "Sirius", "endSystem": "Vega", "travelTime": 2},
    {"_id": "8", "startSystem": "Vega", "endSystem": "Alpha Centauri", "travelTime": 3},
    {"_id": "9", "startSystem": "Solar System", "endSystem": "Vega", "travelTime": 7}
]);