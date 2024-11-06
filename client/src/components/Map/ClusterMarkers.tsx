import L from "leaflet";
import { useEffect } from "react";
import { useMap } from "react-leaflet";
import arrowIcon from "../../assets/icons/arrow.svg";
import { Producer } from "../../types/Producer";
import styles from "./ClusterMarkers.module.scss";
import { mapIcon } from "./MapIcon";

const ClusterMarkers = ({ producers }: { producers: Producer[] }) => {
  const map = useMap();

  useEffect(() => {
    const markers = L.markerClusterGroup();

    producers.forEach((producer) => {
      const marker = L.marker([producer.location.lat, producer.location.lng], {
        icon: mapIcon,
      }).bindPopup(`
          <div class="${styles.producer}">
            <div class="${styles.producerInfo}">
              <img
                class="${styles.profilePhoto}"
                src="${producer.profilePhoto}"
                alt="${producer.name}"
              />
              <h3 class="${styles.name}">${producer.name}</h3>
            </div>
           <div class="${styles.producerStatus} ${
        producer.isOpen ? styles.open : styles.closed
      }">
            <div class="${styles.dot}"></div>
            <div class="${styles.statusText}">
              ${producer.isOpen ? "Disponible" : "Indisponible"}
            </div>
          </div>
            <div class="${styles.moreInfo}">
              <div class="${styles.moreInfoText}">Voir plus</div>
              <img src="${arrowIcon}" alt="arrow" />
            </div>
          </div>
        `);
      markers.addLayer(marker);
    });

    map.addLayer(markers);

    return () => {
      map.removeLayer(markers);
    };
  }, [map, producers]);

  return null;
};

export default ClusterMarkers;
