// src/components/StarSystemsList.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';

function StarSystemsList() {
  const [starSystems, setStarSystems] = useState([]);

  useEffect(() => {
    console.log('Backend URL:', process.env.REACT_APP_BACKEND_URL);
    axios.get(`${process.env.REACT_APP_BACKEND_URL}/navigator/starsystems`)
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
