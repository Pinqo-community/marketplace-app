import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

const categories = [
  {
    id: 1,
    name: "Fruits",
    image:
      "https://plus.unsplash.com/premium_photo-1683133445874-1939d22758df?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1yZWxhdGVkfDQzfHx8ZW58MHx8fHx8",
  },
  {
    id: 2,
    name: "Légumes",
    image:
      "https://images.unsplash.com/photo-1590779033100-9f60a05a013d?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
  },
  {
    id: 3,
    name: "Fromages",
    image:
      "https://plus.unsplash.com/premium_photo-1691939610797-aba18030c15f?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8RnJvbWFnZXN8ZW58MHx8MHx8fDA%3D",
  },
  {
    id: 4,
    name: "Viennoiseries",
    image:
      "https://images.unsplash.com/photo-1679812000098-ff557c197028?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NDZ8fFZpZW5ub2lzZXJpZXN8ZW58MHx8MHx8fDA%3D",
  },
  {
    id: 5,
    name: "Pains",
    image:
      "https://plus.unsplash.com/premium_photo-1673111979369-0222c7314b82?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OXx8YnJlYWR8ZW58MHx8MHx8fDA%3D",
  },
  {
    id: 6,
    name: "Boissons",
    image:
      "https://images.unsplash.com/photo-1643094263180-9ca517fe03b2?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NTR8fGp1c3xlbnwwfHwwfHx8MA%3D%3D",
  },
  {
    id: 7,
    name: "Viandes",
    image:
      "https://images.unsplash.com/photo-1690983323540-d6e889c4b107?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NjB8fG1lYXR8ZW58MHx8MHx8fDA%3D",
  },
];
export const categoriesApi = createApi({
  reducerPath: "categoriesApi",
  baseQuery: fetchBaseQuery({ baseUrl: "/" }),
  endpoints: (builder) => ({
    getCategories: builder.query({
      queryFn: () => ({ data: categories }),
    }),
  }),
});

export const { useGetCategoriesQuery } = categoriesApi;
