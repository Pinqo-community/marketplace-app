import {
  Coordinates,
  LocationStoreState,
  RecentLocation,
} from "@/types/Location";
import { isUserLocation } from "@/types/typeValidators";
import {
  loadFromLocalStorage,
  removeFromLocalStorage,
  saveToLocalStorage,
} from "@/utils/localStorageUtils";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";

// Chargement des localisations depuis localStorage
const loadRecentLocations = (): RecentLocation[] => {
  return (
    loadFromLocalStorage<RecentLocation[]>("recentLocations", Array.isArray) ||
    []
  );
};

const userLocation = loadFromLocalStorage("userLocation", isUserLocation);

const MAX_RECENT_LOCATIONS = 2;

const initialState: LocationStoreState = {
  name: userLocation?.name || null,
  address: userLocation?.address || null,
  coordinates: userLocation?.coordinates || null,
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
      saveToLocalStorage("userLocation", {
        name: action.payload.name,
        address: action.payload.address,
        coordinates: action.payload.coordinates,
      });
    },
    resetLocation: (state) => {
      state.name = null;
      state.address = null;
      state.coordinates = null;

      // Supprime les données du cache
      removeFromLocalStorage("userLocation");
    },
    removeRecentLocation: (state, action: PayloadAction<Coordinates>) => {
      state.recentLocations = state.recentLocations.filter(
        (loc) =>
          loc.coordinates.latitude !== action.payload.latitude ||
          loc.coordinates.longitude !== action.payload.longitude,
      );

      // Met à jour le cache
      saveToLocalStorage("recentLocations", state.recentLocations);
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
        ].slice(0, MAX_RECENT_LOCATIONS);

        // Met à jour le cache
        saveToLocalStorage("recentLocations", state.recentLocations);
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
