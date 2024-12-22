import buyer from "@/assets/images/buyer-profile.png";
import producer from "@/assets/images/producer-profile.png";
import BuyerProducerCard from "./BuyerProducerCard";

const BuyerProducer: React.FC = () => {
  return (
    <>
      <BuyerProducerCard
        type="buyer"
        image={buyer}
        title="Vous êtes acheteur ?"
        subtitle="Savourez votre région, simplifiez votre vie"
        description="Découvrez les trésors de nos producteurs locaux en quelques clics. Créez votre compte pour accéder à des produits frais et authentiques, livrés près de chez vous. Soutenez les artisans de votre région tout en mangeant mieux."
        buttonText="Je suis acheteur"
      />
      <BuyerProducerCard
        type="producer"
        image={producer}
        title="Vous êtes producteur ?"
        subtitle="Vendez directement aux consommateurs"
        description="Élargissez votre clientèle sans intermédiaire. Notre marketplace vous connecte directement aux consommateurs de votre région, maximise votre visibilité et simplifie votre gestion quotidienne."
        buttonText="Je suis producteur"
      />
    </>
  );
};

export default BuyerProducer;
