import { motion } from "framer-motion";
import { CategoryCardProps } from "../../types/Category";
import styles from "./CategoryCard.module.scss";

const CategoryCard: React.FC<CategoryCardProps> = ({ title, image }) => {
  /* -------------------------------------------------------------------------- */
  /*                                  Functions                                 */
  /* -------------------------------------------------------------------------- */

  const getDynamicStyles = (text: string) => {
    if (text.length > 15) {
      return { fontSize: "18px", lineHeight: "2" };
    }
  };

  /* -------------------------------------------------------------------------- */
  /*                                   Render                                   */
  /* -------------------------------------------------------------------------- */
  return (
    <motion.article
      whileHover={{
        scale: 1.03,
        boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.13)",
        transition: {
          duration: 0.1,
        },
      }}
      className={styles.categoryCard}
      role="button"
      tabIndex={0}
    >
      <img className={styles.image} src={image} alt={`Catégorie ${title}`} />
      <h3 className={styles.title} style={getDynamicStyles(title)}>
        {title}
      </h3>
    </motion.article>
  );
};
export default CategoryCard;
