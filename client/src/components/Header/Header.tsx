import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../../node_modules/hamburgers/_sass/hamburgers/hamburgers.scss";
import helpIcon from "../../assets/icons/help.svg";
import locationIcon from "../../assets/icons/location.svg";
import logo from "../../assets/images/logo.svg";
import { CartButton, UserButton } from "../Button/Buttons";
import styles from "./Header.module.scss";
import SearchBar from "./SearchBar";

const Header: React.FC = () => {
  /* -------------------------------------------------------------------------- */
  /*                                  Statement                                 */
  /* -------------------------------------------------------------------------- */

  const navigate = useNavigate();

  const [isActive, setIsActive] = useState(false);

  /* -------------------------------------------------------------------------- */
  /*                                  Function                                  */
  /* -------------------------------------------------------------------------- */

  const toggleHamburger = () => setIsActive(!isActive);

  /* -------------------------------------------------------------------------- */
  /*                                   Render                                   */
  /* -------------------------------------------------------------------------- */

  return (
    <header className={styles.header}>
      {/* Top banner */}
      <div className={styles.topBanner}>
        <button className={styles.helpButton}>
          <img
            src={helpIcon}
            alt="Besoin d'aide ?"
            aria-label="Besoin d'aide ?"
            loading="lazy"
          />
          Besoin d’aide ?
        </button>
        <button className={styles.locationButton}>
          <img
            src={locationIcon}
            alt="Ajouter ma localisation"
            aria-label="Ajouter ma localisation"
            loading="lazy"
          />
          Ajouter ma localisation
        </button>
      </div>

      {/* Main part */}
      <div className={styles.container}>
        <div className={styles.leftContainer}>
          <div className={styles.leftContainerInner}>
            <button
              className={`${styles.hamburger} hamburger hamburger--collapse ${
                isActive ? "is-active" : ""
              }`}
              type="button"
              onClick={toggleHamburger}
            >
              <span className={`${styles.hamburgerBox} hamburger-box`}>
                <span
                  className={`${styles.hamburgerInner} hamburger-inner`}
                ></span>
              </span>
            </button>
            <img
              src={logo}
              alt="logo"
              className={styles.logo}
              onClick={() => navigate("/")}
            />
          </div>

          <nav className={styles.mobileNav}>
            <UserButton />
            <CartButton />
          </nav>
        </div>

        <SearchBar />

        <nav>
          <UserButton />
          <CartButton />
        </nav>
      </div>
    </header>
  );
};

export default Header;
