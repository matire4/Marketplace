import { useState } from "react"
import { Link, useNavigate } from "react-router-dom"

import { loginUser, loadToken } from "../api"

function LoginForm() {
    const [formData, setFormdata] = useState({
            username: null,
            password: null,
        })
    const [error, setError] = useState(null)
    const navigate = useNavigate()
    
    function handleSubmit(e) {
        e.preventDefault()
        console.log(formData)
        async function submitLogin() {
            try {
                const token = await loginUser(formData)
                console.log(token)
                loadToken(token)
                navigate('/app')
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
            status={error.status}
            statusText={error.statusText}
        />
    )

    return (
        <section className="login-form shadow">
            <h1>Inicia sesión</h1>
            <form
                onSubmit={handleSubmit} 
                method="post"
            >
                <div className="inputs">
                    <div className="input-container">
                        <label htmlFor="email-input">Nombre de usuario</label>
                        <input 
                            id="email-input"
                            type="text"
                            name="username"
                            value={formData.username}
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

            <Link to="../signin">¿No tenes cuenta? Registrate.</Link>
        </section>
    )
}

export default LoginForm