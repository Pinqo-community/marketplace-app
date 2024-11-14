import CategoryCard from "../../components/CategoryCard/CategoryCard";
import Map from "../../components/Map/Map";
import MainLayout from "../../layouts/MainLayout";
import styles from "./HomePage.module.scss";
import fruit from "../../assets/images/fruit-category.png";
import arrowSlider from "../../assets/icons/arrow-slider.svg";

const HomePage: React.FC = () => {
  /* -------------------------------------------------------------------------- */
  /*                                  Statement                                 */
  /* -------------------------------------------------------------------------- */

  const category = [
    {
      id: 1,
      name: "Fruits",
      image: fruit,
    },
    {
      id: 2,
      name: "Fruits",
      image: fruit,
    },
    {
      id: 3,
      name: "Fruits",
      image: fruit,
    },
    {
      id: 4,
      name: "Fruits",
      image: fruit,
    },
    {
      id: 5,
      name: "Fruits",
      image: fruit,
    },
    {
      id: 6,
      name: "Fruits",
      image: fruit,
    },
  ];

  /* -------------------------------------------------------------------------- */
  /*                                  Function                                  */
  /* -------------------------------------------------------------------------- */

  /* -------------------------------------------------------------------------- */
  /*                                   Render                                   */
  /* -------------------------------------------------------------------------- */
  return (
    <MainLayout>
      <section className={styles.categorySection}>
        <div className={styles.titleContainer}>
          <h2>Catégories</h2>
          <div className={styles.arrowContainer}>
            <button className={styles.arrow}>
              <img src={arrowSlider} />
            </button>
            <button className={styles.arrow}>
              <img src={arrowSlider} />
            </button>
          </div>
        </div>
        <div className={styles.categoryContainer}>
          {category.map((item) => (
            <CategoryCard key={item.id} title={item.name} image={item.image} />
          ))}
        </div>
      </section>
      <section>
        <div className={styles.title}>
          <h2>Les producteurs près de chez vous</h2>
        </div>
        <div className={styles.mapContainer}>
          <Map />
        </div>
      </section>
    </MainLayout>
  );
};

export default HomePage;
