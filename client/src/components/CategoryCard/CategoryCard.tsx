import { motion } from "framer-motion";
import { CategoryCardProps } from "../../types/Category";
import styles from "./CategoryCard.module.scss";

const CategoryCard: React.FC<CategoryCardProps> = ({ title, image }) => {
  return (
    <motion.article
      whileHover={{
        scale: 1.05,
        boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.13)",
      }}
      transition={{ type: "spring", stiffness: 300 }}
      className={styles.categoryCard}
      role="button"
      tabIndex={0}
    >
      <img className={styles.image} src={image} alt={`Catégorie ${title}`} />
      <h3 className={styles.title}>{title}</h3>
    </motion.article>
  );
};
export default CategoryCard;
