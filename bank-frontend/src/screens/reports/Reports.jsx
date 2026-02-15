import { useState } from 'react';
import './Reports.css';
import '../../../styles/Entities.css';

export default function Reports() {
  const [search, setSearch] = useState('');
  const [reports, setReports] = useState([]);
  const [searched, setSearched] = useState(false);

  const handleSearch = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/bank/v1/reports?initialDate=12-02-2026&finalDate=12-02-2026&clientIdentification=${search}`,
      );
      const bankResponse = await response.json();
      console.log('Bank response:', bankResponse);

      const results = bankResponse.data.reports.sort(
        (a, b) => new Date(a.date) - new Date(b.date),
      );

      setReports(results);
      setSearched(true);
    } catch (error) {
      console.error('Error fetching report:', error);
      setReports([]);
      setSearched(true);
      alert('Ocurrio un error, intente mas tarde.');
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
              <td>{r.accountType == 'SAVINGS' ? 'Ahorros' : 'Corriente'}</td>
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
              placeholder="Buscar por identificación cliente"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
            <button onClick={handleSearch} disabled={search.length < 3}>
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
