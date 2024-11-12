import Map from "../../components/Map/Map";
import ProductCard from "../../components/ProductCard/ProductCard";
import MainLayout from "../../layouts/MainLayout";
import styles from "./HomePage.module.scss";
import tomate from "../../assets/images/tomate.png";
import { motion } from "framer-motion";
import arrowIcon from "../../assets/icons/arrow.svg";

const HomePage: React.FC = () => {
  /* -------------------------------------------------------------------------- */
  /*                                  Statement                                 */
  /* -------------------------------------------------------------------------- */
  const products = [
    {
      image:
        "https://plus.unsplash.com/premium_photo-1663957861996-8093b48a22e6?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8aG9uZXl8ZW58MHx8MHx8fDA%3D",
      name: "Miel de fleurs sauvages",
      rating: 4.7,
      price: 12.99,
      previousPrice: 14.99,
      stock: 7,
    },

    {
      image:
        "https://images.unsplash.com/photo-1542820191-bdc08051351b?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fE9yZ2FuaWMlMjBhcHBsZXxlbnwwfHwwfHx8MA%3D%3D",
      name: "Pomme bio du verger",
      rating: 4.2,
      price: 1.2,
      previousPrice: null,
      stock: 51,
    },

    {
      image:
        "https://plus.unsplash.com/premium_photo-1668616815449-b61c3f4d4f44?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MjV8fG9saXZlJTIwb2lsfGVufDB8fDB8fHww",
      name: "Huile d'olive extra vierge",
      rating: 4.8,
      price: 15.49,
      previousPrice: 16.99,
      stock: 27,
    },
    {
      image:
        "https://img-3.journaldesfemmes.fr/ZfmzxO5Kyg0e3j1URh4V8Mf3slc=/1500x/smart/097777a79f144a048f7008573f8584d5/ccmcms-jdf/27424516.jpg",
      name: "Pomme de terre bio",
      rating: 4.7,
      price: 3.99,
      previousPrice: null,
      stock: 32,
    },

    {
      image:
        "https://images.unsplash.com/photo-1513088222195-4388e0a0c553?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTR8fGphbXxlbnwwfHwwfHx8MA%3D%3D",
      name: "Confiture de fraise artisanale",
      rating: 4.3,
      price: 6.5,
      previousPrice: null,
      stock: 15,
    },
    {
      image:
        "https://images.unsplash.com/photo-1522249341405-3871994ac062?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTl8fERhcmslMjBjaG9jb2xhdGV8ZW58MHx8MHx8fDA%3D",
      name: "Chocolat noir bio 80%",
      rating: 4.4,
      price: 3.5,
      previousPrice: 4.0,
      stock: 18,
    },
    {
      image: tomate,
      name: "Tomate Fandango",
      rating: 4.7,
      price: 2.9,
      previousPrice: 3.35,
      stock: 72,
    },
    {
      image:
        "https://plus.unsplash.com/premium_photo-1700767180790-f24ec173ba2e?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTEzfHxXaG9sZW1lYWwlMjBicmVhZHxlbnwwfHwwfHx8MA%3D%3D",
      name: "Pain complet aux céréales",
      rating: 4.5,
      price: 2.99,
      previousPrice: 3.5,
      stock: 3,
    },
  ];

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
      <section className={styles.popularProducts} aria-labelledby="popular-products-title">
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
            {products.map((product) => (
              <ProductCard key={product.name} product={product} />
            ))}
          </motion.div>
        </div>
      </section>

      <section className={styles.localProducers} aria-labelledby="local-producers-title">
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
