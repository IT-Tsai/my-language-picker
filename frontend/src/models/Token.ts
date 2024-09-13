
export interface IAccessToken {
  accessToken: string
  accessTokenExpiresAt: Date;
}

export interface IToken extends IAccessToken {
  refreshToken: string
  refreshTokenExpiresAt: Date;
}

export interface IRenewAccessRequest {
  refreshToken: string
}

export type IRenewAccessResponse = IAccessToken