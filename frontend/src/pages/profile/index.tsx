import {useRouter} from "next/router";


export default async function ProfileRedirect() {
    const router = useRouter()
    await router.push("/profile/"+localStorage.getItem("username"))
    return <></>
}