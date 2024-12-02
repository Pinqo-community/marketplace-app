import classNames from "classnames";
import { motion } from "framer-motion";
import { Bell, ChevronDown, HelpCircle, MapPin } from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../../node_modules/hamburgers/_sass/hamburgers/hamburgers.scss";
import logo from "../../assets/images/logo.svg";
import { useScroll } from "../../hooks/useScroll";
import { CartButton, UserButton } from "../Button/Buttons";
import styles from "./Header.module.scss";
import SearchBar from "./SearchBar";

const Header: React.FC = () => {
  /* -------------------------------------------------------------------------- */
  /*                                  Statement                                 */
  /* -------------------------------------------------------------------------- */

  const navigate = useNavigate();
  const { scrollPosition, isScrolledUp } = useScroll();
  const [isActive, setIsActive] = useState(false);
  // * const [isHovered, setIsHovered] = useState(false);
  const isTopBarVisible = scrollPosition < 50;
  const shouldShowTopBar = isTopBarVisible || isScrolledUp;
  // * const shouldShowTopBar = isTopBarVisible || isScrolledUp || isHovered;

  /* -------------------------------------------------------------------------- */
  /*                                  Function                                  */
  /* -------------------------------------------------------------------------- */

  const toggleHamburger = () => setIsActive(!isActive);

  // ? J'ai implémenté une fonctionnalité qui permet d'afficher la topbar au survol du header, mais je suis pas sûr que ce soit une bonne idée.
  // ? Pour la tester, il vous suffit de décommenter les lignes commençant par "// *".

  // Handlers pour le survol
  // * const handleMouseEnter = () => setIsHovered(true);
  // * const handleMouseLeave = () => setIsHovered(false);

  /* -------------------------------------------------------------------------- */
  /*                                   Render                                   */
  /* -------------------------------------------------------------------------- */

  return (
    <header
      className={classNames(styles.header, {
        [styles.compactMode]: !shouldShowTopBar,
      })}
      // * onMouseEnter={handleMouseEnter}
      // * onMouseLeave={handleMouseLeave}
    >
      {/* Top banner */}
      <motion.div
        initial={{ opacity: 0, paddingTop: 0, paddingBottom: 0, maxHeight: 0 }}
        animate={{
          opacity: shouldShowTopBar ? 1 : 0,
          paddingTop: shouldShowTopBar ? "6px" : "0",
          paddingBottom: shouldShowTopBar ? "6px" : "0",
          maxHeight: shouldShowTopBar ? "100px" : "0",
        }}
        className={styles.topBanner}
      >
        <div className={styles.leftContainer}>
          <button type="button" className={styles.button}>
            <MapPin size={16} className={styles.icon} />
            <span>Paris 11e</span>
          </button>

          <div className={styles.separator}></div>

          <button type="button" className={styles.button}>
            <Bell size={16} className={styles.icon} />
            <span>Nouveautés</span>
          </button>
        </div>
        <div
          className={`${styles.separator} ${styles.separatorMobileOnly}`}
        ></div>

        <div className={styles.rightContainer}>
          <button type="button" className={styles.button}>
            <HelpCircle size={16} className={styles.icon} />
            <span>Centre d'aide</span>
          </button>

          <div className={styles.separator}></div>

          <button type="button" className={styles.button}>
            <span>FR</span>
            <ChevronDown size={16} className={styles.icon} />
          </button>
        </div>
      </motion.div>

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

        <div className={styles.searchBar}>
          <SearchBar />
        </div>

        <nav>
          <UserButton />
          <CartButton />
        </nav>
      </div>
    </header>
  );
};

export default Header;
