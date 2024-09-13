"use client"

import { useState } from 'react';
import classes from './Register.module.css';
import { useForm, SubmitHandler } from "react-hook-form";
import { register as signUp } from '@/_util/auth';
import { useRouter } from 'next/navigation'
import { getEnumKeys } from '@/_util/profile';
import { Role } from '@/models/Role';


type Inputs = {
  password: string,
  role: Role,
  email: string,
};

const Register = () => {
  const { register, handleSubmit, formState: { errors } } = useForm<Inputs>();
  const [errorMessage, setErrorMessage] = useState('');
  const router = useRouter();

  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    try {
      // Clear previous error messages
      setErrorMessage('');
      const newUser = await signUp(data.password, data.email, data.role);
      router.push("/languages");

    } catch (error: any) {
      // If login failed, display error message
      setErrorMessage(error.message);
    }
  };

  return (
    <div className={classes.register}>
      <h1 className={classes.header}>Register</h1>
      <form className={classes.form} onSubmit={handleSubmit(onSubmit)}>
        <div className={classes["register-content"]}>
          <label htmlFor='email' className={classes["register-label"]}>Email</label>
          <input className={classes["register-input"]} {...register("email", {
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
        <div className={classes["register-content"]}>
          <label htmlFor='password' className={classes["register-label"]}>Password</label>
          <input className={classes["register-input"]} type="password" {...register("password", {
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
        <div className={classes["register-content"]}>
          <label htmlFor='name' className={classes["register-label"]}>Role</label>
          <select className={classes["register-select"]} {...register("role", {
            required: {
              value: true,
              message: "Required",
            }
          })}>
            {getEnumKeys(Role).map((key) => (
              <option key={Role[key]} value={key}>
                {Role[key]}
              </option>
            ))}
          </select>
          {errors.role && <span className={classes["input-error"]}>{errors.role?.message}</span>}
        </div>

        <button
          type="submit"
          className={classes["register-btn"]}>Sign up</button>
        <p className={classes.error}>{errorMessage}</p>
      </form>
    </div >
  )
}

export default Register;