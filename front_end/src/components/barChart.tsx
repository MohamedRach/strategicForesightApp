import { Bar, BarChart, CartesianGrid, XAxis } from "recharts"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "./ui/card"
import { ChartConfig, ChartContainer, ChartTooltip, ChartTooltipContent } from "./ui/chart"
import { Result } from "../api/search.api"
import { Spinner } from "./Spinner"


const chartConfig = {
  nbrofposts: {
    label: "Number of posts",
    color: "hsl(var(--chart-1))",
  },
  
} satisfies ChartConfig
interface BarCharProps{
  items: Result[] | undefined
  isIdle: boolean
  isLoading: boolean
}
interface OutputObject {
  user: string;
  nbrofposts: number;
}
function countSourceAppearances(inputArray: Result[] | undefined): OutputObject[] {
  const sourceCountMap: { [key: string]: number } = {};

  if(inputArray) {
    inputArray.forEach(item => {
      if (sourceCountMap[item.username]) {
        sourceCountMap[item.username]++;
      } else {
        sourceCountMap[item.username] = 1;
      }
    });
  }
  const outputArray: OutputObject[] = Object.keys(sourceCountMap).map(source => ({
    user: source,
    nbrofposts: sourceCountMap[source],
  }));

  return outputArray;
}
export function BarChartComponent({items, isIdle, isLoading}: BarCharProps) {
  
  const data: OutputObject[] = countSourceAppearances(items)
  console.log(data)
  return (
    <Card>
      <CardHeader>
        <CardTitle>nombre de postes publiés par chaque utilisateur</CardTitle>
        <CardDescription></CardDescription>
      </CardHeader>
      <CardContent>
        {data && data.length > 0 ? (<ChartContainer config={chartConfig} className="h-[300px] w-[1500px]">
          <BarChart accessibilityLayer data={data}>
            <CartesianGrid vertical={false} />
            <XAxis
              dataKey="user"
              tickLine={false}
              tickMargin={10}
              axisLine={false}
              tickFormatter={(value) => value}
            />
            <ChartTooltip
              cursor={false}
              content={<ChartTooltipContent indicator="dashed" />}
            />
            <Bar dataKey="nbrofposts" fill="var(--color-desktop)" radius={4} />
          </BarChart>
        </ChartContainer>): isLoading ? (
          <div className="flex justify-center items-center h-64 text-gray-500">
                <Spinner>cela peut prendre un certain temps, veuillez patienter</Spinner>
          </div> 
        ): isIdle ? (
           <div className="flex justify-center items-center h-64 text-gray-500">
                <Spinner>cela peut prendre un certain temps, veuillez patienter</Spinner>
          </div> 
        ) : (
           <div className="flex justify-center items-center h-64 text-gray-500">
                aucune donnée à afficher
          </div> 
        )
        } 
      </CardContent>
    </Card>
  )
}

