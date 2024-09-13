import { deleteUser, getUser, updateUser } from "@/services/user.service"
import { AUTH, returnAuth } from "./auth";

export const retrieveUser = async () => {
  const auth = returnAuth();
  return await getUser(auth.email, auth.accessToken);
}

export const onUpdateUser = async (password: string) => {
  const auth = returnAuth();
  return await updateUser(auth.id, password, auth.email, auth.accessToken);
}

export const onDeleteUser = async () => {
  const auth = returnAuth();
  return await deleteUser(auth.id, auth.accessToken);
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