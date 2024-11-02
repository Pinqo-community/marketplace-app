import searchIcon from "../../assets/icons/search.svg";
import styles from "./SearchBar.module.scss";

const SearchBar: React.FC = () => {
  return (
    <div className={styles.searchBar}>
      <input
        className={styles.searchInput}
        type="text"
        placeholder="Rechercher un produit..."
        aria-label="Champ de recherche de produits"
      />
      <button>
        <img src={searchIcon} alt="search" />
      </button>
    </div>
  );
};

export default SearchBar;
