import LoginForm from '../components/LoginForm'

import { RiHomeHeartFill as HomeIcon } from "react-icons/ri";

import '../styles/LoginPage.css'

function LoginPage() {
    return (
        <section className="login">
            <div className="two-cols">
                <div className="first-col">
                    <div className="logo">
                        <HomeIcon />
                        <span>Destino Libre</span>
                    </div>
                    <LoginForm />
                </div>

                <div className="second-col"></div>
            </div>
        </section>
    )
}

export default LoginPage