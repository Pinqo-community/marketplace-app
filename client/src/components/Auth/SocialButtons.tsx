import { FcGoogle } from "react-icons/fc";
import { FaFacebook } from "react-icons/fa";
import styles from "./Auth.module.scss";

interface SocialButtonsProps {
  type: "login" | "signup";
}

export const SocialButtons: React.FC<SocialButtonsProps> = ({ type }) => (
  <>
    <div className={styles.separator}>
      <span>ou</span>
    </div>
    <div className={styles.socialLogin}>
      <button className={styles.googleBtn}>
        <FcGoogle size={20} />
        {type === "login" ? "Se connecter" : "S'inscrire"} avec Google
      </button>
      <button className={styles.facebookBtn}>
        <FaFacebook size={20} color="#1877F2" />
        {type === "login" ? "Se connecter" : "S'inscrire"} avec Facebook
      </button>
    </div>
  </>
);
