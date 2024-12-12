import { BuyerProducerCardProps } from "@/types/Card";
import { motion, useInView } from "framer-motion";
import { useRef } from "react";
import { PrimaryButton } from "../Button/Buttons";
import styles from "./BuyerProducer.module.scss";

const BuyerProducerCard: React.FC<BuyerProducerCardProps> = ({
  type,
  image,
  title,
  subtitle,
  description,
  buttonText,
}) => {
  /* -------------------------------------------------------------------------- */
  /*                                 Déclaration                                */
  /* -------------------------------------------------------------------------- */

  const ref = useRef(null);
  const isInView = useInView(ref, { once: true });

  /* -------------------------------------------------------------------------- */
  /*                                  Animation                                 */
  /* -------------------------------------------------------------------------- */

  const containerVariants = {
    hidden: { opacity: 0, y: 50 },
    visible: {
      opacity: 1,
      y: 0,
      transition: {
        duration: 0.5,
        ease: "easeOut",
        staggerChildren: 0.3,
      },
    },
  };

  const childVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: {
      opacity: 1,
      y: 0,
      transition: { duration: 0.4, ease: "easeOut" },
    },
  };

  return (
    <motion.div
      ref={ref}
      className={`${styles.container} ${styles[type]}`}
      initial="hidden"
      animate={isInView ? "visible" : "hidden"}
      variants={containerVariants}
    >
      <motion.img
        className={styles.image}
        src={image}
        alt={type}
        variants={childVariants}
      />
      <motion.div className={styles.content} variants={childVariants}>
        <motion.h2 className={styles.title} variants={childVariants}>
          <span>{title}</span>
          <span>{subtitle}</span>
        </motion.h2>
        <motion.div
          className={styles.descriptionContainer}
          variants={childVariants}
        >
          <p className={styles.description}>{description}</p>
          <PrimaryButton>{buttonText}</PrimaryButton>
        </motion.div>
      </motion.div>
    </motion.div>
  );
};

export default BuyerProducerCard;
