import { categoriesApi } from "@/api/categoriesApi";
import { productsApi } from "@/api/productsApi";
import { testimonialsApi } from "@/api/testimonialsApi";
import { configureStore } from "@reduxjs/toolkit";
import productsReducer from "./slices/postsSlice";
import locationReducer from "./slices/locationSlice";

const store = configureStore({
  reducer: {
    products: productsReducer,
    location: locationReducer,
    [productsApi.reducerPath]: productsApi.reducer,
    [categoriesApi.reducerPath]: categoriesApi.reducer,
    [testimonialsApi.reducerPath]: testimonialsApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware()
      .concat(productsApi.middleware)
      .concat(categoriesApi.middleware)
      .concat(testimonialsApi.middleware),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
