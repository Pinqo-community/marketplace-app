import { buttonVariants } from "@/animations/animations";
import arrowSlider from "@/assets/icons/arrow-slider.svg";
import { motion } from "framer-motion";
import { ShoppingCart, User } from "lucide-react";
import {
  MenuButtonProps,
  PrimaryButtonProps,
  SliderButtonProps,
} from "../../types/Button";
import styles from "./Buttons.module.scss";

/* -------------------------------------------------------------------------- */
/*                                 UserButton                                 */
/* -------------------------------------------------------------------------- */

/* -------------------------------------------------------------------------- */
/*                                 UserButton                                 */
/* -------------------------------------------------------------------------- */

export const UserButton: React.FC = () => (
  <button className={styles.userButton}>
    <User size={25} />
  </button>
);

/* -------------------------------------------------------------------------- */
/*                                 CartButton                                 */
/* -------------------------------------------------------------------------- */

/* -------------------------------------------------------------------------- */
/*                                 CartButton                                 */
/* -------------------------------------------------------------------------- */

export const CartButton: React.FC = () => (
  <button className={styles.cartButton}>
    <ShoppingCart size={25} />
    <span className={styles.quantity}>3</span>
  </button>
);

/* -------------------------------------------------------------------------- */
/*                                PrimaryButton                               */
/* -------------------------------------------------------------------------- */

export const PrimaryButton: React.FC<PrimaryButtonProps> = ({
  children,
  light,
}) => (
  <motion.button
    className={`${styles.primaryButton} ${light ? styles.light : ""}`}
    whileTap={{ scale: 0.95 }}
    transition={{
      type: "spring",
      stiffness: 400,
      damping: 10,
    }}
  >
    {children}
  </motion.button>
);

/* -------------------------------------------------------------------------- */
/*                                SliderButton                                */
/* -------------------------------------------------------------------------- */

export const SliderButton: React.FC<SliderButtonProps> = ({
  prevRef,
  nextRef,
}) => {
  return (
    <>
      <motion.button
        ref={prevRef}
        className={styles.sliderButton}
        aria-label="Précédent"
        variants={buttonVariants}
        initial="initial"
        whileHover="hover"
        whileTap="tap"
      >
        <img alt="précédent" src={arrowSlider} />
      </motion.button>

      <motion.button
        ref={nextRef}
        className={styles.sliderButton}
        aria-label="Suivant"
        variants={buttonVariants}
        initial="initial"
        whileHover="hover"
        whileTap="tap"
      >
        <img alt="suivant" src={arrowSlider} />
      </motion.button>
    </>
  );
};

/* -------------------------------------------------------------------------- */
/*                                 MenuButton                                 */
/* -------------------------------------------------------------------------- */

export const MenuButton: React.FC<MenuButtonProps> = ({
  isOpened,
  toggleMenu,
}) => (
  <button
    className={`${styles.menu} ${isOpened ? styles.opened : ""}`}
    onClick={toggleMenu}
    aria-label={isOpened ? "Fermer le menu" : "Ouvrir le menu"}
    aria-expanded={isOpened}
  >
    <svg className={styles.icon} viewBox="0 0 100 100">
      <path
        className={`${styles.line} ${styles.line1}`}
        d="M 20,29.000046 H 80.000231 C 80.000231,29.000046 94.498839,28.817352 94.532987,66.711331 94.543142,77.980673 90.966081,81.670246 85.259173,81.668997 79.552261,81.667751 75.000211,74.999942 75.000211,74.999942 L 25.000021,25.000058"
      />
      <path className={`${styles.line} ${styles.line2}`} d="M 20,50 H 80" />
      <path
        className={`${styles.line} ${styles.line3}`}
        d="M 20,70.999954 H 80.000231 C 80.000231,70.999954 94.498839,71.182648 94.532987,33.288669 94.543142,22.019327 90.966081,18.329754 85.259173,18.331003 79.552261,18.332249 75.000211,25.000058 75.000211,25.000058 L 25.000021,74.999942"
      />
    </svg>
  </button>
);
