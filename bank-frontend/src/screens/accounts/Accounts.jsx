import { useState } from 'react';
import './Accounts.css';
import '../../../styles/Entities.css';

export default function Accounts() {
  const [accounts] = useState([
    { nro: '001', userId: 'Juan Pérez', balance: 1500 },
    { nro: '002', userId: 'María López', balance: 3200 },
    { nro: '003', userId: 'Carlos Sánchez', balance: 500 },
  ]);

  const [search, setSearch] = useState('');

  const filteredAccounts = accounts.filter(
    (a) =>
      a.nro.toLowerCase().includes(search.toLowerCase()) ||
      a.userId.toLowerCase().includes(search.toLowerCase()),
  );

  return (
    <div className="entity-container">
      <div className="entity-header">
        <h2>Cuentas</h2>
        <div className="entity-header-actions">
          <input
            type="text"
            placeholder="Buscar"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
          <button onClick={() => alert('Nueva cuenta')}>Nueva</button>
        </div>
      </div>

      <div className="entity-data-container">
        {filteredAccounts.length > 0 ? (
          <table className="accounts-grid">
            <thead>
              <tr>
                <th>Nro. de cuenta</th>
                <th>Usuario</th>
                <th>Balance</th>
              </tr>
            </thead>
            <tbody>
              {filteredAccounts.map((acc, index) => (
                <tr key={index}>
                  <td>{acc.nro}</td>
                  <td>{acc.userId}</td>
                  <td>${acc.balance}</td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p className="entity-empty">No hay cuentas disponibles.</p>
        )}
      </div>
    </div>
  );
}
