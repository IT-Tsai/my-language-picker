'use client'
import { createContext, useState, useContext, useEffect, useCallback } from 'react';
import { useRouter } from 'next/navigation';
import { usePathname } from 'next/navigation';
import { AUTH, isAuthenticated } from '@/_util/auth';

interface AuthContextType {
  isLoggedIn: boolean;
  logout: () => void;
}

const paths: string[] = ['/login', '/register'];

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const router = useRouter();
  const pathName = usePathname();

  useEffect(() => {
    // Ensure this runs only on the client side

    if (typeof window !== 'undefined') {
      const onAuthenticate = async () => {
        const authenticated = await isAuthenticated();
        console.log("authenticated rse " + authenticated);
        if (authenticated) {
          console.log("isAuthenticated");
          setIsLoggedIn(true);
        } else {
          console.log(" not isAuthenticated");
          localStorage.removeItem(AUTH);
          setIsLoggedIn(false);
          if (!paths.includes(pathName))
            router.push('/login'); // Redirect to login page if not authenticated
        }
      };

      onAuthenticate();
    }
  }, [pathName, router]);

  const logout = () => {
    setIsLoggedIn(false);
    localStorage.removeItem('token');
    router.push('/login');
  };

  return (
    <AuthContext.Provider value={{ isLoggedIn, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};