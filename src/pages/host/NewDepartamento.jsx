import NewDepartamentoForm from '../../components/host/NewDepartamentoForm'

import '../../styles/host/NewAlojamiento.css'

function NewDepartamento() {
    return (
        <section className="container new-alojamiento">
            <h1>Carga los datos de tu Departamento</h1>
            <NewDepartamentoForm />
        </section>
    )
}

export default NewDepartamento