import BasePopup from "@/layouts/BasePopup";
import LoginPage from "@/pages/LoginPage/LoginPage";
import SignupPage from "@/pages/SignupPage/SignupPage";
import { AuthPopupProps } from "@/types/Auth";
import { useState } from "react";

const AuthPopup = ({ isOpen, onClose }: AuthPopupProps) => {
  const [isLogin, setIsLogin] = useState(true);

  return (
    <BasePopup
      isOpen={isOpen}
      onClose={onClose}
      title={isLogin ? "Connexion" : "Inscription"}
      titleTag="h2"
      titleCentered
      variant="auth"
    >
      {isLogin ? (
        <LoginPage setIsLogin={setIsLogin} />
      ) : (
        <SignupPage setIsLogin={setIsLogin} />
      )}
    </BasePopup>
  );
};

export default AuthPopup;
