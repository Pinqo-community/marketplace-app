import { loginSuccess } from "@/store/slices/authSlice";
import { motion } from "framer-motion";
import { Loader2 } from "lucide-react";
import React, { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import styles from "./Auth.module.scss";

const OAuthRedirect: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const accessToken = params.get("access_token");
    const refreshToken = params.get("refresh_token");

    if (accessToken && refreshToken) {
      const user = {
        id: params.get("user_id") ?? "",
        email: params.get("email") ?? "",
        firstname: params.get("first_name") ?? "",
        lastname: params.get("last_name") ?? "",
      };
      // Dispatch de l'action loginSuccess pour maj l'état d'authentification
      dispatch(
        loginSuccess({
          accessToken,
          refreshToken,
          user,
        }),
      );
      navigate("/");
    } else {
      // Si tokens pas présents ou erreur
      navigate("/login");
    }
  }, [location, navigate, dispatch]);

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      className={styles.auth}
    >
      <Loader2 size={50} className={styles.loader} />
      <h1>Authentification en cours</h1>
      <p>Vous allez être redirigé dans un instant...</p>
    </motion.div>
  );
};

export default OAuthRedirect;
