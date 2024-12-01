import { motion } from "framer-motion";
import { ShoppingCart, User } from "lucide-react";
import { PrimaryButtonProps } from "../../types/Button";
import styles from "./Buttons.module.scss";

export const UserButton: React.FC = () => (
  <button className={styles.userButton}>
    <User size={25} />
  </button>
);

export const CartButton: React.FC = () => (
  <button className={styles.cartButton}>
    <ShoppingCart size={25} />
    <span className={styles.quantity}>3</span>
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
