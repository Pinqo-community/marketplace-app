import styles from "@/components/Auth/Auth.module.scss";
import AuthButton from "@/components/Auth/AuthButton";
import { AuthInput } from "@/components/Auth/AuthInput";
import { AuthLayout } from "@/components/Auth/AuthLayout";
import { SocialButtons } from "@/components/Auth/SocialButtons";
import { Checkbox } from "@/components/Checkbox/Checkbox";
import { login } from "@/services/authService";
import { AxiosError } from "axios";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(""); // Reset l'erreur avant chaque tentative
    setLoading(true);
    try {
      await login(dispatch, { email, password });
      console.log("Connexion reussi ✅");

      navigate("/"); // Envoie l'utilisateur vers la page d'accueil
    } catch (err) {
      const error = err as AxiosError<{ message?: string }>;
      setError("Échec de la connexion. Vérifiez vos identifiants.");
      console.error("Erreur de connexion ❌", error);
    } finally {
      setLoading(false);
    }
  };

  const [rememberMe, setRememberMe] = useState(false);

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
            <Checkbox
              checked={rememberMe}
              onChange={setRememberMe}
              label="Se souvenir de moi"
            />
            <a className={styles.forgotPassword}>Mot de passe oublié ?</a>
          </div>
        </div>
        <AuthButton loading={loading} text="Se connecter" />
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
