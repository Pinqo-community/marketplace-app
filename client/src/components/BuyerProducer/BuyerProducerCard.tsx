type CardType = "buyer" | "producer";
import { PrimaryButton } from "../Button/Buttons";
import styles from "./BuyerProducer.module.scss";

interface BuyerProducerCardProps {
  type: CardType;
  image: string;
  title: string;
  subtitle: string;
  description: string;
  buttonText: string;
}

const BuyerProducerCard: React.FC<BuyerProducerCardProps> = ({
  type,
  image,
  title,
  subtitle,
  description,
  buttonText,
}) => {
  return (
    <div className={`${styles.container} ${styles[type]}`}>
      <img className={styles.image} src={image} alt={type} />
      <div className={styles.content}>
        <h2 className={styles.title}>
          <span>{title}</span>
          <span>{subtitle}</span>
        </h2>
        <div className={styles.descriptionContainer}>
          <p className={styles.description}>{description}</p>
          <PrimaryButton children={buttonText} />
        </div>
      </div>
    </div>
  );
};

export default BuyerProducerCard;
