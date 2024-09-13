"use client"
import classes from './Login.module.css';
import { useForm, SubmitHandler } from "react-hook-form";
import { useFormStatus } from 'react-dom';
import { useRouter } from 'next/navigation'
import { useState } from "react";
import { login } from '@/_util/auth'
type Inputs = {
  email: string,
  password: string,
};

const Login = () => {
  const { register, handleSubmit, formState: { errors } } = useForm<Inputs>();
  const { pending } = useFormStatus();
  const router = useRouter();
  const [errorMessage, setErrorMessage] = useState('');

  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    try {
      // Clear previous error messages
      setErrorMessage('');
      const user = await login(data.email, data.password);
      router.push("/languages");
      console.log("login");
    } catch (error: any) {
      // If login failed, display error message
      setErrorMessage(error.message);
    }
  };

  return <div className={classes.login}>
    <h1 className={classes.header}>Login</h1>
    <form className={classes.form} onSubmit={handleSubmit(onSubmit)}>
      <div className={classes["login-content"]}>
        <label htmlFor='email' className={classes["login-label"]}>Email</label>
        <input className={classes["login-input"]} {...register("email", {
          required: {
            value: true,
            message: "Required",
          },
          pattern: {
            value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
            message: "Invalid email address",
          }
        })} />
        {errors.email && <span className={classes["input-error"]}>{errors.email?.message}</span>}
      </div>
      <div className={classes["login-content"]}>
        <label htmlFor='password' className={classes["login-label"]}>Password</label>
        <input className={classes["login-input"]} type="password" {...register("password", {
          required: {
            value: true,
            message: "Required",
          },
          maxLength: {
            value: 20,
            message: "Max length 20"
          },
          minLength: {
            value: 5,
            message: "Min length 5"
          }
        })} />
        {errors.password && <span className={classes["input-error"]}>{errors.password?.message}</span>}
      </div>

      <button
        type="submit"
        className={classes["login-btn"]} disabled={pending}>Log in
      </button>
      <p className={classes.error}>{errorMessage}</p>
    </form>
  </div >
}

export default Login;