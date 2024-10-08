
import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Navigation from "../components/Navigation";
import { Suspense } from "react";
import Loading from "./loading";
import { ErrorBoundary } from "next/dist/client/components/error-boundary";
import Error from "./error";
import { AuthProvider } from "@/lib/AuthContext";
import { AUTH, getRefreshToken, isValidToken } from "@/_util/auth"
import { IAuthUser } from "@/models/User";
import 'react-toastify/ReactToastify.min.css';
import { ToastContainer } from 'react-toastify';

const inter = Inter({ subsets: ["latin"] });
export const metadata: Metadata = {
  title: "Create Recycling App",
  description: "Generated by create recycling app",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {

  /*
  const getToken = useCallback(
    async () => await getRefreshToken(),
    []
  );

  useEffect(() => {
    const auth = JSON.parse(localStorage.getItem(AUTH) ?? "{}") as IAuthUser;
    let interval = null;
    if (isValidToken(auth)) {
      interval = setInterval(() => getToken(), auth.refreshTokenExpiresAt.getTime() - Date.now() - 60 * 1000);
    }
    // Clear interval on component unmount or re-render
    return () => {
      if (interval) {
        clearInterval(interval);
      }
    };
  }, []);*/

  return (
    <html lang="en">
      <body>
        <AuthProvider>
          <Navigation />
          <ErrorBoundary errorComponent={Error}>
            <Suspense fallback={<Loading />}>

              {children}
            </Suspense>
            <ToastContainer />
          </ErrorBoundary>
        </AuthProvider>
      </body>
    </html>
  );
}
