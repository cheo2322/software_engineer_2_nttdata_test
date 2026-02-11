import { useState } from 'react';
import './Clients.css';

export default function Clients() {
  const [clients] = useState(['Juan Pérez', 'María López', 'Carlos Sánchez']);

  const [search, setSearch] = useState('');

  const filteredClients = clients.filter((c) =>
    c.toLowerCase().includes(search.toLowerCase()),
  );

  return (
    <div className="clients-container">
      <div className="clients-header">
        <h2>Clientes</h2>
        <div className="clients-header-actions">
          <input
            type="text"
            placeholder="Buscar"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
          <button onClick={() => alert('Nuevo cliente')}>Nuevo</button>
        </div>
      </div>

      <div className="clients-list-container">
        {filteredClients.length > 0 ? (
          <ul className="clients-list">
            {filteredClients.map((client, index) => (
              <li key={index}>{client}</li>
            ))}
          </ul>
        ) : (
          <p className="clients-empty">No hay clientes disponibles.</p>
        )}
      </div>
    </div>
  );
}
