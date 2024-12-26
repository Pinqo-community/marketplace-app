import BasePopup from "@/layouts/BasePopup";
import Loader from "@/shared/Loader";
import { BasePopupProps } from "@/types/BasePopup";
import { Coordinates, Feature, Suggestion } from "@/types/Location";
import classNames from "classnames";
import { AnimatePresence, motion } from "framer-motion";
import {
  Check,
  CircleHelp,
  Locate,
  LocateOff,
  MapPin,
  Search,
} from "lucide-react";
import { useState } from "react";
import styles from "./LocationPopup.module.scss";
import SuggestionList from "./SuggestionList";

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

  /* -------------------------------------------------------------------------- */
  /*                               Handle Address                               */
  /* -------------------------------------------------------------------------- */
  const [isValidAddress, setIsValidAddress] = useState(false);
  const [suggestions, setSuggestions] = useState<Suggestion[]>([]);
  const [coordinates, setCoordinates] = useState<Coordinates | null>(null);
  const [selectedAddress, setSelectedAddress] = useState(null);

  const handleAddressChange = async (
    e: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const value = e.target.value;
    setAddress(value);
    setIsValidAddress(false);
    setSelectedAddress(null);
    setCoordinates(null);

    if (value.length < 3) {
      setSuggestions([]);
      return;
    }

    try {
      const response = await fetch(
        `https://api-adresse.data.gouv.fr/search/?q=${encodeURIComponent(
          value,
        )}&limit=5&autocomplete=1`,
      );
      const data = await response.json();

      const formattedSuggestions = data.features.map((feature: Feature) => ({
        label: feature.properties.label,
        coordinates: feature.geometry.coordinates,
        context: feature.properties.context,
        postcode: feature.properties.postcode,
        city: feature.properties.city,
      }));

      setSuggestions(formattedSuggestions);
    } catch {
      console.error("Erreur lors de la recherche d'adresses");
    } finally {
      setIsLoading(false);
    }
  };

  const handleSelectAddress = (suggestion: Suggestion) => {
    setAddress(suggestion.label);
    setCoordinates({
      latitude: suggestion.coordinates[1],
      longitude: suggestion.coordinates[0],
    });
    setSuggestions([]);
    setIsValidAddress(true);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!isValidAddress) {
      console.error(
        "Veuillez sélectionner une adresse dans la liste des suggestions.",
      );

      return;
    }

    console.log("Adresse validée:", address, coordinates);
  };

  /* ------------------------------------ x ----------------------------------- */

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
          {/* <Search size={20} className={styles.icon} /> */}

          <AnimatePresence mode="wait">
            {address === "" ? (
              <motion.div
                key="search-icon"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }}
                transition={{ duration: 0.2 }}
              >
                <Search size={20} color="#1ab66d" className={styles.icon} />
              </motion.div>
            ) : isValidAddress ? (
              <motion.div
                key="check-icon"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }}
                transition={{ duration: 0.2 }}
              >
                <Check size={20} color="#1ab66d" className={styles.icon} />
              </motion.div>
            ) : (
              <motion.div
                key="help-icon"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }}
                transition={{ duration: 0.2 }}
              >
                <CircleHelp size={20} color="#ff008f" className={styles.icon} />
              </motion.div>
            )}
          </AnimatePresence>
          <form onSubmit={handleSubmit}>
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
          </form>
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
