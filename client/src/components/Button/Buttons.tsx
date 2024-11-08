import styles from "./Buttons.module.scss";
import userIcon from "../../assets/icons/user.svg";
import cartIcon from "../../assets/icons/cart.svg";
import { PrimaryButtonProps } from "../../types/Button";
import { motion } from "framer-motion";

export const UserButton: React.FC = () => (
  <button className={styles.userButton}>
    <img src={userIcon} alt="user" />
    <p>Me connecter</p>
  </button>
);

export const CartButton: React.FC = () => (
  <button className={styles.cartButton}>
    <img src={cartIcon} alt="cart" />
    <p>Mon panier</p>
  </button>
);

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
