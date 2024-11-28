type CardType = "buyer" | "producer";

export interface BuyerProducerCardProps {
  type: CardType;
  image: string;
  title: string;
  subtitle: string;
  description: string;
  buttonText: string;
}
