import { AlertCircle, Archive, Search, User } from "lucide-react"
import { Nav } from "./nav"
import { Separator } from "./ui/separator"
import { Tabs, TabsContent} from "./ui/tabs"
import {useGetAllSearches} from "../api/search.api"
import { Spinner } from "./Spinner"
import { SearchList } from "./Search-list"
import { useKeycloak } from "@react-keycloak/web"



export function HistoryPage() {
  const { keycloak } = useKeycloak()
  const { data, isError, isLoading } = useGetAllSearches(keycloak.token);
  return (
    <div className="grid grid-cols-[300px_1fr] gap-4">
        <div>
          
          <Nav
           links={[
              { title: "Recherche", href:"/", label: "", icon: Search, variant: "ghost" },
              { title: "Alerts", href: "/alerts", label: "9", icon: AlertCircle, variant: "ghost" },
              { title: "Historique", href: "/history", label: "", icon: Archive, variant: "default" },
              { title: "Profile", href:"/profile", label: "23", icon: User, variant: "ghost" }
            ]}
          />
        </div>
        <div>  
          <Tabs defaultValue="all">
            <div className="flex items-center px-4 py-2">
              <h1 className="text-xl font-bold">Historique</h1>
            </div>
            <Separator />
            <TabsContent value="all" className="m-0">
            
              {isLoading ? (
                <div className="flex justify-center items-center h-64 text-gray-500">
                  <Spinner>This my take some time please wait!!</Spinner>
                </div>
              ) : isError ? (
                <div className="flex justify-center items-center h-64 text-red-500">
                  Error: Something went wrong try again later
                </div>
              ) : data && data.length > 0 ? (
                <SearchList items={data} />
              ) : (
                <div className="flex justify-center items-center h-64 text-gray-500">
                  No data to show
                </div>
              )}            
            </TabsContent>
          </Tabs>
        </div>
    </div>
  );
  }

