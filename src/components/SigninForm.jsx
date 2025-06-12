import { useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import Error from "./Error"

import { signinUser, loadToken } from "../api"

function SigninForm() {
    const [formData, setFormdata] = useState({
            name: null,
            lastname: null,
            role: 'ADMINISTRADOR',
            password: null,
            email: null,
            phone: null
        })
    const [error, setError] = useState(null)
    const navigate = useNavigate()
    
    function handleSubmit(e) {
        e.preventDefault()
        console.log(formData)
        async function submitSigning() {
            try {
                const token = await signinUser(formData)
                console.log(token)
                loadToken(token)
                navigate('/app')
            } catch(err) {
                console.error(err)
                setError(err)
            }
        }
        submitSigning()
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
            <h1>Registrate</h1>
            <form
                onSubmit={handleSubmit} 
                method="post"
            >
                <div className="inputs">
                    <div className="input-container">
                        <label htmlFor="name-input">Nombre</label>
                        <input 
                            id="name-input"
                            type="text"
                            name="name"
                            value={formData.name}
                            placeholder="Ej: Maria"
                            onChange={handleChange}
                        />
                    </div>

                    <div className="input-container">
                        <label htmlFor="lastName-input">Apellido</label>
                        <input 
                            id="lastName-input"
                            type="text"
                            name="lastname"
                            value={formData.lastname}
                            placeholder="Ej: Martinez"
                            onChange={handleChange}
                        />
                    </div>

                    <div className="input-container">
                        <label htmlFor="phone-input">Telefono</label>
                        <input 
                            id="phone-input"
                            type="number"
                            name="phone"
                            value={formData.phone}
                            placeholder="##-####-##"
                            onChange={handleChange}
                        />
                    </div>

                    <div className="input-container">
                        <label htmlFor="email-input">Email</label>
                        <input 
                            id="email-input"
                            type="email"
                            name="email"
                            value={formData.email}
                            placeholder="nombre@ejemplo.com"
                            onChange={handleChange}
                        />
                    </div>

                    <div className="input-container">
                        <label htmlFor="password-input">Contraseña</label>
                        <input 
                            id="password-input"
                            type="password"
                            name="password"
                            placeholder="* Al menos 12 caracteres y un caracter especial *"
                            value={formData.password}
                            onChange={handleChange}
                        />
                    </div>

                    <button>Crear Cuenta</button>
                </div>
            </form>

            <Link to="../login">¿Ya tienes cuenta? Inicia sesión.</Link>
        </section>
    )
}

export default SigninForm