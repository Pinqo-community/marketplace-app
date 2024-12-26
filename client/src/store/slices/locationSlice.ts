import { Coordinates } from "@/types/Location";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface LocationState {
  address: string | null;
  coordinates: Coordinates | null;
}

const initialState: LocationState = {
  address: null,
  coordinates: null,
};

const locationSlice = createSlice({
  name: "location",
  initialState,
  reducers: {
    setUserLocation: (
      state,
      action: PayloadAction<{ address: string; coordinates: Coordinates }>,
    ) => {
      state.address = action.payload.address;
      state.coordinates = action.payload.coordinates;
    },
    resetLocation: (state) => {
      state.address = null;
      state.coordinates = null;
    },
  },
});

export const { setUserLocation, resetLocation } = locationSlice.actions;
export default locationSlice.reducer;
