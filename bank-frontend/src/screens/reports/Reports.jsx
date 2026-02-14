import { useState } from 'react';
import './Reports.css';
import '../../../styles/Entities.css';

export default function Reports() {
  const [movements] = useState([
    {
      id: 1,
      date: '2026-02-11 09:30',
      account: '001',
      value: -200,
      balance: 1300,
    },
    {
      id: 2,
      date: '2026-02-11 10:15',
      account: '002',
      value: 500,
      balance: 3700,
    },
    {
      id: 3,
      date: '2026-02-11 11:00',
      account: '003',
      value: -100,
      balance: 400,
    },
    {
      id: 4,
      date: '2026-02-11 12:00',
      account: '001',
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
            <th>Fecha y hora</th>
            <th>Cuenta</th>
            <th>Valor</th>
            <th>Balance</th>
          </tr>
        </thead>
        <tbody>
          {filteredMovements.map((m) => (
            <tr key={m.id}>
              <td>{m.date}</td>
              <td>{m.account}</td>
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
