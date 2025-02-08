import styles from "./Auth.module.scss";
import MainLayout from "@/layouts/MainLayout";

interface AuthLayoutProps {
  children: React.ReactNode;
  title: string;
}

export const AuthLayout: React.FC<AuthLayoutProps> = ({ children, title }) => (
  <MainLayout>
    <div className={styles.container}>
      <h1 className={styles.title}>{title}</h1>
      {children}
    </div>
  </MainLayout>
);
