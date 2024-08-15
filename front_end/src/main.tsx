import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import { HistoryPage } from './components/history.tsx';
import { ReactKeycloakProvider} from '@react-keycloak/web';
import keycloak from './hooks/keycloak.ts';
import { AlertPage } from './components/alertPage.tsx';
const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
  },
  {
    path: "/history",
    element: <HistoryPage />,
  },
  {
    path: "/alerts",
    element: <AlertPage />,
  },
  
]);
const queryClient = new QueryClient()

const initOptions = {
  onLoad: "login-required",
  enableLogging: true,
}

ReactDOM.createRoot(document.getElementById('root')!).render(
  
    <QueryClientProvider client={queryClient}>
    <ReactKeycloakProvider  authClient={keycloak} initOptions={initOptions}>
    <RouterProvider router={router} />
    </ReactKeycloakProvider>
    </QueryClientProvider>
  
)
