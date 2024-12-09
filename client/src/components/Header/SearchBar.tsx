import { Search } from "lucide-react";
import styles from "./SearchBar.module.scss";

const SearchBar: React.FC = () => {
  return (
    <div className={styles.searchBar}>
      <div className={styles.searchInputContainer}>
        <input
          className={styles.searchInput}
          type="text"
          placeholder="Rechercher un produit..."
          aria-label="Champ de recherche de produits"
        />
        <button>
          <Search size={20} className={styles.icon} />
        </button>
      </div>
    </div>
  );
};

export default SearchBar;
