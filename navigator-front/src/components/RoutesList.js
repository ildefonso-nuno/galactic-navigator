// src/components/RoutesList.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';

function RoutesList() {
  const [routes, setRoutes] = useState([]);

  useEffect(() => {
    axios.get('http://localhost:8080/api/navigator/routes')
      .then(response => {
        setRoutes(response.data);
      })
      .catch(error => console.error('There was an error!', error));
  }, []);

  return (
    <div>
      <ul>
        {routes.map(route => (
          <li key={route.id}>{`${route.startSystem} to ${route.endSystem}: ${route.travelTime} Hours`}</li>
        ))}
      </ul>
    </div>
  );
}

export default RoutesList;
