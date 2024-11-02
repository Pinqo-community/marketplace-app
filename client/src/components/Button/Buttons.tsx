import styles from "./Buttons.module.scss";
import userIcon from "../../assets/icons/user.svg";
import cartIcon from "../../assets/icons/cart.svg";

export const UserButton: React.FC = () => (
  <button className={styles.userButton}>
    <img src={userIcon} alt="user" />
    <p>Me connecter</p>
  </button>
);

export const CartButton: React.FC = () => (
  <button className={styles.cartButton}>
    <img src={cartIcon} alt="cart" />
    <p>Mon panier</p>
  </button>
);
