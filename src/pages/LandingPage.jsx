import { Link } from "react-router-dom"

function LandingPage() {
    return (
        <section className="container landing">
            <Link to="/login">Login</Link>
        </section>
    )
}

export default LandingPage