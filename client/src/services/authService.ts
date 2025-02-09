import api from "@/api/api";
import { AppDispatch } from "@/store";
import { loginSuccess, logout } from "@/store/slices/authSlice";

export const register = async (dispatch: AppDispatch, userData: any) => {
  const { data } = await api.post("/auth/register", userData);
  dispatch(
    loginSuccess({
      accessToken: data.accessToken,
      refreshToken: data.refreshToken,
      user: data.user,
    }),
  );
};

export const login = async (dispatch: AppDispatch, credentials: any) => {
  const { data } = await api.post("/auth/login", credentials);
  dispatch(
    loginSuccess({
      accessToken: data.accessToken,
      refreshToken: data.refreshToken,
      user: data.user,
    }),
  );
};

export const logoutUser = (dispatch: AppDispatch) => {
  dispatch(logout());
};
