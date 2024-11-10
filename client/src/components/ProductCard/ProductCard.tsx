import { animate, motion } from "framer-motion";
import { useEffect, useState } from "react";
import bag from "../../assets/icons/bag.svg";
import star from "../../assets/icons/star.svg";
import tomate from "../../assets/images/tomate.png";
import styles from "./ProductCard.module.scss";

const ProductCard: React.FC = () => {
  const [quantity, setQuantity] = useState(1);
  const [displayQuantity, setDisplayQuantity] = useState(1);

  useEffect(() => {
    const controls = animate(displayQuantity, quantity, {
      duration: 0.3,
      onUpdate: (value) => setDisplayQuantity(Math.round(value)),
    });
    return controls.stop;
  }, [quantity, displayQuantity]);

  const buttonVariants = {
    initial: { scale: 1 },
    hover: { scale: 1.1 },
    tap: { scale: 0.95 },
  };

  return (
    <motion.article
      whileHover={{ scale: 1.02 }}
      transition={{
        duration: 0.1,
      }}
      className={styles.productCard}
    >
      <div className={styles.imageContainer}>
        <img src={tomate} alt="Tomate" className={styles.productImage} />
      </div>

      <div className={styles.content}>
        <div className={styles.header}>
          <h3 className={styles.title}>Tomate</h3>
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
            <motion.button
              className={styles.quantityBtn}
              aria-label="Diminuer la quantité"
              disabled={quantity === 1}
              onClick={() => setQuantity((prevQuantity) => prevQuantity - 1)}
              variants={buttonVariants}
              initial="initial"
              whileTap="tap"
            >
              -
            </motion.button>

            <motion.div
              className={styles.quantityInput}
              aria-label="Quantité"
              key={displayQuantity}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ duration: 0.2 }}
            >
              {displayQuantity}
            </motion.div>

            <motion.button
              className={styles.quantityBtn}
              aria-label="Augmenter la quantité"
              disabled={quantity === 100}
              onClick={() => setQuantity((prevQuantity) => prevQuantity + 1)}
              variants={buttonVariants}
              initial="initial"
              whileTap="tap"
            >
              +
            </motion.button>
          </div>

          <motion.button
            className={styles.addToCart}
            aria-label="Ajouter au panier"
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
          >
            <img src={bag} alt="bag" className={styles.bagIcon} />
          </motion.button>
        </div>
      </div>
    </motion.article>
  );
};

export default ProductCard;
