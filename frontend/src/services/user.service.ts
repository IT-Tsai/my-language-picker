'use server'
import { IUser } from "@/models/User";
import { DefaultHeader } from '@/_util/auth';
const url = `${process.env.NEXT_PUBLIC_SERVER_HOST}/${process.env.NEXT_PUBLIC_API}/${process.env.NEXT_PUBLIC_MYSQL_USER_DB}`;;

export const updateUser = async (id: number | undefined, password: string, email: string, name: string): Promise<void> => {
  try {
    const response = await fetch(`${url}/update`, {
      method: "PUT",
      headers: { ...DefaultHeader },
      body: JSON.stringify({
        id,
        password,
        email,
        name,
      })
    });

    const data = await response.json();

    if (!response.ok) {
      throw data.message;
    }
  } catch (e) {
    console.log(e);
    throw new Error("An unexpected error occurred.");
  }

}

export const deleteUser = async (id: number, accessToken: string): Promise<void> => {
  try {
    console.log(accessToken)
    const response = await fetch(`${url}/delete?` + new URLSearchParams({
      "userId": id.toString(),
    }).toString(), {
      method: "DELETE",
      headers: {
        'Authorization': "Bearer " + accessToken,
        ...DefaultHeader
      }
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


export const getUser = async (email: string, accessToken: string): Promise<IUser> => {
  try {
    const response = await fetch(`${url}/email?` + new URLSearchParams({
      "email": email,
    }).toString(), {
      method: "GET",
      headers: {
        'Authorization': "Bearer " + accessToken,
        ...DefaultHeader
      }
    });

    const data = await response.json();

    if (!response.ok) {
      throw data.message;
    }

    return data.user;

  } catch (error: any) {
    console.log(error, " error");
    throw new Error(error || "An unexpected error occurred.");
  }
}
