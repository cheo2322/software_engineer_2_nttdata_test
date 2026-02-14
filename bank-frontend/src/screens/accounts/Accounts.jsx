import { useState, useEffect } from 'react';
import './Accounts.css';
import '../../../styles/Entities.css';
import '../../../styles/Modal.css';

export default function Accounts() {
  const [accounts, setAccounts] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [newAccount, setNewAccount] = useState({
    number: '',
    type: 'SAVINGS',
    initialBalance: 0,
    clientId: '',
    status: true,
  });

  const fetchAccounts = async () => {
    setLoading(true);
    setError(null);

    try {
      const res = await fetch('http://localhost:8080/bank/v1/accounts');
      if (!res.ok) {
        throw new Error('Error en la respuesta del servidor');
      }
      const response = await res.json();
      setAccounts(response.data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAccounts();
  }, []);

  const handleCreateAccount = async () => {
    try {
      const res = await fetch('http://localhost:8080/bank/v1/accounts', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newAccount),
      });

      if (!res.ok) {
        throw new Error('Error al crear cuenta');
      }

      const response = await res.json();
      alert(response.message);

      setShowModal(false);
      await fetchAccounts();
    } catch (err) {
      alert(err.message);
    }
  };

  const filteredAccounts = accounts.filter(
    (a) =>
      a.number.toLowerCase().includes(search.toLowerCase()) ||
      a.clientName.toLowerCase().includes(search.toLowerCase()),
  );

  let content;
  if (loading) {
    content = <p>Cargando cuentas...</p>;
  } else if (error) {
    content = <p className="entity-error">{error}</p>;
  } else if (filteredAccounts.length > 0) {
    content = (
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
              <td>{acc.status ? 'Activo' : 'Inactivo'}</td>
              <td>{acc.clientName ?? acc.clientId}</td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  } else {
    content = <p className="entity-empty">No hay cuentas disponibles.</p>;
  }

  return (
    <div className="entity-container">
      <div className="entity-header">
        <h2>Cuentas</h2>
        <div className="entity-header-actions accounts-actions">
          <div className="accounts-search">
            <input
              type="text"
              placeholder="Buscar cuenta"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>
          <button onClick={() => setShowModal(true)}>Nueva</button>
        </div>
      </div>

      <div className="entity-data-container">{content}</div>

      {showModal && (
        <div className="modal">
          <div className="modal-content">
            <h3>Nueva cuenta</h3>
            <input
              type="text"
              placeholder="Número de cuenta"
              value={newAccount.number}
              onChange={(e) =>
                setNewAccount({ ...newAccount, number: e.target.value })
              }
            />
            <label>
              Tipo:
              <select
                value={newAccount.type}
                onChange={(e) =>
                  setNewAccount({ ...newAccount, type: e.target.value })
                }
              >
                <option value="SAVINGS">Ahorros</option>
                <option value="CHECKING">Corriente</option>
              </select>
            </label>
            <input
              type="number"
              placeholder="Saldo inicial"
              value={newAccount.initialBalance}
              onChange={(e) =>
                setNewAccount({
                  ...newAccount,
                  initialBalance: Number.parseFloat(e.target.value),
                })
              }
            />
            <input
              type="text"
              placeholder="ID Cliente"
              value={newAccount.clientId}
              onChange={(e) =>
                setNewAccount({ ...newAccount, clientId: e.target.value })
              }
            />
            <div className="modal-actions">
              <button onClick={handleCreateAccount}>Guardar</button>
              <button onClick={() => setShowModal(false)}>Cancelar</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
