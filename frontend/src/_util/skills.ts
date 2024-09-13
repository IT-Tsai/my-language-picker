import { IAuthUser } from "@/models/User"
import { AUTH, returnAuth } from "./auth";
import { getSkillsByUserId, addNewSkillToUser, checkSkillInUser, removeSkill } from "@/services/skill.service";

export const retrieveSkillsByUserId = async () => {
  const auth = returnAuth();
  return getSkillsByUserId(auth.id, auth.accessToken);
}

export const addNewSkill = async (userId: number, languageId: number) => {
  const auth = returnAuth();
  return addNewSkillToUser(userId, languageId, auth.accessToken);
}

export const checkExistingSkill = async (userId: number, languageId: number) => {
  const auth = returnAuth();
  return checkSkillInUser(userId, languageId, auth.accessToken);
}

export const deleteSkillFromUser = async (skillId: number) => {
  const auth = returnAuth();
  return removeSkill(skillId, auth.accessToken);
}