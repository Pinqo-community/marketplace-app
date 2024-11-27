import buyer from "../../assets/images/buyer-profile.png";
import { PrimaryButton } from "../Button/Buttons";
import styles from "./BuyerProducer.module.scss";

const BuyerProducer: React.FC = () => {
  return (
    <div className={styles.container}>
      <img className={styles.image} src={buyer} alt="buyer" />
      <div className={styles.content}>
        <h2 className={styles.title}>
          <span>Vous êtes acheteur ?</span>
          <span>Savourez votre région, simplifiez votre vie</span>
        </h2>
        <p className={styles.description}>
          Découvrez les trésors de nos producteurs locaux en quelques clics.
          Créez votre compte pour accéder à des produits frais et authentiques,
          livrés près de chez vous. Soutenez les artisans de votre région tout
          en mangeant mieux.
        </p>
        <PrimaryButton children={"Je suis acheteur"} />
      </div>
    </div>
  );
};

export default BuyerProducer;
