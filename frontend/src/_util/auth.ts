import { getCookie } from "typescript-cookie";
import { refreshToken, signIn, signUp, validToken } from '@/services/auth.service';
import { IAuthUser } from "@/models/User";

//const csrfToken = getCookie('XSRF-TOKEN') ?? "";

export const ACCESS_TOKEN = 'accessToken';
export const ACCESS_TOKEN_EXPIRES_AT = 'accessTokenExpiresAt';
export const REFRESH_TOKEN = 'refreshToken';
export const REFRESH_TOKEN_EXPIRES_AT = 'refreshTokenExpiresAt';
export const AUTH = 'auth';

export const DefaultHeader = {
  'Content-Type': 'application/json',
  //'X-XSRF-TOKEN': csrfToken
}

export const login = async (email: string, password: string) => {
  const data = await signIn(email, password) as IAuthUser;
  localStorage.setItem(AUTH, JSON.stringify(data));
}

export const register = async (password: string, email: string, role: string) => {
  const data = await signUp(password, email, role) as IAuthUser;
  localStorage.setItem(AUTH, JSON.stringify(data));
}

export const isAuthenticated = async () => {
  const auth = returnAuth();
  return await validToken(auth.accessToken);
}

export const isValidToken = (auth: IAuthUser) => {
  return (auth.accessToken && auth.refreshToken && auth.refreshTokenExpiresAt.getTime() > Date.now())
}

export const getRefreshToken = async () => {
  const auth = returnAuth();

  if (!(auth.accessToken && auth.refreshToken && auth.refreshTokenExpiresAt < new Date())) return false;

  refreshToken(auth.accessToken, auth.refreshToken, auth.refreshTokenExpiresAt);
}

export const returnAuth = () => {
  return JSON.parse(localStorage.getItem(AUTH) ?? "{}") as IAuthUser;
}