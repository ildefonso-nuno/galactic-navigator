import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { FormControl, InputLabel, Select, MenuItem, Button, Grid, IconButton, Box, Typography} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';


function CalculateTravelTime() {
  const [availableStarSystems, setAvailableStarSystems] = useState([]);
  const [selectedStarSystems, setSelectedStarSystems] = useState(['', '']);
  const [route, setRoute] = useState('');
  const [totalTravelTime, setTravelTime] = useState('');

  useEffect(() => {
    axios.get(`${process.env.REACT_APP_BACKEND_URL}/navigator/starsystems`)
      .then(response => {
        setAvailableStarSystems(response.data.map(system => system.name));
      })
      .catch(error => console.error('There was an error fetching the star systems:', error));
  }, []);

  const handleStarSystemChange = (index, event) => {
    const updatedStarSystems = [...selectedStarSystems];
    updatedStarSystems[index] = event.target.value;
    setSelectedStarSystems(updatedStarSystems);
  };

  const addStarSystemField = () => {
    setSelectedStarSystems([...selectedStarSystems, '']);
  };

  const removeStarSystemField = (index) => {
    const updatedStarSystems = selectedStarSystems.filter((_, i) => i !== index);
    setSelectedStarSystems(updatedStarSystems);
  };

  const calculateTime = (event) => {
    event.preventDefault();

    const queryParams = selectedStarSystems
      .filter(system => system.trim() !== '')
      .map(system => `starSystems=${encodeURIComponent(system)}`)
      .join('&');

    axios.get(`${process.env.REACT_APP_BACKEND_URL}/navigator/calculateRouteTravelTime?${queryParams}`)
      .then(response => {
        setRoute(response.data.route);
        setTravelTime(response.data.totalTravelTime);
      })
      .catch(error => console.error('There was an error!', error));
  };

  return (
    <div>
      <form onSubmit={calculateTime}>
        <Grid container spacing={2} alignItems="center">
          {selectedStarSystems.map((system, index) => (
            <Grid item key={index} xs={12} sm="auto">
              <FormControl size="small">
                <InputLabel>{`System ${index + 1}`}</InputLabel>
                <Select
                  value={system}
                  onChange={(e) => handleStarSystemChange(index, e)}
                  style={{ minWidth: 120 }}
                >
                  {availableStarSystems.map((name) => (
                    <MenuItem key={name} value={name}>{name}</MenuItem>
                  ))}
                </Select>
              </FormControl>
              {selectedStarSystems.length > 2 && (
                <IconButton onClick={() => removeStarSystemField(index)} size="small">
                  <DeleteIcon />
                </IconButton>
              )}
            </Grid>
          ))}
          <Grid item xs={12}>
            <Button type="submit" variant="contained" color="primary" style={{ marginRight: '8px' }}>Calculate</Button>
            <Button variant="outlined" onClick={addStarSystemField} >Add System</Button>
          </Grid>
        </Grid>
      </form>
      {route.length > 0 && (
        <Box my={2}>
          <Typography variant="body2">{`Path: ${route.join(' -> ')}`}</Typography>
          <Typography variant="body2">{`Total Travel Time: ${totalTravelTime} Hours`}</Typography>
        </Box>
      )}
    </div>
  );
}

export default CalculateTravelTime;
