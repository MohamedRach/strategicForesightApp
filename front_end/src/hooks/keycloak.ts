import Keycloak from 'keycloak-js';

const keycloak = new Keycloak({
  realm: 'my_realm',
  url: 'http://localhost:8080',
  clientId: "reactapp"
});

export default keycloak;
