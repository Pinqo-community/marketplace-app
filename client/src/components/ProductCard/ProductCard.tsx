import { animate, motion } from "framer-motion";
import { useEffect, useState } from "react";
import bag from "../../assets/icons/bag.svg";
import star from "../../assets/icons/star.svg";
import styles from "./ProductCard.module.scss";
import { ProductProps } from "../../types/Product";

const ProductCard: React.FC<ProductProps> = ({ product }) => {
  /* -------------------------------------------------------------------------- */
  /*                                  Statement                                 */
  /* -------------------------------------------------------------------------- */

  const MIN_QUANTITY = 1;
  const MAX_QUANTITY = 100;

  const [currentQuantity, setCurrentQuantity] = useState(MIN_QUANTITY);
  const [displayedQuantity, setDisplayedQuantity] = useState(MIN_QUANTITY);

  const buttonVariants = {
    initial: { scale: 1 },
    hover: { scale: 1.1 },
    tap: { scale: 0.95 },
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0 },
  };

  /* -------------------------------------------------------------------------- */
  /*                                  Function                                  */
  /* -------------------------------------------------------------------------- */

  // Animation de changement de quantité
  useEffect(() => {
    const controls = animate(displayedQuantity, currentQuantity, {
      duration: 0.3,
      onUpdate: (value) => setDisplayedQuantity(Math.round(value)),
    });
    return controls.stop;
  }, [currentQuantity, displayedQuantity]);

  /* -------------------------------------------------------------------------- */
  /*                                   Render                                   */
  /* -------------------------------------------------------------------------- */
  return (
    <motion.article
      variants={itemVariants}
      whileHover={{
        scale: 1.03,
        transition: {
          duration: 0.1,
        },
      }}
      transition={{
        duration: 0.3,
      }}
      className={styles.productCard}
    >
      <div className={styles.imageContainer}>
        <img
          src={product.image}
          alt={product.name}
          className={styles.productImage}
        />
      </div>

      <div className={styles.content}>
        <div className={styles.header}>
          <h3 className={styles.title}>{product.name}</h3>
          <div className={styles.rating}>
            <img src={star} alt="Rating" className={styles.starIcon} />
            <span className={styles.ratingValue}>{product.rating}</span>
          </div>
        </div>
        <div className={styles.body}>
          <div className={styles.prices}>
            <span className={styles.currentPrice}>
              {product.price.toFixed(2)}€
            </span>
            {product.previousPrice && (
              <span className={styles.originalPrice}>
                {product.previousPrice.toFixed(2)}€
              </span>
            )}
          </div>

          <div
            className={`${styles.stock} ${
              product.stock < 10 ? styles.low : ""
            }`}
          >
            <p>{product.stock} ex. en stock</p>
          </div>

          <div className={styles.actions}>
            <div className={styles.quantity}>
              <motion.button
                className={styles.quantityBtn}
                aria-label="Diminuer la quantité"
                disabled={currentQuantity === MIN_QUANTITY}
                onClick={() =>
                  setCurrentQuantity((prevQuantity) => prevQuantity - 1)
                }
                variants={buttonVariants}
                initial="initial"
                whileTap="tap"
              >
                -
              </motion.button>

              <motion.div
                className={styles.quantityInput}
                aria-label="Quantité"
                key={displayedQuantity}
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ duration: 0.2 }}
              >
                {displayedQuantity}
              </motion.div>

              <motion.button
                className={styles.quantityBtn}
                aria-label="Augmenter la quantité"
                disabled={currentQuantity === MAX_QUANTITY}
                onClick={() =>
                  setCurrentQuantity((prevQuantity) => prevQuantity + 1)
                }
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
      </div>
    </motion.article>
  );
};

export default ProductCard;
