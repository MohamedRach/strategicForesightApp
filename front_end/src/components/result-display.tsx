import {format} from "date-fns/format"
import { Separator } from "./ui/separator"
import { Result} from "../data/data"
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar"


interface ResultDisplayProps {
  result: Result | null
}

export function ResultDisplay({ result }: ResultDisplayProps) {
  //const today = new Date()

  return (
    <div className="flex h-full flex-col">
      <Separator />
      {result ? (
        <div className="flex flex-1 flex-col">
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
                <div className="line-clamp-1 text-xs">{result.platform}</div>
                <div className="line-clamp-1 text-xs">
                  <span className="font-medium">Likes:</span> {result.likes}
                </div>
              </div>
            </div>
            {result.date && (
              <div className="ml-auto text-xs text-muted-foreground">
                {format(new Date(result.date), "PPpp")}
              </div>
            )}
          </div>
          <Separator />
          <div className="flex-1 whitespace-pre-wrap p-4 text-sm">
            {result.content}
          </div>
        </div>
      ) : (
        <div className="p-8 text-center text-muted-foreground">
          No message selected
        </div>
      )}
    </div>
  )
}
