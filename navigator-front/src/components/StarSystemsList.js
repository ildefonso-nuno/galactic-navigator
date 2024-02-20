// src/components/StarSystemsList.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';

function StarSystemsList() {
  const [starSystems, setStarSystems] = useState([]);

  useEffect(() => {
    axios.get('http://localhost:8080/api/navigator/starsystems')
      .then(response => {
        setStarSystems(response.data);
      })
      .catch(error => console.error('There was an error!', error));
  }, []);

  return (
    <div>
      <ul>
        {starSystems.map(system => (
          <li key={system.id}>{system.name}</li>
        ))}
      </ul>
    </div>
  );
}

export default StarSystemsList;
