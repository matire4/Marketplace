const API_URL = 'http://localhost:8080/api/v1'
 

// Function to create a bring all the alojamientos
export async function getAlojamientos() {
    const token = localStorage.getItem('userToken') || ''
    console.log(token)
    const res = await fetch(`${API_URL}/alojamientos/${id}`, {
        // Agregue ID ------
        method: 'GET',
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}` 
        }
    })

    if (!res.ok) {
        throw {
            message: `Se produjo un error al intentar obtener el alojamiento con id ${id}`,
            status: res.status
        }
    }
    return await res.json()
}

//Agrego funcion getHabitacionByAlojamiento ---------------------
export async function getHabitacionesByAlojamiento(alojamientoId) {
    const token = localStorage.getItem('userToken') || ''
    const res = await fetch(`${API_URL}/habitaciones/alojamiento/${alojamientoId}`, {
        method: 'GET',
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    })

    if (!res.ok) {
        throw {
            message: 'Se produjo un error al intentar obtener las habitaciones del alojamiento',
            status: res.status
        }
    }
    return await res.json()
}


export async function getHabitacionesHotel(name) {
    const token = localStorage.getItem('userToken') || ''
    const res = await fetch(`${API_URL}/habitaciones/hotel/${name}`, {
        method: 'GET',
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    })

    if (!res.ok) {
        throw {
            message: 'Se produjo un error al intentar obtener las habitaciones del hotel',
            status: res.status
        }
    }
    return await res.json()
}

export async function signinUser(userInfoObj) {
    const res = await fetch(`${API_URL}/auth/register`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(userInfoObj)
    })

    if (!res.ok) {
        console.log("Throwing error")
        throw {
            message: 'Se produjo un error al intentar crear el usuario.',
            status: res.status,
            statusText: res.statusText
        }
    }

    const userData = await res.json()
    return userData.access_token
}

export async function loginUser(userInfoObj) {
    const res = await fetch(`${API_URL}/auth/authenticate`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(userInfoObj)
    })
    const userData = await res.json()
    localStorage.setItem('usuario', userInfoObj.username)
    return userData.access_token
}

export function loadToken(token) {
    localStorage.setItem('userToken', token)
}

export function removeToken() {
    localStorage.removeItem('userToken')
}

export async function verifyToken() {
    const token = localStorage.getItem('userToken')
    if (!token) return false

    const res = await fetch(`${API_URL}/auth/verify`, {
        method: 'GET',
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    })

    if(!res.ok) return false

    return true
}

export async function getCarrito() {
    try {
        const token = localStorage.getItem('userToken') || ''
        const usuario = localStorage.getItem('usuario') || ''
        const response = await fetch(`${API_URL}/carrito/usuario/${usuario}`, {
            method: 'GET',
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        })
        
        if (!response.ok) {
            throw {
                message: 'Error al obtener el carrito',
                status: response.status
            }
        }
        const data = await response.json()
        return data
    } catch (error) {
        throw {
            message: error.message,
            status: error.status
        }
    }
}

export async function getAlojamientosByUsername(username) {
    const token = localStorage.getItem('userToken') || ''
    const res = await fetch(`${API_URL}/alojamientos/usuario/${username}`, {
        method: 'GET',
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    })

    if (!res.ok) {
        throw {
            message: 'Se produjo un error al intentar obtener los alojamientos por usuario',
            status: res.status
        }
    }
    return await res.json()
}