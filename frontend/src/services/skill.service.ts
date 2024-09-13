'use server'
import { DefaultHeader } from "@/_util/auth";
import { Skill } from "@/models/Skill";

const url = `${process.env.NEXT_PUBLIC_SERVER_HOST}/${process.env.NEXT_PUBLIC_API}/${process.env.NEXT_PUBLIC_MYSQL_SKILL_DB}`;

export const checkSkillInUser = async (userId: number, languageId: number, accessToken: string): Promise<void> => {
  try {

    const response = await fetch(`${url}/existingSkill?` + new URLSearchParams({
      "userId": userId.toString(),
      "languageId": languageId.toString(),
    }).toString(), {
      method: "GET",
      headers: {
        'Authorization': "Bearer " + accessToken,
        ...DefaultHeader
      },
    })

    const data = await response.json();
    if (!response.ok) {
      throw data.message;
    }
    console.log(data);

  } catch (error: any) {
    console.log(error);
    throw new Error(error || "An unexpected error occurred.");
  }
}

export const getSkillsByUserId = async (userId: number, accessToken: string): Promise<Skill[]> => {
  try {
    const response = await fetch(`${url}/skill?` + new URLSearchParams({
      "userId": userId.toString(),
    }).toString(), {
      method: "GET",
      headers: {
        'Authorization': "Bearer " + accessToken,
        ...DefaultHeader
      }
    });
    const data = await response.json();
    if (!response.ok) {
      (data.message) || 'Unable to get skills.';
    }

    return data.skills;

  } catch (error: any) {
    console.log(error, " error");
    throw new Error(error || "An unexpected error occurred.");
  }

}

export const addNewSkillToUser = async (userId: number, languageId: number, accessToken: string): Promise<number> => {
  try {
    const response = await fetch(`${url}/user/add`,
      {
        method: "POST",
        headers: {
          'Authorization': "Bearer " + accessToken,
          ...DefaultHeader
        },
        body: JSON.stringify({ userId, languageId })
      });

    const data = await response.json();

    if (!response.ok) {
      (data.message) || 'Unable to add new skill.';
    }

    return data;

  } catch (error: any) {
    console.log(error, " error");
    throw new Error(error || "An unexpected error occurred.");
  }
}

export const removeSkill = async (skillId: number, accessToken: string): Promise<void> => {

  try {
    const response = await fetch(`${url}/skill/delete`, {
      method: "POST",
      headers: {
        'Authorization': "Bearer " + accessToken,
        ...DefaultHeader
      },
      body: JSON.stringify({ skillId })
    });

    const data = await response.json();

    if (!response.ok) {
      throw data.message;
    }

  } catch (error: any) {
    console.log(error, " error");
    throw new Error(error || "An unexpected error occurred.");
  }
}