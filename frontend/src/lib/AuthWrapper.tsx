// lib/withAuth.tsx
import React, { ComponentType, useEffect } from 'react';
import { useAuth } from './AuthContext';
import { useRouter } from 'next/navigation';

interface WithAuthProps {
  isLoggedIn: boolean;
}

// <P extends WithAuthProps>(WrappedComponent: ComponentType<P>) => {
//  const Auth = (props: Omit<P, keyof WithAuthProps>) => {
const AuthWrapper = <P extends WithAuthProps>(WrappedComponent: ComponentType<P>) => {
  const AuthComponent = (props: Omit<P, keyof WithAuthProps>) => {
    const { isLoggedIn } = useAuth();
    const router = useRouter();

    useEffect(() => {
      if (!isLoggedIn) {
        router.push('/login');
      }
    }, [isLoggedIn, router]);

    if (!isLoggedIn) {
      return null; // or a loading spinner, etc.
    }

    return <WrappedComponent {...(props as P)} isLoggedIn={isLoggedIn} />;
  };

  // Set the display name for the wrapped component for better debugging
  AuthComponent.displayName = `withAuth(${WrappedComponent.displayName || WrappedComponent.name || 'Component'})`;

  return AuthComponent;
};


export default AuthWrapper;
