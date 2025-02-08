import { FcGoogle } from "react-icons/fc";
import { FaFacebook } from "react-icons/fa";
import styles from "./LoginPage.module.scss";
import MainLayout from "@/layouts/MainLayout";
import { useNavigate } from "react-router-dom";

const LoginPage: React.FC = () => {
  const navigate = useNavigate();

  return (
    <MainLayout>
      <div className={styles.container}>
        <h1 className={styles.title}>Se connecter</h1>
        <div className={styles.formGroup}>
          <input className={styles.input} type="text" placeholder="Email" />
          <input
            className={styles.input}
            type="password"
            placeholder="Mot de passe"
          />
          <div className={styles.checkboxContainer}>
            <label className={styles.checkboxLabel}>
              <input type="checkbox" />
              Se souvenir de moi
            </label>
            <a className={styles.forgotPassword} href="">
              Mot de passe oublié ?
            </a>
          </div>
        </div>
        <button className={styles.loginButton}>Se connecter</button>
        <p className={styles.signupText}>
          Pas encore de compte ?{" "}
          <a onClick={() => navigate("/signup")}>S'inscrire</a>
        </p>

        <div className={styles.separator}>
          <span>ou</span>
        </div>

        <div className={styles.socialLogin}>
          <button className={styles.googleBtn}>
            <FcGoogle size={20} />
            Se connecter avec Google
          </button>
          <button className={styles.facebookBtn}>
            <FaFacebook size={20} color="#1877F2" />
            Se connecter avec Facebook
          </button>
        </div>
      </div>
    </MainLayout>
  );
};

export default LoginPage;
