import Hero from "../../components/Hero/Hero";
import Map from "../../components/Map/Map";
import ProductCard from "../../components/ProductCard/ProductCard";
import MainLayout from "../../layouts/MainLayout";
import styles from "./HomePage.module.scss";
import { motion } from "framer-motion";
import arrowIcon from "../../assets/icons/arrow.svg";
import { useGetProductsQuery } from "../../api/productsApi";

const HomePage: React.FC = () => {
  /* -------------------------------------------------------------------------- */
  /*                                  Statement                                 */
  /* -------------------------------------------------------------------------- */
  const { data: products, error, isLoading } = useGetProductsQuery({});

  if (isLoading) {
    return <p>Chargement des produits...</p>;
  }

  if (error) {
    return <p>Une erreur est survenue</p>;
  }

  const listVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1,
      },
    },
  };
  /* -------------------------------------------------------------------------- */
  /*                                  Function                                  */
  /* -------------------------------------------------------------------------- */

  /* -------------------------------------------------------------------------- */
  /*                                   Render                                   */
  /* -------------------------------------------------------------------------- */
  return (
    <MainLayout>
      <Hero />
      <section
        className={styles.popularProducts}
        aria-labelledby="popular-products-title"
      >
        <div className={styles.sectionContainer}>
          <div className={styles.titleContainer}>
            <h2 className={styles.title}>Produits populaires</h2>
            <div className={styles.moreInfo}>
              <div className={styles.moreInfoText}>Voir tout</div>
              <img src={arrowIcon} alt="arrow" />
            </div>
          </div>

          <motion.div
            initial="hidden"
            animate="visible"
            variants={listVariants}
            className={styles.productsGrid}
          >
            {products?.map((product) => (
              <ProductCard key={product.name} product={product} />
            ))}
          </motion.div>
        </div>
      </section>

      <section
        className={styles.localProducers}
        aria-labelledby="local-producers-title"
      >
        <div className={styles.sectionContainer}>
          <h2 className={styles.title}>Les producteurs près de chez vous</h2>
          <div className={styles.mapWrapper}>
            <Map />
          </div>
        </div>
      </section>
    </MainLayout>
  );
};

export default HomePage;
