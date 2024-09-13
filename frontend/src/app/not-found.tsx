// app/not-found.tsx
'use client';

import React, { useEffect } from "react";
import { useRouter } from "next/navigation";
import classes from "./not-found.module.css";

export default function NotFound() {
  const router = useRouter();

  useEffect(() => {
    const timer = setTimeout(() => {
      router.push('/languages');
    }, 1500);

    // Clean up the timer if the component is unmounted
    return () => clearTimeout(timer);
  }, [router]);

  return (
    <div className={classes.container}>
      <h1>404 - Page Not Found</h1>
      <p>You will be redirected shortly...</p>
    </div>
  );
}
