import { useState } from 'react'

function ReservationForm({ alojamientoId }) {

    const [formData, setFormdata] = useState({
        checkIn: null,
        checkOut: null,
        cantidad: null,
        alojamientoId
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

    const isFormFilled = 
        formData.checkIn &&
        formData.checkOut &&
        formData.cantidad ? true : false

    return (
        <form
            onSubmit={handleSubmit} 
            method="post"
        >
            <div className="inputs">
                <div className="row">
                    <div className="input-container">
                        <label htmlFor="checkIn-input">Check-in</label>
                        <input 
                            id='checkIn-input'
                            type="date"
                            name="checkIn"
                            value={formData.checkIn}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="input-container">
                        <label htmlFor="checkOut-input">Check-out</label>
                        <input 
                            id='checkOut-input'
                            type="date"
                            name="checkOut"
                            value={formData.checkOut}
                            onChange={handleChange}
                        />
                    </div>
                </div>

                <div className="input-container">
                    <label htmlFor="cantidad-input">Huespedes</label>
                    <input 
                        id='cantidad-input'
                        type="number"
                        name="cantidad"
                        placeholder="2 huespedes"
                        value={formData.cantidad}
                        onChange={handleChange}
                    />
                </div>
            </div>

            <button disabled={!isFormFilled}>Reservar</button>
        </form>
    )
}

export default ReservationForm