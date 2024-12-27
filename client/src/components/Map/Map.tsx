// External libraries
import "leaflet/dist/leaflet.css";
import { MapContainer, TileLayer } from "react-leaflet";
import "react-leaflet-markercluster/dist/styles.min.css";

// Assets and styles
import "leaflet.markercluster/dist/leaflet.markercluster";
import styles from "./Map.module.scss";

// Types and Components
import { Producer } from "@/types/Producer";
import { useRef, useState } from "react";
import ClusterMarkers from "./ClusterMarkers";
import LocateUser from "./LocateUser";

const Map: React.FC = () => {
  /* -------------------------------------------------------------------------- */
  /*                                  Statement                                 */
  /* -------------------------------------------------------------------------- */

  const mapRef = useRef<HTMLDivElement>(null);

  const [defaultPosition] = useState<{ lat: number; lng: number }>({
    lat: 44.85173127060631,
    lng: -0.25886535644531256,
  });

  const jawgApiKey = import.meta.env.VITE_JAWG_API_KEY;

  const producers: Producer[] = [
    {
      id: 1,
      profilePhoto: "https://randomuser.me/api/portraits/men/21.jpg",
      name: "Ferme des Champs Fleuris",
      isOpen: true,
      location: { lat: 48.652381, lng: 6.101264 },
    },
    {
      id: 3,
      profilePhoto: "https://randomuser.me/api/portraits/men/23.jpg",
      name: "La Maison des Saveurs",
      isOpen: true,
      location: { lat: 48.665402, lng: 6.305518 },
    },
    {
      id: 11,
      profilePhoto: "https://randomuser.me/api/portraits/women/31.jpg",
      name: "Les Délices Parisiens",
      isOpen: true,
      location: { lat: 48.8566, lng: 2.3522 },
    },
    {
      id: 12,
      profilePhoto: "https://randomuser.me/api/portraits/men/32.jpg",
      name: "Lyonnais Terroir",
      isOpen: true,
      location: { lat: 45.764043, lng: 4.835659 },
    },
    {
      id: 13,
      profilePhoto: "https://randomuser.me/api/portraits/women/33.jpg",
      name: "La Ferme Provençale",
      isOpen: true,
      location: { lat: 43.296482, lng: 5.36978 },
    },
    {
      id: 14,
      profilePhoto: "https://randomuser.me/api/portraits/men/34.jpg",
      name: "Le Champ Bordelais",
      isOpen: true,
      location: { lat: 44.937789, lng: -0.57918 },
    },
    {
      id: 15,
      profilePhoto: "https://randomuser.me/api/portraits/women/35.jpg",
      name: "Saveurs de Toulouse",
      isOpen: true,
      location: { lat: 43.604652, lng: 1.444209 },
    },
    {
      id: 16,
      profilePhoto: "https://randomuser.me/api/portraits/men/36.jpg",
      name: "Normandie Ferme",
      isOpen: true,
      location: { lat: 49.443232, lng: 1.099971 },
    },
    {
      id: 17,
      profilePhoto: "https://randomuser.me/api/portraits/women/37.jpg",
      name: "Produits Bretons",
      isOpen: true,
      location: { lat: 48.117266, lng: -1.6777926 },
    },
    {
      id: 18,
      profilePhoto: "https://randomuser.me/api/portraits/men/38.jpg",
      name: "Les Saveurs Nantaises",
      isOpen: true,
      location: { lat: 47.218371, lng: -1.553621 },
    },
    {
      id: 19,
      profilePhoto: "https://randomuser.me/api/portraits/women/39.jpg",
      name: "Au Goût Savoyard",
      isOpen: true,
      location: { lat: 45.899247, lng: 6.129384 },
    },
    {
      id: 20,
      profilePhoto: "https://randomuser.me/api/portraits/men/40.jpg",
      name: "Terroir Alsacien",
      isOpen: true,
      location: { lat: 48.573405, lng: 7.752111 },
    },
    {
      id: 21,
      profilePhoto: "https://randomuser.me/api/portraits/women/41.jpg",
      name: "Montpellier Gourmand",
      isOpen: true,
      location: { lat: 43.6119, lng: 3.8772 },
    },
    {
      id: 22,
      profilePhoto: "https://randomuser.me/api/portraits/men/42.jpg",
      name: "Saveurs Dijonnaises",
      isOpen: true,
      location: { lat: 47.322047, lng: 5.04148 },
    },
    {
      id: 23,
      profilePhoto: "https://randomuser.me/api/portraits/women/43.jpg",
      name: "Gastronomie Corse",
      isOpen: true,
      location: { lat: 41.919229, lng: 8.738635 },
    },
    {
      id: 24,
      profilePhoto: "https://randomuser.me/api/portraits/men/44.jpg",
      name: "Pays Basque Fermier",
      isOpen: true,
      location: { lat: 43.2951, lng: -1.3612 },
    },
    {
      id: 25,
      profilePhoto: "https://randomuser.me/api/portraits/women/45.jpg",
      name: "Les Vergers de Lille",
      isOpen: true,
      location: { lat: 50.62925, lng: 3.057256 },
    },
    {
      id: 26,
      profilePhoto: "https://randomuser.me/api/portraits/men/46.jpg",
      name: "La Ferme de la Loire",
      isOpen: true,
      location: { lat: 47.2336, lng: -0.1336 },
    },
    {
      id: 27,
      profilePhoto: "https://randomuser.me/api/portraits/women/47.jpg",
      name: "Verger des Monts d'Ardèche",
      isOpen: true,
      location: { lat: 44.6226, lng: 4.3903 },
    },
    {
      id: 28,
      profilePhoto: "https://randomuser.me/api/portraits/men/48.jpg",
      name: "Les Champs Normands",
      isOpen: true,
      location: { lat: 49.0878, lng: 1.5271 },
    },
    {
      id: 29,
      profilePhoto: "https://randomuser.me/api/portraits/women/49.jpg",
      name: "Au Terroir du Médoc",
      isOpen: true,
      location: { lat: 45.2335, lng: -0.7264 },
    },
    {
      id: 30,
      profilePhoto: "https://randomuser.me/api/portraits/men/50.jpg",
      name: "Bergerie de la Drôme",
      isOpen: true,
      location: { lat: 44.5594, lng: 5.2914 },
    },
    {
      id: 31,
      profilePhoto: "https://randomuser.me/api/portraits/women/51.jpg",
      name: "Ferme de la Montagne Noire",
      isOpen: true,
      location: { lat: 43.4546, lng: 2.3727 },
    },
    {
      id: 32,
      profilePhoto: "https://randomuser.me/api/portraits/men/52.jpg",
      name: "Produits du Morvan",
      isOpen: true,
      location: { lat: 47.0536, lng: 4.1166 },
    },
    {
      id: 33,
      profilePhoto: "https://randomuser.me/api/portraits/women/53.jpg",
      name: "La Laiterie des Vosges",
      isOpen: true,
      location: { lat: 48.0167, lng: 6.8667 },
    },
    {
      id: 34,
      profilePhoto: "https://randomuser.me/api/portraits/men/54.jpg",
      name: "Ferme de la Côte d'Opale",
      isOpen: true,
      location: { lat: 50.7201, lng: 1.6147 },
    },
    {
      id: 35,
      profilePhoto: "https://randomuser.me/api/portraits/women/55.jpg",
      name: "Les Vergers du Lot",
      isOpen: true,
      location: { lat: 44.9292, lng: 1.8654 },
    },
    {
      id: 36,
      profilePhoto: "https://randomuser.me/api/portraits/men/56.jpg",
      name: "La Ferme Basque",
      isOpen: true,
      location: { lat: 43.3333, lng: -1.6667 },
    },
    {
      id: 37,
      profilePhoto: "https://randomuser.me/api/portraits/women/57.jpg",
      name: "Du Pays du Bar",
      isOpen: true,
      location: { lat: 48.2868, lng: 4.2805 },
    },
    {
      id: 38,
      profilePhoto: "https://randomuser.me/api/portraits/men/58.jpg",
      name: "Les Fruits de Provence",
      isOpen: true,
      location: { lat: 43.5263, lng: 5.4454 },
    },
    {
      id: 39,
      profilePhoto: "https://randomuser.me/api/portraits/women/59.jpg",
      name: "Légumes de Vendée",
      isOpen: true,
      location: { lat: 46.6702, lng: -1.4266 },
    },
    {
      id: 40,
      profilePhoto: "https://randomuser.me/api/portraits/men/60.jpg",
      name: "Miel du Jura",
      isOpen: true,
      location: { lat: 46.7111, lng: 5.5489 },
    },
    {
      id: 41,
      profilePhoto: "https://randomuser.me/api/portraits/women/61.jpg",
      name: "Saveurs de Camargue",
      isOpen: true,
      location: { lat: 43.6778, lng: 4.6273 },
    },
    {
      id: 42,
      profilePhoto: "https://randomuser.me/api/portraits/men/62.jpg",
      name: "Brebis du Pays Basque",
      isOpen: true,
      location: { lat: 43.1908, lng: -1.2362 },
    },
    {
      id: 43,
      profilePhoto: "https://randomuser.me/api/portraits/women/63.jpg",
      name: "Ferme d'Auvergne",
      isOpen: true,
      location: { lat: 45.555, lng: 3.2646 },
    },
    {
      id: 44,
      profilePhoto: "https://randomuser.me/api/portraits/men/64.jpg",
      name: "Les Champs de Beauce",
      isOpen: true,
      location: { lat: 48.4512, lng: 1.4841 },
    },
    {
      id: 45,
      profilePhoto: "https://randomuser.me/api/portraits/women/65.jpg",
      name: "Pêcheurs de l'Étang",
      isOpen: true,
      location: { lat: 43.424, lng: 3.6846 },
    },
    {
      id: 46,
      profilePhoto: "https://randomuser.me/api/portraits/men/66.jpg",
      name: "Viande d'Aubrac",
      isOpen: true,
      location: { lat: 44.5499, lng: 2.9945 },
    },
    {
      id: 47,
      profilePhoto: "https://randomuser.me/api/portraits/women/67.jpg",
      name: "Herbes de la Garrigue",
      isOpen: true,
      location: { lat: 43.9845, lng: 4.8079 },
    },
    {
      id: 48,
      profilePhoto: "https://randomuser.me/api/portraits/men/68.jpg",
      name: "Le Potager de Sarthe",
      isOpen: true,
      location: { lat: 47.9938, lng: 0.1919 },
    },
    {
      id: 49,
      profilePhoto: "https://randomuser.me/api/portraits/women/69.jpg",
      name: "Miel de Lozère",
      isOpen: true,
      location: { lat: 44.5259, lng: 3.5028 },
    },
    {
      id: 50,
      profilePhoto: "https://randomuser.me/api/portraits/men/70.jpg",
      name: "Le Guide de Côte Roannaise",
      isOpen: true,
      location: { lat: 46.0386, lng: 3.9245 },
    },
    {
      id: 51,
      profilePhoto: "https://randomuser.me/api/portraits/women/71.jpg",
      name: "Agrumes de Menton",
      isOpen: true,
      location: { lat: 43.775, lng: 7.4975 },
    },
    {
      id: 52,
      profilePhoto: "https://randomuser.me/api/portraits/men/72.jpg",
      name: "Bleu d'Avesnois",
      isOpen: true,
      location: { lat: 50.2475, lng: 3.9243 },
    },
    {
      id: 53,
      profilePhoto: "https://randomuser.me/api/portraits/women/73.jpg",
      name: "Les Fleurs de Grasse",
      isOpen: true,
      location: { lat: 43.6667, lng: 6.9225 },
    },
    {
      id: 54,
      profilePhoto: "https://randomuser.me/api/portraits/men/74.jpg",
      name: "Les Délices de Beauvais",
      isOpen: true,
      location: { lat: 49.433, lng: 2.083 },
    },
    {
      id: 55,
      profilePhoto: "https://randomuser.me/api/portraits/women/75.jpg",
      name: "Fromagerie de la Meuse",
      isOpen: true,
      location: { lat: 49.1562, lng: 5.3867 },
    },
    {
      id: 56,
      profilePhoto: "https://randomuser.me/api/portraits/men/76.jpg",
      name: "Saveurs du Cotentin",
      isOpen: true,
      location: { lat: 49.5885, lng: -1.6617 },
    },
    {
      id: 57,
      profilePhoto: "https://randomuser.me/api/portraits/women/77.jpg",
      name: "Miel des Causses",
      isOpen: true,
      location: { lat: 44.1069, lng: 3.0817 },
    },
    {
      id: 58,
      profilePhoto: "https://randomuser.me/api/portraits/men/78.jpg",
      name: "Les Saveurs d'Anjou",
      isOpen: true,
      location: { lat: 47.3508, lng: -0.7629 },
    },
    {
      id: 59,
      profilePhoto: "https://randomuser.me/api/portraits/women/79.jpg",
      name: "Saveurs d'Artois",
      isOpen: true,
      location: { lat: 50.4439, lng: 2.825 },
    },
    {
      id: 60,
      profilePhoto: "https://randomuser.me/api/portraits/men/80.jpg",
      name: "Fermes du Lubéron",
      isOpen: true,
      location: { lat: 43.8235, lng: 5.446 },
    },
    {
      id: 61,
      profilePhoto: "https://randomuser.me/api/portraits/women/81.jpg",
      name: "Poterie de Gien",
      isOpen: true,
      location: { lat: 47.6865, lng: 2.6323 },
    },
    {
      id: 62,
      profilePhoto: "https://randomuser.me/api/portraits/men/82.jpg",
      name: "Terroir du Beaujolais",
      isOpen: true,
      location: { lat: 46.16, lng: 4.6996 },
    },
    {
      id: 63,
      profilePhoto: "https://randomuser.me/api/portraits/women/83.jpg",
      name: "Céréalier du Lauragais",
      isOpen: true,
      location: { lat: 43.5333, lng: 1.85 },
    },
    {
      id: 64,
      profilePhoto: "https://randomuser.me/api/portraits/men/84.jpg",
      name: "Vergers d'Auvergne",
      isOpen: true,
      location: { lat: 45.7541, lng: 3.2354 },
    },
    {
      id: 65,
      profilePhoto: "https://randomuser.me/api/portraits/women/85.jpg",
      name: "Les Champs de Brie",
      isOpen: true,
      location: { lat: 48.7841, lng: 2.3716 },
    },
    {
      id: 66,
      profilePhoto: "https://randomuser.me/api/portraits/men/86.jpg",
      name: "Fromagerie de Cahors",
      isOpen: true,
      location: { lat: 44.4533, lng: 1.4402 },
    },
    {
      id: 67,
      profilePhoto: "https://randomuser.me/api/portraits/women/87.jpg",
      name: "Bergers des Hautes-Pyrénées",
      isOpen: true,
      location: { lat: 43.2284, lng: 0.0745 },
    },
    {
      id: 68,
      profilePhoto: "https://randomuser.me/api/portraits/men/88.jpg",
      name: "Soleil de Provence",
      isOpen: true,
      location: { lat: 43.5128, lng: 5.4695 },
    },
    {
      id: 69,
      profilePhoto: "https://randomuser.me/api/portraits/women/89.jpg",
      name: "Ferme de la Haute-Marne",
      isOpen: true,
      location: { lat: 48.0974, lng: 5.1355 },
    },
    {
      id: 70,
      profilePhoto: "https://randomuser.me/api/portraits/men/90.jpg",
      name: "Charcuterie de Savoie",
      isOpen: true,
      location: { lat: 45.4133, lng: 6.6387 },
    },
    {
      id: 71,
      profilePhoto: "https://randomuser.me/api/portraits/women/91.jpg",
      name: "Beurre de Fronton",
      isOpen: true,
      location: { lat: 43.8271, lng: 1.3895 },
    },
    {
      id: 72,
      profilePhoto: "https://randomuser.me/api/portraits/men/92.jpg",
      name: "Saveurs du Vercors",
      isOpen: true,
      location: { lat: 44.9736, lng: 5.5444 },
    },
    {
      id: 73,
      profilePhoto: "https://randomuser.me/api/portraits/women/93.jpg",
      name: "Miel du Gers",
      isOpen: true,
      location: { lat: 43.6584, lng: 0.6016 },
    },
    {
      id: 74,
      profilePhoto: "https://randomuser.me/api/portraits/men/94.jpg",
      name: "Beurre de Haute-Saône",
      isOpen: true,
      location: { lat: 47.6396, lng: 6.8637 },
    },
    {
      id: 75,
      profilePhoto: "https://randomuser.me/api/portraits/women/95.jpg",
      name: "Les Sept de Touraine",
      isOpen: true,
      location: { lat: 47.3936, lng: 0.6846 },
    },
    {
      id: 76,
      profilePhoto: "https://randomuser.me/api/portraits/men/96.jpg",
      name: "Grains de Cassis",
      isOpen: true,
      location: { lat: 43.2146, lng: 5.5374 },
    },
    {
      id: 77,
      profilePhoto: "https://randomuser.me/api/portraits/women/97.jpg",
      name: "Herbes de l'Aubrac",
      isOpen: true,
      location: { lat: 44.6759, lng: 3.0833 },
    },
    {
      id: 78,
      profilePhoto: "https://randomuser.me/api/portraits/men/98.jpg",
      name: "Truffes du Quercy",
      isOpen: true,
      location: { lat: 44.2631, lng: 1.5299 },
    },
    {
      id: 79,
      profilePhoto: "https://randomuser.me/api/portraits/women/99.jpg",
      name: "Pâtes de la Drôme",
      isOpen: true,
      location: { lat: 44.754, lng: 5.6367 },
    },
    {
      id: 80,
      profilePhoto: "https://randomuser.me/api/portraits/men/45.jpg",
      name: "Bleu de Bresse",
      isOpen: true,
      location: { lat: 46.2052, lng: 5.2233 },
    },
    {
      id: 81,
      profilePhoto: "https://randomuser.me/api/portraits/men/41.jpg",
      name: "Ferme de Saint-Émilion",
      isOpen: true,
      location: { lat: 44.8925, lng: -0.1567 },
    },
    {
      id: 82,
      profilePhoto: "https://randomuser.me/api/portraits/women/3.jpg",
      name: "Les Fermes du Médoc",
      isOpen: true,
      location: { lat: 45.2545, lng: -0.7722 },
    },
    {
      id: 83,
      profilePhoto: "https://randomuser.me/api/portraits/men/10.jpg",
      name: "L'Olivier de Vence",
      isOpen: true,
      location: { lat: 44.7547, lng: -0.3479 },
    },
    {
      id: 84,
      profilePhoto: "https://randomuser.me/api/portraits/women/24.jpg",
      name: "Miel de Léognan",
      isOpen: true,
      location: { lat: 44.7559, lng: -0.5991 },
    },
    {
      id: 85,
      profilePhoto: "https://randomuser.me/api/portraits/men/74.jpg",
      name: "Délices de Blaye",
      isOpen: true,
      location: { lat: 45.1286, lng: -0.6617 },
    },
    {
      id: 86,
      profilePhoto: "https://randomuser.me/api/portraits/women/32.jpg",
      name: "La Maison de Libourne",
      isOpen: true,
      location: { lat: 44.9128, lng: -0.345 },
    },
  ];

  /* -------------------------------------------------------------------------- */
  /*                                  Function                                  */
  /* -------------------------------------------------------------------------- */

  if (!jawgApiKey) {
    console.error("Missing Jawg API key");
  }

  /* -------------------------------------------------------------------------- */
  /*                                   Render                                   */
  /* -------------------------------------------------------------------------- */
  return (
    <div ref={mapRef} className={styles.mapContainer}>
      <MapContainer
        className={styles.map}
        center={defaultPosition}
        zoom={11}
        minZoom={10}
        maxBoundsViscosity={1.0}
      >
        <TileLayer
          attribution='<a href="https://jawg.io" title="Tiles Courtesy of Jawg Maps" target="_blank">&copy; <b>Jawg</b>Maps</a> &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url={`https://tile.jawg.io/jawg-lagoon/{z}/{x}/{y}{r}.png?access-token=${jawgApiKey}`}
        />
        <ClusterMarkers producers={producers} />
        <LocateUser defaultPosition={defaultPosition} />
      </MapContainer>
    </div>
  );
};

export default Map;
