import { BrowserRouter, Routes, Route } from 'react-router-dom'

import AuthRequired from './components/AuthRequired'

// LAYOUTS
import UserLayout from './components/user/UserLayout'
import HostLayout from './components/host/HostLayout'

// PAGES

//general
import LandingPage from './pages/LandingPage'
import NotFoundPage from './pages/NotFoundPage'
import LoginPage from './pages/LoginPage'
import SigninPage from './pages/SigninPage'
//host
import LoginHostPage from './pages/host/LoginHostPage'
import RegisterHostPage from './pages/host/RegisterHostPage'
import DashboardPage from './pages/host/DashboardHostPage'
import NewAlojamiento from './pages/host/NewAlojamiento'
import NewDepartamento from './pages/host/NewDepartamento'
import NewHotel from './pages/host/NewHotel'
import NewHabitacion from './pages/host/NewHabitacion'
// user
import HomePage from './pages/user/HomePage'
import Alojamientos from './pages/user/AlojamientosPage'
import Alojamiento from './pages/user/AlojamientoPage'
import ProfilePage from './pages/user/ProfilePage'
import CartPage from './pages/user/Cart'

import './App.css'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<LandingPage />}/>
        <Route path='/signin' element={<SigninPage />} />
        <Route path='/login' element={<LoginPage />}/>

        <Route element={<AuthRequired />}>
          <Route path='/app' element={<UserLayout />}>
            <Route index element={<HomePage />}/>
            <Route path='alojamientos' element={<Alojamientos />}/>
            <Route path='alojamientos/:id' element={<Alojamiento />}/>
            <Route path='perfil' element={<ProfilePage />}/>
            <Route path='carrito' element={<CartPage />}/>
          </Route>
        </Route>

        <Route path='/login-host' element={<LoginHostPage />} />
        <Route path='/signin-host' element={<RegisterHostPage />} />

        <Route element={<AuthRequired />}>
            <Route path='/host' element={<HostLayout />}>
              <Route index element={<DashboardPage />} />
              <Route path='alojamientos' element={<h1>Mis Alojamientos</h1>} />
              <Route path='alojamientos/nuevo' element={<NewAlojamiento />} />
              <Route path='alojamientos/nuevo/departamento' element={<NewDepartamento />} />
              <Route path='alojamientos/nuevo/hotel' element={<NewHotel />} />
              <Route path='alojamientos/nuevo/habitacion' element={<NewHabitacion />} />
              <Route path='reservas' element={<h1>Mis Reservas</h1>} />
            </Route>
        </Route>

        <Route path='*' element={<NotFoundPage />}/>
      </Routes>
    </BrowserRouter>
  )
}

export default App
