import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { FormControl, InputLabel, Select, MenuItem, Button, Grid, Typography, Box } from '@mui/material';

function FindShortestTravelTime() {
    const [availableStarSystems, setAvailableStarSystems] = useState([]);
    const [startSystem, setStartSystem] = useState('');
    const [endSystem, setEndSystem] = useState('');
    const [route, setRoute] = useState('');
    const [totalTravelTime, setTravelTime] = useState('');

    useEffect(() => {
        axios.get(`${process.env.REACT_APP_BACKEND_URL}/navigator/starsystems`)
            .then(response => {
                setAvailableStarSystems(response.data.map(system => system.name));
            })
            .catch(error => console.error('There was an error fetching the star systems:', error));
    }, []);

    const handleSubmit = (event) => {
        event.preventDefault();
        if (!startSystem || !endSystem) {
            alert('Please select both start and end star systems.');
            return;
        }
        axios.get(`${process.env.REACT_APP_BACKEND_URL}/navigator/findShortestTravelTime`, {
            params: {
                startSystem: startSystem,
                endSystem: endSystem,
            }
        })
        .then(response => {
            setRoute(response.data.route);
            setTravelTime(response.data.totalTravelTime);
        })
        .catch(error => console.error('There was an error calculating the shortest travel time:', error));
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
                                style={{ minWidth: 120 }}
                                required
                            >
                                {availableStarSystems.map((name) => (
                                    <MenuItem key={name} value={name}>{name}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </Grid>
                    <Grid item xs={12} sm={4}>
                    </Grid>
                    <Grid item xs={12}>
                        <Button type="submit" variant="contained" color="primary">Calculate</Button>
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

export default FindShortestTravelTime;
