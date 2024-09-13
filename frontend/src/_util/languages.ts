
import { returnAuth } from "./auth";
import { getAllLanguages } from "@/services/language.service";

export const retrieveAllLanguages = async () => {
  const auth = returnAuth();
  return getAllLanguages(auth.accessToken);
}
