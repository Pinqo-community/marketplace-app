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
