import { AuthState, User } from "@/types/Auth";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState: AuthState = {
  accessToken: null,
  refreshToken: null,
  user: JSON.parse(localStorage.getItem("user") || "null"),
  isAuthPopupOpen: false,
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    openAuthPopup: (state) => {
      state.isAuthPopupOpen = true;
    },
    closeAuthPopup: (state) => {
      state.isAuthPopupOpen = false;
    },
    loginSuccess: (
      state,
      action: PayloadAction<{
        accessToken: string;
        refreshToken: string;
        user: User;
      }>,
    ) => {
      state.accessToken = action.payload.accessToken;
      state.refreshToken = action.payload.refreshToken;
      state.user = action.payload.user;
      localStorage.setItem("accessToken", action.payload.accessToken);
      localStorage.setItem("refreshToken", action.payload.refreshToken);
    },
    logout: (state) => {
      state.accessToken = null;
      state.refreshToken = null;
      state.user = null;
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");
    },
    refreshTokenSuccess: (state, action: PayloadAction<string>) => {
      state.accessToken = action.payload;
      localStorage.setItem("accessToken", action.payload);
    },
  },
});

export const {
  openAuthPopup,
  closeAuthPopup,
  loginSuccess,
  logout,
  refreshTokenSuccess,
} = authSlice.actions;
export default authSlice.reducer;
