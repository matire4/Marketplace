import { useState } from "react"

function NewDepartamentoForm() {
    const [formData, setFormdata] = useState({
        breveDescripcion: null,
        ciudad: null,
        pais: null,
        direccion: null,
        numeroDepartamento: null,
        capacidad: null,
        dormitorios: null,
        camas: null,
        banos: null,
        descripcion: null,
        imagenesNuevas: null
    })

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

    return (
        <form onSubmit={handleSubmit} method="post">
            <div className="input-container">
                <label htmlFor="breveDescripcion-input">Titulo de tu Departamento</label>
                <input 
                    type="text"
                    id="breveDescripcion-input"
                    name="breveDescripcion"
                    placeholder="Ej: Hermoso departamento con vista el mar."
                    value={formData.breveDescripcion}
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
                    placeholder="Ingresa la ciudad donde se encuentra tu departamento"
                    value={formData.ciudad}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="input-container">
                <label htmlFor="pais-input">País</label>
                <input 
                    type="text"
                    id="pais-input"
                    name="pais"
                    placeholder="Ingresa el país donde se encuentra tu departamento"
                    value={formData.pais}
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
                    placeholder="Ingresa la direccion donde se encuentra tu departamento"
                    value={formData.direccion}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="input-container">
                <label htmlFor="numeroDepartamento-input">Número departamento</label>
                <input 
                    type="text"
                    id="numeroDepartamento-input"
                    name="numeroDepartamento"
                    placeholder="Ingresa el número del departamento"
                    value={formData.numeroDepartamento}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="input-container">
                <label htmlFor="capacidad-input">Capacidad</label>
                <input 
                    type="number"
                    id="capacidad-input"
                    name="capacidad"
                    placeholder="Describi las características del departamento"
                    value={formData.capacidad}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="input-container">
                <label htmlFor="dormitorios-input">Cantidad de Dormitorios</label>
                <input 
                    type="number"
                    id="dormitorios-input"
                    name="dormitorios"
                    placeholder="Ej: 3 dormitorios."
                    value={formData.dormitorios}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="input-container">
                <label htmlFor="camas-input">Cantidad de Camas</label>
                <input 
                    type="number"
                    id="camas-input"
                    name="camas"
                    placeholder="Ej: 4 camas."
                    value={formData.camas}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="input-container">
                <label htmlFor="banos-input">Cantidad de Baños</label>
                <input 
                    type="number"
                    id="banos-input"
                    name="banos"
                    placeholder="Ej: 4 baños."
                    value={formData.banos}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="input-container">
                <label htmlFor="descripcion-input">Descripción</label>
                <input 
                    type="text"
                    id="descripcion-input"
                    name="descripcion"
                    placeholder="Describi las características del departamento"
                    value={formData.descripcion}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="input-container">
                <label htmlFor="descripcion-input">Fotos del departamento</label>
                <input 
                    type="file"
                    id="file-input"
                    name="imagenesNuevas"
                    value={formData.imagenesNuevas}
                    onChange={handleChange}
                    required
                />
            </div>

            <button>Subir Habitación</button>

        </form>
    )
}

export default NewDepartamentoForm