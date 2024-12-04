import { listVariants } from "@/animations/animations";
import { useGetCategoriesQuery } from "@/api/categoriesApi";
import { useGetProductsQuery } from "@/api/productsApi";
import { useGetTestimonialsQuery } from "@/api/testimonialsApi";
import arrowIcon from "@/assets/icons/arrow.svg";
import BuyerProducer from "../../components/BuyerProducer/BuyerProducer";
import CategoryCard from "@/components/CategoryCard/CategoryCard";
import Hero from "@/components/Hero/Hero";
import Map from "@/components/Map/Map";
import ProductCard from "@/components/ProductCard/ProductCard";
import Slider from "@/components/Slider/Slider";
import TestimonialCard from "@/components/Testimonials/TestimonialCard";
import MainLayout from "@/layouts/MainLayout";
import { motion } from "framer-motion";
import "swiper/css";
import "swiper/css/pagination";
import styles from "./HomePage.module.scss";

const HomePage: React.FC = () => {
  /* -------------------------------------------------------------------------- */
  /*                                  References                                */
  /* -------------------------------------------------------------------------- */

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

  // Testimonials
  const {
    data: testimonials,
    isLoading: isLoadingTestimonials,
    error: errorTestimonials,
  } = useGetTestimonialsQuery({});

  /* -------------------------------------------------------------------------- */
  /*                               Loading & Errors                             */
  /* -------------------------------------------------------------------------- */

  // Loading / Error Handling
  if (isLoadingCategories || isLoadingProducts || isLoadingTestimonials) {
    return <p>Chargement des données...</p>;
  }
  if (errorCategories || errorProducts || errorTestimonials) {
    return <p>Une erreur est survenue lors du chargement des données.</p>;
  }

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
              <ProductCard key={product.id} product={product} />
            ))}
          </motion.div>
        </div>
      </section>
      <section className={styles.categorySection}>
        <Slider
          title="Catégories"
          items={categories || []}
          renderItem={(category) => (
            <CategoryCard title={category.name} image={category.image} />
          )}
          slidesPerViewDefault={6}
          breakpoints={{
            320: { slidesPerView: 1 },
            420: { slidesPerView: 2 },
            580: { slidesPerView: 3 },
            768: { slidesPerView: 4 },
            1024: { slidesPerView: 5 },
            1440: { slidesPerView: 6 },
          }}
        />
      </section>
      <section className={styles.buyerProducer}>
        <BuyerProducer />
      </section>

      <section
        className={styles.localProducers}
        aria-labelledby="local-producers-title"
      >
        <div className={styles.sectionContainer}>
          <div className={styles.titleContainer}>
            <h2 className={styles.title}>Les producteurs près de chez vous</h2>
          </div>
          <div className={styles.mapWrapper}>
            <Map />
          </div>
        </div>
      </section>
      <div className={styles.testimonials}>
        <Slider
          title="Ce que disent nos clients"
          items={testimonials || []}
          customClassName="testimonials"
          renderItem={(testimonial) => (
            <TestimonialCard
              text={testimonial.text}
              name={testimonial.name}
              avatar={testimonial.avatar}
              rating={testimonial.rating}
            />
          )}
          slidesPerViewDefault={3}
          pagination={true}
          breakpoints={{
            320: { slidesPerView: 1 },
            768: { slidesPerView: 1 },
            1024: { slidesPerView: 2 },
            1440: { slidesPerView: 3 },
          }}
        />
      </div>
    </MainLayout>
  );
};

export default HomePage;
