import { useNavigate } from "react-router-dom";
import cartIcon from "../../assets/icons/cart.svg";
import helpIcon from "../../assets/icons/help.svg";
import locationIcon from "../../assets/icons/location.svg";
import menuIcon from "../../assets/icons/menu.svg";
import userIcon from "../../assets/icons/user.svg";
import logo from "../../assets/images/logo.svg";
import styles from "./Header.module.scss";
import SearchBar from "./SearchBar";

const Header: React.FC = () => {
  const navigate = useNavigate();

  return (
    <header className={styles.header}>
      {/* Top banner */}
      <div className={styles.topBanner}>
        <button className={styles.helpButton}>
          <img
            src={helpIcon}
            alt="help"
            aria-label="Besoin d'aide ?"
            loading="lazy"
          />
          Besoin d’aide ?
        </button>
        <button className={styles.locationButton}>
          <img
            src={locationIcon}
            alt="location"
            aria-label="Ajouter ma localisation"
            loading="lazy"
          />
          Ajouter ma localisation
        </button>
      </div>

      {/* Main part */}
      <div className={styles.container}>
        <div className={styles.leftContainer}>
          <button className={styles.menuButton}>
            <img src={menuIcon} alt="menu" />
          </button>
          <img
            src={logo}
            alt="logo"
            className={styles.logo}
            onClick={() => navigate("/")}
          />
        </div>
        <SearchBar />
        <nav>
          <button className={styles.userButton}>
            <img src={userIcon} alt="user" />
            <p>Me connecter</p>
          </button>
          <button className={styles.cartButton}>
            <img src={cartIcon} alt="cart" />
            <p>Mon panier</p>
          </button>
        </nav>
      </div>
    </header>
  );
};

export default Header;
