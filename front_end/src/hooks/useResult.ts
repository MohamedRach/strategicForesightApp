import { atom, useAtom } from "jotai";
import { Result } from "../api/search.api";



type config = {
  selected: Result["id"] | null
}

const configAtom = atom<config>({
  selected: null
})

export function useResult() {
  return useAtom(configAtom)
}
