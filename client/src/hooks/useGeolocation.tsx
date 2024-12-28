import {
  addRecentLocation,
  setUserLocation,
} from "@/store/slices/locationSlice";
import { Coordinates } from "@/types/Location";
import { useState } from "react";
import { useDispatch } from "react-redux";

export const useGeolocation = () => {
  const [coordinates, setCoordinates] = useState<Coordinates | null>(null);
  const [address, setAddress] = useState<string>("");
  const [isLoading, setIsLoading] = useState(false);
  const [hasError, setHasError] = useState(false);

  const dispatch = useDispatch();

  const locate = () => {
    // Vérifie si la géolocalisation est disponible
    if (!navigator.geolocation) {
      setHasError(true);
      return;
    }
    setAddress("");
    setIsLoading(true);
    setHasError(false);

    navigator.geolocation.getCurrentPosition(
      async (position) => {
        try {
          const { latitude, longitude } = position.coords;

          // Mise à jour des coordonnées
          setCoordinates({ latitude, longitude });

          // Récupère l'adresse depuis OpenStreetMap
          const response = await fetch(
            `https://nominatim.openstreetmap.org/reverse?format=json&lat=${latitude}&lon=${longitude}`,
          );
          const data = await response.json();

          // Extraction des informations d'adresse
          const street = data.address?.road;
          const city =
            data.address?.city || data.address?.town || data.address?.village;
          const postcode = data.address?.postcode;

          // Combine l'adresse
          const geoAddress = `${street} ${postcode} ${city}`;

          // Regex pour extraire le code postal et la ville
          const regex = /(\d{5})\s+(.+)$/;
          const match = geoAddress.match(regex);

          // Retire le dernier mot de l'adresse
          const addressWithoutLastWord = geoAddress
            .split(" ")
            .slice(0, -1)
            .join(" ");

          const location = {
            name: match ? match[2] : geoAddress.split(",")[0],
            address: addressWithoutLastWord,
            coordinates: { latitude, longitude },
          };

          setAddress(geoAddress);

          // Mise à jour et ajout à la liste des récentes
          dispatch(setUserLocation(location));
          dispatch(addRecentLocation(location));
        } catch (error) {
          console.error("Erreur lors de la récupération de l'adresse :", error);
          setHasError(true);
        } finally {
          setIsLoading(false);
        }
      },
      () => {
        setHasError(true);
        setIsLoading(false);
      },
    );
  };

  return { coordinates, address, isLoading, hasError, locate };
};
