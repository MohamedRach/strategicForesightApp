import { useDeleteSearch } from "../api/search.api";
import { ScrollArea } from "./ui/scroll-area";
import { cn } from "../lib/utils";
import { Badge } from "./ui/badge";
import { Button } from "./ui/button";
import { MouseEvent, useEffect } from "react";
import { Alert } from "../api/alert.api";
import { useKeycloak } from "@react-keycloak/web";


interface AlertListProps {
  items: Alert[]
}

export function AlertList({ items }: AlertListProps) {
  const { keycloak } = useKeycloak()
  const {mutate, isSuccess, isError} = useDeleteSearch(keycloak.token)

  const handleClick = (id: string, e: MouseEvent<HTMLButtonElement>) => {
    e.preventDefault()
    mutate(id)
  }

  useEffect(() => {
    if (isSuccess) alert("avec succés")
    if (isError) alert("erreur, essayer plus tard")
  }, [isSuccess, isError])

  return (
    <ScrollArea className="h-screen">
      
      <div className="flex flex-col gap-2 p-4 pt-0">
        {items.map((item) => (
          <a  className={cn(
              "flex flex-col items-start gap-2 rounded-lg p-3 text-left text-sm transition-all hover:bg-accent")} href={`/?id=${item.searchId}`}>
          <button
            key={item.id}
            className={cn(
              "flex flex-col items-start gap-2 rounded-lg p-3 text-left text-sm transition-all hover:bg-accent"
            )}
            
          >
            <div className="flex w-full flex-col gap-1">
              <div className="flex items-center">
                <div className="flex items-center gap-2">
                  Sources: 
                  {item.sources.map((source) => (
                     <div className="font-semibold">{source}</div>
                  ))}
                </div>
              <div
                  className={cn(
                    "ml-[1000px] text-xs"
                  )}
                >
                  <Button onClick={(e) => console.log(e)} variant="destructive">Delete</Button>
                </div>
            </div>            
              <div className="flex items-center gap-2">
                Keywords: 
                {item.keywords.map((keyword) => (
                     <Badge key={keyword} variant="default">
                      {keyword}
                     </Badge>
                ))}
                                  
              </div>
              <div className="flex items-center gap-2">
                New Results: 
                {item.count}           
              </div>

             
           </div> 
          </button>
          </a>
        ))}
      </div>
    </ScrollArea>
  )
}




