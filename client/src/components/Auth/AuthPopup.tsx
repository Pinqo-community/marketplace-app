import BasePopup from "@/layouts/BasePopup";
import Login from "@/components/Auth/Login";
import Signup from "@/components/Auth/Signup";
import { AuthPopupProps } from "@/types/Auth";
import { AnimatePresence, motion } from "framer-motion";
import { useState } from "react";

const AuthPopup = ({ isOpen, onClose }: AuthPopupProps) => {
  const [isLogin, setIsLogin] = useState(true);
  return (
    <BasePopup
      isOpen={isOpen}
      onClose={() => {
        onClose();
        setIsLogin(true);
      }}
      title={isLogin ? "Connexion" : "Inscription"}
      titleTag="h2"
      titleCentered
      variant="auth"
    >
      <AnimatePresence mode="wait">
        <motion.div
          layout
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          transition={{ layout: { duration: 0.3 }, opacity: { duration: 0.2 } }}
          style={{ overflow: "hidden" }}
        >
          {isLogin ? (
            <Login setIsLogin={setIsLogin} />
          ) : (
            <Signup setIsLogin={setIsLogin} />
          )}
        </motion.div>
      </AnimatePresence>
    </BasePopup>
  );
};

export default AuthPopup;
