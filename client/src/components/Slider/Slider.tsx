import "@/components/Testimonials/testimonials.scss";
import { SliderProps } from "@/types/Slider";
import { useRef } from "react";
import { Navigation, Pagination } from "swiper/modules";
import { Swiper, SwiperSlide } from "swiper/react";
import { NavigationOptions } from "swiper/types";
import { SliderButton } from "../Button/Buttons";
import styles from "./Slider.module.scss";

const Slider = <T,>({
  items,
  renderItem,
  breakpoints,
  slidesPerViewDefault,
  title,
  pagination = false,
  customClassName = "",
}: SliderProps<T>) => {
  const prevRef = useRef<HTMLButtonElement | null>(null);
  const nextRef = useRef<HTMLButtonElement | null>(null);

  return (
    <section className={`${styles.sectionContainer} ${customClassName}`}>
      <div className={styles.titleContainer}>
        <h2>{title}</h2>
        <div className={styles.arrowContainer} id="arrowContainer">
          <SliderButton prevRef={prevRef} nextRef={nextRef} />
        </div>
      </div>

      <Swiper
        className={styles.testimonialsSwiper}
        modules={[Navigation, pagination ? Pagination : Navigation]}
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
        slidesPerView={slidesPerViewDefault}
        grabCursor={true}
        pagination={
          pagination ? { clickable: true, dynamicBullets: true } : undefined
        }
        breakpoints={breakpoints}
      >
        {items.map((item, index) => (
          <SwiperSlide key={index}>{renderItem(item)}</SwiperSlide>
        ))}
      </Swiper>
    </section>
  );
};
export default Slider;
