import { useState, useEffect } from 'react';
import './Movements.css';
import '../../../styles/Entities.css';

export default function Movements() {
  const [movements, setMovements] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchMovements = async () => {
    setLoading(true);
    setError(null);

    try {
      const res = await fetch('http://localhost:8080/bank/v1/movements');
      if (!res.ok) {
        throw new Error('Error en la respuesta del servidor');
      }
      const response = await res.json();
      setMovements(response.data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMovements();
  }, []);

  const filteredMovements = movements.filter(
    (m) =>
      m.timestamp.toLowerCase().includes(search.toLowerCase()) ||
      m.accountNumber.toLowerCase().includes(search.toLowerCase()),
  );

  const formatTimestamp = (isoString) => {
    const date = new Date(isoString);

    const pad = (num) => String(num).padStart(2, '0');

    const day = pad(date.getDate());
    const month = pad(date.getMonth() + 1);
    const year = date.getFullYear();

    const hours = pad(date.getHours());
    const minutes = pad(date.getMinutes());
    const seconds = pad(date.getSeconds());

    return `${day}-${month}-${year} ${hours}:${minutes}:${seconds}`;
  };

  let content;
  if (loading) {
    content = <p>Cargando movimientos...</p>;
  } else if (error) {
    content = <p className="entity-error">{error}</p>;
  } else if (filteredMovements.length > 0) {
    content = (
      <table className="movements-grid">
        <thead>
          <tr>
            <th>Fecha y hora</th>
            <th>Cuenta</th>
            <th>Tipo</th>
            <th>Valor</th>
            <th>Balance</th>
          </tr>
        </thead>
        <tbody>
          {filteredMovements.map((m, index) => {
            const isDeposit = m.type === 'DEPOSIT';
            const formattedValue = isDeposit ? `+${m.amount}` : `-${m.amount}`;
            return (
              <tr key={index}>
                <td>{formatTimestamp(m.timestamp)}</td>
                <td>{m.accountNumber}</td>
                <td>{isDeposit ? 'Depósito' : 'Retiro'}</td>
                <td className={isDeposit ? 'positive' : 'negative'}>
                  {formattedValue}
                </td>
                <td>{m.balance}</td>
              </tr>
            );
          })}
        </tbody>
      </table>
    );
  } else {
    content = <p className="entity-empty">No hay movimientos disponibles.</p>;
  }

  return (
    <div className="entity-container">
      <div className="entity-header">
        <h2>Movimientos</h2>
        <div className="entity-header-actions movements-actions">
          <div className="movements-search">
            <input
              type="text"
              placeholder="Buscar movimiento"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>
        </div>
      </div>

      <div className="entity-data-container">{content}</div>
    </div>
  );
}
