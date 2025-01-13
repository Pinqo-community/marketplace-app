import L from "leaflet";

export const getBoundsFromCoordinates = (
  lat: number,
  lng: number,
  radiusKm: number,
) => {
  const earthRadius = 6371;
  const latDelta = (radiusKm / earthRadius) * (180 / Math.PI);
  const lngDelta =
    (radiusKm / (earthRadius * Math.cos((Math.PI * lat) / 180))) *
    (180 / Math.PI);

  return L.latLngBounds(
    [lat - latDelta, lng - lngDelta],
    [lat + latDelta, lng + lngDelta],
  );
};
