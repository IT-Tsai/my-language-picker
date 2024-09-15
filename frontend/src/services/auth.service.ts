'use server'
import { DefaultHeader } from "@/_util/auth";
import { IAuthUser } from "@/models/User";

const url = `${process.env.NEXT_PUBLIC_SERVER_HOST}/${process.env.NEXT_PUBLIC_MYSQL_AUTH}`;

export const signIn = async (email: string, password: string): Promise<IAuthUser> => {

  try {
    const response = await fetch(`${url}/login`, {
      method: "POST",
      headers: DefaultHeader,
      body: JSON.stringify({
        email,
        password
      })
    });
    const data = await response.json();

    if (!response.ok) {
      throw (data.message) || 'Unable to Login';
    }

    return data;

  } catch (error: any) {
    console.log(error, " error");
    throw new Error(error || "An unexpected error occurred.");
  }
}

export const signUp = async (password: string, email: string, role: string): Promise<IAuthUser> => {
  try {
    const response = await fetch(`${url}/register`, {
      method: "POST",
      headers: DefaultHeader,
      body: JSON.stringify({
        password,
        email,
        role
      })
    });

    const data = await response.json();

    if (!response.ok) {
      throw (data.message) || 'Unable to register';
    }
    return data;

  } catch (error: any) {
    console.log(error);
    throw new Error(error || "An unexpected error occurred.");
  }

}

export const validToken = async (accessToken: string): Promise<boolean> => {

  try {

    const response = await fetch(`${url}/validateToken?` + new URLSearchParams({
      "accessToken": accessToken,
    }).toString(), {
      method: "GET",
      headers: DefaultHeader,
    })

    const data = await response.json();

    return data.isValid;

  } catch (error: any) {
    console.log(error);
    return false;
  }
}

export const refreshToken = async (accessToken: string, refreshToken: string, refreshTokenExpiresAt: Date) => {

  try {
    const response = await fetch(`${url}/refreshToken`, {
      method: "PUT",
      headers: DefaultHeader,
      body: JSON.stringify({
        accessToken,
        refreshToken,
        refreshTokenExpiresAt
      })
    });

    const data = await response.json();

    if (!response.ok) {
      console.log(data.message);
      throw (data.message) || 'Unable to refreshToken';
    }
    return data;

  } catch (error: any) {
    console.log(error);
    throw new Error(error || "An unexpected error occurred.");
  }

}