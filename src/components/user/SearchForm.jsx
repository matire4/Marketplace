import { useState } from "react"

import { FaSearch as SearchIcon } from "react-icons/fa";

function SearchForm() {
    const [formData, setFormdata] = useState({
        lugar: "",
        checkIn: "",
        checkOut: "",
        cantidad: ""
    })

    function handleSubmit(e) {
        e.preventDefault()
        console.log(formData)
    }

    function handleChange(e) {
        const { name, value } = e.target
        setFormdata(prevFormData => ({
            ...prevFormData,
            [name]: value
        }))
    }

    return (
        <div className="container form">
            <form 
                onSubmit={handleSubmit} 
                method="post"
                className="shadow"
            >
                <div className="inputs">
                    <div className="input-container">
                        <label htmlFor="where-input">Lugar</label>
                        <input 
                            type="text"
                            name="lugar"
                            placeholder="Elegí tu lugar de destino"
                            value={formData.lugar}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="input-container">
                        <label htmlFor="where-input">Check-in</label>
                        <input 
                            type="date"
                            name="checkIn"
                            placeholder="¿Cuándo?"
                            value={formData.checkIn}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="input-container">
                        <label htmlFor="where-input">Check-out</label>
                        <input 
                            type="date"
                            name="checkOut"
                            placeholder="¿Cuándo?"
                            value={formData.checkOut}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="input-container">
                        <label htmlFor="where-input">Cantidad de viajeros</label>
                        <input 
                            type="number"
                            name="cantidad"
                            placeholder="Ej: 2 Pasajeros"
                            value={formData.cantidad}
                            onChange={handleChange}
                        />
                    </div>
                </div>

                <button><SearchIcon /></button>
            </form>
        </div>
    )
}

export default SearchForm