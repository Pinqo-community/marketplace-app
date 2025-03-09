import styles from "@/components/Auth/Auth.module.scss";
import Loader from "@/shared/Loader";
import { AnimatePresence, motion } from "framer-motion";

const AuthButton: React.FC<{ loading: boolean; text: string }> = ({
  loading,
  text,
}) => (
  <motion.button
    type="submit"
    className={styles.authButton}
    disabled={loading}
    whileTap={{ scale: loading ? 1 : 0.98 }}
  >
    <AnimatePresence mode="wait">
      {loading ? (
        <motion.div
          key="loader"
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          exit={{ opacity: 0, y: -10 }}
          transition={{ duration: 0.2 }}
          className={styles.loader}
        >
          Chargement
          <Loader size={24} color="#ffffff" />
        </motion.div>
      ) : (
        <motion.span
          key="text"
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          exit={{ opacity: 0, y: -10 }}
          transition={{ duration: 0.2 }}
        >
          {text}
        </motion.span>
      )}
    </AnimatePresence>
  </motion.button>
);

export default AuthButton;
