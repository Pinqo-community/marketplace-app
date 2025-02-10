import { FcGoogle } from "react-icons/fc";
import { FaFacebook } from "react-icons/fa";
import styles from "./Auth.module.scss";

interface SocialButtonsProps {
  type: "login" | "signup";
}

export const SocialButtons: React.FC<SocialButtonsProps> = ({ type }) => {
  const googleAuthUrl =
    "http://localhost:8080/api/v1/oauth2/authorization/google?redirect_uri=http://localhost:5173/oauth/redirect";
  const facebookAuthUrl =
    "http://localhost:8080/api/v1/oauth2/authorization/facebook?redirect_uri=http://localhost:5173/oauth/redirect";

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
