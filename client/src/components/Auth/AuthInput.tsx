import { AnimatePresence, motion } from "framer-motion";
import { Eye, EyeOff, Lock, Mail, User } from "lucide-react";
import React, { useState } from "react";
import styles from "./Auth.module.scss";

interface AuthInputProps {
  type: string;
  name: string;
  placeholder: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  error?: string;
}

export const AuthInput: React.FC<AuthInputProps> = ({
  type: initialType,
  name,
  placeholder,
  value,
  onChange,
  error,
}) => {
  const [showPassword, setShowPassword] = useState(false);
  const type =
    initialType === "password" && showPassword ? "text" : initialType;

  const getIcon = () => {
    switch (name) {
      case "firstName":
        return <User size={20} className={styles.inputIcon} />;
      case "email":
        return <Mail size={20} className={styles.inputIcon} />;
      case "password":
      case "confirmPassword":
        return <Lock size={20} className={styles.inputIcon} />;
      case "username":
        return <User size={20} className={styles.inputIcon} />;
      default:
        return null;
    }
  };

  return (
    <motion.div className={styles.inputWrapper} layout>
      <div className={styles.inputContainer}>
        {getIcon()}
        <input
          className={`${styles.input} ${error ? styles.inputError : ""} ${getIcon() ? styles.withIcon : ""}`}
          type={type}
          name={name}
          placeholder={placeholder}
          value={value}
          onChange={onChange}
        />
        {initialType === "password" && (
          <button
            type="button"
            onClick={() => setShowPassword(!showPassword)}
            className={styles.togglePassword}
          >
            {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
          </button>
        )}
      </div>
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
};
