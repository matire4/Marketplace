import { NavLink, Link } from "react-router-dom";

import SearchForm from "./SearchForm";

import { RiHomeHeartFill as HomeIcon } from "react-icons/ri";
import { CgProfile as ProfileIcon } from "react-icons/cg";
import { FaShoppingCart as CartIcon } from "react-icons/fa";


import '../../styles/user/Header.css'

function Header() {
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
                    <span>Destino Libre</span>
                </Link>
               
                <div className="sections">
                    <NavLink 
                        to="."
                        end
                        style={({ isActive }) => isActive ? activeStyle : null}
                    >
                        Home
                    </NavLink>
                    <NavLink 
                        to="favoritos"
                        style={({ isActive }) => isActive ? activeStyle : null}
                    >
                        Favoritos
                    </NavLink>
                    <NavLink 
                        to="ayuda"
                        style={({ isActive }) => isActive ? activeStyle : null}
                    >
                        Ayuda
                    </NavLink>
                    <NavLink 
                        to="../login-host"
                        style={({ isActive }) => isActive ? activeStyle : null}
                    >
                        Transformate en Host
                    </NavLink>
                    <NavLink 
                        to="carrito"
                        style={({ isActive }) => isActive ? activeStyle : null}
                    >
                        <CartIcon className="cart"/>
                    </NavLink>
                    <NavLink 
                        to="perfil"
                        style={({ isActive }) => isActive ? activeStyle : null}
                    >
                        <ProfileIcon className="icon"/>
                    </NavLink>
                </div>
            </nav>

            <SearchForm />

        </header>
    )
}

export default Header