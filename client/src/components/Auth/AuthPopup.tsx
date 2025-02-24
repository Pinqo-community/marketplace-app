import BasePopup from "@/layouts/BasePopup";
import LoginPage from "@/pages/LoginPage/LoginPage";
import SignupPage from "@/pages/SignupPage/SignupPage";
import { useState } from "react";

interface AuthPopupProps {
  isOpen: boolean;
  onClose: () => void;
}

const AuthPopup = ({ isOpen, onClose }: AuthPopupProps) => {
  const [isLogin, setIsLogin] = useState(true);

  return (
    <BasePopup
      isOpen={isOpen}
      onClose={onClose}
      title={isLogin ? "Se connecter" : "Inscription"}
      titleTag="h2"
      titleCentered
      variant="auth"
    >
      {isLogin ? <LoginPage /> : <SignupPage />}
      <button onClick={() => setIsLogin(!isLogin)}></button>
    </BasePopup>
  );
};

export default AuthPopup;
