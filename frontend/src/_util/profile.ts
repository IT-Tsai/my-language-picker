import { deleteUser, getUser } from "@/services/user.service"
import { AUTH } from "./auth";
import { IAuthUser } from "@/models/User";

export const retrieveUser = async () => {
  const auth = JSON.parse(localStorage.getItem(AUTH) ?? "{}") as IAuthUser;
  return await getUser(auth.email, auth.accessToken);
}

export const onDeleteUser = async () => {
  const auth = JSON.parse(localStorage.getItem(AUTH) ?? "{}") as IAuthUser;
  return await deleteUser(auth.id);
}

export const logout = () => {
  localStorage.removeItem(AUTH);
}

export function getEnumKeys<
  T extends string,
  TEnumValue extends string | number,
>(enumVariable: { [key in T]: TEnumValue }) {
  return Object.keys(enumVariable) as Array<T>;
}