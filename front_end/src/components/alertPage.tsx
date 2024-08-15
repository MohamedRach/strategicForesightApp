import { AlertCircle, Archive, Search, User, XIcon } from "lucide-react"
import { Nav } from "./nav"
import { Separator } from "./ui/separator"
import { Tabs, TabsContent} from "./ui/tabs"
import { Spinner } from "./Spinner"
import { useGetAllAlert, useNotification} from "../api/alert.api"
import { AlertList } from "./AlertList"
import { useKeycloak } from "@react-keycloak/web"
import { Button } from "./ui/button"
import { Dialog, DialogFooter, DialogHeader } from "./ui/dialog"
import { DialogContent, DialogDescription, DialogTitle, DialogTrigger } from "./ui/dialog"
import { useState } from "react"
import { Input } from "./ui/input"
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectValue } from "./ui/select"
import { SelectTrigger } from "@radix-ui/react-select"



export function AlertPage() {
  const [sources, setSources] = useState<string[]>([])
  const [keywords, setKeywords] = useState<string[]>([])
  const [text, setText] = useState("")
  const [frequency, setFrequency] = useState("")
  const { keycloak } = useKeycloak()
  const { data, isError, isLoading } = useGetAllAlert(keycloak.token);
  const {mutate, isPending} = useNotification(keycloak.token)  
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setText(e.target.value);
  };
  const handleXClick = (keyword: string) => {
    const updatedKeywords = keywords.filter((word) => word !== keyword)
    setKeywords(updatedKeywords)
  }

  const handleXSourceClick = (source: string) => {
    const updatedSources = sources.filter((word) => word !== source)
    setSources(updatedSources)
  }

  const handleValueChange = (source: string) => {
    const newSources = [...sources, source]
    setSources(newSources)
  }

  const handleFrequenctChange = (frequency: string) => {
    setFrequency(frequency)
  }

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === ' ') {
      e.preventDefault();
      const words = text.trim().split(/\s+/);
      if (words.length > 0) {
        const newKeywords = [...keywords, ...words];
        setKeywords(newKeywords);
        setText('');
      }
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    const data = {keywords, sources, frequency}
    mutate(data)
  }

 
  return (
    <div className="grid grid-cols-[300px_1fr] gap-4">
        <div>
          
          <Nav
           links={[
              { title: "Recherche", href:"/", label: "", icon: Search, variant: "ghost" },
              { title: "Alerts", href: "/alerts", label: "", icon: AlertCircle, variant: "default" },
              { title: "Historique", href: "/history", label: "", icon: Archive, variant: "ghost" },
              { title: "Profile", href:"/profile", label: "", icon: User, variant: "ghost" }
            ]}
          />
        </div>
        <div>  
          <Tabs defaultValue="all">
            <div className="flex items-center px-4 py-2">
              <h1 className="text-xl font-bold">Alerts</h1>
            </div>
            <Separator />
           <Dialog>
            <DialogTrigger asChild>
              <Button className="ml-[-1370px] mt-[20px]">Ajouter</Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[1425px] max-h-[1000px]">
        <DialogHeader>
          <DialogTitle>Edit profile</DialogTitle>
          <DialogDescription>
            Make changes to your profile here. Click save when you're done.
          </DialogDescription>
        </DialogHeader>
        <form onSubmit={handleSubmit}>
                <div className="relative flex flex-row gap-x-[10px] mb-[20px]">
                  {keywords.map((keyword, index) => (
                    <div key = {index} className="bg-gray-200 px-3 py-1 rounded-full text-sm flex items-center gap-2 dark:bg-gray-700 dark:text-gray-200">
                      <span>{keyword}</span>
                        <Button onClick={(e) =>
                        {
                        e.preventDefault();
                        e.stopPropagation();
                        handleXClick(keyword)
                        }} variant="ghost" size="icon" className="w-[20px] h-2 p-0 hover:bg-gray-300 dark:hover:bg-gray-600">
                          <XIcon className="w-4 h-4" />
                        </Button>
                     </div>
                  ))}
                                  </div>
                <div className="relative">
                  <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
                  <Input 
                    type="text"
                    value={text}
                    onChange={handleInputChange}
                    onKeyDown={handleKeyDown}
                    placeholder="Tags" className="pl-8" />
                </div>
                <div className="relative flex flex-row gap-x-[10px] mt-[15px] mb-[20px]">
                  {sources.map((source, index) => (
                    <div key = {index} className="bg-gray-200 px-3 py-1 rounded-full text-sm flex items-center gap-2 dark:bg-gray-700 dark:text-gray-200">
                      <span>{source}</span>
                        <Button onClick={(e) =>
                        {
                        e.preventDefault();
                        e.stopPropagation();
                        handleXSourceClick(source)
                        }} variant="ghost" size="icon" className="w-[20px] h-5 p-0 hover:bg-gray-300 dark:hover:bg-gray-600">
                          <XIcon className="w-4 h-4" />
                        </Button>
                     </div>
                  ))}
                </div>
                <div className="relative mt-3">
                  <Select onValueChange={handleValueChange}>
                    <SelectTrigger className="w-[170px]">
                      <SelectValue placeholder="Select a source" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectGroup>
                        <SelectLabel>Sources</SelectLabel>
                        <SelectItem value="facebook">Facebook</SelectItem>
                        <SelectItem value="instagram">Instagram</SelectItem>
                        <SelectItem value="news">News</SelectItem> 
                      </SelectGroup>
                    </SelectContent>
                  </Select>
                </div>
              <div className="relative mt-3">
                  <Select onValueChange={handleFrequenctChange}>
                    <SelectTrigger className="w-[200px]">
                      <SelectValue placeholder="Select a frequency" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectGroup>
                        <SelectLabel>Sources</SelectLabel>
                        <SelectItem value="every day">chaque jour</SelectItem>
                        <SelectItem value="every week">chaque semaine</SelectItem>
                        <SelectItem value="every month">chaque mois</SelectItem> 
                      </SelectGroup>
                    </SelectContent>
                  </Select>
                </div> 
        <DialogFooter>
          <Button type="submit" disabled={isPending}>{isPending ? (
        <Spinner 
        />
      ) : (
        'Save changes'
      )}</Button>
          
        </DialogFooter>
        </form>
      </DialogContent>
          </Dialog> 
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
                <AlertList items={data} />
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

export default AlertPage;
