import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { FormControl, InputLabel, Select, MenuItem, Button, Grid, Typography, TextField, Box } from '@mui/material';

function FindRoutesWithMaxStops() {
    const [availableStarSystems, setAvailableStarSystems] = useState([]);
    const [startSystem, setStartSystem] = useState('');
    const [endSystem, setEndSystem] = useState('');
    const [maxStops, setMaxStops] = useState('');
    const [routes, setRoutes] = useState([]);

    useEffect(() => {
        axios.get(`${process.env.REACT_APP_BACKEND_URL}/navigator/starsystems`)
            .then(response => {
                setAvailableStarSystems(response.data.map(system => system.name));
            })
            .catch(error => console.error('Error fetching star systems:', error));
    }, []);

    const handleSubmit = (event) => {
        event.preventDefault();
        if (!startSystem || !endSystem || maxStops === '') {
            alert('Please select both start and end star systems and specify the maximum number of stops.');
            return;
        }

        axios.get(`${process.env.REACT_APP_BACKEND_URL}/navigator/findRoutesWithMaxStops`, {
            params: {
                startSystem: startSystem,
                endSystem: endSystem,
                maxStops: maxStops,
            }
        })
        .then(response => {
            setRoutes(response.data);
        })
        .catch(error => console.error('Error calculating routes with max stops:', error));
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
                            label="Max Stops"
                            type="number"
                            size="small"
                            fullWidth
                            value={maxStops}
                            onChange={(e) => setMaxStops(e.target.value)}
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

export default FindRoutesWithMaxStops;
