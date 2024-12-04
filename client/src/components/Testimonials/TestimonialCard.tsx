import quote from "@/assets/icons/quote.svg";
import star from "@/assets/icons/star.svg";
import { TestimonialCardProps } from "@/types/Testimonial";
import styles from "./TestimonialCard.module.scss";

const TestimonialCard: React.FC<TestimonialCardProps> = ({
  text,
  name,
  avatar,
  rating,
}) => {
  return (
    <div className={styles.testimonial}>
      <div className={styles.content}>
        <img src={quote} alt="quote" className={styles.icon} />
        <p className={styles.text}>{text}</p>
      </div>
      <div className={styles.footer}>
        <div className={styles.author}>
          <img src={avatar} alt="profile" className={styles.profile} />
          <h4>{name}</h4>
        </div>
        <div className={styles.rating}>
          {Array.from({ length: rating }).map((_, index) => (
            <img key={index} src={star} alt="star" className={styles.icon} />
          ))}
        </div>
      </div>
    </div>
  );
};

export default TestimonialCard;
