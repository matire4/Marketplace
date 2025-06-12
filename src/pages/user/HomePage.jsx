import { useState, useEffect } from 'react'

import RentCard from "../../components/RentCard"
import Error from '../../components/Error'

import { getAlojamientos } from '../../api'

import '../../styles/user/Home.css'

const src = 'https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1a/cc/33/ff/duque-hotel-boutique.jpg?w=1200&h=-1&s=1'

function HomePage() {

    const [alojamientos, setAlojamientos] = useState([])
    const [error, setError] = useState(null)

    useEffect(() => {
        async function loadAlojamientos() {
            try {
                const data = await getAlojamientos()
                setAlojamientos(data)
                console.log(alojamientos)
            } catch(err) {
                setError(err)
            }
        }
        loadAlojamientos()
    }, [])

    const alojamientosCards = alojamientos.map(alojamiento => 
        <RentCard />
    )

    if (error) return (
        <Error 
            message={error.message}
            status={error.status}
        />
    )

    return (
        <section className="home-section">
            <div className="container home">
                <RentCard 
                    id={1}
                    img={src}
                    place="Buenos Aires, Argentina"
                    rate="3.8"
                    type="Matrimonial"
                    amenities="Desayuno incluido"
                    price="180.000"
                />

                {alojamientosCards}
            </div>
        </section>
    )
}

export default HomePage