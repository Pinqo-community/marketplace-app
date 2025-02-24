import { AuthLayoutProps } from "@/types/Auth";
import styles from "./Auth.module.scss";
import { motion } from "framer-motion";

export const AuthLayout: React.FC<AuthLayoutProps> = ({ children }) => (
  <motion.div
    initial={{ opacity: 0, y: 20 }}
    animate={{ opacity: 1, y: 0 }}
    exit={{ opacity: 0, y: 20 }}
    transition={{ duration: 0.2 }}
    layout
    className={styles.container}
  >
    {children}
  </motion.div>
);
