import { Outlet } from "react-router-dom"
import HostHeader from "./HostHeader"
import Footer from '../Footer'

function HostLayout() {
    return (
        <>
            <HostHeader />
            <Outlet />
            <Footer />
        </>
    )
}

export default HostLayout