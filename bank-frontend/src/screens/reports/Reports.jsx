import { useState } from 'react';
import './Reports.css';
import '../../../styles/Entities.css';

const BANK_BACKEND_BASE_URL = import.meta.env.VITE_BANK_BACKEND_BASE_URL;

export default function Reports() {
  const [search, setSearch] = useState('');
  const [initialDate, setInitialDate] = useState('');
  const [finalDate, setFinalDate] = useState('');
  const [reports, setReports] = useState([]);
  const [searched, setSearched] = useState(false);

  const formatDate = (dateStr) => {
    if (!dateStr) return '';
    const [year, month, day] = dateStr.split('-');
    return `${day}-${month}-${year}`;
  };

  const handleSearch = async () => {
    try {
      const response = await fetch(
        `${BANK_BACKEND_BASE_URL}/bank/v1/reports?initialDate=${formatDate(initialDate)}&finalDate=${formatDate(finalDate)}&clientIdentification=${search}`,
      );
      const bankResponse = await response.json();
      if (bankResponse.data === null) {
        setReports([]);
        setSearched(true);
        return;
      }

      const results = bankResponse.data.reports.sort(
        (a, b) => new Date(a.date) - new Date(b.date),
      );

      setReports(results);
      setSearched(true);
    } catch (error) {
      console.error('Error fetching report:', error);
      setReports([]);
      setSearched(true);
      alert('Ocurrió un error, intente más tarde.');
    }
  };

  const handleDownload = () => {
    alert('Por implementar');
  };

  let content;
  if (reports.length > 0) {
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
          {reports.map((r, idx) => (
            <tr key={idx}>
              <td>{r.date}</td>
              <td>{r.client}</td>
              <td>{r.accountNumber}</td>
              <td>{r.accountType === 'SAVINGS' ? 'Ahorros' : 'Corriente'}</td>
              <td>${r.initialBalance}</td>
              <td>{String(r.status)}</td>
              <td className={r.type === 'DEPOSIT' ? 'positive' : 'negative'}>
                {r.type === 'DEPOSIT' ? '+' : '-'}${r.movement}
              </td>
              <td>${r.availableBalance}</td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  } else if (searched) {
    content = <p className="entity-empty">No hay resultados.</p>;
  } else {
    content = (
      <p className="entity-empty">
        Ingrese una identificación y presione Buscar.
      </p>
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
              placeholder="Identificación del cliente"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
            Fecha inicio:
            <input
              type="date"
              value={initialDate}
              onChange={(e) => setInitialDate(e.target.value)}
            />
            Fecha fin:
            <input
              type="date"
              value={finalDate}
              onChange={(e) => setFinalDate(e.target.value)}
            />
            <button
              onClick={handleSearch}
              disabled={search.length < 3 || !initialDate || !finalDate}
            >
              Buscar
            </button>
          </div>
          <button onClick={handleDownload} disabled={reports.length === 0}>
            Descargar
          </button>
        </div>
      </div>

      <div className="entity-data-container">{content}</div>
    </div>
  );
}
