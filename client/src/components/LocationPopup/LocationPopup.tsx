import { useGeolocation } from "@/hooks/useGeolocation";
import { useSuggestions } from "@/hooks/useSuggestions";
import BasePopup from "@/layouts/BasePopup";
import Loader from "@/shared/Loader";
import { RootState } from "@/store";
import {
  addRecentLocation,
  removeRecentLocation,
  setUserLocation,
} from "@/store/slices/locationSlice";
import { BasePopupProps } from "@/types/BasePopup";
import { RecentLocation, Suggestion } from "@/types/Location";
import classNames from "classnames";
import { AnimatePresence, motion } from "framer-motion";
import { Locate, LocateOff, MapPin, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import styles from "./LocationPopup.module.scss";
import SearchIcon from "./SearchIcon";
import SuggestionList from "./SuggestionList";

const LocationPopup = ({ isOpen, onClose }: BasePopupProps) => {
  const [address, setAddress] = useState("");
  const [isValidAddress, setIsValidAddress] = useState(false);

  const { suggestions, fetchSuggestions, clearSuggestions } = useSuggestions();
  const {
    coordinates,
    address: geoAddress,
    isLoading,
    hasError,
    locate,
  } = useGeolocation();

  const dispatch = useDispatch();
  const recentLocations = useSelector(
    (state: RootState) => state.location.recentLocations,
  );

  /* -------------------------------------------------------------------------- */
  /*                            Gestion des suggestions                         */
  /* -------------------------------------------------------------------------- */
  const handleSelectAddress = (suggestion: Suggestion) => {
    setAddress(suggestion.label);
    clearSuggestions();
    setIsValidAddress(true);

    // Regex pour extraire la ville
    const match = suggestion.label.match(/(\d{5})\s+(.+)$/);

    // Retire le dernier mot de l'adresse
    const addressWithoutLastWord = suggestion.label
      .split(" ")
      .slice(0, -1)
      .join(" ");

    const location = {
      name: match ? match[2] : suggestion.label.split(",")[0],
      address: addressWithoutLastWord,
      coordinates: {
        latitude: suggestion.coordinates[1],
        longitude: suggestion.coordinates[0],
      },
    };

    dispatch(setUserLocation(location));
    dispatch(addRecentLocation(location));
  };

  const handleAddressChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setAddress(value);
    setIsValidAddress(false);
    fetchSuggestions(value);
  };

  /* -------------------------------------------------------------------------- */
  /*                      Gestion de la géolocalisation automatique             */
  /* -------------------------------------------------------------------------- */

  useEffect(() => {
    if (coordinates && geoAddress) {
      setAddress(geoAddress);
      setIsValidAddress(true);
    }
  }, [coordinates, geoAddress]);

  /* -------------------------------------------------------------------------- */
  /*                  Gestion de la sélection d'une localisation récente        */
  /* -------------------------------------------------------------------------- */
  const handleSelectRecentLocation = (location: RecentLocation) => {
    setAddress(`${location.address} ${location.name}`);
    setIsValidAddress(true);

    // Met à jour la localisation actuelle sans ajouter un doublon
    dispatch(setUserLocation(location));
    onClose(); // Ferme la pop-up
  };

  /* -------------------------------- Animation ------------------------------- */

  const itemVariants = {
    hidden: {
      opacity: 0,
      x: -20,
      scale: 0.8,
    },
    show: {
      opacity: 1,
      x: 0,
      scale: 1,
      transition: {
        type: "spring",
        stiffness: 300,
        damping: 24,
        delay: 0.2,
      },
    },
    exit: {
      opacity: 0,
      x: 20,
      scale: 0.8,
      transition: {
        duration: 0.2,
      },
    },
  };

  /* -------------------------------------------------------------------------- */
  /*                                   Render                                   */
  /* -------------------------------------------------------------------------- */

  return (
    <BasePopup
      isOpen={isOpen}
      onClose={onClose}
      title="Choisissez votre localisation"
    >
      <AnimatePresence mode="wait">
        {isLoading && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className={styles.loading}
          >
            <Loader />
            <p className={styles.text}>Recherche de votre localisation...</p>
          </motion.div>
        )}
      </AnimatePresence>

      <div className={styles.searchContainer}>
        <div className={styles.searchWrapper}>
          <div className={styles.icon}>
            <SearchIcon address={address} isValidAddress={isValidAddress} />
          </div>

          <input
            type="text"
            value={address}
            onChange={handleAddressChange}
            placeholder="Entrez votre adresse, code postal, ville..."
            className={classNames(styles.searchInput, {
              [styles.valid]: isValidAddress,
              [styles.invalid]: !isValidAddress && address !== "",
            })}
          />

          <AnimatePresence>
            {suggestions.length > 0 && (
              <SuggestionList
                suggestions={suggestions}
                onSelect={handleSelectAddress}
              />
            )}
          </AnimatePresence>
        </div>
      </div>

      <div className={styles.locationOptions}>
        <button
          className={classNames(styles.automaticLocationButton, {
            [styles.error]: hasError,
          })}
          onClick={locate}
        >
          <div className={styles.iconContainer}>
            {hasError ? (
              <LocateOff className={styles.icon} />
            ) : (
              <Locate className={styles.icon} />
            )}
          </div>
          <div className={styles.textContainer}>
            <h4 className={styles.title}>
              {hasError
                ? "Impossible de détecter votre position"
                : "Détecter ma position"}
            </h4>
            <p className={styles.subtitle}>
              {hasError
                ? "Veuillez utiliser une adresse ou un code postal"
                : "Utilisez la localisation de votre appareil"}
            </p>
          </div>
        </button>

        <div className={styles.recentLocations}>
          <h5 className={styles.title}>Localisations récentes</h5>

          <AnimatePresence mode="popLayout">
            {recentLocations.length > 0 ? (
              recentLocations.map((location) => (
                <motion.button
                  variants={itemVariants}
                  initial="hidden"
                  animate="show"
                  exit="exit"
                  layout
                  key={location.address}
                  className={styles.locationButton}
                  onClick={() => handleSelectRecentLocation(location)}
                >
                  <div className={styles.locationContent}>
                    <MapPin className={styles.icon} />
                    <div className={styles.locationInfo}>
                      <h4 className={styles.locationName}>{location.name}</h4>
                      <p className={styles.locationAddress}>
                        {location.address}
                      </p>
                    </div>
                  </div>
                  <div
                    onClick={(e) => {
                      e.stopPropagation();
                      dispatch(removeRecentLocation(location.coordinates));
                    }}
                    className={styles.removeButton}
                  >
                    <Trash2 size={16} />
                  </div>
                </motion.button>
              ))
            ) : (
              <p className={styles.noLocations}>
                Aucune localisation récente disponible.
              </p>
            )}
          </AnimatePresence>
        </div>
      </div>
    </BasePopup>
  );
};

export default LocationPopup;
