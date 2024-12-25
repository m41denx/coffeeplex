import {useRouter} from "next/router";
import {useEffect, useState} from "react";
import {API_URL, Maybe} from "@/pages/config";
import axios from "axios";
import toast, {Toaster} from "react-hot-toast";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faBell, faPlusCircle, faQuoteLeft, faTrash, faUser} from "@fortawesome/free-solid-svg-icons";
import {Button, Input, Modal, Upload} from "antd";


type Comment = {
    id: number,
    user: string,
    content: string
}

type Photo = {
    id: number,
    caption?: string,
    likes: number,
    dislikes: number,
    created_at: string,
    comments?: Comment[]
}

const zodiacIcons = {
    "aries": "♈️",
    "taurus": "♉️",
    "gemini": "♊️",
    "cancer": "♋️",
    "leo": "♌️",
    "virgo": "♍️",
    "libra": "♎️",
    "scorpio": "♏️",
    "sagittarius": "♐️",
    "capricorn": "♑️",
    "aquarius": "♒️",
    "pisces": "♓️"
} as { [key: string]: string }

const genderIcons = {
    "male": "♂️",
    "female": "♀️",
    "other": "⚧️"
} as { [key: string]: string }

export default function ProfilePage() {

    const router = useRouter()
    const {uname} = router.query as { uname: string }

    const [user, setUser] = useState<Maybe<{
        username: string,
        blacklist?: boolean,
        relationship?: string,
        gender: string,
        age: string,
        zodiac: string,
        bio: string,
        photos: Photo[]
    }>>(null)

    useEffect(() => {
        uname && axios.post(`${API_URL}/profiles/get`, {
            uid: localStorage.getItem("id"),
            username: uname
        }).then((res) => {
            setUser(res.data)
        }).catch(e => {
            toast.error((e as Error).message)
            router.push("/profile/" + localStorage.getItem("username"))
        })
    }, [uname])


    const [modalOpen, setModalOpen] = useState<boolean>(false)

    const [postData, setPostData] = useState<{
        file: any,
        caption: string
    }>({file: null, caption: ""})
    const [previewImg, setPreviewImg] = useState<string>("")

    const asBase64 = (file: any) => {
        return new Promise((resolve, reject) => {
            const reader = new FileReader()
            reader.readAsDataURL(file)
            reader.onload = () => resolve(reader.result)
            reader.onerror = error => reject(error)
        })
    }

    const [comment, setComment] = useState<string>("")


    const uploadPhoto = async () => {

        const file: string = await new Promise((resolve) => {
            const reader = new FileReader();
            reader.readAsDataURL(postData.file);
            reader.onload = () => resolve(reader.result as string);
        });

        const {data} = await axios.post(`${API_URL}/photos/post`, {
            uid: localStorage.getItem("id"),
            caption: postData.caption,
            data: file.split(",")[1]
        })
        if (data.status !== "success") {
            throw new Error(data.message)
        }
        toast("Фото загружено")
        await router.reload()
    }

    const likePhoto = async (id: number) => {
        const {data} = await axios.get(`${API_URL}/photos/like`, {
            params: {
                uid: localStorage.getItem("id"),
                id: id
            }
        })
        if (data.status !== "success") {
            throw new Error(data.message)
        }

        await router.reload()
    }

    const dislikePhoto = async (id: number) => {
        const {data} = await axios.get(`${API_URL}/photos/dislike`, {
            params: {
                uid: localStorage.getItem("id"),
                id: id
            }
        })
        if (data.status !== "success") {
            throw new Error(data.message)
        }
        await router.reload()
    }

    const postComment = async (id: number) => {
        const {data} = await axios.post(`${API_URL}/comments/post`, {
            uid: localStorage.getItem("id"),
            id: id,
            comment: comment
        })
        if (data.status !== "success") {
            throw new Error(data.message)
        }
        await router.reload()
    }

    const deleteComment = async (id: number) => {
        const {data} = await axios.get(`${API_URL}/comments/delete`, {
            params: {
                uid: localStorage.getItem("id"),
                id: id,
            }
        })
        if (data.status !== "success") {
            throw new Error(data.message)
        }
        await router.reload()
    }

    const followUser = async (username: string) => {
        const {data} = await axios.get(`${API_URL}/profiles/follow`, {
            params: {
                uid: localStorage.getItem("id"),
                username: username
            }
        })
        if (data.status !== "success") {
            throw new Error(data.message)
        }
        await router.reload()
    }


    return <>
        <Toaster/>
        {user?.username &&
            <div className="flex flex-col gap-4 p-4 w-2/3 mx-auto">
                <div className="flex gap-4">
                    <FontAwesomeIcon icon={faUser} className="h-32 w-32 p-4 bg-white shadow-2xl rounded-xl"/>
                    <div className="flex-1 flex flex-col gap-2 bg-white shadow-2xl rounded-xl p-4">
                        <p className="font-semibold text-2xl">@{user?.username}</p>
                        <div className="flex gap-4 items-center">
                            <span className="px-1.5 py-0.5 shadow rounded bg-[#f0f0eb]">
                                {genderIcons[user?.gender.toLowerCase()] || ""} {user?.gender}
                            </span>
                            <span className="px-1.5 py-0.5 shadow rounded bg-[#f0f0eb]">
                                {zodiacIcons[user?.zodiac.toLowerCase()] || ""} {user?.zodiac}
                            </span>
                            <span className="px-1.5 py-0.5 shadow rounded bg-[#f0f0eb]">
                                👋 {user?.age} y.o
                            </span>
                            <Button className="ml-auto" onClick={()=>followUser(uname)} icon={<FontAwesomeIcon icon={faBell} />}>Подписаться</Button>
                        </div>
                        <pre className="p-1.5 pt-6 shadow rounded bg-[#f0f0eb] font-sans relative">
                            <FontAwesomeIcon icon={faQuoteLeft} className="absolute top-2 lef-2"/>
                            {user.bio}
                        </pre>
                    </div>
                </div>

                <div className="flex justify-end">
                    <Button className="shadow-2xl" size="large" type="primary" onClick={() => setModalOpen(true)}
                            icon={<FontAwesomeIcon icon={faPlusCircle}/>}>Новый пост</Button>
                </div>

                <div className="flex gap-4">
                    <div className="w-56"></div>
                    <div className="flex flex-col gap-4 ">
                        {(!user.photos || user.photos.length === 0) &&
                            <p className="bg-white shadow-2xl rounded-xl p-4 text-sm text-gray-500 mx-auto">У
                                пользователя нет постов</p>}

                        {user.photos?.reverse().map(photo => {
                            return <div key={photo.id}
                                        className="flex flex-col gap-2 bg-white shadow-2xl rounded-xl p-4">
                                <img className="rounded-lg"
                                     src={`${API_URL}/photos/get?uid=${localStorage.getItem("id")}&id=${photo.id}`}/>
                                {photo.caption &&
                                    <pre className="p-1.5 pt-6 shadow rounded bg-[#f0f0eb] font-sans relative">
                            <FontAwesomeIcon icon={faQuoteLeft} className="absolute top-2 lef-2"/>
                                        {photo.caption}
                        </pre>}
                                <div className="flex items-center gap-4">
                                <span onClick={() => likePhoto(photo.id)}
                                      className="px-1.5 py-0.5 shadow rounded bg-[#f0f0eb] hover:bg-gray-300 cursor-pointer transition-colors duration-300">
                                    👍 {photo.likes}
                                </span>
                                    <span onClick={() => dislikePhoto(photo.id)}
                                          className="px-1.5 py-0.5 shadow rounded bg-[#f0f0eb] hover:bg-gray-300 cursor-pointer transition-colors duration-300">
                                    👎 {photo.dislikes}
                                </span>
                                    <span
                                        className="px-1.5 py-0.5 shadow rounded bg-[#f0f0eb] hover:bg-gray-300 cursor-pointer transition-colors duration-300">
                                    🗨 {photo.comments?.length}
                                </span>
                                    <span
                                        className="px-1.5 py-0.5 shadow rounded bg-[#f0f0eb] ml-auto">
                                    🕓 {photo.created_at}
                                </span>
                                </div>
                                <div className="flex flex-col gap-2 shadow rounded bg-[#f0f0eb] p-2">
                                    {photo.comments?.map(comment => {
                                        return <div key={comment.id}>
                                            <p className="font-semibold flex gap-2 items-center">
                                                {comment.user}
                                                {comment.user === localStorage.getItem("username") &&
                                                <Button icon={<FontAwesomeIcon icon={faTrash}/>} onClick={()=> deleteComment(comment.id)} /> }
                                            </p>
                                            <p>{comment.content}</p>
                                        </div>
                                    })}
                                    <div className="flex gap-2 items-center">
                                        <FontAwesomeIcon icon={faUser} />
                                        <Input value={comment} onChange={(e) => setComment(e.target.value)}
                                               placeholder="Комментарий"/>
                                        <Button type="primary" onClick={() => postComment(photo.id)}>Отправить</Button>
                                    </div>
                                </div>
                            </div>
                        })}
                    </div>
                </div>
            </div>
        }

        <Modal title="Новый пост" okText="Запостить" cancelText="Отмена" open={modalOpen} onOk={() => uploadPhoto()}
               onCancel={() => setModalOpen(false)}>
            <Input className="mb-4" placeholder="Комментарий" value={postData.caption}
                   onChange={(e) => setPostData({...postData, caption: e.target.value})}/>
            <Upload name="file" accept="image/*" beforeUpload={(file) => {
                setPostData({...postData, file: file})
                asBase64(file).then(res => setPreviewImg(res as string))
                return false
            }} maxCount={1}>
                <Button className="mb-4">Добавить фотографию</Button>
            </Upload>
            {postData.file && <img className="rounded-lg w-64" src={previewImg}/>}
        </Modal>
    </>
}