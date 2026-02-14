import { useEffect, useState } from 'react';
import './Clients.css';
import '../../../styles/Entities.css';

export default function Clients() {
  const [clients, setClients] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetch('http://localhost:8080/bank/v1/clients')
      .then((res) => {
        if (!res.ok) {
          throw new Error('Error en la respuesta del servidor');
        }

        return res.json();
      })
      .then((response) => {
        setClients(response.data);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  const filteredClients = clients.filter((c) =>
    c.name.toLowerCase().includes(search.toLowerCase()),
  );

  let content;
  if (loading) {
    content = <p className="entity-empty">Cargando clientes...</p>;
  } else if (error) {
    content = <p className="entity-empty">Error: {error}</p>;
  } else if (filteredClients.length > 0) {
    content = (
      <table className="entity-grid">
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Dirección</th>
            <th>Teléfono</th>
            <th>Estado</th>
          </tr>
        </thead>
        <tbody>
          {filteredClients.map((c) => (
            <tr key={c.id}>
              <td>{c.name}</td>
              <td>{c.address}</td>
              <td>{c.phone}</td>
              <td>{String(c.status)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  } else {
    content = <p className="entity-empty">No hay clientes disponibles.</p>;
  }

  return (
    <div className="entity-container">
      <div className="entity-header">
        <h2>Clientes</h2>
        <div className="entity-header-actions clients-actions">
          <div className="clients-search">
            <input
              type="text"
              placeholder="Buscar cliente"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>
          <button onClick={() => alert('Por implementar: creación de cliente')}>
            Nuevo
          </button>
        </div>
      </div>

      <div className="entity-data-container">{content}</div>
    </div>
  );
}
