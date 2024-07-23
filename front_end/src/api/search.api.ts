import { QueryKey, useMutation, useQuery} from "@tanstack/react-query"


export type Result = {
  id: string;
  username: string;
  source: string;
  caption: string;
  img: string;
  likes: string;
  keyword: string;
  date: Date
}

export type Query = {
  keywords: string[]
  sources: string[]
}

export type Search = {
  id: string
  keywords: string[]
  sources: string[]
  createdAt: string,
  updatedAt: string
}
const searchAPI = async (searchData: Query) : Promise<Result[]> => {
  console.log(searchData)
  const response = await fetch('http://localhost:8080/search', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },    
    body: JSON.stringify(searchData),
  });

  
  return response.json();
};

const fetchAllSearches = async () => {
  const response = await fetch('http://localhost:8080/search');
  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  return response.json();
};

const fetchResultsById = async (id: string): Promise<Result[]> => {
  const response = await fetch(`http://localhost:8080/search/${id}`);
  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  return response.json();
};

export const useGetResultsById = (id: string) => {
  return useQuery<Result[], Error>({
    queryKey: ['results', id],
    queryFn: () => fetchResultsById(id),
  });
};

export const useGetAllSearches = () => {
  return useQuery({queryKey:['searches'], queryFn:fetchAllSearches});
};

type Body = {
  url : string
}
const getImage = async (img: Body | undefined) : Promise<string> => {
    const response = await fetch('http://localhost:8080/download-image', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(img),
    });

    if (!response.ok) {
        throw new Error(`Error downloading image: ${response.statusText}`);
    }

    const dataUrl = await response.text();
    return dataUrl;  
};

export const useImage = () => {
  const mutation = useMutation<string, Error, Body>({
    mutationFn: getImage,
    onSuccess: (data) => {
      console.log(data)
    },
    onError: (error: Error) => {
      console.log(error)
    }
  })
  return mutation

}

export const useSearch = () => {
  const mutation = useMutation<Result[], Error, Query>({
    mutationFn: searchAPI,
    onSuccess: (data) => {
      console.log(data)
    },
    onError: (error: Error) => {
      // Handle error here
      console.error("An error occurred:", error);
    },
  });

  return mutation;
}
