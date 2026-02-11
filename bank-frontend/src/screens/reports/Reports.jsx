import { useState } from 'react';
import './Reports.css';
import '../../../styles/Entities.css';

export default function Reports() {
  const [movements] = useState([
    { date: '2026-02-11 09:30', account: '001', value: -200, balance: 1300 },
    { date: '2026-02-11 10:15', account: '002', value: 500, balance: 3700 },
    { date: '2026-02-11 11:00', account: '003', value: -100, balance: 400 },
    { date: '2026-02-11 12:00', account: '001', value: 300, balance: 1600 },
  ]);

  const [search, setSearch] = useState('');
  const [filteredMovements, setFilteredMovements] = useState([]);

  const handleSearch = () => {
    const results = movements
      .filter((m) => m.account.toLowerCase().includes(search.toLowerCase()))
      .sort((a, b) => new Date(a.date) - new Date(b.date));
    setFilteredMovements(results);
  };

  return (
    <div className="entity-container">
      <div className="entity-header">
        <h2>Reportes</h2>
        <div className="entity-header-actions">
          <input
            type="text"
            placeholder="Buscar cuenta"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
          <button onClick={handleSearch}>Buscar</button>
        </div>
      </div>

      <div className="entity-data-container">
        {filteredMovements.length > 0 ? (
          <table className="reports-grid">
            <thead>
              <tr>
                <th>Fecha y hora</th>
                <th>Cuenta</th>
                <th>Valor</th>
                <th>Balance</th>
              </tr>
            </thead>
            <tbody>
              {filteredMovements.map((m, index) => (
                <tr key={index}>
                  <td>{m.date}</td>
                  <td>{m.account}</td>
                  <td className={m.value < 0 ? 'negative' : 'positive'}>
                    {m.value < 0 ? m.value : `+${m.value}`}
                  </td>
                  <td>{m.balance}</td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p className="entity-empty">Ingrese una cuenta y presione Buscar.</p>
        )}
      </div>
    </div>
  );
}
