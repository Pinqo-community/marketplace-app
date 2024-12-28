import { Coordinates, RecentLocation } from "@/types/Location";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface LocationState {
  name: string | null;
  address: string | null;
  isLocationPopupOpen: boolean;
  coordinates: Coordinates | null;
  recentLocations: RecentLocation[];
}

// Charge les infos de localisation depuis localStorage
export const loadUserLocation = () => {
  const savedLocation = localStorage.getItem("userLocation");
  return savedLocation ? JSON.parse(savedLocation) : null;
};

const loadRecentLocations = (): RecentLocation[] => {
  const savedLocations = localStorage.getItem("recentLocations");
  return savedLocations ? JSON.parse(savedLocations) : [];
};

const initialState: LocationState = {
  name: loadUserLocation()?.name || null,
  address: loadUserLocation()?.address || null,
  coordinates: loadUserLocation()?.coordinates || null,
  recentLocations: loadRecentLocations(),
  isLocationPopupOpen: false,
};

const locationSlice = createSlice({
  name: "location",
  initialState,
  reducers: {
    openLocationPopup: (state) => {
      state.isLocationPopupOpen = true;
    },
    closeLocationPopup: (state) => {
      state.isLocationPopupOpen = false;
    },
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

      // Sauvegarde dans localStorage
      localStorage.setItem(
        "userLocation",
        JSON.stringify({
          name: action.payload.name,
          address: action.payload.address,
          coordinates: action.payload.coordinates,
        }),
      );
    },
    resetLocation: (state) => {
      state.name = null;
      state.address = null;
      state.coordinates = null;

      // Supprime les données du cache
      localStorage.removeItem("userLocation");
    },
    removeRecentLocation: (state, action: PayloadAction<Coordinates>) => {
      state.recentLocations = state.recentLocations.filter(
        (loc) =>
          loc.coordinates.latitude !== action.payload.latitude ||
          loc.coordinates.longitude !== action.payload.longitude,
      );

      // Met à jour le cache
      localStorage.setItem(
        "recentLocations",
        JSON.stringify(state.recentLocations),
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

        // Met à jour le cache
        localStorage.setItem(
          "recentLocations",
          JSON.stringify(state.recentLocations),
        );
      }
    },
  },
});

export const {
  setUserLocation,
  resetLocation,
  addRecentLocation,
  removeRecentLocation,
  openLocationPopup,
  closeLocationPopup,
} = locationSlice.actions;
export default locationSlice.reducer;
