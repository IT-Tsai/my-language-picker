import { IAuthUser } from "@/models/User"
import { AUTH } from "./auth";
import { getSkillsByUserId, addNewSkillToUser, checkSkillInUser, removeSkill } from "@/services/skill.service";

export const retrieveSkillsByUserId = async () => {
  const auth = JSON.parse(localStorage.getItem(AUTH) ?? "{}") as IAuthUser;
  return getSkillsByUserId(auth.id, auth.accessToken);
}

export const addNewSkill = async (userId: number, languageId: number) => {
  const auth = JSON.parse(localStorage.getItem(AUTH) ?? "{}") as IAuthUser;
  return addNewSkillToUser(userId, languageId, auth.accessToken);
}

export const checkExistingSkill = async (userId: number, languageId: number) => {
  const auth = JSON.parse(localStorage.getItem(AUTH) ?? "{}") as IAuthUser;
  return checkSkillInUser(userId, languageId, auth.accessToken);
}

export const deleteSkillFromUser = async (skillId: number) => {
  const auth = JSON.parse(localStorage.getItem(AUTH) ?? "{}") as IAuthUser;
  return removeSkill(skillId, auth.accessToken);
}