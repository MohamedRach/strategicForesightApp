import { AlertCircle, Archive, Search, User } from "lucide-react"
import { cn } from "../lib/utils"
import { Nav } from "./nav"
import { ResizableHandle, ResizablePanel, ResizablePanelGroup } from "./ui/resizable"
import { Separator } from "./ui/separator"
import { TooltipProvider } from "./ui/tooltip"
import { Tabs, TabsContent} from "./ui/tabs"
import { Input } from "./ui/input"
import { ResultList } from "./result-list"
import { ResultDisplay } from "./result-display"
import { useResult } from "../hooks/useResult"
import { useState } from "react"
import { Result} from "../data/data"
import { Button } from "./ui/button"
import { BarChartComponent } from "./barChart"
import { AreaChartComponent } from "./areaChart"

interface ResultProps {
  
  results: Result[]
  defaultLayout: number[] | undefined
  defaultCollapsed?: boolean
  navCollapsedSize: number
}


export function ResultPage({
  results,
  defaultLayout = [265, 440, 655],
  defaultCollapsed = false,
  navCollapsedSize,
}: ResultProps) {
  const [isCollapsed, setIsCollapsed] = useState(defaultCollapsed)
  const [result] = useResult()

  return (
    <TooltipProvider delayDuration={0}>
      <ResizablePanelGroup
        direction="horizontal"
        onLayout={(sizes: number[]) => {
          document.cookie = `react-resizable-panels:layout=${JSON.stringify(
            sizes
          )}`
        }}
        className="h-full max-h-[800px] items-stretch"
      >
        <ResizablePanel
          defaultSize={defaultLayout[0]}
          collapsedSize={navCollapsedSize}
          collapsible={true}
          minSize={15}
          maxSize={20}
          onCollapse={() => {
            setIsCollapsed(true)
            document.cookie = `react-resizable-panels:collapsed=${JSON.stringify(true)}`
          }}
          onExpand={() => {
            setIsCollapsed(false)
             document.cookie = `react-resizable-panels:collapsed=${JSON.stringify(false)}`
          }}
          className={cn(
            isCollapsed &&
              "min-w-[50px] transition-all duration-300 ease-in-out"
          )}
        >
            <Nav
            isCollapsed={isCollapsed}
            links={[
              {
                title: "Recherce",
                label: "",
                icon: Search,
                variant: "default",
              },
              {
                title: "Alerts",
                label: "9",
                icon: AlertCircle,
                variant: "ghost",
              },
              {
                title: "Historique",
                label: "",
                icon: Archive,
                variant: "ghost",
              },
              {
                title: "Profile",
                label: "23",
                icon: User,
                variant: "ghost",
              }
            ]}
          />
        </ResizablePanel>
        <ResizableHandle withHandle />
        <ResizablePanel defaultSize={defaultLayout[1]} minSize={30}>
          <Tabs defaultValue="all">
            <div className="flex items-center px-4 py-2">
              <h1 className="text-xl font-bold">Recherche</h1>
            </div>
            <Separator />
            <div className="bg-background/95 p-4 backdrop-blur supports-[backdrop-filter]:bg-background/60">
              <form>
                <div className="relative">
                  <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
                  <Input placeholder="Tags" className="pl-8" />
                </div>
                <div className="relative mt-3">
                  <Input placeholder="Sources" className="pl-8" />
                </div>
                <div className="relative">
                  <Button className="ml-[-630px] mt-3">Rechercher</Button>
                </div>
              </form>
            </div>
            <TabsContent value="all" className="m-0">
              <ResultList items={results} />
            </TabsContent>
          </Tabs>
        </ResizablePanel>
        <ResizableHandle withHandle />
        <ResizablePanel defaultSize={defaultLayout[2]}>
          <ResultDisplay
            result={results.find((item: Result) => item.id === result.selected) || null}
          />
        </ResizablePanel>
      </ResizablePanelGroup>
      <Separator />
      <div className="ml-[300px] grid grid-cols-2 gap-x-3">
        <div className="mt-3">
          <BarChartComponent />
        </div>
        <div className="mt-3">
          <AreaChartComponent />
        </div>
      </div>
    </TooltipProvider>
  )
}
