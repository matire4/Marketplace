import { removeToken } from "../../api"
import { useNavigate } from "react-router-dom"

function ProfilePage() {
    const navigate = useNavigate()

    function handleClick() {
        removeToken()
        navigate('/')
    }

    return (
        <section className="container perfil">
            <button onClick={handleClick}>Log out</button>
        </section>
    )
}

export default ProfilePage