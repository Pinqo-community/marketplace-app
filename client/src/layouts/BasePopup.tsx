import { BasePopupProps } from "@/types/BasePopup";
import { AnimatePresence, motion } from "framer-motion";
import { X } from "lucide-react";
import { useEffect } from "react";
import styles from "./BasePopup.module.scss";

const BasePopup: React.FC<BasePopupProps> = ({
  isOpen,
  onClose,
  title,
  children,
}) => {
  // Gestion du clic en dehors
  const handleOutsideClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if ((e.target as HTMLElement).dataset.overlay) {
      onClose();
    }
  };

  // Gestion de la touche Escape
  useEffect(() => {
    const handleEscape = (e: KeyboardEvent) => {
      if (e.key === "Escape") {
        onClose();
      }
    };
    window.addEventListener("keydown", handleEscape);
    return () => window.removeEventListener("keydown", handleEscape);
  }, [onClose]);

  return (
    <AnimatePresence>
      {isOpen && (
        <motion.div
          className={styles.popup}
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          onClick={handleOutsideClick}
          data-overlay
        >
          <motion.div
            className={styles.content}
            initial={{ y: 0, opacity: 0, scale: 0.8 }}
            animate={{ y: 0, opacity: 1, scale: 1 }}
            exit={{ y: 0, opacity: 0, scale: 0.8 }}
            layout
            transition={{ duration: 0.2, ease: "easeOut" }}
            onClick={(e) => e.stopPropagation()}
          >
            <div className={styles.header}>
              {title && <h3 className={styles.title}>{title}</h3>}
              <motion.button
                whileHover={{ scale: 1.1, rotate: 90 }}
                className={styles.close}
                onClick={onClose}
                aria-label="Fermer la popup"
              >
                <X size={24} />
              </motion.button>
            </div>
            <div>{children}</div>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  );
};

export default BasePopup;
