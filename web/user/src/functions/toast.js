import { toast } from "react-toastify";

export function toastSuccess(msg, time, position) {
  toast.success(`${msg}`, {
    icon: false,
    position: position || "top-center",
    autoClose: time || 3000,
    hideProgressBar: true,
    closeOnClick: true,
    pauseOnHover: false,
    draggable: false,
    theme: "colored",
    className:
      "text-[white] font-main text-xl bg-green border-4 border-solid border-green rounded-2xl w-[29rem]",
  });
}
export function toastFail(msg, time) {
  toast.error(`${msg}`, {
    closeButton: false,
    position: "bottom-left",
    autoClose: time || 3000,
    hideProgressBar: false,
    closeOnClick: true,
    pauseOnHover: false,
    draggable: false,
    theme: "colored",
    className:
      "text-[red] font-main text-xl bg-[white] border-4 border-solid border-green rounded-2xl w-[29rem]",
  });
}
