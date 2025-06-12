import { FaStar as StarIcon } from "react-icons/fa";

import '../styles/RentCard.css'
import { Link } from "react-router-dom";

function RentCard({ img, place, rate, type, amenities, price, id }) {
    return (
        <Link to={`alojamientos/${id}`}>
            <div className="rent-card">
                <div className="img-container">
                    <img src={img} />
                </div>
                <div className="info">
                    <div className="row">
                        <p>{place}</p>
                        <span>
                            <StarIcon className="icon"/>
                            {rate}
                        </span>
                    </div>
                    <p className="type">Habitación {type}</p>
                    <p className="amenities">{amenities}</p>
                    <p className="price">${price} <span>por noche</span></p>                                                
                </div>
            </div>
        </Link>
    )
}

export default RentCard