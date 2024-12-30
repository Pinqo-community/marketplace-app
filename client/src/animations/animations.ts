export const listVariants = {
  hidden: { opacity: 0 },
  visible: {
    opacity: 1,
    transition: {
      staggerChildren: 0.1,
    },
  },
};

export const buttonVariants = {
  initial: { scale: 1, opacity: 0.5 },
  hover: { scale: 1.1, opacity: 1, transition: { duration: 0.2 } },
  tap: { scale: 0.95, transition: { duration: 0.1 } },
};

export const itemVariants = {
  hidden: {
    opacity: 0,
    x: -20,
    scale: 0.8,
  },
  show: {
    opacity: 1,
    x: 0,
    scale: 1,
    transition: {
      type: "spring",
      stiffness: 300,
      damping: 24,
      delay: 0.2,
    },
  },
  exit: {
    opacity: 0,
    x: 20,
    scale: 0.8,
    transition: {
      duration: 0.2,
    },
  },
};
