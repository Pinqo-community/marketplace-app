import { NominatimResponse } from "@/types/Location";

export const parseAddress = (label: string) => {
  const match = label.match(/(\d{5})\s+(.+)$/);
  const addressWithoutLastWord = label.split(" ").slice(0, -1).join(" ");
  return {
    name: match ? match[2] : label.split(",")[0],
    address: addressWithoutLastWord,
  };
};

// Utilitaire pour parser l'adresse
export const parseGeoAddress = (data: NominatimResponse) => {
  const street = data.address?.road;
  const city =
    data.address?.city || data.address?.town || data.address?.village;
  const postcode = data.address?.postcode;

  const geoAddress = `${street} ${postcode} ${city}`;

  const regex = /(\d{5})\s+(.+)$/;
  const match = geoAddress.match(regex);

  const addressWithoutLastWord = geoAddress.split(" ").slice(0, -1).join(" ");

  return {
    geoAddress,
    name: match ? match[2] : geoAddress.split(",")[0],
    addressWithoutLastWord,
  };
};
