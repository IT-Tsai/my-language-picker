"use client"
import Link from "next/link";
import classes from "./Navigation.module.css";
import { useAuth } from "@/lib/AuthContext";

const Navigation = () => {
  const { isLoggedIn } = useAuth();
  return (
    <div className={classes.container}>
      <div className={classes["nav-left"]}>
        {isLoggedIn && <Link href="/languages" className={classes["nav-item"]}>Languages</Link>}
      </div>
      <div className={classes["nav-right"]}>
        {!isLoggedIn && <Link href="/login" className={classes["nav-item"]}>Login</Link>}
        {!isLoggedIn && <Link href="/register" className={classes["nav-item"]}>Register</Link>}
        {isLoggedIn && <Link href="/profile" className={classes["nav-item"]}>Profile</Link>}
      </div>
    </div>
  )
}

export default Navigation;