import { buttonVariants } from "@/animations/animations";
import arrowSlider from "@/assets/icons/arrow-slider.svg";
import { motion } from "framer-motion";
import cartIcon from "../../assets/icons/cart.svg";
import userIcon from "../../assets/icons/user.svg";
import { PrimaryButtonProps, SliderButtonProps } from "../../types/Button";
import styles from "./Buttons.module.scss";

/* -------------------------------------------------------------------------- */
/*                                 UserButton                                 */
/* -------------------------------------------------------------------------- */

export const UserButton: React.FC = () => (
  <button className={styles.userButton}>
    <img src={userIcon} alt="user" />
    <p>Me connecter</p>
  </button>
);

/* -------------------------------------------------------------------------- */
/*                                 CartButton                                 */
/* -------------------------------------------------------------------------- */

export const CartButton: React.FC = () => (
  <button className={styles.cartButton}>
    <img src={cartIcon} alt="cart" />
    <p>Mon panier</p>
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
        className={styles.arrow}
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
        className={styles.arrow}
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
