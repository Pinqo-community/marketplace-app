import { useState } from "react";
import { FcGoogle } from "react-icons/fc";
import { FaFacebook } from "react-icons/fa";
import MainLayout from "@/layouts/MainLayout";
import styles from "./SignupPage.module.scss";

interface FormErrors {
  email?: string;
  password?: string;
  confirmPassword?: string;
  firstName?: string;
  lastName?: string;
  terms?: string;
}

const SignupPage: React.FC = () => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    confirmPassword: "",
    firstName: "",
    lastName: "",
    terms: false,
  });

  const [errors, setErrors] = useState<FormErrors>({});

  const validateEmail = (email: string): boolean => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const validatePassword = (password: string): boolean => {
    // 8 caractères, une majuscule, une minuscule, un chiffre
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
    return passwordRegex.test(password);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const newErrors: FormErrors = {};

    if (!validateEmail(formData.email)) {
      newErrors.email = "Adresse email invalide";
    }

    if (!validatePassword(formData.password)) {
      newErrors.password =
        "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un chiffre";
    }

    // Validation confirmation mot de passe
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
      // Soumission du formulaire
      console.log("Formulaire soumis :", formData);
    }
  };

  return (
    <MainLayout>
      <div className={styles.container}>
        <h1 className={styles.title}>S'inscrire</h1>
        <form onSubmit={handleSubmit} className={styles.formGroup}>
          <div className={styles.nameWrapper}>
            <div className={styles.inputWrapper}>
              <input
                className={`${styles.input} ${errors.firstName ? styles.inputError : ""}`}
                type="text"
                name="firstName"
                placeholder="Prénom"
                value={formData.firstName}
                onChange={handleChange}
              />
              {errors.firstName && (
                <span className={styles.errorMessage}>{errors.firstName}</span>
              )}
            </div>
            <div className={styles.inputWrapper}>
              <input
                className={`${styles.input} ${errors.lastName ? styles.inputError : ""}`}
                type="text"
                name="lastName"
                placeholder="Nom"
                value={formData.lastName}
                onChange={handleChange}
              />
              {errors.lastName && (
                <span className={styles.errorMessage}>{errors.lastName}</span>
              )}
            </div>
          </div>
          <div className={styles.inputWrapper}>
            <input
              className={`${styles.input} ${errors.email ? styles.inputError : ""}`}
              type="email"
              name="email"
              placeholder="Email"
              value={formData.email}
              onChange={handleChange}
            />
            {errors.email && (
              <span className={styles.errorMessage}>{errors.email}</span>
            )}
          </div>

          <div className={styles.inputWrapper}>
            <input
              className={`${styles.input} ${errors.password ? styles.inputError : ""}`}
              type="password"
              name="password"
              placeholder="Mot de passe"
              value={formData.password}
              onChange={handleChange}
            />
            {errors.password && (
              <span className={styles.errorMessage}>{errors.password}</span>
            )}
          </div>

          <div className={styles.inputWrapper}>
            <input
              className={`${styles.input} ${errors.confirmPassword ? styles.inputError : ""}`}
              type="password"
              name="confirmPassword"
              placeholder="Confirmer le mot de passe"
              value={formData.confirmPassword}
              onChange={handleChange}
            />
            {errors.confirmPassword && (
              <span className={styles.errorMessage}>
                {errors.confirmPassword}
              </span>
            )}
          </div>

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

          <button type="submit" className={styles.signupButton}>
            S'inscrire
          </button>
        </form>

        <p className={styles.loginText}>
          Déjà inscrit ? <a href="">Se connecter</a>
        </p>

        <div className={styles.separator}>
          <span>ou</span>
        </div>

        <div className={styles.socialLogin}>
          <button className={styles.googleBtn}>
            <FcGoogle size={20} />
            S'inscrire avec Google
          </button>
          <button className={styles.facebookBtn}>
            <FaFacebook size={20} color="#1877F2" />
            S'inscrire avec Facebook
          </button>
        </div>
      </div>
    </MainLayout>
  );
};

export default SignupPage;
