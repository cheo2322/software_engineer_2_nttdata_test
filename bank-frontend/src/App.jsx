import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import Clients from './screens/clients/Clients';
import Accounts from './screens/Accounts';
import Movements from './screens/Movements';
import Reports from './screens/Reports';
import './App.css';

function App() {
  return (
    <Router>
      <div className="app-root">
        <header className="app-header">
          <h1>BANCO</h1>
        </header>

        <div className="app-layout">
          <nav className="app-sidebar">
            <ul>
              <li>
                <Link to="/clients">Clientes</Link>
              </li>
              <li>
                <Link to="/accounts">Cuentas</Link>
              </li>
              <li>
                <Link to="/movements">Movimientos</Link>
              </li>
              <li>
                <Link to="/reports">Reportes</Link>
              </li>
            </ul>
          </nav>

          <main className="app-content">
            <Routes>
              <Route path="/clients" element={<Clients />} />
              <Route path="/accounts" element={<Accounts />} />
              <Route path="/movements" element={<Movements />} />
              <Route path="/reports" element={<Reports />} />
            </Routes>
          </main>
        </div>
      </div>
    </Router>
  );
}

export default App;
