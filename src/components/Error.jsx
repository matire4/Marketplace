function Error({ message, statusText, status }) {
   return (
        <section className="container error">
            <h1>Error: {message}</h1>
            <pre>{status} - {statusText}</pre>
        </section>
   )
}

export default Error