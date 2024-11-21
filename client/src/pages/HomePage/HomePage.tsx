import Hero from "../../components/Hero/Hero";
import Map from "../../components/Map/Map";
import MainLayout from "../../layouts/MainLayout";
import styles from "./HomePage.module.scss";

const HomePage: React.FC = () => {
  /* -------------------------------------------------------------------------- */
  /*                                  Statement                                 */
  /* -------------------------------------------------------------------------- */

  /* -------------------------------------------------------------------------- */
  /*                                  Function                                  */
  /* -------------------------------------------------------------------------- */

  /* -------------------------------------------------------------------------- */
  /*                                   Render                                   */
  /* -------------------------------------------------------------------------- */
  return (
    <MainLayout>
      <Hero />
      <section className={styles.section}>
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
