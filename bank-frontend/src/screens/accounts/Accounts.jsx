import { useState, useEffect } from 'react';
import './Accounts.css';
import '../../../styles/Entities.css';

export default function Accounts() {
  const [accounts, setAccounts] = useState([]);
  const [search, setSearch] = useState('');

  useEffect(() => {
    fetch('http://localhost:8080/bank/v1/accounts')
      .then((res) => res.json())
      .then((response) => {
        console.log('Fetched accounts:', response);
        setAccounts(response.data);
      })
      .catch((err) => console.error('Error fetching accounts:', err));
  }, []);

  const filteredAccounts = accounts.filter(
    (a) =>
      a.number.toLowerCase().includes(search.toLowerCase()) ||
      String(a.clientId).includes(search.toLowerCase()),
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
                <th>Número Cuenta</th>
                <th>Tipo</th>
                <th>Saldo Inicial</th>
                <th>Estado</th>
                <th>Cliente</th>
              </tr>
            </thead>
            <tbody>
              {filteredAccounts.map((acc) => (
                <tr key={acc.id}>
                  <td>{acc.number}</td>
                  <td>{acc.type === 'SAVINGS' ? 'Ahorros' : 'Corriente'}</td>
                  <td>${acc.initialBalance}</td>
                  <td>{String(acc.status)}</td>
                  <td>{acc.clientName}</td>
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
