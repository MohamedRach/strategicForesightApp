import { useMutation, useQuery } from "@tanstack/react-query";

export interface Notification {
  keywords: string[];
  sources: string[];
  frequency: string;
}

export type Alert = {
  id: string;
  keywords: string[];
  sources: string[];
  count: number;
  searchId: number;
};

const AddNotification = async (
  notifData: Notification,
  token: string | undefined
): Promise<Notification> => {
  const response = await fetch("http://localhost:8222/notification", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      ...(token && { Authorization: `Bearer ${token}` }),
    },
    body: JSON.stringify(notifData),
  });

  return response.json();
};

const fetchAllAlerts = async (token: string | undefined) => {
  const response = await fetch("http://localhost:8222/notification/alert", {
    headers: {
      ...(token && { Authorization: `Bearer ${token}` }),
    },
  });
  if (!response.ok) {
    throw new Error("Network response was not ok");
  }
  return response.json();
};

export const useGetAllAlert = (token: string | undefined) => {
  return useQuery({ queryKey: ["alerts"], queryFn: () => fetchAllAlerts(token) });
};

export const useNotification = (token: string | undefined) => {
  return useMutation<Notification, Error, Notification>({
    mutationFn: (notifData: Notification) => AddNotification(notifData, token),
    onSuccess: (data) => {
      console.log(data);
    },
    onError: (error: Error) => {
      console.error("An error occurred:", error);
    },
  });
};

