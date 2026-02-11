import { useState } from 'react';

function App() {
  const [selected, setSelected] = useState('home');

  return (
    <div style={{ display: 'flex', height: '100vh' }}>
      <div style={{ width: '200px', background: '#f0f0f0', padding: '1rem' }}>
        <ul>
          <li onClick={() => setSelected('home')}>Clientes</li>
          <li onClick={() => setSelected('profile')}>Cuentas</li>
          <li onClick={() => setSelected('settings')}>Movimientos</li>
          <li onClick={() => setSelected('reports')}>Reportes</li>
        </ul>
      </div>

      <div style={{ flex: 1, padding: '2rem' }}>
        {selected === 'home' && <h2>Clientes</h2>}
        {selected === 'profile' && <h2>Cuentas</h2>}
        {selected === 'settings' && <h2>Movimientos</h2>}
        {selected === 'reports' && <h2>Reportes</h2>}
      </div>
    </div>
  );
}

export default App;
