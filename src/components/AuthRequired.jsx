import { useEffect, useState } from "react"
import { Outlet, Navigate } from "react-router-dom"
import { verifyToken } from "../api"

function AuthRequired() {
    const [isLoggedIn, setIsLoggedIn] = useState(null)

    useEffect(() => {
        async function checkAuth() {
            const result = await verifyToken()
            setIsLoggedIn(result)
        }
        checkAuth()
    }, [])

    if (isLoggedIn === null) return <h1>Verificando autenticación...</h1>
    if (!isLoggedIn) return <Navigate to="/login" />

    console.log('Token valid')
    return <Outlet />
}

export default AuthRequired