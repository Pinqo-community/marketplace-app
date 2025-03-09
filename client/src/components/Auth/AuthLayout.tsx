import styles from "./Auth.module.scss";
import MainLayout from "@/layouts/MainLayout";
import { motion } from "framer-motion";

interface AuthLayoutProps {
  children: React.ReactNode;
  title: string;
}

export const AuthLayout: React.FC<AuthLayoutProps> = ({ children, title }) => (
  <MainLayout>
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      exit={{ opacity: 0, y: 20 }}
      transition={{ duration: 0.2 }}
      layout
      className={styles.container}
    >
      <h1 className={styles.title}>{title}</h1>
      {children}
    </motion.div>
  </MainLayout>
);
