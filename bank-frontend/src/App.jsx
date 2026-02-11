import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import Clients from './screens/Clients';
import Accounts from './screens/Accounts';
import Movements from './screens/Movements';
import Reports from './screens/Reports';

function App() {
  return (
    <Router>
      <div style={{ display: 'flex', height: '100vh' }}>
        {/* Sidebar */}
        <nav style={{ width: '200px', background: '#f0f0f0', padding: '1rem' }}>
          <ul style={{ listStyle: 'none', padding: 0, margin: 0 }}>
            <li style={{ marginBottom: '1rem' }}>
              <Link
                to="/clients"
                style={{ textDecoration: 'none', color: 'black' }}
              >
                Clientes
              </Link>
            </li>
            <li style={{ marginBottom: '1rem' }}>
              <Link
                to="/accounts"
                style={{ textDecoration: 'none', color: 'black' }}
              >
                Cuentas
              </Link>
            </li>
            <li style={{ marginBottom: '1rem' }}>
              <Link
                to="/movements"
                style={{ textDecoration: 'none', color: 'black' }}
              >
                Movimientos
              </Link>
            </li>
            <li style={{ marginBottom: '1rem' }}>
              <Link
                to="/reports"
                style={{ textDecoration: 'none', color: 'black' }}
              >
                Reportes
              </Link>
            </li>
          </ul>
        </nav>

        {/* Contenido dinámico */}
        <main style={{ flex: 1, padding: '2rem' }}>
          <Routes>
            <Route path="/clients" element={<Clients />} />
            <Route path="/accounts" element={<Accounts />} />
            <Route path="/movements" element={<Movements />} />
            <Route path="/reports" element={<Reports />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
