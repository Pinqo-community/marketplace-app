export interface ProductProps {
  product: {
    image: string;
    name: string;
    rating: number;
    price: number;
    previousPrice: number | null;
    stock: number;
  };
}
