import { CategoryCardProps } from "../../types/Category";
import styles from "./CategoryCard.module.scss";

const CategoryCard: React.FC<CategoryCardProps> = ({ title, image }) => {
  return (
    <article className={styles.categoryCard} role="button" tabIndex={0}>
      <img className={styles.image} src={image} alt={`Catégorie ${title}`} />
      <h3 className={styles.title}>{title}</h3>
    </article>
  );
};
export default CategoryCard;
