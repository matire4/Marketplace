import { Link } from 'react-router-dom'
import '../../styles/host/Dashboard.css'

function DashboardPage() {
    return (
        <main className="container host-dashboard">
            <div className="stats">
                <div className="stat-card shadow">
                    <h3>Total Alojamientos</h3>
                    <p className="value">12</p>
                </div>
                <div className="stat-card shadow">
                    <h3>Reservas Activas</h3>
                    <p className="value">8</p>
                </div>
                <div className="stat-card shadow">
                    <h3>Ingresos del Mes</h3>
                    <p className="value">$45,600</p>
                </div>
            </div>

            <section className="alojamientos-list shadow">
                <div className="header">
                    <h2>Mis Alojamientos</h2>
                    <Link to="./alojamientos/nuevo" className="add-btn">
                        + Agregar Alojamiento
                    </Link>
                </div>
                <div className="alojamientos-grid">
                    {/* Aquí irían las cards de los alojamientos */}
                </div>
            </section>
        </main>
    )
}

export default DashboardPage