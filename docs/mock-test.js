import fetch from 'node-fetch';

const endpoints = [
  '/api/zones',
  '/api/devices',
  '/api/user/permissions',
  '/api/themes/operator1',
  '/api/i18n/en',
  '/api/i18n/pl'
];

for (const endpoint of endpoints) {
  try {
    const response = await fetch(`http://localhost:8080${endpoint}`, {
        headers: {
          'Authorization': 'Basic ' + Buffer.from('maciej:testpass').toString('base64')
        }
      });
    console.log(`${endpoint} -> status: ${response.status}`);

    const text = await response.text();
    console.log(`${endpoint} -> raw response:\n`, text);

    try {
      const data = JSON.parse(text);
      console.log(`${endpoint} -> JSON parsed:`, data);
    } catch (err) {
      console.log(`${endpoint} returned invalid JSON`);
    }

  } catch (err) {
    console.error(`Error fetching ${endpoint}:`, err);
  }
}