import BasePopup from "@/layouts/BasePopup";
import Loader from "@/shared/Loader";
import { BasePopupProps } from "@/types/BasePopup";
import classNames from "classnames";
import { AnimatePresence, motion } from "framer-motion";
import { Locate, LocateOff, MapPin, Search } from "lucide-react";
import { useState } from "react";
import styles from "./LocationPopup.module.scss";

const LocationPopup = ({ isOpen, onClose }: BasePopupProps) => {
  const locations = [
    { name: "Paris 11e", address: "Paris, 11ème arrondissement" },
    { name: "Lyon", address: "Lyon, Rhône-Alpes" },
  ];

  const [address, setAddress] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [hasError, setHasError] = useState(false);

  const handleLocate = () => {
    if (!navigator.geolocation) {
      setHasError(true);
      return;
    }
    setIsLoading(true);
    setHasError(false);

    navigator.geolocation.getCurrentPosition(
      async (position) => {
        const { latitude, longitude } = position.coords;

        try {
          const response = await fetch(
            `https://nominatim.openstreetmap.org/reverse?format=json&lat=${latitude}&lon=${longitude}`,
          );
          const data = await response.json();

          const city =
            data.address?.city ||
            data.address?.town ||
            data.address?.village ||
            "Ville introuvable";
          const postcode = data.address?.postcode || "Code postal introuvable";
          const street = data.address?.road || "Rue introuvable";

          setAddress(`${street} - ${city}, ${postcode}`);
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

  return (
    <BasePopup
      isOpen={isOpen}
      onClose={onClose}
      title="Choisissez votre localisation"
    >
      <AnimatePresence>
        {isLoading && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className={styles.loading}
          >
            <Loader />
            <motion.p
              initial={{ opacity: 0, y: -10 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.3 }}
              className={styles.text}
            >
              Recherche de votre localisation...
            </motion.p>
          </motion.div>
        )}
      </AnimatePresence>
      {/* Search Input */}
      <div className={styles.searchContainer}>
        <div className={styles.searchWrapper}>
          <Search size={20} className={styles.icon} />
          <input
            type="text"
            value={address}
            onChange={(e) => setAddress(e.target.value)}
            placeholder="Entrez votre adresse, code postal, ville..."
            className={styles.searchInput}
          />
        </div>
      </div>

      {/* Location Options */}
      <div className={styles.locationOptions}>
        <motion.button
          className={classNames(styles.automaticLocationButton, {
            [styles.error]: hasError,
          })}
          onClick={handleLocate}
        >
          <motion.div
            initial={{ opacity: 0, scale: 0 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ delay: 0.2 }}
            className={styles.iconContainer}
          >
            {hasError ? (
              <LocateOff className={styles.icon} />
            ) : (
              <Locate className={styles.icon} />
            )}
          </motion.div>
          <div className={styles.textContainer}>
            <motion.h4 className={styles.title}>
              {hasError
                ? "Impossible de détecter votre position"
                : "Détecter ma position"}
            </motion.h4>
            <p className={styles.subtitle}>
              {hasError
                ? "Veuillez utiliser une adresse ou un code postal"
                : "Utilisez la localisation de votre appareil"}
            </p>
          </div>
        </motion.button>

        {/* Recent Locations */}
        <div className={styles.recentLocations}>
          <h5 className={styles.title}>Localisations récentes</h5>
          {locations.length > 0 ? (
            locations.map((location) => (
              <button key={location.name} className={styles.locationButton}>
                <div className={styles.locationContent}>
                  <MapPin className={styles.icon} />
                  <div className={styles.locationInfo}>
                    <h4 className={styles.locationName}>{location.name}</h4>
                    <p className={styles.locationAddress}>{location.address}</p>
                  </div>
                </div>
              </button>
            ))
          ) : (
            <p className={styles.noLocations}>
              Aucune localisation récente disponible.
            </p>
          )}
        </div>
      </div>
    </BasePopup>
  );
};

export default LocationPopup;
