import { useNavigate } from "react-router-dom";
import { AuthLayout } from "@/components/Auth/AuthLayout";
import { AuthInput } from "@/components/Auth/AuthInput";
import { SocialButtons } from "@/components/Auth/SocialButtons";
import styles from "@/components/Auth/Auth.module.scss";

const LoginPage: React.FC = () => {
  const navigate = useNavigate();

  return (
    <AuthLayout title="Se connecter">
      <div className={styles.formGroup}>
        <AuthInput
          type="text"
          name="email"
          placeholder="Email"
          value=""
          onChange={() => {}}
        />
        <AuthInput
          type="password"
          name="password"
          placeholder="Mot de passe"
          value=""
          onChange={() => {}}
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
      <button className={styles.authButton}>Se connecter</button>
      <p className={styles.switchAuthText}>
        Pas encore de compte ?{" "}
        <a onClick={() => navigate("/signup")}>S'inscrire</a>
      </p>
      <SocialButtons type="login" />
    </AuthLayout>
  );
};

export default LoginPage;
