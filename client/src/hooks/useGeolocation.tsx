import { Coordinates } from "@/types/Location";
import { useState } from "react";

export const useGeolocation = () => {
  const [coordinates, setCoordinates] = useState<Coordinates | null>(null);
  const [address, setAddress] = useState<string>("");
  const [isLoading, setIsLoading] = useState(false);
  const [hasError, setHasError] = useState(false);

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

          // Mise à jour de l'adresse
          setAddress(`${street} ${postcode} ${city}`);
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
