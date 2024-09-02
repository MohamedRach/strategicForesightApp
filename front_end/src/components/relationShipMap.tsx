import { useKeycloak } from '@react-keycloak/web';
import React, { useRef, useEffect, useState } from 'react';
import ForceGraph2D, { ForceGraphMethods } from 'react-force-graph-2d';
import { useLocation } from 'react-router-dom';
import { Response, useGetResultsById } from '../api/search.api';
import { Spinner } from './Spinner';

const RelationshipMap: React.FC = () => {
  const { keycloak } = useKeycloak();
  const graphRef = useRef<ForceGraphMethods>();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const id = queryParams.get('id');
  const { data, isLoading } = useGetResultsById(id || "", keycloak.token);
  const [graphData, setGraphData] = useState<{ nodes: any[]; links: any[] } | null>(null);
  const [hoveredNode, setHoveredNode] = useState<any>(null);

  useEffect(() => {
    if (data) {
      let nodes: any[] = [];
      let links: any[] = [];
      data.forEach((d) => {
         
          const nodeExists = (id: string) => nodes.find(node => node.id === id);

          if (!nodeExists(d.username)) {
            nodes.push({
              id: d.username,
              sentiment: d.sentiment,
              val: 30,
              hashtags: [],
              caption: d.caption, // Add caption here
            });
          }

          if (d.responses) {
            d.responses.forEach((response: Response) => {
              if (!nodeExists(response.username)) {
                nodes.push({
                  id: response.username,
                  sentiment: response.sentiment,
                  val: 20,
                  hashtags: [],
                  caption: response.tweet, // Add caption here
                });
              }

              links.push({
                source: d.username,
                target: response.username,
              });
            });
          }
        
      });

      setGraphData({ nodes, links });
    }
  }, [data]);

  useEffect(() => {
    const fg = graphRef.current;
    if (fg) {
      fg.d3Force('charge')?.strength(-50);
      fg.d3Force('link')?.distance(150);
      fg.d3Force('center', null);
      fg.d3Force('radial', null);
    }
  }, []);

  const handleNodeDrag = (node: any) => {
    node.fx = node.x;
    node.fy = node.y;
  };

  const handleNodeDragEnd = (node: any) => {
    node.fx = node.x;
    node.fy = node.y;
  };

  const handleNodeClick = (node: any) => {
    setHoveredNode({
      id: node.id,
      sentiment: node.sentiment,
      hashtags: node.hashtags,
      caption: node.caption, // Include the caption in the hovered node state
    });
  };

  return (
    <div style={{ width: '100%', height: '100vh', position: 'relative' }}>
      {isLoading ? (
        <Spinner>Loading.....</Spinner>
      ) : (
        graphData && (
          <ForceGraph2D
            ref={graphRef}
            graphData={graphData}
            nodeLabel="id"
            nodeColor={(node: any) => node.sentiment === 'positive' ? 'green' : 'red'}
            nodeRelSize={3}
            linkColor={() => '#cccccc'}
            linkWidth={2}
            linkDirectionalParticles={2}
            linkDirectionalParticleWidth={2}
            onNodeClick={handleNodeClick}
            onNodeDrag={handleNodeDrag}
            onNodeDragEnd={handleNodeDragEnd}
            enableNodeDrag={true}
            nodeCanvasObject={(node: any, ctx, globalScale) => {
              const label = node.id;
              const fontSize = 16 / globalScale;
              ctx.font = `${fontSize}px Sans-Serif`;
              ctx.textAlign = 'center';
              ctx.textBaseline = 'middle';
              ctx.fillStyle = node.sentiment === 'positive' ? 'green' : 'red';
              ctx.fillText(label, node.x, node.y);

              ctx.beginPath();
              ctx.arc(node.x, node.y, node.val / 2, 0, 2 * Math.PI, false);
              ctx.fillStyle = node.sentiment === 'positive' ? 'rgba(0, 255, 0, 0.2)' : 'rgba(255, 0, 0, 0.2)';
              ctx.fill();
            }}
            cooldownTicks={100}
            onEngineStop={() => graphRef.current?.zoomToFit(400)}
          />
        )
      )}
      {hoveredNode && (
        <div
          style={{
            position: 'absolute',
            top: `200px`,
            left: `600px`,
            padding: '10px',
            background: '#fff',
            border: '1px solid #ccc',
            borderRadius: '3px',
            pointerEvents: 'auto', // Changed to allow button interaction
            transform: 'translate(-50%, -100%)',
            boxShadow: '0 2px 8px rgba(0, 0, 0, 0.15)',
          }}
        >
          <button
            onClick={() => setHoveredNode(null)}
            style={{
              position: 'absolute',
              top: '5px',
              right: '5px',
              background: 'transparent',
              border: 'none',
              cursor: 'pointer',
              fontSize: '16px',
              fontWeight: 'bold',
              color: '#555',
            }}
          >
            &times;
          </button>
          <div><strong>Username:</strong> {hoveredNode?.id}</div>
          <div><strong>Sentiment:</strong> {hoveredNode?.sentiment}</div>
          <div><strong>Hashtags:</strong> {hoveredNode?.hashtags.join(', ')}</div>
          <div><strong>Tweet:</strong> {hoveredNode?.caption}</div> {/* Display the tweet/caption */}
        </div>
      )}
    </div>
  );
};

export default RelationshipMap;


