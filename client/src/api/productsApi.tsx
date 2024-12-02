import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import tomate from "../assets/images/tomate.png";

const products = [
  {
    image:
      "https://plus.unsplash.com/premium_photo-1663957861996-8093b48a22e6?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8aG9uZXl8ZW58MHx8MHx8fDA%3D",
    name: "Miel de fleurs sauvages",
    rating: 4.7,
    price: 12.99,
    previousPrice: 14.99,
    stock: 7,
  },

  {
    image:
      "https://images.unsplash.com/photo-1542820191-bdc08051351b?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fE9yZ2FuaWMlMjBhcHBsZXxlbnwwfHwwfHx8MA%3D%3D",
    name: "Pomme bio du verger",
    rating: 4.2,
    price: 1.2,
    previousPrice: null,
    stock: 51,
  },

  {
    image:
      "https://plus.unsplash.com/premium_photo-1668616815449-b61c3f4d4f44?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MjV8fG9saXZlJTIwb2lsfGVufDB8fDB8fHww",
    name: "Huile d'olive extra vierge",
    rating: 4.8,
    price: 15.49,
    previousPrice: 16.99,
    stock: 27,
  },
  {
    image:
      "https://img-3.journaldesfemmes.fr/ZfmzxO5Kyg0e3j1URh4V8Mf3slc=/1500x/smart/097777a79f144a048f7008573f8584d5/ccmcms-jdf/27424516.jpg",
    name: "Pomme de terre bio",
    rating: 4.7,
    price: 3.99,
    previousPrice: null,
    stock: 32,
  },

  {
    image:
      "https://images.unsplash.com/photo-1513088222195-4388e0a0c553?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTR8fGphbXxlbnwwfHwwfHx8MA%3D%3D",
    name: "Confiture de fraise artisanale",
    rating: 4.3,
    price: 6.5,
    previousPrice: null,
    stock: 15,
  },
  {
    image:
      "https://images.unsplash.com/photo-1522249341405-3871994ac062?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTl8fERhcmslMjBjaG9jb2xhdGV8ZW58MHx8MHx8fDA%3D",
    name: "Chocolat noir bio 80%",
    rating: 4.4,
    price: 3.5,
    previousPrice: 4.0,
    stock: 18,
  },
  {
    image: tomate,
    name: "Tomate Fandango",
    rating: 4.7,
    price: 2.9,
    previousPrice: 3.35,
    stock: 72,
  },
  {
    image:
      "https://plus.unsplash.com/premium_photo-1700767180790-f24ec173ba2e?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTEzfHxXaG9sZW1lYWwlMjBicmVhZHxlbnwwfHwwfHx8MA%3D%3D",
    name: "Pain complet aux céréales",
    rating: 4.5,
    price: 2.99,
    previousPrice: 3.5,
    stock: 3,
  },
];

export const productsApi = createApi({
  reducerPath: "productsApi",
  baseQuery: fetchBaseQuery({ baseUrl: "/" }),
  endpoints: (builder) => ({
    getProducts: builder.query({
      queryFn: () => ({ data: products }),
    }),
  }),
});

export const { useGetProductsQuery } = productsApi;
