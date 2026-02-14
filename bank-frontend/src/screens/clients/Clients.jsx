import { useState } from 'react';
import './Clients.css';
import '../../../styles/Entities.css';

export default function Clients() {
  const [clients] = useState([
    {
      id: 1,
      name: 'Juan Pérez',
      address: 'Calle Falsa 123',
      phone: '555-1234',
      status: true,
    },
    {
      id: 2,
      name: 'María López',
      address: 'Avenida Siempre Viva 456',
      phone: '555-5678',
      status: false,
    },
    {
      id: 3,
      name: 'Carlos Sánchez',
      address: 'Boulevard de los Sueños Rotos 789',
      phone: '555-9012',
      status: true,
    },
  ]);

  const [search, setSearch] = useState('');

  const filteredClients = clients.filter((c) =>
    c.name.toLowerCase().includes(search.toLowerCase()),
  );

  let content;
  if (filteredClients.length > 0) {
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
              <td className={c.status ? 'active' : 'inactive'}>
                {c.status ? 'Activo' : 'Inactivo'}
              </td>
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
