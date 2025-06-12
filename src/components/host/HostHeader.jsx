import { NavLink, Link } from "react-router-dom";
import { RiHomeHeartFill as HomeIcon } from "react-icons/ri";
import { CgProfile as ProfileIcon } from "react-icons/cg";
import '../../styles/user/Header.css'

function HostHeader() {
    const activeStyle = {
        fontWeight: '700',
        textDecoration: 'underline',
        color: '#7A68DE'
    }

    return (
        <header>
            <nav>
                <Link 
                    className="logo"
                    to="."
                >
                    <HomeIcon />
                    <span>Destino Libre Host</span>
                </Link>
               
                <div className="sections">
                    <NavLink 
                        to="."
                        end
                        style={({ isActive }) => isActive ? activeStyle : null}
                    >
                        Dashboard
                    </NavLink>
                    <NavLink 
                        to="alojamientos"
                        style={({ isActive }) => isActive ? activeStyle : null}
                    >
                        Mis Alojamientos
                    </NavLink>
                    <NavLink 
                        to="reservas"
                        style={({ isActive }) => isActive ? activeStyle : null}
                    >
                        Reservas
                    </NavLink>
                    <NavLink 
                        to="profile"
                        style={({ isActive }) => isActive ? activeStyle : null}
                    >
                        <ProfileIcon className="icon"/>
                    </NavLink>
                </div>
            </nav>
        </header>
    )
}

export default HostHeader