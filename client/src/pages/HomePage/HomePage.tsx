import { useRef } from "react";
import "swiper/css";
import { Navigation } from "swiper/modules";
import { Swiper, SwiperSlide } from "swiper/react";
import arrowSlider from "../../assets/icons/arrow-slider.svg";
import fruit from "../../assets/images/fruit-category.png";
import CategoryCard from "../../components/CategoryCard/CategoryCard";
import Map from "../../components/Map/Map";
import MainLayout from "../../layouts/MainLayout";
import styles from "./HomePage.module.scss";

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
      name: "Légumes",
      image:
        "https://images.unsplash.com/photo-1590779033100-9f60a05a013d?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
    },
    {
      id: 3,
      name: "Fromages",
      image:
        "https://plus.unsplash.com/premium_photo-1691939610797-aba18030c15f?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8RnJvbWFnZXN8ZW58MHx8MHx8fDA%3D",
    },
    {
      id: 4,
      name: "Viennoiseries",
      image:
        "https://images.unsplash.com/photo-1679812000098-ff557c197028?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NDZ8fFZpZW5ub2lzZXJpZXN8ZW58MHx8MHx8fDA%3D",
    },
    {
      id: 5,
      name: "Pains",
      image:
        "https://plus.unsplash.com/premium_photo-1673111979369-0222c7314b82?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OXx8YnJlYWR8ZW58MHx8MHx8fDA%3D",
    },
    {
      id: 6,
      name: "Boissons",
      image:
        "https://images.unsplash.com/photo-1643094263180-9ca517fe03b2?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NTR8fGp1c3xlbnwwfHwwfHx8MA%3D%3D",
    },
    {
      id: 7,
      name: "Viennoiseries",
      image:
        "https://images.unsplash.com/photo-1679812000098-ff557c197028?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NDZ8fFZpZW5ub2lzZXJpZXN8ZW58MHx8MHx8fDA%3D",
    },
  ];
  const prevRef = useRef(null);
  const nextRef = useRef(null);
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
            <button ref={prevRef} className={styles.arrow}>
              <img src={arrowSlider} />
            </button>
            <button ref={nextRef} className={styles.arrow}>
              <img src={arrowSlider} />
            </button>
          </div>
        </div>
        <div className={styles.categoryContainer}>
          <Swiper
            modules={[Navigation]}
            navigation={{
              prevEl: prevRef.current,
              nextEl: nextRef.current,
            }}
            onBeforeInit={(swiper: typeof Swiper) => {
              swiper.params.navigation.prevEl = prevRef.current;
              swiper.params.navigation.nextEl = nextRef.current;
            }}
            spaceBetween={50}
            slidesPerView={6}
            loop={true}
          >
            {category.map((item) => (
              <SwiperSlide key={item.id}>
                <CategoryCard title={item.name} image={item.image} />
              </SwiperSlide>
            ))}
          </Swiper>
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
