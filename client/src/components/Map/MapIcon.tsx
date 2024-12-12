import pinIcon from "@/assets/icons/pin.svg";
import L from "leaflet";

export const mapIcon = new L.Icon({
  iconUrl: pinIcon,
  iconSize: [50, 50],
  iconAnchor: [14, 0],
});
