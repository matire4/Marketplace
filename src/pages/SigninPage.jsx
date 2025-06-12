import SigninForm from '../components/SigninForm'

import { RiHomeHeartFill as HomeIcon } from "react-icons/ri";

import '../styles/LoginPage.css'

function SigninPage() {
    return (
        <section className="login">
            <div className="two-cols">
                <div className="first-col">
                    <div className="logo">
                        <HomeIcon />
                        <span>Destino Libre</span>
                    </div>
                    <SigninForm />
                </div>

                <div className="second-col"></div>
            </div>
        </section>
    )
}

export default SigninPage