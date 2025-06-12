import RegisterHostForm from '../../components/host/RegisterHostForm'
import { RiHomeHeartFill as HomeIcon } from "react-icons/ri";
import '../../styles/LoginPage.css'

function RegisterHostPage() {
    return (
        <section className="login">
            <div className="two-cols">
                <div className="first-col">
                    <div className="logo">
                        <HomeIcon />
                        <span>Destino Libre Host</span>
                    </div>
                    <RegisterHostForm />
                </div>
                <div className="second-col"></div>
            </div>
        </section>
    )
}

export default RegisterHostPage