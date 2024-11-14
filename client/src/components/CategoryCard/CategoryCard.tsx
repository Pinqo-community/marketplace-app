import styles from "./CategoryCard.module.scss";
import fruit from "../../assets/images/fruit-category.png";

const CategoryCard: React.FC = () => {
  return (
    <div className={styles.categoryCard}>
      <img className={styles.image} src={fruit} alt="fruit" />
      <h3 className={styles.title}>Fruits</h3>
    </div>
  );
};

export default CategoryCard;
