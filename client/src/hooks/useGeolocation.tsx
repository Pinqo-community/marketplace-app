import { Coordinates } from "@/types/Location";
import { useState } from "react";

export const useGeolocation = () => {
  const [coordinates, setCoordinates] = useState<Coordinates | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [hasError, setHasError] = useState(false);

  const locate = () => {
    if (!navigator.geolocation) {
      setHasError(true);
      return;
    }

    setIsLoading(true);
    setHasError(false);

    navigator.geolocation.getCurrentPosition(
      async (position) => {
        try {
          const { latitude, longitude } = position.coords;
          setCoordinates({ latitude, longitude });
        } catch {
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

  return { coordinates, isLoading, hasError, locate };
};
