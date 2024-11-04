import L from "leaflet";
import pinIcon from "../../assets/icons/pin.svg";

export const mapIcon = new L.Icon({
  iconUrl: pinIcon,
  iconSize: [50, 50],
  iconAnchor: [14, 0],
});
