import './App.css'
import { ResultPage } from './components/result'
import { results } from './data/data'

function getCookie(cname: string) {
  const name = cname + "=";
  const ca = document.cookie.split(';');
  for(let i = 0; i < ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

/*
 * the main component
 * */
function App() {
  const layout = getCookie("react-resizable-panels:layout")
  const collapsed = getCookie("react-resizable-panels:collapsed")
  
  const defaultLayout = layout ? JSON.parse(layout) : undefined
  const defaultCollapsed = collapsed ? JSON.parse(collapsed) : undefined
  
  return (
    <>
      <div className="hidden flex-col md:flex">
        <ResultPage
          results={results}
          defaultLayout={defaultLayout}
          defaultCollapsed={defaultCollapsed}
          navCollapsedSize={4}
        />
      </div>
    </>
  )  
}

export default App
