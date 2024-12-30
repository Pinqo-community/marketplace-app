import {
  addRecentLocation,
  setUserLocation,
} from "@/store/slices/locationSlice";
import { Coordinates, NominatimResponse } from "@/types/Location";
import { parseGeoAddress } from "@/utils/locationUtils";
import { useState } from "react";
import { useDispatch } from "react-redux";

export const useGeolocation = () => {
  const [coordinates, setCoordinates] = useState<Coordinates | null>(null);
  const [address, setAddress] = useState<string>("");
  const [isLoading, setIsLoading] = useState(false);
  const [hasError, setHasError] = useState(false);

  const dispatch = useDispatch();

  const resetState = () => {
    setAddress("");
    setCoordinates(null);
    setIsLoading(false);
    setHasError(false);
  };

  const locate = () => {
    resetState();

    if (!navigator.geolocation) {
      setHasError(true);
      alert("La géolocalisation n'est pas supportée par votre navigateur.");
      return;
    }

    setIsLoading(true);

    navigator.geolocation.getCurrentPosition(
      async (position) => {
        try {
          const { latitude, longitude } = position.coords;

          if (!latitude || !longitude) {
            throw new Error("Coordonnées invalides.");
          }

          setCoordinates({ latitude, longitude });

          const response = await fetch(
            `https://nominatim.openstreetmap.org/reverse?format=json&lat=${latitude}&lon=${longitude}`,
          );

          if (!response.ok) {
            throw new Error("Erreur API Nominatim.");
          }

          const data: NominatimResponse = await response.json();
          const { geoAddress, name, addressWithoutLastWord } =
            parseGeoAddress(data);

          const location = {
            name,
            address: addressWithoutLastWord,
            coordinates: { latitude, longitude },
          };

          setAddress(geoAddress);

          dispatch(setUserLocation(location));
          dispatch(addRecentLocation(location));
        } catch (error) {
          console.error("Erreur lors de la localisation :", error);
          setHasError(true);
        } finally {
          setIsLoading(false);
        }
      },
      (error) => {
        console.error("Erreur de géolocalisation :", error);
        setHasError(true);
        setIsLoading(false);
      },
    );
  };

  return { coordinates, address, isLoading, hasError, locate };
};
