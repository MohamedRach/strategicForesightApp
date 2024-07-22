import { formatDistanceToNow } from "date-fns";
import { Result } from "../api/search.api";
import { useResult } from "../hooks/useResult";
import { ScrollArea } from "./ui/scroll-area";
import { cn } from "../lib/utils";
import { Badge } from "./ui/badge";

interface ResultListProps {
  items: Result[]
}

export function ResultList({ items }: ResultListProps) {
  const [result, setResult] = useResult()

  return (
    <ScrollArea className="h-screen">
      <div className="flex flex-col gap-2 p-4 pt-0">
        {items.map((item) => (
          <button
            key={item.id}
            className={cn(
              "flex flex-col items-start gap-2 rounded-lg border p-3 text-left text-sm transition-all hover:bg-accent",
              result.selected === item.id && "bg-muted"
            )}
            onClick={() =>
              setResult({
                ...result,
                selected: item.id,
              })
            }
          >
            <div className="flex w-full flex-col gap-1">
              <div className="flex items-center">
                <div className="flex items-center gap-2">
                  <div className="font-semibold">{item.username}</div>
                </div>
                <div
                  className={cn(
                    "ml-auto text-xs",
                    result.selected === item.id
                      ? "text-foreground"
                      : "text-muted-foreground"
                  )}
                >
                  {formatDistanceToNow(new Date(item.date), {
                    addSuffix: true,
                  })}
                </div>
              </div>
              <div className="text-xs font-medium">{item.source}</div>
            </div>
            <div className="line-clamp-2 text-xs text-muted-foreground">
              {item.caption.substring(0, 300)}
            </div>
            
              <div className="flex items-center gap-2">
                
                  <Badge key={item.keyword} variant="default">
                    {item.keyword}
                  </Badge>
                
              </div>
            
          </button>
        ))}
      </div>
    </ScrollArea>
  )
}


