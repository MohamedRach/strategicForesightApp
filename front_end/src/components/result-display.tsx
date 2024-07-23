import {format} from "date-fns/format"
import { Separator } from "./ui/separator"
import { Result, useDeleteResult, useImage} from "../api/search.api"
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar"
import { useEffect} from "react"
import { Button } from "./ui/button"
import { useResultStore } from "../hooks/store"

interface ResultDisplayProps {
  result: Result | undefined
}
export function ResultDisplay({ result }: ResultDisplayProps) {
  const {mutateAsync, data} = useImage()
  const {mutate, isSuccess, isError} = useDeleteResult()
  const {removeResult} = useResultStore()
  useEffect(() => {
    if (result?.img && (result?.img.includes("instagram") || result?.img.includes("news"))) {
      mutateAsync({url: result.img})
    }
  }, [result, mutateAsync]);

  const handleClick = (id: string) => {
    mutate(id)
    if(result) removeResult(result)
  }

  useEffect(() => {
    if (isSuccess) {
      
      alert("avec succés")
    }
    if(isError) {
      alert("erreur essayer plus tard!!")
    } 

  }, [isSuccess, isError])
  return (
    
    <div className="flex h-full flex-col">
      <Separator />
      {result ? (
        <div className="flex flex-1 flex-col overflow-y-scroll">
          <div className="flex items-start p-4">
            <div className="flex items-start gap-4 text-sm">
              <Avatar>
                <AvatarImage alt={result?.username} />
                <AvatarFallback>
                  {result?.username
                    .split(" ")
                    .map((chunk) => chunk[0])
                    .join("")}
                </AvatarFallback>
              </Avatar>
              <div className="grid gap-1">
                <div className="font-semibold">{result.username}</div>
                <div className="line-clamp-1 text-xs">{result.source}</div>
                <div className="line-clamp-1 text-xs">
                  <span className="font-medium">Likes:</span> {result.likes}
                </div>
                              </div>
            </div>
            
            {result.date && (
              <div className="ml-auto text-xs text-muted-foreground">
                {format(new Date(result.date), "PPpp")}
                  <div className="mt-[10px]">
                    <Button onClick={() => handleClick(result.id)} variant="destructive" className="w-[140px]">Delete</Button>
                  
                  </div>
              </div>
            )}
          
            </div>
          <Separator />
            <div>
            <div className="flex-1 whitespace-pre-wrap p-4 text-sm">
              {result.href ? (<a className="underline" href={`${result.href}`}> {result.caption}</a>): (result.caption)}
            </div>
            <div className="flex-1 whitespace-pre-wrap p-4 text-sm">
              {data ? ( <img src={data} />) : ( <img src={result.img} />)}
            </div>
            </div>
        </div>
        
      ) : (
        <div className="p-8 text-center text-muted-foreground">
          Aucun message sélectionné
        </div>
      )}
    </div>
    
  )
}
