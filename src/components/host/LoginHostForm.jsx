import { useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import { loginUser, loadToken } from "../../api"
import Error from "../Error"

function LoginHostForm() {
    const [formData, setFormdata] = useState({
        username: null,
        password: null
    })
    const [error, setError] = useState(null)
    const navigate = useNavigate()
    
    function handleSubmit(e) {
        e.preventDefault()
        console.log(formData)

        async function submitLogin() {
            try {
                const token = await loginUser(formData)
                loadToken(token)
                navigate('/host')
            } catch(err) {
                console.error(err)
                setError(err)
            }
        }
        submitLogin()
    }

    function handleChange(e) {
        const { name, value } = e.target
        setFormdata(prevFormData => ({
            ...prevFormData,
            [name]: value
        }))
    }

    if (error) return (
        <Error 
            message={error.message}
        />
    )

    return (
        <section className="login-form shadow">
            <h1>Iniciar sesión como Host</h1>
            <form
                onSubmit={handleSubmit} 
                method="post"
            >
                <div className="inputs">
                    <div className="input-container">
                        <label htmlFor="email-input">Email</label>
                        <input 
                            id="email-input"
                            type="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="input-container">
                        <label htmlFor="password-input">Contraseña</label>
                        <input 
                            id="password-input"
                            type="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                        />
                    </div>

                    <button>Iniciar sesión</button>
                </div>
            </form>

            <Link to="../signin-host">¿Todavía no sos Host? Registrate.</Link>
        </section>
    )
}

export default LoginHostForm