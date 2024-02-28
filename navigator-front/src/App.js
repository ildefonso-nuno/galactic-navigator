import './App.css';
import * as React from 'react';
import StarSystemsList from './components/StarSystemsList';
import RoutesList from './components/RoutesList';
import CalculateTravelTime from './components/CalculateTravelTime';
import FindShortestTravelTime from './components/FindShortestTravelTime';
import FindRoutesWithMaxStops from './components/FindRoutesWithMaxStops';
import FindRoutesWithExactStops from './components/FindRoutesWithExactStops';
import FindRoutesWithinMaxTime from './components/FindRoutesWithinMaxTime';
import GalaxyGraph from './components/GalaxyGraph';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import { Container, Grid } from '@mui/material';

function App() {
  const backendUrl = process.env.REACT_APP_BACKEND_URL;

  return (
    <div>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6">Galaxy Navigator</Typography>
          {backendUrl && (
              <Typography variant="body1" style={{ marginLeft: '20px' }}>
                Backend URL: {backendUrl}
              </Typography>
          )}
        </Toolbar>
      </AppBar>
      <Container maxWidth="lg" style={{ marginTop: '20px' }}>
        <Container>
          <Grid container spacing={2} columns={16}>
            <Grid xs={8}>
              <Container maxWidth="lg" style={{ marginTop: '20px' }}>
                <Typography variant="h4" gutterBottom>Star Systems</Typography>
                <StarSystemsList />
              </Container>
              <Container style={{ marginTop: '40px' }}>
                <Typography variant="h4" gutterBottom style={{ marginTop: '20px' }}>Routes</Typography>
                <RoutesList />
              </Container>
            </Grid>
            <Grid xs={6}>
              <Container maxWidth="lg" style={{ marginTop: '20px'}}>
                <Typography variant="h4" gutterBottom style={{ marginTop: '20px' }}>StarMap</Typography>
                <GalaxyGraph />
              </Container>
            </Grid>
          </Grid>
        </Container>
        <Container maxWidth="lg" style={{ margin: '50px 0' }}>
          <Typography variant="h4" gutterBottom style={{ marginTop: '20px' }}>Calculate Travel Time</Typography>
          <CalculateTravelTime />
        </Container>
        <Container maxWidth="lg" style={{ margin: '50px 0' }}>
          <Typography variant="h4" gutterBottom style={{ marginTop: '20px' }}>Find Shortest Travel Time</Typography>
          <FindShortestTravelTime />
        </Container>
        <Container maxWidth="lg" style={{ margin: '50px 0' }}>
          <Typography variant="h4" gutterBottom style={{ marginTop: '20px' }}>Find Routes With Max Stops</Typography>
          <FindRoutesWithMaxStops />
        </Container>
        <Container maxWidth="lg" style={{ margin: '50px 0' }}>
          <Typography variant="h4" gutterBottom style={{ marginTop: '20px' }}>Find Routes With Exact Stops</Typography>
          <FindRoutesWithExactStops />
        </Container>
        <Container maxWidth="lg" style={{ margin: '50px 0' }}>
          <Typography variant="h4" gutterBottom style={{ marginTop: '20px' }}>Find Routes Within Max Time</Typography>
          <FindRoutesWithinMaxTime />
        </Container>
      </Container>
    </div>
  );
}

export default App;