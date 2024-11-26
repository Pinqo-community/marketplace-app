import { motion } from "framer-motion";
import { useRef } from "react";
import "swiper/css";
import { Navigation } from "swiper/modules";
import { Swiper, SwiperSlide } from "swiper/react";
import { NavigationOptions } from "swiper/types";
import { useGetCategoriesQuery } from "../../api/categoriesApi";
import { useGetProductsQuery } from "../../api/productsApi";
import arrowSlider from "../../assets/icons/arrow-slider.svg";
import arrowIcon from "../../assets/icons/arrow.svg";
import CategoryCard from "../../components/CategoryCard/CategoryCard";
import Hero from "../../components/Hero/Hero";
import Map from "../../components/Map/Map";
import ProductCard from "../../components/ProductCard/ProductCard";
import MainLayout from "../../layouts/MainLayout";
import styles from "./HomePage.module.scss";

const HomePage: React.FC = () => {
  /* -------------------------------------------------------------------------- */
  /*                                  References                                */
  /* -------------------------------------------------------------------------- */

  const prevRef = useRef<HTMLButtonElement | null>(null);
  const nextRef = useRef<HTMLButtonElement | null>(null);

  /* -------------------------------------------------------------------------- */
  /*                                 API Queries                                */
  /* -------------------------------------------------------------------------- */

  // Categories
  const {
    data: categories,
    isLoading: isLoadingCategories,
    error: errorCategories,
  } = useGetCategoriesQuery({});

  // Products
  const {
    data: products,
    isLoading: isLoadingProducts,
    error: errorProducts,
  } = useGetProductsQuery({});

  /* -------------------------------------------------------------------------- */
  /*                               Loading & Errors                             */
  /* -------------------------------------------------------------------------- */

  // Loading / Error Handling
  if (isLoadingCategories || isLoadingProducts) {
    return <p>Chargement des données...</p>;
  }
  if (errorCategories || errorProducts) {
    return <p>Une erreur est survenue lors du chargement des données.</p>;
  }

  /* -------------------------------------------------------------------------- */
  /*                                Animations                                  */
  /* -------------------------------------------------------------------------- */

  const listVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1,
      },
    },
  };

  const buttonVariants = {
    initial: { scale: 1, opacity: 0.5 },
    hover: { scale: 1.1, opacity: 1, transition: { duration: 0.2 } },
    tap: { scale: 0.95, transition: { duration: 0.1 } },
  };

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
      <section className={styles.categorySection}>
        <div className={styles.titleContainer}>
          <h2>Catégories</h2>
          <div className={styles.arrowContainer}>
            <motion.button
              ref={prevRef}
              className={styles.arrow}
              variants={buttonVariants}
              initial="initial"
              whileHover="hover"
              whileTap="tap"
              aria-label="Précédent"
            >
              <img src={arrowSlider} />
            </motion.button>

            <motion.button
              ref={nextRef}
              className={styles.arrow}
              variants={buttonVariants}
              initial="initial"
              whileHover="hover"
              whileTap="tap"
              aria-label="Suivant"
            >
              <img src={arrowSlider} />
            </motion.button>
          </div>
        </div>
        <div className={styles.categoryContainer}>
          <Swiper
            modules={[Navigation]}
            navigation={{
              prevEl: prevRef.current,
              nextEl: nextRef.current,
            }}
            onBeforeInit={(swiper) => {
              const navigation = swiper.params.navigation as NavigationOptions;
              navigation.prevEl = prevRef.current;
              navigation.nextEl = nextRef.current;
            }}
            spaceBetween={20}
            loop={true}
            slidesPerView={6}
            breakpoints={{
              320: { slidesPerView: 1 },
              420: { slidesPerView: 2 },
              580: { slidesPerView: 3 },
              768: { slidesPerView: 4 },
              1024: { slidesPerView: 5 },
              1440: { slidesPerView: 6 },
            }}
          >
            {categories?.map((item) => (
              <SwiperSlide key={item.id}>
                <CategoryCard title={item.name} image={item.image} />
              </SwiperSlide>
            ))}
          </Swiper>
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
