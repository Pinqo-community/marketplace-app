import quote from "../../assets/icons/quote.svg";
import star from "../../assets/icons/star.svg";
import profile from "../../assets/images/user-profile-1.png";
import styles from "./TestimonialCard.module.scss";

const TestimonialCard: React.FC = () => {
  return (
    <div className={styles.testimonial}>
      <div className={styles.content}>
        <img src={quote} alt="quote" className={styles.icon} />
        <p className={styles.text}>
          Pellentesque eu nibh eget mauris congue mattis mattis nec tellus.
          Phasellus imperdiet elit eu magna dictum, bibendum cursus velit
          sodales. Donec sed neque eget
        </p>
      </div>
      <div className={styles.footer}>
        <div className={styles.author}>
          <img src={profile} alt="profile" className={styles.profile} />
          <h4>Eleanor Pena</h4>
        </div>
        <div className={styles.rating}>
          <img src={star} alt="star" className={styles.icon} />
          <img src={star} alt="star" className={styles.icon} />
          <img src={star} alt="star" className={styles.icon} />
          <img src={star} alt="star" className={styles.icon} />
          <img src={star} alt="star" className={styles.icon} />
        </div>
      </div>
    </div>
  );
};

export default TestimonialCard;
