import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

export type Result = {
  id: string;
  username: string;
  source: string;
  caption: string;
  img: string;
  likes: string;
  keyword: string;
  href: string;
  sentiment: string;
  date: Date;
};

export type Query = {
  keywords: string[];
  sources: string[];
};

export type Search = {
  id: string;
  keywords: string[];
  sources: string[];
  createdAt: string;
  updatedAt: string;
};

const searchAPI = async (searchData: Query, token: string | undefined): Promise<Result[]> => {
  const response = await fetch("http://localhost:8222/search", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      ...(token && { Authorization: `Bearer ${token}` }),
    },
    body: JSON.stringify(searchData),
  });

  return response.json();
};

const fetchAllSearches = async (token: string | undefined) => {
  const response = await fetch("http://localhost:8222/search", {
    headers: {
      ...(token && { Authorization: `Bearer ${token}` }),
    },
  });

  if (!response.ok) {
    throw new Error("Network response was not ok");
  }
  return response.json();
};

const fetchResultsById = async (id: string, token: string | undefined): Promise<Result[]> => {
  const response = await fetch(`http://localhost:8222/search/${id}`, {
    headers: {
      ...(token && { Authorization: `Bearer ${token}` }),
    },
  });

  if (!response.ok) {
    throw new Error("Network response was not ok");
  }
  return response.json();
};

const deleteItem = async (id: string, token: string | undefined): Promise<string> => {
  const response = await fetch(`http://localhost:8222/result/${id}`, {
    method: "DELETE",
    headers: {
      ...(token && { Authorization: `Bearer ${token}` }),
    },
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message);
  }
  return response.text();
};

const deleteSearch = async (id: string, token: string | undefined): Promise<string> => {
  const response = await fetch(`http://localhost:8222/search/${id}`, {
    method: "DELETE",
    headers: {
      ...(token && { Authorization: `Bearer ${token}` }),
    },
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message);
  }
  return response.text();
};

export const useDeleteSearch = (token: string | undefined) => {
  const queryClient = useQueryClient();

  return useMutation<string, Error, string>({
    mutationFn: (id: string) => deleteSearch(id, token),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["searches"] });
      console.log("Search deleted successfully");
    },
    onError: (error: Error) => {
      console.error("An error occurred while deleting the search:", error);
    },
  });
};

export const useDeleteResult = (token: string | undefined) => {
  const queryClient = useQueryClient();

  return useMutation<string, Error, string>({
    mutationFn: (id: string) => deleteItem(id, token),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["results"] });
      console.log("Result deleted successfully");
    },
    onError: (error: Error) => {
      console.error("An error occurred while deleting the result:", error);
    },
  });
};

export const useGetResultsById = (id: string, token: string | undefined) => {
  return useQuery<Result[], Error>({
    queryKey: ["results", id],
    queryFn: () => fetchResultsById(id, token),
  });
};

export const useGetAllSearches = (token: string | undefined) => {
  return useQuery({ queryKey: ["searches"], queryFn: () => fetchAllSearches(token) });
};

type Body = {
  url: string;
};

const getImage = async (img: Body | undefined, token: string | undefined): Promise<string> => {
  const response = await fetch("http://localhost:8080/download-image", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      ...(token && { Authorization: `Bearer ${token}` }),
    },
    body: JSON.stringify(img),
  });

  if (!response.ok) {
    throw new Error(`Error downloading image: ${response.statusText}`);
  }

  const dataUrl = await response.text();
  return dataUrl;
};

export const useImage = (token: string | undefined) => {
  return useMutation<string, Error, Body>({
    mutationFn: (img: Body) => getImage(img, token),
    onSuccess: (data) => {
      console.log(data);
    },
    onError: (error: Error) => {
      console.log(error);
    },
  });
};

export const useSearch = (token: string | undefined) => {
  return useMutation<Result[], Error, Query>({
    mutationFn: (searchData: Query) => searchAPI(searchData, token),
    onSuccess: (data) => {
      console.log(data);
    },
    onError: (error: Error) => {
      console.error("An error occurred:", error);
    },
  });
};

