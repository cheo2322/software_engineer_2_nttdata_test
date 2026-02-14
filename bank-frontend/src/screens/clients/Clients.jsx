import { useEffect, useState } from 'react';
import './Clients.css';
import '../../../styles/Entities.css';
import '../../../styles/Modal.css';

export default function Clients() {
  const [clients, setClients] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [newClient, setNewClient] = useState({
    name: '',
    genre: '',
    age: '',
    identification: '',
    address: '',
    phone: '',
    password: '',
    status: true,
  });

  useEffect(() => {
    fetch('http://localhost:8080/bank/v1/clients')
      .then((res) => {
        if (!res.ok) {
          throw new Error('Error en la respuesta del servidor');
        }

        return res.json();
      })
      .then((response) => setClients(response.data))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  const filteredClients = clients.filter((c) =>
    c.name.toLowerCase().includes(search.toLowerCase()),
  );

  const handleCreateClient = () => {
    fetch('http://localhost:8080/bank/v1/clients', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newClient),
    })
      .then((res) => {
        if (!res.ok) throw new Error('Error al crear cliente');
        return res.json();
      })
      .then(() => {
        alert('Cliente creado correctamente');
        setShowModal(false);
        // refrescar lista
        return fetch('/bank/v1/clients')
          .then((res) => res.json())
          .then((response) => setClients(response.data));
      })
      .catch((err) => alert(err.message));
  };

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
              <td>{c.status.toString()}</td>
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
          <button onClick={() => setShowModal(true)}>Nuevo</button>
        </div>
      </div>

      <div className="entity-data-container">{content}</div>

      {showModal && (
        <div className="modal">
          <div className="modal-content">
            <h3>Nuevo cliente</h3>
            <input
              type="text"
              placeholder="Nombre"
              value={newClient.name}
              onChange={(e) =>
                setNewClient({ ...newClient, name: e.target.value })
              }
            />
            <input
              type="text"
              placeholder="Género"
              value={newClient.genre}
              onChange={(e) =>
                setNewClient({ ...newClient, genre: e.target.value })
              }
            />
            <input
              type="number"
              placeholder="Edad"
              value={newClient.age}
              onChange={(e) =>
                setNewClient({
                  ...newClient,
                  age: Number.parseInt(e.target.value),
                })
              }
            />
            <input
              type="text"
              placeholder="Identificación"
              value={newClient.identification}
              onChange={(e) =>
                setNewClient({ ...newClient, identification: e.target.value })
              }
            />
            <input
              type="text"
              placeholder="Dirección"
              value={newClient.address}
              onChange={(e) =>
                setNewClient({ ...newClient, address: e.target.value })
              }
            />
            <input
              type="text"
              placeholder="Teléfono"
              value={newClient.phone}
              onChange={(e) =>
                setNewClient({ ...newClient, phone: e.target.value })
              }
            />
            <input
              type="password"
              placeholder="Contraseña"
              value={newClient.password}
              onChange={(e) =>
                setNewClient({ ...newClient, password: e.target.value })
              }
            />
            <div className="modal-actions">
              <button onClick={handleCreateClient}>Guardar</button>
              <button onClick={() => setShowModal(false)}>Cancelar</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
