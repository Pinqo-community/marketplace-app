import { useGeolocation } from "@/hooks/useGeolocation";
import BasePopup from "@/layouts/BasePopup";
import Loader from "@/shared/Loader";
import { BasePopupProps } from "@/types/BasePopup";
import { Coordinates, Suggestion } from "@/types/Location";
import classNames from "classnames";
import { AnimatePresence, motion } from "framer-motion";
import { Locate, LocateOff, MapPin } from "lucide-react";
import { useEffect, useState } from "react";
import { useSuggestions } from "@/hooks/useSuggestions";
import styles from "./LocationPopup.module.scss";
import SearchIcon from "./SearchIcon";
import SuggestionList from "./SuggestionList";
import { setUserLocation } from "@/store/slices/locationSlice";
import { useDispatch } from "react-redux";

const RECENT_LOCATIONS = [
  { name: "Paris 11e", address: "Paris, 11ème arrondissement" },
  { name: "Lyon", address: "Lyon, Rhône-Alpes" },
];

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

  const handleSelectAddress = (suggestion: Suggestion) => {
    setAddress(suggestion.label);
    clearSuggestions();
    setIsValidAddress(true);
    const location: { address: string; coordinates: Coordinates } = {
      address: suggestion.label,
      coordinates: {
        latitude: suggestion.coordinates[1],
        longitude: suggestion.coordinates[0],
      },
    };
    dispatch(setUserLocation(location));
  };

  const handleAddressChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setAddress(value);
    setIsValidAddress(false);
    fetchSuggestions(value);
  };

  useEffect(() => {
    if (geoAddress) {
      console.log(geoAddress);

      setAddress(geoAddress);
      setIsValidAddress(true);

      const location: { address: string; coordinates: Coordinates } = {
        address: geoAddress,
        coordinates: {
          latitude: coordinates?.latitude || 0,
          longitude: coordinates?.longitude || 0,
        },
      };

      dispatch(setUserLocation(location));
    }
  }, [geoAddress, coordinates, dispatch]);

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
          {RECENT_LOCATIONS.map((location) => (
            <button key={location.name} className={styles.locationButton}>
              <div className={styles.locationContent}>
                <MapPin className={styles.icon} />
                <div className={styles.locationInfo}>
                  <h4 className={styles.locationName}>{location.name}</h4>
                  <p className={styles.locationAddress}>{location.address}</p>
                </div>
              </div>
            </button>
          ))}
        </div>
      </div>
    </BasePopup>
  );
};

export default LocationPopup;
