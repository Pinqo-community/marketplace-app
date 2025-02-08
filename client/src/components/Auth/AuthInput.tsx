import styles from "./Auth.module.scss";

interface AuthInputProps {
  type: string;
  name: string;
  placeholder: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  error?: string;
}

export const AuthInput: React.FC<AuthInputProps> = ({
  type,
  name,
  placeholder,
  value,
  onChange,
  error,
}) => (
  <div className={styles.inputWrapper}>
    <input
      className={`${styles.input} ${error ? styles.inputError : ""}`}
      type={type}
      name={name}
      placeholder={placeholder}
      value={value}
      onChange={onChange}
    />
    {error && <span className={styles.errorMessage}>{error}</span>}
  </div>
);
