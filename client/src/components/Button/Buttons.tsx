import { buttonVariants } from "@/animations/animations";
import arrowSlider from "@/assets/icons/arrow-slider.svg";
import { motion } from "framer-motion";
import { ShoppingCart, User } from "lucide-react";
import { PrimaryButtonProps, SliderButtonProps } from "../../types/Button";
import styles from "./Buttons.module.scss";

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
