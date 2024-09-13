"use client"
import { useCallback, useEffect, useState } from "react";
import classes from "./Profile.module.css";
import { IUser } from "@/models/User";
import { MdDelete, MdLogout, MdOutlineCancel } from "react-icons/md";
import { FaEdit, FaSave } from "react-icons/fa";
import { updateUser, deleteUser } from "@/services/user.service";
import { logout, retrieveUser } from '@/_util/profile';
import { useForm, SubmitHandler } from "react-hook-form";
import Image from "next/image";
import { useRouter } from 'next/navigation'
import AuthWrapper from '@/lib/AuthWrapper';
import { deleteSkillFromUser, retrieveSkillsByUserId } from "@/_util/skills";
import { ToastContainer } from "react-toastify";
import { errorToast, successToast } from "./Toast";
import { Skill } from "@/models/Skill";

type Inputs = {
  password: string;
  name: string;
  email: string;
}

const Profile = () => {
  const { register, handleSubmit, watch, reset, formState: { errors } } = useForm<Inputs>();
  const [user, setUser] = useState<IUser>({ id: NaN, email: "" });
  const [userSkills, setUserSkills] = useState<Skill[]>([]);
  const [isEdit, setIsEdit] = useState<boolean>(false);
  const [errorMessage, setErrorMessage] = useState('');
  const router = useRouter();

  const fetchUser = useCallback(async () => {
    try {
      const userData = await retrieveUser();
      setUser({ ...userData });
      const userSkills = await retrieveSkillsByUserId();
      setUserSkills(userSkills ?? [])
    } catch (error) {
      console.error('Failed to fetch user data:', error);
    }
  }, []);
  // Watch the values of the fields
  const password = watch('password');

  useEffect(() => {
    fetchUser();
  }, []);

  const onRemoveSkill = async (id: number) => {
    try {
      await deleteSkillFromUser(id);
      setUserSkills((prevSkills) => prevSkills.filter((skill: Skill) => skill.skillId !== id));
      successToast("Successfully remove skill");
    } catch (error: any) {
      console.error('Failed to fetch user:', error);
      errorToast(error);
    }
  }

  const onLogout = () => {
    try {
      console.log("logout")
      logout();
      router.push("/login");
    } catch (error: any) {
      console.log("dd")
      errorToast(error);
    }
  }

  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    try {
      const userId = user?.id;
      // Clear previous error messages
      setErrorMessage('');
      await updateUser(userId, data.password, data.email, data.name);
    } catch (error: any) {
      console.error('Failed to update user', error);
      // If update user failed, display error message
      setErrorMessage(error.message);
    }
  };

  const onDeleteUser = async () => {
    try {
      if (user != undefined) {
        await onDeleteUser();
        router.push("/register");
      }
    } catch (error: any) {
      console.error('Failed to delete user', error);
      // If delete user failed, display error message
      setErrorMessage(error.message);
    }

  }
  return (
    <div className={classes.container}>
      <div className={classes.container}>
        {!isEdit && <div className={classes.form}>
          <label className={classes["profile-label"]}>EMAIL:</label>
          <h4 className={classes["profile-text"]}>{user.email}</h4>
          <label className={classes["profile-label"]}>SKILLS:</label>
          <div className={classes["skill-table"]}>
            {userSkills.map((skill) => {
              return <div className={classes["skill"]} key={skill.id}>
                <div className={classes["skill-item"]}>
                  <Image src={skill.imageUrl} width={30} height={30} alt={skill.name} priority />
                </div>
              </div>
            })}
          </div>
          <div className="btn">
            <button className={classes["btn-item"]} onClick={() => setIsEdit(true)} disabled={isEdit}><FaEdit /> Edit</button>
            <button className={classes["btn-item"]} onClick={onDeleteUser}><MdDelete /> Delete User</button >
            <button className={classes["btn-item"]} onClick={onLogout}><MdLogout /> Log out</button >
          </div >
          <p className={classes.error}>{errorMessage}</p>
        </div>}
        {isEdit && <form className={classes.form} onSubmit={handleSubmit(onSubmit)}>
          <div className={classes["edit-item"]}>
            <label htmlFor="password" className={classes["edit-input-label"]}>New Password</label>
            <div>
              <input type="password" className={classes["edit-input"]} {...register("password", {
                maxLength: {
                  value: 20,
                  message: "Max length 20"
                },
                minLength: {
                  value: 5,
                  message: "Min length 5"
                }
              })} />
              {errors.password && <p className={classes["edit-input-error"]}>{errors.password?.message}</p>}
            </div>
          </div>
          <div className={classes["btn"]}>
            <button type="submit" className={classes["btn-item"]} disabled={!password}><FaSave /> Save</button>
            <button className={classes["btn-item"]} onClick={() => { reset(); setIsEdit(false); }} ><MdOutlineCancel /> Cancel</button >
          </div >
          <p className={classes.error}>{errorMessage}</p>
          <div className={classes["edit-item"]}>
            <div className={classes["edit-skill-table"]}>
              {userSkills.map((skill) => {
                return <div key={skill.id} className={classes["edit-skill"]}>
                  <div className={classes["edit-skill-item"]}>
                    <Image src={skill.imageUrl} width={30} height={30} alt={skill.name} priority />
                    <button type="button" className={classes["edit-icon-btn"]} onClick={() => onRemoveSkill(skill.skillId)}><MdDelete size={20} /></button>
                  </div>
                </div>
              })}
            </div>
          </div>
        </form >}
      </div >
      <ToastContainer />
    </div>

  )
}

export default AuthWrapper(Profile);