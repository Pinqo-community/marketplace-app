import styles from "@/components/Auth/Auth.module.scss";
import AuthButton from "@/components/Auth/AuthButton";
import { AuthInput } from "@/components/Auth/AuthInput";
import { AuthLayout } from "@/components/Auth/AuthLayout";
import { SocialButtons } from "@/components/Auth/SocialButtons";
import { Checkbox } from "@/components/Checkbox/Checkbox";
import { login } from "@/services/authService";
import { closeAuthPopup } from "@/store/slices/authSlice";
import { AuthProps } from "@/types/Auth";
import { AxiosError } from "axios";
import { useState } from "react";
import { useDispatch } from "react-redux";

const Login: React.FC<AuthProps> = ({ setIsLogin }) => {
  const dispatch = useDispatch();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();

    const trimmedEmail = email.trim();
    const trimmedPassword = password.trim();

    if (!trimmedEmail || !trimmedPassword) {
      setError("Veuillez remplir tous les champs");
      return;
    }

    setError(""); // Reset des erreurs
    setLoading(true);

    try {
      await login(dispatch, { email: trimmedEmail, password: trimmedPassword });
      dispatch(closeAuthPopup());
      console.log("Connexion réussie ✅");
    } catch (err) {
      const error = err as AxiosError<{ message?: string }>;
      setError(
        error.response?.data?.message ||
          "Échec de la connexion. Vérifiez vos identifiants.",
      );
    } finally {
      setLoading(false);
    }
  };

  const [rememberMe, setRememberMe] = useState(false);

  return (
    <AuthLayout>
      <form data-testid="login-form" onSubmit={handleLogin}>
        <div className={styles.formGroup}>
          <AuthInput
            type="text"
            name="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            error={error}
          />
          <AuthInput
            type="password"
            name="password"
            placeholder="Mot de passe"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            error={error}
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
        <a onClick={() => setIsLogin(false)}>S'inscrire</a>
      </p>
      <SocialButtons type="login" />
    </AuthLayout>
  );
};

export default Login;
