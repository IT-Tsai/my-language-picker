// middleware.ts
import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

export function middleware(req: NextRequest) {
  const token = req.headers.get("Authorization");
  //if (!token && req.nextUrl.pathname !== '/login' && req.nextUrl.pathname !== '/register') {
  // return NextResponse.redirect(new URL('/login', req.url));
  //}

  return NextResponse.next();
}

export const config = {
  matcher: ['/', '/profile', '/languages'], // specify the routes you want to protect
};
