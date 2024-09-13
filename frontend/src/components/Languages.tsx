"use client"
import Language from "@/models/Language"
import Image from "next/image"
import classes from './Languages.module.css';
import { useEffect, useState } from "react";
import { AUTH } from "@/_util/auth";
import { IAuthUser } from "@/models/User";
import { retrieveAllLanguages } from "@/_util/languages";
import { addNewSkill, checkExistingSkill } from "@/_util/skills";
import { errorToast, successToast } from "./Toast";

const Languages = () => {
  const [userId, setUserId] = useState<number>(NaN);
  const [languages, seLanguages] = useState<Language[] | []>([]);

  const add = async (languageId: number) => {
    try {
      await checkExistingSkill(userId, languageId);
      await addNewSkill(userId, languageId);
      successToast("Successfully added");
    } catch (e: any) {
      errorToast(e.message);
    }
  }

  useEffect(() => {
    // Access localStorage only on the client-side
    if (typeof window !== 'undefined') {
      const auth = JSON.parse(localStorage.getItem(AUTH) ?? "{}") as IAuthUser;
      setUserId(auth.id ?? null);
      const onRetrieveAllLanguages = async () => {
        seLanguages(await retrieveAllLanguages());
      };

      onRetrieveAllLanguages();

    }
  }, []);

  return (<div>
    <h2 className={classes.header}>Add language skill to your profile</h2>
    <div className={classes.container}>
      {languages.map((language: Language) => {
        return (
          <div key={language.id} className={classes.item}>
            <h3 className={classes.title}>{language.name}</h3>
            <div>
              <Image src={language.imageUrl} alt={language.name} width={150} height={150} priority />
            </div>
            {userId &&
              <div>
                <button
                  type='button'
                  className={classes["language-btn"]}
                  onClick={() => add(language.id)}>Add</button>
              </div>}
          </div>
        )
      })}
    </div >
  </div>
  )
}

export default Languages;