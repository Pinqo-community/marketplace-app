import { motion } from "framer-motion";
import { Bell, ChevronDown, HelpCircle, MapPin } from "lucide-react";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../../node_modules/hamburgers/_sass/hamburgers/hamburgers.scss";
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
  const [scrollPosition, setScrollPosition] = useState(0);
  const [isTopBarVisible, setIsTopBarVisible] = useState(true);

  /* -------------------------------------------------------------------------- */
  /*                                  Function                                  */
  /* -------------------------------------------------------------------------- */

  const toggleHamburger = () => setIsActive(!isActive);

  // Handle scroll
  useEffect(() => {
    const handleScroll = () => {
      const currentPosition = window.scrollY;
      setIsTopBarVisible(currentPosition < 50);
      setScrollPosition(currentPosition);
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  /* -------------------------------------------------------------------------- */
  /*                                   Render                                   */
  /* -------------------------------------------------------------------------- */

  return (
    <header
      className={`${styles.header} ${scrollPosition > 50 ? styles.fixed : ""} `}
    >
      {/* Top banner */}
      <motion.div
        initial={{ opacity: 0, paddingTop: 0, paddingBottom: 0, maxHeight: 0 }}
        animate={{
          opacity: isTopBarVisible ? 1 : 0,
          paddingTop: isTopBarVisible ? "6px" : "0",
          paddingBottom: isTopBarVisible ? "6px" : "0",
          maxHeight: isTopBarVisible ? "100px" : "0",
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
            <motion.img
              whileHover={{ scale: 1.05 }}
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
