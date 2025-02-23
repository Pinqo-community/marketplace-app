import Footer from "@/components/Footer/Footer";
import Header from "@/components/Header/Header";
import { motion } from "framer-motion";

const MainLayout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  console.log("Render MainLayout");

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      transition={{ duration: 0.3 }}
      className="main-layout"
    >
      <Header />
      <main>{children}</main>
      <Footer />
    </motion.div>
  );
};

export default MainLayout;
