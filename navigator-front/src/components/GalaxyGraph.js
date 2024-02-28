import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { ForceGraph2D } from 'react-force-graph';

function GalaxyGraph() {
  const [nodes, setNodes] = useState([]);
  const [links, setLinks] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        console.log('Backend URL:', process.env.REACT_APP_BACKEND_URL);
        const starSystemsResponse = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/navigator/starsystems`);
        const routesResponse = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/navigator/routes`);

        const starSystems = starSystemsResponse.data.map(system => ({
          id: system.name,
          name: system.name,
        }));

        const routes = routesResponse.data.map(route => ({
          source: route.startSystem,
          target: route.endSystem,
        }));

        setNodes(starSystems);
        setLinks(routes);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', width: '100%' }}>
        <div>
            <p>Interact with the graph: Hover over and move the nodes to explore.</p>
        </div>
        <div style={{ display: 'flex', flexDirection: 'row', width: '100%', border: '1px solid LightGray'}}>
            <div style={{ width: '100%'}}>
                <ForceGraph2D
                graphData={{ nodes, links }}
                nodeLabel="name"
                nodeAutoColorBy="name"
                linkDirectionalParticles={1}
                linkDirectionalParticleSpeed={0.003}
                d3VelocityDecay={0.5}
                width={window.innerWidth * 0.5 * 0.5}
                height={window.innerHeight * 0.35}
            />
            </div>
        </div>
    </div>
    );
}

export default GalaxyGraph;
