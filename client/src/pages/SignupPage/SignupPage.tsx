import styles from "@/components/Auth/Auth.module.scss";
import { AuthInput } from "@/components/Auth/AuthInput";
import { AuthLayout } from "@/components/Auth/AuthLayout";
import { SocialButtons } from "@/components/Auth/SocialButtons";
import { register } from "@/services/authService";
import { validateEmail, validatePassword } from "@/utils/validationUtils";
import { AxiosError } from "axios";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";

interface FormData {
  email: string;
  password: string;
  confirmPassword: string;
  firstName: string;
  lastName: string;
  terms: boolean;
}

interface FormErrors {
  email?: string;
  password?: string;
  confirmPassword?: string;
  firstName?: string;
  lastName?: string;
  terms?: string;
}

const SignupPage: React.FC = () => {
  const [formData, setFormData] = useState<FormData>({
    email: "",
    password: "",
    confirmPassword: "",
    firstName: "",
    lastName: "",
    terms: false,
  });

  const [errors, setErrors] = useState<FormErrors>({});
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const newErrors: FormErrors = {};

    if (!validateEmail(formData.email)) {
      newErrors.email = "Adresse email invalide";
    }

    if (!validatePassword(formData.password)) {
      newErrors.password =
        "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un chiffre";
    }

    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = "Les mots de passe ne correspondent pas";
    }

    if (!formData.firstName.trim()) {
      newErrors.firstName = "Le prénom est requis";
    }

    if (!formData.lastName.trim()) {
      newErrors.lastName = "Le nom est requis";
    }

    if (!formData.terms) {
      newErrors.terms = "Vous devez accepter les conditions d'utilisation";
    }

    setErrors(newErrors);

    if (Object.keys(newErrors).length === 0) {
      try {
        console.log("Données envoyées:", formData);
        await register(dispatch, {
          firstname: formData.firstName,
          lastname: formData.lastName,
          email: formData.email,
          password: formData.password,
        });
        navigate("/"); // Redirige après l'inscription réussie
        console.log("Inscription reussie ✅");
      } catch (err) {
        const error = err as AxiosError<{ message?: string }>;
        console.error("Erreur lors de l'inscription", error);
        console.log("Réponse de l'API:", error.response?.data);
        setErrors({
          email:
            error.response?.data?.message ||
            "Erreur lors de l'inscription, réessayez.",
        });
      }
    }
  };

  return (
    <AuthLayout title="S'inscrire">
      <form onSubmit={handleSubmit} className={styles.formGroup}>
        <div className={styles.nameWrapper}>
          <AuthInput
            type="text"
            name="firstName"
            placeholder="Prénom"
            value={formData.firstName}
            onChange={handleChange}
            error={errors.firstName}
          />
          <AuthInput
            type="text"
            name="lastName"
            placeholder="Nom"
            value={formData.lastName}
            onChange={handleChange}
            error={errors.lastName}
          />
        </div>
        <AuthInput
          type="email"
          name="email"
          placeholder="Email"
          value={formData.email}
          onChange={handleChange}
          error={errors.email}
        />
        <AuthInput
          type="password"
          name="password"
          placeholder="Mot de passe"
          value={formData.password}
          onChange={handleChange}
          error={errors.password}
        />
        <AuthInput
          type="password"
          name="confirmPassword"
          placeholder="Confirmer le mot de passe"
          value={formData.confirmPassword}
          onChange={handleChange}
          error={errors.confirmPassword}
        />
        <div className={styles.checkboxContainer}>
          <label className={styles.checkboxLabel}>
            <input
              type="checkbox"
              name="terms"
              checked={formData.terms}
              onChange={handleChange}
            />
            J'accepte les conditions d'utilisation
          </label>
          {errors.terms && (
            <span className={styles.errorMessage}>{errors.terms}</span>
          )}
        </div>
        <button type="submit" className={styles.authButton}>
          S'inscrire
        </button>
      </form>
      <p className={styles.switchAuthText}>
        Déjà inscrit ? <a onClick={() => navigate("/login")}>Se connecter</a>
      </p>
      <SocialButtons type="signup" />
    </AuthLayout>
  );
};

export default SignupPage;
