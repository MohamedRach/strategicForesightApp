import { AlertCircle, Archive, Search, User } from "lucide-react"
import { Nav } from "./nav"
import { Separator } from "./ui/separator"
import { Tabs, TabsContent} from "./ui/tabs"
import { Input } from "./ui/input"
import { ResultList } from "./result-list"
import { ResultDisplay } from "./result-display"
import { useResult } from "../hooks/useResult"
import { useEffect, useState } from "react"
import { XIcon } from "lucide-react"
import { Button } from "./ui/button"
import { BarChartComponent } from "./barChart"
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from "./ui/select"
import { Query, Result, useGetResultsById, useSearch } from "../api/search.api"
import { Spinner } from "./Spinner"
import { useLocation } from "react-router-dom"
import { useResultStore } from "../hooks/store"



export function ResultPage() {
  const [result] = useResult()
  const [text, setText] = useState<string>("")
  const [sources, setSources] = useState<string[]>([])
  const [keywords, setKeywords] = useState<string[]>([])

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

  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const id = queryParams.get('id');
  const {results, addResult} = useResultStore()
  const {data: idData, isLoading: idLoading, isError: idIsError} = useGetResultsById(id || "")
  const { mutateAsync, data: searchData,isPending, isError: searchIsError } = useSearch();
  const isError = id ? idIsError : searchIsError;
  const isLoading = id ? idLoading : false
  useEffect(() => {
    if(id && idData && idData.length > 0) {
      addResult(idData)
    } else if (!id && searchData && searchData.length > 0) {
      addResult(searchData)
    }
  }, [idData, addResult, id, searchData])
  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    const query: Query= { keywords, sources };
    mutateAsync(query);
  };
  return (
    <>
    <div className="grid grid-cols-[300px_1fr_1fr] gap-4">
      <div>
        {/* Left panel (Nav) stays the same */}
        <Nav
            
            links={[
              { title: "Recherche", href:"/", label: "", icon: Search, variant: "default" },
              { title: "Alerts", href: "/alerts", label: "", icon: AlertCircle, variant: "ghost" },
              { title: "Historique", href: "/history", label: "", icon: Archive, variant: "ghost" },
              { title: "Profile", href:"/profile", label: "", icon: User, variant: "ghost" }
            ]}
        />
      </div>
        <div>
          <Tabs defaultValue="all">
            <div className="flex items-center px-4 py-2">
              <h1 className="text-xl font-bold">Recherche</h1>
            </div>
            <Separator />
            <div className="bg-background/95 p-4 backdrop-blur supports-[backdrop-filter]:bg-background/60">
              <form onSubmit={handleSearch}>
                <div className="relative flex flex-row gap-x-[10px] mb-[20px]">
                  {keywords.map((keyword, index) => (
                    <div key = {index} className="bg-gray-200 px-3 py-1 rounded-full text-sm flex items-center gap-2 dark:bg-gray-700 dark:text-gray-200">
                      <span>{keyword}</span>
                        <Button onClick={(e) =>
                        {
                        e.preventDefault();
                        e.stopPropagation();
                        handleXClick(keyword)
                        }} variant="ghost" size="icon" className="w-5 h-5 p-0 hover:bg-gray-300 dark:hover:bg-gray-600">
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
                        }} variant="ghost" size="icon" className="w-5 h-5 p-0 hover:bg-gray-300 dark:hover:bg-gray-600">
                          <XIcon className="w-4 h-4" />
                        </Button>
                     </div>
                  ))}
                </div>
                <div className="relative mt-3">
                  <Select onValueChange={handleValueChange}>
                    <SelectTrigger className="w-[770px]">
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
                <div className="relative">
                  <Button type="submit" className="ml-[-630px] mt-3">Rechercher</Button>
                </div>
              </form>
            </div>
            <TabsContent value="all" className="m-0">
              {isError ? (
                <div className="flex justify-center items-center h-64 text-red-500">
                  Error: Something went wrong try again later
                </div>
              ) : results && results.length > 0 ? (
                <ResultList items={results} />
              ) : isLoading ? (
                <div className="flex justify-center items-center h-64 text-gray-500">
                  <Spinner>cela peut prendre un certain temps, veuillez patienter!!</Spinner>
                </div>
              ): isPending ? (
                <div className="flex justify-center items-center h-64 text-gray-500">
                  <Spinner>cela peut prendre un certain temps, veuillez patienter!!</Spinner>
                </div> 
              ): (
                <div className="flex justify-center items-center h-64 text-gray-500">
                  aucune donnée à afficher
                </div>  
              )}            
            </TabsContent>
          </Tabs>
        </div>
        <div>
          <ResultDisplay
             result={results ? results.find((item: Result) => item.id === result.selected) : undefined}
          />
        </div>
      </div>  
      <Separator />
      <div className=" ml-[250px] w-[1500px] grid grid-cols-1">
        <div className="mt-3">
          <BarChartComponent items={results} isLoading={isLoading} isIdle={isPending}/>

        </div>
      </div>
    </>
  );
  }
