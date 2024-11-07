import L from "leaflet";
import { useCallback, useEffect, useState } from "react";
import { useMap } from "react-leaflet";
import { LocateUserProps } from "../../types/types";
import styles from "./LocateUser.module.scss";

const LocateUser: React.FC<LocateUserProps> = ({ mapRef, defaultPosition }) => {
  /* -------------------------------------------------------------------------- */
  /*                                  Statement                                 */
  /* -------------------------------------------------------------------------- */

  const map = useMap();
  const [position, setPosition] = useState(false);
  const [defaultMarker, setDefaultMarker] = useState<L.Marker | null>(null);

  /* -------------------------------------------------------------------------- */
  /*                                  Function                                  */
  /* -------------------------------------------------------------------------- */

  /* --------------------------- Marqueur par défaut --------------------------- */

  useEffect(() => {
    const marker = L.marker(defaultPosition, {
      icon: new L.DivIcon({
        className: `${styles.userLocationIcon} ${styles.inactive}`,
      }),
    }).addTo(map);
    setDefaultMarker(marker);

    // Cleanup du marqueur lors du démontage
    return () => {
      marker.remove();
    };
  }, [map, defaultPosition]);

  /* ----------------------------- Géolocalisation ---------------------------- */

  const handleLocation = useCallback(() => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition((position) => {
        const { latitude, longitude } = position.coords;
        setPosition(true);
        map.flyTo([latitude, longitude], 13);

        // Retire le marqueur par défaut et ajouter le marqueur utilisateur
        if (defaultMarker) map.removeLayer(defaultMarker);

        const userLocationIcon = new L.DivIcon({
          className: `${styles.userLocationIcon} ${styles.active}`,
        });
        L.marker([latitude, longitude], { icon: userLocationIcon }).addTo(map);
      });
    }
  }, [map, defaultMarker]);

  /* ------------------------------------ Observer ----------------------------------- */

  // Observer pour voir si la map est visible et lancer la localisation
  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting) {
          handleLocation();
        }
      },
      { threshold: 0.7 }
    );
    const mapElement = mapRef.current;
    if (mapElement) observer.observe(mapElement);

    return () => {
      if (mapElement) observer.unobserve(mapElement);
    };
  }, [mapRef, handleLocation]);

  /* -------------------------------------------------------------------------- */
  /*                                   Render                                   */
  /* -------------------------------------------------------------------------- */

  return (
    <div
      className={styles.locateUser}
      onClick={(e) => {
        e.stopPropagation();
        handleLocation();
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
