import styles from "./ProductCard.module.scss";
import tomate from "../../assets/images/tomate.png";
import star from "../../assets/icons/star.svg";
import bag from "../../assets/icons/bag.svg";

const ProductCard: React.FC = () => {
  return (
    <article className={styles.productCard}>
      <div className={styles.imageContainer}>
        <img src={tomate} alt="Tomate" className={styles.productImage} />
      </div>

      <div className={styles.content}>
        <div className={styles.header}>
          <h2 className={styles.title}>Tomate</h2>
          <div className={styles.rating}>
            <img src={star} alt="Rating" className={styles.starIcon} />
            <span className={styles.ratingValue}>4.5</span>
          </div>
        </div>

        <div className={styles.prices}>
          <span className={styles.currentPrice}>20€</span>
          <span className={styles.originalPrice}>24€</span>
        </div>

        <div className={styles.stock}>
          <p>100 ex. en stock</p>
        </div>

        <div className={styles.actions}>
          <div className={styles.quantity}>
            <button
              className={styles.quantityBtn}
              aria-label="Diminuer la quantité"
            >
              -
            </button>
            <input
              type="number"
              value={1}
              min={1}
              max={100}
              className={styles.quantityInput}
              aria-label="Quantité"
            />
            <button
              className={styles.quantityBtn}
              aria-label="Augmenter la quantité"
            >
              +
            </button>
          </div>
          <button className={styles.addToCart} aria-label="Ajouter au panier">
            <img src={bag} alt="" className={styles.bagIcon} />
          </button>
        </div>
      </div>
    </article>
  );
};

export default ProductCard;
