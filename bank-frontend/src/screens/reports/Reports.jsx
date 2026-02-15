import { useState } from 'react';
import './Reports.css';
import '../../../styles/Entities.css';

export default function Reports() {
  const [movements] = useState([
    {
      id: 1,
      date: '2026-02-11 09:30',
      client: 'Juan Pérez',
      account: '001',
      type: 'Ahorros',
      initialBalance: 1500,
      state: 'Activo',
      value: -200,
      balance: 1300,
    },
    {
      id: 2,
      date: '2026-02-11 10:15',
      client: 'María Gómez',
      account: '002',
      type: 'Corriente',
      initialBalance: 3200,
      state: 'Activo',
      value: 500,
      balance: 3700,
    },
    {
      id: 3,
      date: '2026-02-11 11:00',
      client: 'Carlos Ruiz',
      account: '003',
      type: 'Ahorros',
      initialBalance: 500,
      state: 'Inactivo',
      value: -100,
      balance: 400,
    },
    {
      id: 4,
      date: '2026-02-11 12:00',
      client: 'Juan Pérez',
      account: '001',
      type: 'Ahorros',
      initialBalance: 1500,
      state: 'Activo',
      value: 300,
      balance: 1600,
    },
  ]);

  const [search, setSearch] = useState('');
  const [filteredMovements, setFilteredMovements] = useState([]);
  const [searched, setSearched] = useState(false);

  const handleSearch = () => {
    const results = movements
      .filter((m) => m.account.toLowerCase().includes(search.toLowerCase()))
      .sort((a, b) => new Date(a.date) - new Date(b.date));
    setFilteredMovements(results);
    setSearched(true);
  };

  const handleDownload = () => {
    alert('Por implementar');
  };

  let content;
  if (filteredMovements.length > 0) {
    content = (
      <table className="entity-grid">
        <thead>
          <tr>
            <th>Fecha</th>
            <th>Cliente</th>
            <th>Número Cuenta</th>
            <th>Tipo</th>
            <th>Saldo Inicial</th>
            <th>Estado</th>
            <th>Movimiento</th>
            <th>Saldo Disponible</th>
          </tr>
        </thead>
        <tbody>
          {filteredMovements.map((m) => (
            <tr key={m.id}>
              <td>{m.date}</td>
              <td>{m.client}</td>
              <td>{m.account}</td>
              <td>{m.type}</td>
              <td>${m.initialBalance}</td>
              <td>{m.state}</td>
              <td className={m.value < 0 ? 'negative' : 'positive'}>
                {m.value < 0 ? m.value : `+${m.value}`}
              </td>
              <td>${m.balance}</td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  } else if (searched) {
    content = <p className="entity-empty">No hay resultados.</p>;
  } else {
    content = (
      <p className="entity-empty">Ingrese una cuenta y presione Buscar.</p>
    );
  }

  return (
    <div className="entity-container">
      <div className="entity-header">
        <h2>Reportes</h2>
        <div className="entity-header-actions reports-actions">
          <div className="reports-search">
            <input
              type="text"
              placeholder="Buscar cuenta"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
            <button onClick={handleSearch} disabled={search.length < 3}>
              Buscar
            </button>
          </div>
          <button
            onClick={handleDownload}
            disabled={filteredMovements.length === 0}
          >
            Descargar
          </button>
        </div>
      </div>

      <div className="entity-data-container">{content}</div>
    </div>
  );
}
