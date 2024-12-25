import {useState} from "react";
import {Button, Form, Input, Select} from "antd";
import toast, {Toaster} from "react-hot-toast";
import axios from "axios";
import {API_URL} from "@/pages/config";
import {router} from "next/client";

export default function Home() {
    const [page, setPage] = useState<"login" | "register">("login");
    const [isLoading, setLoading] = useState<boolean>(false)


    const login = async (values: any) => {
        setLoading(true)
        try {
            const {data} = await axios.post(`${API_URL}/auth/login`, values)
            if (data.status!=="success") {
                throw new Error(data.message)
            }
            localStorage.setItem("id", data.id)
            localStorage.setItem("username", data.username)
            await router.push("/profile/"+data.username)
        } catch (e) {
            toast.error((e as Error).message)
        }
        setLoading(false)
    }

    const register = async (values: any) => {
        setLoading(true)
        try {
            const {data} = await axios.post(`${API_URL}/auth/register`, values)
            if (data.status!=="success") {
                throw new Error(data.message)
            }
            toast("Регистрация прошла успешно")
        } catch (e) {
            toast.error((e as Error).message)
        }
        setLoading(false)
    }

    return (
        <div className="grid grid-cols-1 md:grid-cols-2 h-screen">
            <Toaster />
            <div className="flex items-center justify-center flex-col gap-2">
                <img
                    src="https://img.icons8.com/external-tanah-basah-basic-outline-tanah-basah/96/external-coffee-bean-food-and-drink-tanah-basah-basic-outline-tanah-basah.png"/>
                <p className="text-4xl font-bold">CoffeePlex</p>
                <p className="text-center">Соцсеть кофеманов? Конечно нет! <br/> А вас никто и не спрашивал</p>
            </div>
            <div className="flex items-center justify-center flex-col p-4">
                <div
                    className="p-4 flex flex-col items-center h-2/3 w-2/3 shadow-2xl border-2 rounded-xl bg-[#faecda88] border-[#faecda]">
                    {page === "login" ? <>
                        <p className="font-bold text-2xl">Вход</p>
                        <Form rootClassName="mt-2" onFinish={(values) => login(values)}>
                            <Form.Item name="username" required>
                                <Input placeholder="Логин"/>
                            </Form.Item>
                            <Form.Item name="password" required>
                                <Input.Password placeholder="Пароль"/>
                            </Form.Item>
                            <Form.Item>
                                <Button type="primary" loading={isLoading} htmlType="submit">Войти</Button>
                            </Form.Item>
                        </Form>

                        <Button className="mt-auto" type="text" onClick={() => setPage("register")}>Еще нет аккаунта? Зарегистрируйтесь</Button>
                    </> : <>
                        <p className="font-bold text-2xl">Регистрация</p>
                        <Form rootClassName="mt-2" onFinish={(values) => register(values)}>
                            <Form.Item name="username" required>
                                <Input placeholder="Логин"/>
                            </Form.Item>
                            <Form.Item name="password" required>
                                <Input.Password placeholder="Пароль"/>
                            </Form.Item>
                            <Form.Item name="gender" required>
                                <Select placeholder="Пол">
                                    <Select.Option value="0">Мужской</Select.Option>
                                    <Select.Option value="1">Женский</Select.Option>
                                    <Select.Option value="2">Другой</Select.Option>
                                </Select>
                            </Form.Item>
                            <Form.Item name="age" required rules={[
                                {required: true, pattern: /^\d+$/, message: "Некорректный возраст"}
                            ]}>
                                <Input placeholder="Возраст"></Input>
                            </Form.Item>
                            <Form.Item name="zodiac" required>
                                <Select placeholder="Знак зодиака">
                                    <Select.Option value="aries">Овен</Select.Option>
                                    <Select.Option value="taurus">Телец</Select.Option>
                                    <Select.Option value="gemini">Близнецы</Select.Option>
                                    <Select.Option value="cancer">Рак</Select.Option>
                                    <Select.Option value="leo">Лев</Select.Option>
                                    <Select.Option value="virgo">Дева</Select.Option>
                                    <Select.Option value="libra">Весы</Select.Option>
                                    <Select.Option value="scorpio">Скорпион</Select.Option>
                                    <Select.Option value="sagittarius">Стрелец</Select.Option>
                                    <Select.Option value="capricorn">Козерог</Select.Option>
                                    <Select.Option value="aquarius">Водолей</Select.Option>
                                    <Select.Option value="pisces">Рыбы</Select.Option>
                                </Select>
                            </Form.Item>
                            <Form.Item name="bio" required>
                                <Input.TextArea placeholder="О вас"></Input.TextArea>
                            </Form.Item>
                            <Form.Item>
                                <Button type="primary" loading={isLoading} htmlType="submit">Зарегистрироваться</Button>
                            </Form.Item>
                        </Form>

                        <Button className="mt-auto" type="text" onClick={() => setPage("login")}>Уже есть аккаунт? Войдите</Button>
                    </>}
                </div>
            </div>
        </div>
    );
}
