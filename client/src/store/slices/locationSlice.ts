import { Coordinates, RecentLocation } from "@/types/Location";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface LocationState {
  name: string | null;
  address: string | null;
  coordinates: Coordinates | null;
  recentLocations: RecentLocation[];
}

const initialState: LocationState = {
  name: null,
  address: null,
  coordinates: null,
  recentLocations: [],
};

const locationSlice = createSlice({
  name: "location",
  initialState,
  reducers: {
    setUserLocation: (
      state,
      action: PayloadAction<{
        name: string;
        address: string;
        coordinates: Coordinates;
      }>,
    ) => {
      state.name = action.payload.name;
      state.address = action.payload.address;
      state.coordinates = action.payload.coordinates;
    },
    resetLocation: (state) => {
      state.name = null;
      state.address = null;
      state.coordinates = null;
    },
    removeRecentLocation: (state, action: PayloadAction<Coordinates>) => {
      state.recentLocations = state.recentLocations.filter(
        (loc) =>
          loc.coordinates.latitude !== action.payload.latitude ||
          loc.coordinates.longitude !== action.payload.longitude,
      );
    },
    addRecentLocation: (state, action: PayloadAction<RecentLocation>) => {
      const locationExists = state.recentLocations.some(
        (loc) =>
          loc.coordinates.latitude === action.payload.coordinates.latitude &&
          loc.coordinates.longitude === action.payload.coordinates.longitude,
      );
      if (!locationExists) {
        state.recentLocations = [
          action.payload,
          ...state.recentLocations,
        ].slice(0, 2);
      }
    },
  },
});

export const {
  setUserLocation,
  resetLocation,
  addRecentLocation,
  removeRecentLocation,
} = locationSlice.actions;
export default locationSlice.reducer;
