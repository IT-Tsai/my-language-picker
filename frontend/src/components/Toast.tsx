'use client'
import { toast, Bounce, ToastOptions } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


const toastSetting: ToastOptions = {
  position: "top-center",
  autoClose: 2000,
  hideProgressBar: true,
  closeOnClick: false,
  pauseOnHover: false,
  draggable: false,
  progress: 0,
  theme: "colored",
  transition: Bounce,
};

export const successToast = (message: string) => toast.success(message, toastSetting);
export const errorToast = (message: string) => toast.error(message, toastSetting);
export const warningToast = (message: string) => toast.warning(message, toastSetting);
export const infoToast = (message: string) => toast.info(message, toastSetting);
