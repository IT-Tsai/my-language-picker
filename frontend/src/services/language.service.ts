'use server'
import { DefaultHeader } from "@/_util/auth";
import Language from "@/models/Language";
import { Skill } from "@/models/Skill";

const url = `${process.env.NEXT_PUBLIC_SERVER_HOST}/${process.env.NEXT_PUBLIC_API}/${process.env.NEXT_PUBLIC_MYSQL_LANGUAGE_DB}`;


export const getAllLanguages = async (accessToken: String): Promise<Language[]> => {
  try {

    const response = await fetch(`${url}/all`, {
      method: "GET",
      headers: {
        'Cache-Control': 'no-cache, no-store, must-revalidate',
        'Pragma': 'no-cache',
        'Expires': '0',
        'Authorization': "Bearer " + accessToken,
        ...DefaultHeader
      }
    });

    const data = await response.json();
    console.log(data)

    if (!response.ok) {
      throw data.message;
    }

    return data.languages;

  } catch (error: any) {
    console.log(error);
    throw new Error(error || "An unexpected error occurred.");
  }
}


export const getSkillsByUserId = async (userId: number, accessToken: string): Promise<Skill[]> => {
  try {
    const response = await fetch(`${url}/skill??` + new URLSearchParams({
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

    return data.languages;

  } catch (error: any) {
    console.log(error, " error");
    throw new Error(error || "An unexpected error occurred.");
  }

}
