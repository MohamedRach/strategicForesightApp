import { atom, useAtom } from "jotai";

import { results, Result } from "../data/data";


type config = {
  selected: Result["id"] | null
}

const configAtom = atom<config>({
  selected: results[0].id
})

export function useResult() {
  return useAtom(configAtom)
}
