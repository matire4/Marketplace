import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import '../../styles/user/Cart.css'
import { getCarrito } from '../../api'
import Error from '../../components/Error'

function CartPage() {
    const [cartItems, setCartItems] = useState({
        habitaciones: [],
        departamentos: []
    })
    const [error, setError] = useState(null)
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        async function loadCart() {
            try {
                const data = await getCarrito()
                setCartItems(prevCartItems => ({
                    habitaciones: [...prevCartItems.habitaciones, ...data.habitaciones],
                    departamentos: [...prevCartItems.departamentos, ...data.departamentos]
                }))
            } catch(err) {
                setError(err)
            } finally {
                setLoading(false)
            }
        }
        loadCart()
    }, [])

    console.log('Cart Items:', cartItems)
    if (error) return <Error message={error.message} status={error.status} />
    if (loading) return <h2>Cargando...</h2>

    return (
        <main className="cart-page container">
            <h1>Mi Carrito</h1>
            
            <div className="cart-sections">
                {cartItems.habitaciones.length > 0 && (
                    <section className="cart-section shadow">
                        <h2>Habitaciones</h2>
                        <div className="cart-items">
                            {cartItems.habitaciones.map(item => (
                                <div key={item.id} className="cart-item">
                                    <div className="item-info">
                                        <h3>{item.nombre_reserva}</h3>
                                        <p>{item.noches_reservadas} noches</p>
                                    </div>
                                    <div className="item-price">
                                        <p className="price">${item.precio_por_noche * item.noches_reservadas}</p>
                                        <button className="remove-btn">Eliminar</button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </section>
                )}

                {cartItems.departamentos.length > 0 && (
                    <section className="cart-section shadow">
                        <h2>Departamentos</h2>
                        <div className="cart-items">
                            {cartItems.departamentos.map(item => (
                                <div key={item.id} className="cart-item">
                                    <div className="item-info">
                                        <h3>{item.nombre_reserva}</h3>
                                        <p>{item.noches_reservadas} noches</p>
                                    </div>
                                    <div className="item-price">
                                        <p className="price">${item.precio_por_noche * item.noches_reservadas}</p>
                                        <button className="remove-btn">Eliminar</button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </section>
                )}

                {cartItems.habitaciones.length === 0 && cartItems.departamentos.length === 0 && (
                    <div className="empty-cart">
                        <h2>Tu carrito está vacío</h2>
                        <Link to="/app" className="browse-btn">Ver alojamientos</Link>
                    </div>
                )}
            </div>

            {(cartItems.habitaciones.length > 0 || cartItems.departamentos.length > 0) && (
                <div className="cart-summary shadow">
                    <h3>Resumen</h3>
                    <div className="summary-details">
                        <div className="summary-row">
                            <span>Subtotal</span>
                            <span>$###</span>
                        </div>
                        <div className="summary-row total">
                            <span>Total</span>
                            <span>$###</span>
                        </div>
                    </div>
                    <button className="checkout-btn">Finalizar Reserva</button>
                </div>
            )}
        </main>
    )
}

export default CartPage