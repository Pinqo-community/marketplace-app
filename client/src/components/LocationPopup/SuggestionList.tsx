import { SuggestionListProps } from "@/types/Location";
import { motion } from "framer-motion";
import styles from "./SuggestionList.module.scss";

const SuggestionList = ({ suggestions, onSelect }: SuggestionListProps) => (
  <motion.ul
    initial={{ opacity: 0, y: 10 }}
    animate={{ opacity: 1, y: 0 }}
    exit={{ opacity: 0, y: 10 }}
    className={styles.suggestions}
  >
    {suggestions.map((suggestion, index) => (
      <li
        className={styles.item}
        key={index}
        onClick={() => onSelect(suggestion)}
      >
        <div className={styles.label}>{suggestion.label}</div>
        <div className={styles.context}>
          {suggestion.postcode} {suggestion.city}
        </div>
      </li>
    ))}
  </motion.ul>
);

export default SuggestionList;
