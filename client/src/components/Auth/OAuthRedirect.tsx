import { loginSuccess } from "@/store/slices/authSlice";
import React, { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";

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
        firstName: params.get("first_name") ?? "",
        lastName: params.get("last_name") ?? "",
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
      console.log("Authentication successful.", accessToken, refreshToken);
    } else {
      // Si tokens pas présents ou erreur
      navigate("/login");
      console.error(
        "Invalid or missing access token or refresh token.",
        accessToken,
        refreshToken,
      );
    }
  }, [location, navigate, dispatch]);

  return <div>Authenticating with OAuth...</div>;
};

export default OAuthRedirect;
