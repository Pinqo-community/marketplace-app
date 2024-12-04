import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

const testimonials = [
  {
    id: 1,
    name: "Thomas Girard",
    rating: 5,
    text: "Pellentesque eu nibh eget mauris congue mattis mattis nec tellus. Phasellus imperdiet elit eu magna dictum, bibendum cursus velit sodales. Donec sed neque eget",
    avatar: "https://randomuser.me/api/portraits/men/4.jpg",
  },
  {
    id: 2,
    name: "Clara Bellanger",
    rating: 4,
    text: "Pellentesque eu nibh eget mauris congue mattis mattis nec tellus. Phasellus imperdiet elit eu magna dictum, bibendum cursus velit sodales. Donec sed neque eget",
    avatar: "https://randomuser.me/api/portraits/women/36.jpg",
  },
  {
    id: 3,
    name: "Louise Durand",
    rating: 5,
    text: "Pellentesque eu nibh eget mauris congue mattis mattis nec tellus. Phasellus imperdiet elit eu magna dictum, bibendum cursus velit sodales. Donec sed neque eget",
    avatar: "https://randomuser.me/api/portraits/women/72.jpg",
  },
  {
    id: 4,
    name: "Baptiste Perrin",
    rating: 5,
    text: "Pellentesque eu nibh eget mauris congue mattis mattis nec tellus. Phasellus imperdiet elit eu magna dictum, bibendum cursus velit sodales. Donec sed neque eget",
    avatar: "https://randomuser.me/api/portraits/men/72.jpg",
  },
];

export const testimonialsApi = createApi({
  reducerPath: "testimonialsApi",
  baseQuery: fetchBaseQuery({ baseUrl: "/" }),
  endpoints: (builder) => ({
    getTestimonials: builder.query({
      queryFn: () => ({ data: testimonials }),
    }),
  }),
});

export const { useGetTestimonialsQuery } = testimonialsApi;
