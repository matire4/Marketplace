import '../../styles/host/NewAlojamiento.css'
import { Link } from 'react-router-dom'

function NewAlojamiento() {

    const hostHasHotel = false

    return (
        <section className="container new-alojamiento shadow">
            <h1>Elegí el tipo de alojamiento que queres crear</h1>
            <div className="options">
                <Link to="./departamento">Nuevo Departamento</Link>
                <Link to="./hotel">Nuevo Hotel</Link>
                <Link
                    className={hostHasHotel ? '' : 'disabled'}
                    to={hostHasHotel ? './habitacion' : '.' }
                >Nueva Habitación de Hotel</Link>
            </div>
        </section>
    )
}

export default NewAlojamiento