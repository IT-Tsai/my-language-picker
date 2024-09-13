
import { IAuthUser } from "@/models/User"
import { AUTH } from "./auth";
import { getAllLanguages } from "@/services/language.service";

export const retrieveAllLanguages = async () => {
  const auth = JSON.parse(localStorage.getItem(AUTH) ?? "{}") as IAuthUser;
  return getAllLanguages(auth.accessToken);
}
