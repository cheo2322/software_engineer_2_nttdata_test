import { useState } from 'react';
import './Accounts.css';
import '../../../styles/Entities.css';

export default function Accounts() {
  const [accounts] = useState([
    {
      nro: '001',
      tipo: 'Ahorros',
      saldoInicial: 1500,
      estado: true,
      cliente: 'Juan Pérez',
    },
    {
      nro: '002',
      tipo: 'Corriente',
      saldoInicial: 3200,
      estado: false,
      cliente: 'María López',
    },
    {
      nro: '003',
      tipo: 'Ahorros',
      saldoInicial: 500,
      estado: true,
      cliente: 'Carlos Sánchez',
    },
  ]);

  const [search, setSearch] = useState('');

  const filteredAccounts = accounts.filter(
    (a) =>
      a.nro.toLowerCase().includes(search.toLowerCase()) ||
      a.cliente.toLowerCase().includes(search.toLowerCase()),
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
                <tr key={acc.nro}>
                  <td>{acc.nro}</td>
                  <td>{acc.tipo}</td>
                  <td>${acc.saldoInicial}</td>
                  <td>{acc.estado ? 'Activo' : 'Inactivo'}</td>
                  <td>{acc.cliente}</td>
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
