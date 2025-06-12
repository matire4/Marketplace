import { useState, useRef } from "react"

function NewHotelForm() {
    const [formData, setFormdata] = useState({
        nombre: null,
        email: null,
        telefono: null,
        pais: null,
        ciudad: null,
        direccion: null,
        descripcion: null,
        habitacionesParaCrear: [],
        imagenes: null,
        reviews: null,
        preguntas: null,
        gestorId: null,
        categoriaId: null
    })

    const [habitacion, setHabitacion] = useState({
        tipoHabitacion: null,
        capacidad: null,
        precioPorNoche: null,
        numeroHabitacion: null,
        imagen: null
    })

    const tipoHabitacionInputRef = useRef(null)
    const capacidadInputRef = useRef(null)
    const precioPorNocheInputRef = useRef(null)
    const numeroHabitacionInputRef = useRef(null)
    const fotosHabitacionInputRef = useRef(null)

    function handleHabitacionChange(e) {
        const { name, value } = e.target
        setHabitacion(prevHabitacion => ({
            ...prevHabitacion,
            [name]: value
        }))
    }

    function addHabitacionToHotel(e) {
        e.preventDefault()
        setFormdata(prevFormData => ({
            ...prevFormData,
            habitacionesParaCrear: [...prevFormData.habitacionesParaCrear, habitacion]
        }))

        tipoHabitacionInputRef.current.value = ''
        capacidadInputRef.current.value = null
        precioPorNocheInputRef.current.value = null
        numeroHabitacionInputRef.current.value = null
        fotosHabitacionInputRef.current.value = null
        setHabitacion({
            tipoHabitacion: '',
            capacidad: null,
            precioPorNoche: null,
            numeroHabitacion: null,
            imagen: null
        })
    }

    function handleChange(e) {
        const { name, value } = e.target
        setFormdata(prevFormData => ({
            ...prevFormData,
            [name]: value
        }))
    }

    function handleSubmit(e) {
        e.preventDefault()
        console.log(formData)
    }

    const hasHabitaciones = formData.habitacionesParaCrear.length > 0

    return (
        <form onSubmit={handleSubmit} method="post">
            <div className="input-container">
                <label htmlFor="nombre-input">Nombre del Hotel</label>
                <input 
                    type="text"
                    id="nombre-input"
                    name="nombre"
                    placeholder="Ingresa el nombre de tu hotel"
                    value={formData.breveDescripcion}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="input-container">
                <label htmlFor="ciudad-input">Email de Contacto</label>
                <input 
                    type="email"
                    id="email-input"
                    name="email"
                    placeholder="Ingresa el email de contacto."
                    value={formData.email}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="input-container">
                <label htmlFor="pais-input">Telefono de contacto</label>
                <input 
                    type="number"
                    id="telefono-input"
                    name="telefono"
                    placeholder="### ### ###"
                    value={formData.telefono}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="input-container">
                <label htmlFor="direccion-input">País</label>
                <input 
                    type="text"
                    id="pais-input"
                    name="pais"
                    placeholder="Ej: Argentina"
                    value={formData.pais}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="input-container">
                <label htmlFor="ciudad-input">Ciudad</label>
                <input 
                    type="text"
                    id="ciudad-input"
                    name="ciudad"
                    placeholder="Ingresa el número del departamento"
                    value={formData.ciudad}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="input-container">
                <label htmlFor="direccion-input">Direccion</label>
                <input 
                    type="text"
                    id="direccion-input"
                    name="direccion"
                    placeholder="Describi las características del departamento"
                    value={formData.direccion}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="input-container">
                <label htmlFor="descripcion-input">Descripcion</label>
                <input 
                    type="text"
                    id="descripcion-input"
                    name="descripcion"
                    placeholder="Agrega una descripción de tu hotel."
                    value={formData.descripcion}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="input-container">
                <label htmlFor="file-input">Fotos del hotel</label>
                <input 
                    type="file"
                    id="file-input"
                    name="imagenes"
                    value={formData.imagenes}
                    onChange={handleChange}
                    required
                />
            </div>

            <h2>Agrega las habitaciones de tu Hotel</h2>
            <div className="sub-form">

                <div className="input-container">
                    <label htmlFor="tipoHabitacion-input">Tipo Habitacion</label>
                    <select
                        id="tipoHabitacion-input"
                        defaultValue=""
                        onChange={handleHabitacionChange}
                        ref={tipoHabitacionInputRef}
                    >
                        <option value="" disabled>Selecciona el tipo de habitacion</option>
                        <option value="doble">Doble</option>
                        <option value="individual">Individual</option>
                    </select>
                </div>

                <div className="input-container">
                    <label htmlFor="capacidad-input">Capacidad</label>
                    <input 
                        type="number"
                        id="capacidad-input"
                        name="capacidad"
                        placeholder="Ej: Capacidad para 4."
                        value={habitacion.descripcion}
                        onChange={handleHabitacionChange}
                        ref={capacidadInputRef}
                    />
                </div>

                <div className="input-container">
                    <label htmlFor="precioPorNoche-input">Precio por noche</label>
                    <input 
                        type="number"
                        id="precioPorNoche-input"
                        name="precioPorNoche"
                        placeholder="Ingresa el precio que cuesta la noche en la habitacion."
                        value={habitacion.precioPorNoche}
                        onChange={handleHabitacionChange}
                        ref={precioPorNocheInputRef}
                    />
                </div>

                <div className="input-container">
                    <label htmlFor="numeroHabitacion-input">Número de Habitación</label>
                    <input 
                        type="number"
                        id="numeroHabitacion-input"
                        name="numeroHabitacion"
                        placeholder="Ingresa el número de la habitación."
                        value={habitacion.numeroHabitacion}
                        onChange={handleHabitacionChange}
                        ref={numeroHabitacionInputRef}
                    />
                </div>

                <div className="input-container">
                    <label htmlFor="imagen-input">Fotos de la habitación</label>
                    <input 
                        type="file"
                        id="imagen-input"
                        name="imagen"
                        value={habitacion.imagen}
                        onChange={handleHabitacionChange}
                        ref={fotosHabitacionInputRef}
                    />
                </div>

                <button 
                    className="habitacion-btn"
                    onClick={addHabitacionToHotel}
                >Cargar Habitación al Hotel</button>
                
            </div>

            {
                hasHabitaciones &&
                    (formData.habitaciones.map(habitacion =>
                        <p>Habitacion {habitacion.numeroHabitacion} <span>creada</span></p>
                    ))
            }

            { !hasHabitaciones && <p className="advice">*No podrás cargar el hotel hasta no crear al menos una habitación.</p>}

            <button
                disabled={!hasHabitaciones}
            >Subir Hotel</button>

        </form>
    )
}

export default NewHotelForm