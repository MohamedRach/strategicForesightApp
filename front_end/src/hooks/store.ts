import {create} from "zustand"
import { Result } from "../api/search.api"


type ResultStore = {
  results: Result[];
  addResult: (results: Result[]) => void;
  removeResult: (res: Result) => void;
}

export const useResultStore = create<ResultStore>((set) =>( {
  results: [],
  addResult: (results: Result[]) => {
    set({results: results})
  },
  removeResult: (res: Result) => {
    set((state) => ({results: state.results.filter(result => result.id !== res.id)}))
  }

})) 
