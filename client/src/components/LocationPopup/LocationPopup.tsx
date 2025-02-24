import { itemVariants } from "@/animations/animations";
import { useGeolocation } from "@/hooks/useGeolocation";
import { useSuggestions } from "@/hooks/useSuggestions";
import BasePopup from "@/layouts/BasePopup";
import Loader from "@/shared/Loader";
import { RootState } from "@/store";
import {
  addRecentLocation,
  openLocationPopup,
  removeRecentLocation,
  setUserLocation,
} from "@/store/slices/locationSlice";
import {
  LocationPopupProps,
  RecentLocation,
  Suggestion,
} from "@/types/Location";
import { isUserLocation } from "@/types/typeValidators";
import { loadFromLocalStorage } from "@/utils/localStorageUtils";
import { parseAddress } from "@/utils/locationUtils";
import classNames from "classnames/bind";
import { AnimatePresence, motion } from "framer-motion";
import { MapPin, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { LocationButton } from "../Button/Buttons";
import styles from "./LocationPopup.module.scss";
import SearchIcon from "./SearchIcon";
import SuggestionList from "./SuggestionList";

const LocationPopup = ({ isOpen, onClose }: LocationPopupProps) => {
  const [address, setAddress] = useState("");
  const [isValidAddress, setIsValidAddress] = useState(false);
  const [showWarning, setShowWarning] = useState(false);
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
  const cx = classNames.bind(styles);

  /* -------------------------------------------------------------------------- */
  /*                            Gestion des suggestions                         */
  /* -------------------------------------------------------------------------- */

  const handleSelectAddress = (suggestion: Suggestion) => {
    setAddress(suggestion.label);
    clearSuggestions();
    setIsValidAddress(true);
    setShowWarning(false);

    const location = {
      ...parseAddress(suggestion.label),
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
  /*                     Gestion de la fermeture de la popup                    */
  /* -------------------------------------------------------------------------- */

  const handleClose = () => {
    if (!isValidAddress) {
      setShowWarning(true);
    } else {
      onClose();
    }
  };

  useEffect(() => {
    // Vérifie dans le localStorage si une localisation est enregistrée
    const savedLocation = localStorage.getItem("userLocation");
    if (!savedLocation) {
      dispatch(openLocationPopup());
    }
  }, []);

  /* -------------------------------------------------------------------------- */
  /*                  Récupère l'adresse depuis le localStorage                 */
  /* -------------------------------------------------------------------------- */

  useEffect(() => {
    if (coordinates && geoAddress) {
      setAddress(geoAddress);
      setIsValidAddress(true);
      setShowWarning(false);
    } else {
      const userLocation = loadFromLocalStorage("userLocation", isUserLocation);
      if (userLocation) {
        setAddress(`${userLocation.address} ${userLocation.name}`);
        setIsValidAddress(true);
      }
    }
  }, [coordinates, geoAddress]);

  /* -------------------------------------------------------------------------- */
  /*                  Gestion de la sélection d'une localisation récente        */
  /* -------------------------------------------------------------------------- */
  const handleSelectRecentLocation = (location: RecentLocation) => {
    setAddress(`${location.address} ${location.name}`);
    setIsValidAddress(true);

    // Met à jour la localisation actuelle sans doublon
    dispatch(setUserLocation(location));
    onClose();
  };

  /* -------------------------------------------------------------------------- */
  /*                                   Render                                   */
  /* -------------------------------------------------------------------------- */

  return (
    <BasePopup
      isOpen={isOpen}
      onClose={handleClose}
      title="Choisissez votre localisation"
    >
      <AnimatePresence mode="wait">
        {isLoading && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className={styles.loading}
            data-testid="location-loading"
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
            data-testid="address-input"
            data-valid={isValidAddress}
            type="text"
            value={address}
            onChange={handleAddressChange}
            placeholder="Entrez votre adresse, code postal, ville..."
            className={cx("searchInput", {
              valid: isValidAddress,
              invalid: (!isValidAddress && address !== "") || showWarning,
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
        <LocationButton locate={locate} hasError={hasError} />

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
                  data-testid="recent-location"
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
                    data-testid="remove-location"
                  >
                    <Trash2 size={16} />
                  </div>
                </motion.button>
              ))
            ) : (
              <p
                className={styles.noLocations}
                data-testid="no-recent-locations"
              >
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
