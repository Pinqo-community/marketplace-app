import styles from "./Auth.module.scss";
import { motion, AnimatePresence } from "framer-motion";

interface AuthInputProps {
  type: string;
  name: string;
  placeholder: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  error?: string;
}

export const AuthInput: React.FC<AuthInputProps> = ({
  type,
  name,
  placeholder,
  value,
  onChange,
  error,
}) => (
  <motion.div className={styles.inputWrapper} layout>
    <input
      className={`${styles.input} ${error ? styles.inputError : ""}`}
      type={type}
      name={name}
      placeholder={placeholder}
      value={value}
      onChange={onChange}
    />
    <AnimatePresence mode="wait">
      {error && (
        <motion.span
          className={styles.errorMessage}
          initial={{ height: 0, opacity: 0 }}
          animate={{ height: "auto", opacity: 1 }}
          exit={{ height: 0, opacity: 0 }}
          transition={{
            duration: 0.1,
          }}
        >
          {error}
        </motion.span>
      )}
    </AnimatePresence>
  </motion.div>
);
