import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { FormControl, InputLabel, Select, MenuItem, Button, Grid, Typography, TextField, Box } from '@mui/material';

function FindRoutesWithinMaxTime() {
    const [availableStarSystems, setAvailableStarSystems] = useState([]);
    const [startSystem, setStartSystem] = useState('');
    const [endSystem, setEndSystem] = useState('');
    const [maxTime, setMaxTime] = useState('');
    const [routes, setRoutes] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:8080/api/navigator/starsystems')
            .then(response => {
                setAvailableStarSystems(response.data.map(system => system.name));
            })
            .catch(error => console.error('Error fetching star systems:', error));
    }, []);

    const handleSubmit = (event) => {
        event.preventDefault();
        if (!startSystem || !endSystem || maxTime === '') {
            alert('Please select both start and end star systems and specify the maximum travel time.');
            return;
        }

        axios.get(`http://localhost:8080/api/navigator/findRoutesWithinMaxTime`, {
            params: {
                startSystem: startSystem,
                endSystem: endSystem,
                maxTime: maxTime,
            }
        })
        .then(response => {
            setRoutes(response.data); // Assumes the API returns an array of RouteDTO objects
        })
        .catch(error => console.error('Error calculating routes within max time:', error));
    };

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <Grid container spacing={2} alignItems="center">
                    <Grid item xs={12} sm={4}>
                        <FormControl fullWidth size="small" style={{ minWidth: 120 }}>
                            <InputLabel>Start System</InputLabel>
                            <Select
                                value={startSystem}
                                onChange={(e) => setStartSystem(e.target.value)}
                                required
                            >
                                {availableStarSystems.map((name) => (
                                    <MenuItem key={name} value={name}>{name}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </Grid>
                    <Grid item xs={12} sm={4}>
                        <FormControl fullWidth size="small" style={{ minWidth: 120 }}>
                            <InputLabel>End System</InputLabel>
                            <Select
                                value={endSystem}
                                onChange={(e) => setEndSystem(e.target.value)}
                                required
                            >
                                {availableStarSystems.map((name) => (
                                    <MenuItem key={name} value={name}>{name}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </Grid>
                    <Grid item xs={12} sm={4}>
                        <TextField
                            label="Max Time"
                            type="number"
                            size="small"
                            fullWidth
                            value={maxTime}
                            onChange={(e) => setMaxTime(e.target.value)}
                            InputProps={{ inputProps: { min: 0 } }}
                            required
                        />
                    </Grid>
                    <Box display="flex" justifyContent="left" width="100%" mt={2} ml={2}>
                        <Button type="submit" variant="contained" color="primary">Calculate</Button>
                    </Box>
                </Grid>
            </form>
            {routes.length > 0 && (
                <Box mt={2}>
                    <Typography variant="h6">Routes Found:</Typography>
                    {routes.map((route, index) => (
                        <Box key={index} my={2}>
                            <Typography variant="body1">Route {index + 1}:</Typography>
                            <Typography variant="body2">{`Path: ${route.route.join(' -> ')}`}</Typography>
                            <Typography variant="body2">{`Total Travel Time: ${route.totalTravelTime} Hours`}</Typography>
                        </Box>
                    ))}
                </Box>
            )}
        </div>
    );
}

export default FindRoutesWithinMaxTime;
