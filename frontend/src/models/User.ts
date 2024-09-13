import { IToken } from "./Token";

export interface IUser {
  id: number;
  email: string;
}


export interface IAuthUser extends IToken {
  id: number;
  email: string;
}



