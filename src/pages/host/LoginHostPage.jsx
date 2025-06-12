import LoginHostForm from '../../components/host/LoginHostForm'
import { RiHomeHeartFill as HomeIcon } from "react-icons/ri";
import '../../styles/LoginPage.css'

function LoginHostPage() {
    return (
        <section className="login">
            <div className="two-cols">
                <div className="first-col">
                    <div className="logo">
                        <HomeIcon />
                        <span>Destino Libre Host</span>
                    </div>
                    <LoginHostForm />
                </div>
                <div className="second-col"></div>
            </div>
        </section>
    )
}

export default LoginHostPage