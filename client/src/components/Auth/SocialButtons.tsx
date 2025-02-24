import { FcGoogle } from "react-icons/fc";
import { FaFacebook } from "react-icons/fa";
import styles from "./Auth.module.scss";
import { SocialButtonsProps } from "@/types/Auth";

export const SocialButtons: React.FC<SocialButtonsProps> = ({ type }) => {
  const googleAuthUrl = `${import.meta.env.VITE_API_BASE_URL}/api/v1/oauth2/authorization/google?redirect_uri=${window.location.origin}/oauth/redirect`;
  const facebookAuthUrl = `${import.meta.env.VITE_API_BASE_URL}/api/v1/oauth2/authorization/facebook?redirect_uri=${window.location.origin}}/oauth/redirect`;

  return (
    <>
      <div className={styles.separator}>
        <span>ou</span>
      </div>
      <div className={styles.socialLogin}>
        <button
          className={styles.googleBtn}
          onClick={() => (window.location.href = googleAuthUrl)}
        >
          <FcGoogle size={20} />
          {type === "login" ? "Se connecter" : "S'inscrire"} avec Google
        </button>
        <button
          className={styles.facebookBtn}
          onClick={() => (window.location.href = facebookAuthUrl)}
        >
          <FaFacebook size={20} color="#1877F2" />
          {type === "login" ? "Se connecter" : "S'inscrire"} avec Facebook
        </button>
      </div>
    </>
  );
};
