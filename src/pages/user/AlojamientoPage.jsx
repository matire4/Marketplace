import ReservationForm from "../../components/user/ReservationForm";

import { FaStar as StarIcon } from "react-icons/fa";

import { getAlojamiento, getHabitacionesByAlojamiento } from "../../api";
import RentCard from "../../components/RentCard";
import ReservationForm from "../../components/user/ReservationForm";

import '../../styles/user/Alojamiento.css'
import { Link } from "react-router-dom";

const alojamiento = {
    alojamientoId: 1,
    hotel: 'Grand Paladium',
    ubication: 'Buenos Aires, Argentina',
    images: [
        'https://www.hilton.com/im/en/BUEHIHH/4583681/twin-executive.jpg?impolicy=crop&cw=4288&ch=2791&gravity=NorthWest&xposition=0&yposition=28&rw=768&rh=500',
        'https://www.hilton.com/im/en/BUEHIHH/20162396/accesible-room-01-hilton-buenos-aires.jpg?impolicy=crop&cw=3752&ch=2442&gravity=NorthWest&xposition=0&yposition=278&rw=768&rh=500',
        'https://www.hilton.com/im/en/BUEHIHH/4580694/executive-suite-living.jpg?impolicy=crop&cw=2250&ch=1464&gravity=NorthWest&xposition=0&yposition=17&rw=768&rh=500',
        'https://www.hilton.com/im/en/BUEHIHH/4582370/0000535346.tif?impolicy=crop&cw=4288&ch=2791&gravity=NorthWest&xposition=0&yposition=28&rw=768&rh=500',
        'https://www.hilton.com/im/en/BUEHIHH/13889857/hilton-buenos-aires-swimming-pool-1-.jpg?impolicy=crop&cw=4988&ch=3247&gravity=NorthWest&xposition=0&yposition=38&rw=768&rh=500'
    ],
    placeFor: 2,
    rooms: 1,
    beds: 1,
    bathrooms: 1,
    price: 180,
    description: 'Disfrutá de una estadía confortable en pleno corazón de Buenos Aires. Nuestra habitación con cama matrimonial es ideal para parejas que buscan descanso y comodidad en un entorno moderno y acogedor. Ubicada cerca de los principales puntos turísticos de la ciudad, cuenta con aire acondicionado, Wi-Fi gratuito, baño privado y detalles cuidadosamente pensados para que te sientas como en casa. Perfecta tanto para escapadas románticas como para viajes de negocios con estilo.',
    rate: 3.8,
    hostId: 1,
    username: 'matias'
}

const host = {
    name: 'Manuel Banchero',
    hostSince: 3, //months 
    profilePhoto: 'https://media.licdn.com/dms/image/v2/D5603AQHfF2QGN5X4lA/profile-displayphoto-shrink_800_800/profile-displayphoto-shrink_800_800/0/1719515589706?e=1755129600&v=beta&t=pd9uExX1NE6GyYTNuVlq27jSsRSA1ZxifSEe3KtpchU'
}

function Alojamiento() {
    const { id } = useParams(); // 'id' alojamiento desde URL (ej: /alojamientos/1)

    // Guardar datos que vienen de la API
    const [alojamiento, setAlojamiento] = useState(null);
    const [habitaciones, setHabitaciones] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function loadData() {
            setLoading(true);
            try {
                // Usamos las nuevas funciones de la API
                const alojamientoData = await getAlojamiento(id);
                const habitacionesData = await getHabitacionesByAlojamiento(id);
                setAlojamiento(alojamientoData);
                setHabitaciones(habitacionesData);
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        }
        loadData();
    }, [id]); // Se ejecutar nuevamente si 'id' de la URL cambia

    function getImages() {
        if (!alojamiento.images) return null;
        return alojamiento.images.slice(0, 5).map((img, i) => (
            <img key={i} className={`img-${i + 1}`} src={img} alt={`Vista ${i+1} de ${alojamiento.hotel}`} />
        ));
    }

    if (loading) {
        return <h2 className="container">Cargando alojamiento...</h2>;
    }

    if (error) {
        return <h2 className="container">Error: {error.message}</h2>;
    }

    return (
        <section className="container alojamiento">
            <h2>{alojamiento.hotel}</h2>
            <div className="images-container">{getImages()}</div>
            <h3>Alojamiento en {alojamiento.ubication}</h3>
            <div className="two-columns">
                <div className="first-col">
                    <p>
                        {alojamiento.placeFor} huéspedes 
                        - {alojamiento.rooms} {alojamiento.rooms > 1 ? 'dormitorios' : 'dormitorio'} {' '}
                        - {alojamiento.beds} {alojamiento.beds > 1 ? 'camas' : 'cama'} {' '}
                        - {alojamiento.bathrooms} {alojamiento.bathrooms > 1 ? 'baños' : 'baño'}
                    </p>
                    <p>{alojamiento.description}</p>

                    <div className="host-card shadow">
                            <div className="host-profile">
                                <img src={host.profilePhoto} alt="" />
                            </div>
                            <div className="second-col">
                                <p>{host.name}</p>
                                <p>{host.hostSince} meses como Host</p>
                            </div>
                            <Link to={`../usuario/${alojamiento.username}`}>Más alojamientos</Link>
                    </div>
                    <div className="habitaciones-hotel">
                        <h4>Habitaciones del hotel</h4>
                        <div className="habitaciones-list">
                            {habitaciones.length > 0 ? (
                                habitaciones.map(habitacion => (
                                    <RentCard
                                        key={habitacion.id} // KEY UNICAAAAAAAAAAA
                                        id={habitacion.id}
                                        img={habitacion.imageUrl}
                                        place={habitacion.nombre}
                                        rate={habitacion.calificacion}
                                        type={habitacion.tipo}
                                        amenities={habitacion.servicios}
                                        price={habitacion.precioPorNoche}
                                    />
                                ))
                            ) : (
                                <p>No hay habitaciones disponibles en este momento.</p>
                            )}
                        </div>
                    </div>
                </div>

                <div className="second-col">
                    <div className="reservation-card shadow">
                        <div className="row">
                            <p className="price">${alojamiento.price} <span>por noche</span></p>
                            <span className="rate">
                                <StarIcon className="icon"/>
                                {alojamiento.rate}
                            </span>
                        </div>

                        <ReservationForm alojamientoId={alojamiento.alojamientoId}/>

                        <div className="row price">
                            <p>Total:</p>
                            <p className="total-price">${getTotalPrice()}</p>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    )
}

export default Alojamiento