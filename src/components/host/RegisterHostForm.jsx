import { useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import { signinUser, loadToken } from "../../api"
import Error from "../Error"

function RegisterHostForm() {
    const [formData, setFormdata] = useState({
        name: null,
        password: null,
        email: null,
        phone: null,
        cuil: null,
        role: 'GESTOR',
    })
    const [error, setError] = useState(null)
    const navigate = useNavigate()
    
    function handleSubmit(e) {
        e.preventDefault()
        console.log(formData)
        async function submitSignin() {
            try {
                const token = await signinUser(formData)
                loadToken(token)
                navigate('/host')
            } catch(err) {
                console.error(err)
                setError(err)
            }
        }
        submitSignin()
    }

    function handleChange(e) {
        const { name, value } = e.target
        setFormdata(prevFormData => ({
            ...prevFormData,
            [name]: value
        }))
    }

    if(error) return (
        <Error 
            message={error.message}
            status={error.status}
            statusText={error.statusText}
        />
    )

    return (
        <section className="login-form shadow">
            <h1>Registrate como Host</h1>
            <form
                onSubmit={handleSubmit} 
                method="post"
            >
                <div className="inputs">
                    <div className="input-container">
                        <label htmlFor="companyName-input">Nombre de la empresa</label>
                        <input 
                            id="companyName-input"
                            type="text"
                            name="name"
                            value={formData.name}
                            placeholder="Nombre Compañía SA"
                            onChange={handleChange}
                        />
                    </div>

                    <div className="input-container">
                        <label htmlFor="email-input">Email empresarial</label>
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
                        <label htmlFor="phone-input">Teléfono</label>
                        <input 
                            id="phone-input"
                            type="number"
                            name="phone"
                            value={formData.phone}
                            placeholder="##-####-####"
                            onChange={handleChange}
                        />
                    </div>

                     <div className="input-container">
                        <label htmlFor="cuil-input">Cuil</label>
                        <input 
                            id="cuil-input"
                            type="number"
                            name="cuil"
                            value={formData.cuil}
                            placeholder="####-####"
                            onChange={handleChange}
                        />
                    </div>

                    <div className="input-container">
                        <label htmlFor="password-input">Contraseña</label>
                        <input 
                            id="password-input"
                            type="password"
                            name="password"
                            placeholder="* Al menos 12 caracteres *"
                            value={formData.password}
                            onChange={handleChange}
                        />
                    </div>

                    <button>Crear cuenta</button>
                </div>
            </form>

            <Link to="../login-host">¿Ya tienes cuenta? Inicia sesión.</Link>
        </section>
    )
}

export default RegisterHostForm