import { Coordinates } from "./Location";

// Utilitaires pour valider les types (évite les erreurs avec localStorage)
export const isCoordinates = (data: any): data is Coordinates => {
  return (
    typeof data.latitude === "number" && typeof data.longitude === "number"
  );
};

export const isUserLocation = (
  data: any,
): data is { name: string; address: string; coordinates: Coordinates } => {
  return (
    typeof data.name === "string" &&
    typeof data.address === "string" &&
    (data.coordinates === null || isCoordinates(data.coordinates))
  );
};
