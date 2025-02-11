import { motion } from "framer-motion";
import { Check } from "lucide-react";
import styles from "./Checkbox.module.scss";

interface CheckboxProps {
  checked: boolean;
  onChange: (checked: boolean) => void;
  label: string;
}

export const Checkbox: React.FC<CheckboxProps> = ({
  checked,
  onChange,
  label,
}) => {
  const checkVariants = {
    checked: {
      scale: 1,
      opacity: 1,
      transition: { duration: 0.2 },
    },
    unchecked: {
      scale: 0,
      opacity: 0,
      transition: { duration: 0.2 },
    },
  };

  const boxVariants = {
    hover: { scale: 1.05 },
    tap: { scale: 0.95 },
  };

  return (
    <motion.label
      className={styles.customCheckbox}
      whileHover="hover"
      whileTap="tap"
    >
      <motion.div
        className={`${styles.checkboxBox} ${checked ? styles.checked : ""}`}
        variants={boxVariants}
      >
        <input
          type="checkbox"
          checked={checked}
          onChange={(e) => onChange(e.target.checked)}
          className={styles.hiddenCheckbox}
        />
        <motion.div
          className={styles.checkIcon}
          variants={checkVariants}
          initial="unchecked"
          animate={checked ? "checked" : "unchecked"}
        >
          <Check size={14} strokeWidth={4} />
        </motion.div>
      </motion.div>
      <span className={styles.checkboxLabel}>{label}</span>
    </motion.label>
  );
};
