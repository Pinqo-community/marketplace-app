export interface FormData {
  email: string;
  password: string;
  confirmPassword: string;
  firstName: string;
  lastName: string;
  terms: boolean;
}

export interface FormErrors {
  email?: string;
  password?: string;
  confirmPassword?: string;
  firstName?: string;
  lastName?: string;
  terms?: string;
}

export interface AuthProps {
  setIsLogin: (value: boolean) => void;
}

export interface AuthLayoutProps {
  children: React.ReactNode;
}

export interface AuthInputProps {
  type: string;
  name: string;
  placeholder: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  error?: string;
}

export interface AuthPopupProps {
  isOpen: boolean;
  onClose: () => void;
}

export interface SocialButtonsProps {
  type: "login" | "signup";
}

export interface AuthButtonProps {
  loading: boolean;
  text: string;
}

/* -------------------------------------------------------------------------- */
/*                                    Store                                   */
/* -------------------------------------------------------------------------- */

export interface User {
  id: string;
  email: string;
  firstname: string;
  lastname: string;
}

export interface AuthState {
  accessToken: string | null;
  refreshToken: string | null;
  user: User | null;
  isAuthPopupOpen: boolean;
}

/* -------------------------------------------------------------------------- */
/*                                   Service                                  */
/* -------------------------------------------------------------------------- */

export interface AuthCredentials {
  email: string;
  password: string;
}

export interface RegisterData extends AuthCredentials {
  firstname: string;
  lastname: string;
}
