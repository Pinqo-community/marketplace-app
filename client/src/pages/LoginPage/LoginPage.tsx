import { useNavigate } from "react-router-dom";
import { AuthLayout } from "@/components/Auth/AuthLayout";
import { AuthInput } from "@/components/Auth/AuthInput";
import { SocialButtons } from "@/components/Auth/SocialButtons";
import styles from "@/components/Auth/Auth.module.scss";
import { useDispatch } from "react-redux";
import { useState } from "react";
import { login } from "@/services/authService";

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(""); // Reset l'erreur avant chaque tentative
    try {
      await login(dispatch, { email, password });
      console.log("Connexion reussi ✅");

      navigate("/"); // Envoie l'utilisateur vers la page d'accueil
    } catch (err: any) {
      setError("Échec de la connexion. Vérifiez vos identifiants.");
      console.error("Erreur de connexion ❌", err);
    }
  };

  return (
    <AuthLayout title="Se connecter">
      <form onSubmit={handleLogin}>
        <div className={styles.formGroup}>
          {error && (
            <p className={`${styles.errorMessage} ${styles.errorCenter}`}>
              {error}
            </p>
          )}
          <AuthInput
            type="text"
            name="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <AuthInput
            type="password"
            name="password"
            placeholder="Mot de passe"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <div className={styles.checkboxContainer}>
            <label className={styles.checkboxLabel}>
              <input type="checkbox" />
              Se souvenir de moi
            </label>
            <a className={styles.forgotPassword}>Mot de passe oublié ?</a>
          </div>
        </div>
        <button type="submit" className={styles.authButton}>
          Se connecter
        </button>
      </form>
      <p className={styles.switchAuthText}>
        Pas encore de compte ?{" "}
        <a onClick={() => navigate("/signup")}>S'inscrire</a>
      </p>
      <SocialButtons type="login" />
    </AuthLayout>
  );
};

export default LoginPage;
