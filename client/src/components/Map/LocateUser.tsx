import { RootState } from "@/store";
import { openLocationPopup } from "@/store/slices/locationSlice";
import { LocateUserProps } from "@/types/types";
import { getBoundsFromCoordinates } from "@/utils/mapUtils";
import L from "leaflet";
import { useCallback, useEffect, useRef, useState } from "react";
import { useMap } from "react-leaflet";
import { useDispatch, useSelector } from "react-redux";
import styles from "./LocateUser.module.scss";

const LocateUser: React.FC<LocateUserProps> = ({ defaultPosition }) => {
  /* -------------------------------------------------------------------------- */
  /*                                  Statement                                 */
  /* -------------------------------------------------------------------------- */

  const map = useMap();
  const [position, setPosition] = useState(false);
  const { coordinates } = useSelector((state: RootState) => state.location);
  const markerRef = useRef<L.Marker | null>(null);
  const dispatch = useDispatch();

  /* -------------------------------------------------------------------------- */
  /*                                  Functions                                 */
  /* -------------------------------------------------------------------------- */

  const addMarker = useCallback(
    (lat: number, lng: number, isActive: boolean) => {
      // Supprime le marqueur existant
      if (markerRef.current) {
        map.removeLayer(markerRef.current);
      }

      // Ajoute un nouveau marqueur
      const markerIcon = new L.DivIcon({
        className: `${styles.userLocationIcon} ${isActive ? styles.active : styles.inactive}`,
      });
      const newMarker = L.marker([lat, lng], { icon: markerIcon }).addTo(map);

      // Mise à jour de la reference du marqueur
      markerRef.current = newMarker;
    },
    [map],
  );

  // Initialise le marqueur par défaut
  useEffect(() => {
    if (defaultPosition) {
      addMarker(defaultPosition.lat, defaultPosition.lng, false);
    }

    return () => {
      // Nettoyage du marqueur lors du démontage du composant
      if (markerRef.current) {
        map.removeLayer(markerRef.current);
      }
    };
  }, [defaultPosition, addMarker, map]);

  // Mise à jour du marqueur utilisateur lors du changement de coordonnés
  useEffect(() => {
    if (coordinates) {
      setPosition(true);
      map.flyTo([coordinates.latitude, coordinates.longitude], 13);
      addMarker(coordinates.latitude, coordinates.longitude, true);
    }
  }, [map, coordinates, addMarker]);

  /* -------------------------------------------------------------------------- */
  /*                              Update Bounds                                 */
  /* -------------------------------------------------------------------------- */

  useEffect(() => {
    if (coordinates) {
      const bounds = getBoundsFromCoordinates(
        coordinates.latitude,
        coordinates.longitude,
        50,
      );
      map.flyTo([coordinates.latitude, coordinates.longitude], 13);
      map.setMaxBounds(bounds);
    }
  }, [coordinates, map]);

  /* -------------------------------------------------------------------------- */
  /*                                   Render                                   */
  /* -------------------------------------------------------------------------- */

  return (
    <div
      className={styles.locateUser}
      onClick={(e) => {
        e.stopPropagation();
        dispatch(openLocationPopup());
      }}
    >
      <svg
        viewBox="0 0 24 24"
        xmlns="http://www.w3.org/2000/svg"
        className={`${position ? styles.active : ""}`}
      >
        <path d="M0 0h24v24H0z" fill="none" />
        <path d="M12 8c-2.21 0-4 1.79-4 4s1.79 4 4 4 4-1.79 4-4-1.79-4-4-4zm8.94 3A8.994 8.994 0 0 0 13 3.06V1h-2v2.06A8.994 8.994 0 0 0 3.06 11H1v2h2.06A8.994 8.994 0 0 0 11 20.94V23h2v-2.06A8.994 8.994 0 0 0 20.94 13H23v-2h-2.06zM12 19c-3.87 0-7-3.13-7-7s3.13-7 7-7 7 3.13 7 7-3.13 7-7 7z" />
      </svg>
    </div>
  );
};

export default LocateUser;
