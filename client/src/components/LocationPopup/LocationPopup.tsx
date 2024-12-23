import BasePopup from "@/layouts/BasePopup";
import { BasePopupProps } from "@/types/BasePopup";
import { Locate, MapPin, Search } from "lucide-react";
import { useState } from "react";
import styles from "./LocationPopup.module.scss";

const LocationPopup = ({ isOpen, onClose }: BasePopupProps) => {
  const locations = [
    { name: "Paris 11e", address: "Paris, 11ème arrondissement" },
    { name: "Lyon", address: "Lyon, Rhône-Alpes" },
  ];

  const [searchQuery, setSearchQuery] = useState("");

  return (
    <BasePopup
      isOpen={isOpen}
      onClose={onClose}
      title="Choisissez votre localisation"
    >
      {/* Search Input */}
      <div className={styles.searchContainer}>
        <div className={styles.searchWrapper}>
          <Search size={20} className={styles.icon} />
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="Entrez votre adresse, code postal, ville..."
            className={styles.searchInput}
          />
        </div>
      </div>

      {/* Location Options */}
      <div className={styles.locationOptions}>
        <button className={styles.automaticLocationButton}>
          <div className={styles.iconContainer}>
            <Locate className={styles.icon} />
          </div>
          <div className={styles.textContainer}>
            <h4 className={styles.title}>Détecter ma position</h4>
            <p className={styles.subtitle}>
              Utilisez la localisation de votre appareil
            </p>
          </div>
        </button>

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
